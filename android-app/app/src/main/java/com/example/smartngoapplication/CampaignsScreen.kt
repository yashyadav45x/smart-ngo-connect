package com.example.smartngoapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartngoapplication.data.LocalRepository
import com.example.smartngoapplication.data.NGOCampaign
import com.example.smartngoapplication.network.ApiClient
import com.example.smartngoapplication.ui.components.NGOLogoHeader

private val CampaignGreen = Color(0xFF145C43)
private val CampaignMint = Color(0xFFE5F3EA)
private val CampaignCream = Color(0xFFFAF9F5)
private val CampaignMuted = Color(0xFF6B756F)

@Composable
fun CampaignsScreen(
    onBack: () -> Unit = {},
    onDonateClick: () -> Unit = {}
) {
    var campaignList by remember { mutableStateOf(LocalRepository.getCampaigns()) }
    var isLoading by remember { mutableStateOf(true) }
    var isLiveBackend by remember { mutableStateOf(false) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "FOOD", "EDUCATION", "HEALTH", "ANIMAL")

    LaunchedEffect(Unit) {
        try {
            val response = ApiClient.apiService.getCampaigns()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                campaignList = LocalRepository.getCampaigns(response.body())
                isLiveBackend = true
            } else {
                campaignList = LocalRepository.getCampaigns()
                isLiveBackend = false
            }
        } catch (_: Exception) {
            campaignList = LocalRepository.getCampaigns()
            isLiveBackend = false
        } finally {
            isLoading = false
        }
    }

    val filteredCampaigns = campaignList.filter { c ->
        val matchesSearch = c.title.contains(searchQuery, ignoreCase = true) || c.description.contains(searchQuery, ignoreCase = true)
        val matchesCat = selectedCategory == "All" || c.category.uppercase().contains(selectedCategory)
        matchesSearch && matchesCat
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CampaignCream)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("← Back", color = CampaignGreen)
            }
            if (isLiveBackend) {
                Text("● Live Database", color = Color(0xFF2E7D32), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            } else {
                Text("● Local Offline", color = Color.Gray, fontSize = 11.sp)
            }
        }

        Spacer(Modifier.height(4.dp))

        NGOLogoHeader()

        Spacer(Modifier.height(14.dp))

        Text(
            "Explore Campaigns",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = CampaignGreen
        )

        Spacer(Modifier.height(4.dp))

        Text(
            "Choose a cause and help make a difference.",
            fontSize = 14.sp,
            color = CampaignMuted
        )

        Spacer(Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search campaigns...") },
            leadingIcon = { Text("🔍", fontSize = 16.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CampaignGreen,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(Modifier.height(12.dp))

        // Category Filter Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                Button(
                    onClick = { selectedCategory = cat },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) CampaignGreen else CampaignMint,
                        contentColor = if (isSelected) Color.White else CampaignGreen
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(cat, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = CampaignGreen)
            }
        } else if (filteredCampaigns.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                Text("No campaigns found matching \"$searchQuery\"", color = CampaignMuted, fontSize = 14.sp)
            }
        } else {
            filteredCampaigns.forEach { campaign ->
                CampaignCard(
                    campaign = campaign,
                    onDonateClick = onDonateClick
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun CampaignCard(
    campaign: NGOCampaign,
    onDonateClick: () -> Unit
) {
    val progress = if (campaign.target > 0) {
        campaign.raised.toFloat() / campaign.target.toFloat()
    } else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                campaign.category,
                color = CampaignGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(10.dp))

            Text(
                campaign.title,
                color = CampaignGreen,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(7.dp))

            Text(
                campaign.description,
                color = CampaignMuted,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = CampaignGreen,
                trackColor = CampaignMint
            )

            Spacer(Modifier.height(10.dp))

            Text(
                "₹${campaign.raised} raised of ₹${campaign.target}",
                color = CampaignGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = onDonateClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CampaignGreen
                )
            ) {
                Text("Donate Now  →")
            }
        }
    }
}
