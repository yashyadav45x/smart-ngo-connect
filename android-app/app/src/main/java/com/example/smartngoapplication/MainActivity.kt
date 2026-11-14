package com.example.smartngoapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import com.example.smartngoapplication.network.ApiClient
import com.example.smartngoapplication.network.DonationResponse
import com.example.smartngoapplication.network.VolunteerResponse
import com.example.smartngoapplication.ui.components.NGOLogoHeader

private val Forest = Color(0xFF145C43)
private val Mint = Color(0xFFE5F3EA)
private val Cream = Color(0xFFFAF9F5)
private val Ink = Color(0xFF1B3028)
private val Muted = Color(0xFF6B756F)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NGOHomeScreen()
            }
        }
    }
}

@Composable
fun NGOHomeScreen() {
    var isLoggedIn by remember { mutableStateOf(false) }
    var currentUserEmail by remember { mutableStateOf("yashyadav123@gmail.com") }
    var currentUserName by remember { mutableStateOf("Yash Yadav") }
    var isAdminUser by remember { mutableStateOf(false) }

    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = { email, name, isAdmin ->
                currentUserEmail = email
                currentUserName = name
                isAdminUser = isAdmin
                isLoggedIn = true
            }
        )
        return
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    var showDonateScreen by remember { mutableStateOf(false) }
    var showVolunteerScreen by remember { mutableStateOf(false) }

    var showRaisedDialog by remember { mutableStateOf(false) }
    var showVolunteersDialog by remember { mutableStateOf(false) }
    var showStoriesDialog by remember { mutableStateOf(false) }

    if (showDonateScreen) {
        DonateScreen(onBack = { showDonateScreen = false })
        return
    }

    if (showVolunteerScreen) {
        VolunteerScreen(onBack = { showVolunteerScreen = false })
        return
    }

    Scaffold(
        containerColor = Cream,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Forest
            ) {
                val tabs = listOf("Home", "Explore", "Activity", "Profile")
                val icons = listOf("⌂", "⌕", "▤", "♙")

                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Text(icons[index], fontSize = 23.sp) },
                        label = { Text(title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Forest,
                            selectedTextColor = Forest,
                            indicatorColor = Mint,
                            unselectedIconColor = Muted,
                            unselectedTextColor = Muted
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> HomeContent(
                    onDonateClick = { showDonateScreen = true },
                    onVolunteerClick = { showVolunteerScreen = true },
                    onRaisedClick = { showRaisedDialog = true },
                    onVolunteersClick = { showVolunteersDialog = true },
                    onCampaignsClick = { selectedTab = 1 },
                    onStoriesClick = { showStoriesDialog = true }
                )

                1 -> CampaignsScreen(
                    onBack = { selectedTab = 0 },
                    onDonateClick = { showDonateScreen = true }
                )

                2 -> ActivityContent()

                3 -> ProfileScreen(
                    onNavigateToTab = { tabIndex -> selectedTab = tabIndex },
                    onLogout = { isLoggedIn = false },
                    userName = currentUserName,
                    userEmail = currentUserEmail,
                    isAdmin = isAdminUser
                )
            }
        }
    }

    // Raised Dialog (Offline Supported)
    if (showRaisedDialog) {
        val totalRaisedFormatted = remember { LocalRepository.getFormattedRaisedFull() }
        val totalRaisedLakh = remember { LocalRepository.getFormattedRaisedAmount() }
        val campaigns = remember { LocalRepository.getCampaigns() }
        val donations = remember { LocalRepository.getDonations() }

        AlertDialog(
            onDismissRequest = { showRaisedDialog = false },
            title = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("💚 Funds Raised", fontWeight = FontWeight.Bold, color = Forest, fontSize = 20.sp)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Mint
                        ) {
                            Text("OFFLINE READY ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Forest, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    Text("Live & Offline Storage Unified", fontSize = 12.sp, color = Muted)
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Mint),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Total Amount Collected", fontSize = 12.sp, color = Muted, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.height(4.dp))
                            Text(totalRaisedFormatted, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Forest)
                            Spacer(Modifier.height(4.dp))
                            Text("Total $totalRaisedLakh raised across all active campaigns & offline donors.", fontSize = 12.sp, color = Ink)
                        }
                    }

                    Text("Campaign Funding Progress:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Forest)

                    campaigns.take(4).forEach { c ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(c.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Ink)
                                Text("₹${c.raised} / ₹${c.target}", fontSize = 12.sp, color = Forest, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (c.raised.toFloat() / c.target.toFloat()).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp),
                                color = Forest,
                                trackColor = Mint
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    Text("Recent Registered Donations (${donations.size}):", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Forest)

                    donations.take(3).forEach { d ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF4F6F5),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(d.donorName ?: "Anonymous", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
                                    Text("${d.paymentMethod ?: "UPI"} • ${d.status ?: "SUCCESS"}", fontSize = 11.sp, color = Muted)
                                }
                                Text("₹${d.amount.toInt()}", fontWeight = FontWeight.ExtraBold, color = Forest, fontSize = 14.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRaisedDialog = false
                        showDonateScreen = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Forest)
                ) {
                    Text("Donate Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRaisedDialog = false }) {
                    Text("Close", color = Muted)
                }
            }
        )
    }

    // Volunteers Dialog (Offline Supported)
    if (showVolunteersDialog) {
        val volunteersCount = remember { LocalRepository.getVolunteersCount() }
        val volunteersList = remember { LocalRepository.getVolunteers() }

        AlertDialog(
            onDismissRequest = { showVolunteersDialog = false },
            title = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🤝 Volunteers", fontWeight = FontWeight.Bold, color = Forest, fontSize = 20.sp)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Mint
                        ) {
                            Text("OFFLINE READY ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Forest, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    Text("$volunteersCount Active Registered Members", fontSize = 13.sp, color = Forest, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Volunteers contribute across Teaching, Food Drives, Healthcare, and Animal Welfare.",
                        fontSize = 13.sp,
                        color = Muted
                    )

                    Spacer(Modifier.height(4.dp))
                    Text("Active Volunteers List:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Forest)

                    volunteersList.forEach { v ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Mint),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(v.fullName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Forest)
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White
                                    ) {
                                        Text(v.interest, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Forest, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Spacer(Modifier.height(4.dp))
                                Text("📱 ${v.phoneNumber} • ✉ ${v.email}", fontSize = 12.sp, color = Ink)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showVolunteersDialog = false
                        showVolunteerScreen = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Forest)
                ) {
                    Text("Become a Volunteer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showVolunteersDialog = false }) {
                    Text("Close", color = Muted)
                }
            }
        )
    }

    // Stories of Impact Dialog (Offline Supported)
    if (showStoriesDialog) {
        val stories = remember { LocalRepository.getStoriesOfImpact() }

        AlertDialog(
            onDismissRequest = { showStoriesDialog = false },
            title = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🌟 Stories of Impact", fontWeight = FontWeight.Bold, color = Forest, fontSize = 20.sp)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Mint
                        ) {
                            Text("OFFLINE AVAILABLE ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Forest, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    Text("${stories.size} Impact Stories Available Offline", fontSize = 12.sp, color = Muted)
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    stories.forEach { story ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        story.category,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Forest
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Mint
                                    ) {
                                        Text(
                                            story.metric,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Forest,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(story.icon, fontSize = 22.sp)
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        story.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Ink
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    story.description,
                                    fontSize = 13.sp,
                                    color = Muted,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showStoriesDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Forest)
                ) {
                    Text("Wonderful!")
                }
            }
        )
    }
}

@Composable
fun ActivityContent() {
    var volunteers by remember { mutableStateOf<List<VolunteerResponse>>(emptyList()) }
    var donations by remember { mutableStateOf<List<DonationResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var activeSubTab by remember { mutableIntStateOf(0) } // 0 = Volunteers, 1 = Donations

    LaunchedEffect(Unit) {
        try {
            val volRes = ApiClient.apiService.getAllVolunteers()
            val remoteVols = if (volRes.isSuccessful) volRes.body() else null
            volunteers = LocalRepository.getVolunteers(remoteVols)

            val donRes = ApiClient.apiService.getAllDonations()
            val remoteDons = if (donRes.isSuccessful) donRes.body() else null
            donations = LocalRepository.getDonations(remoteDons)
        } catch (_: Exception) {
            volunteers = LocalRepository.getVolunteers(null)
            donations = LocalRepository.getDonations(null)
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        Text(
            "Live Activity Log",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Forest
        )

        Spacer(Modifier.height(4.dp))

        Text(
            "Records stored in MySQL & Local database",
            fontSize = 14.sp,
            color = Muted
        )

        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { activeSubTab = 0 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeSubTab == 0) Forest else Mint,
                    contentColor = if (activeSubTab == 0) Color.White else Forest
                ),
                modifier = Modifier.weight(1f).padding(end = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volunteers (${volunteers.size})")
            }

            Button(
                onClick = { activeSubTab = 1 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeSubTab == 1) Forest else Mint,
                    contentColor = if (activeSubTab == 1) Color.White else Forest
                ),
                modifier = Modifier.weight(1f).padding(start = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Donations (${donations.size})")
            }
        }

        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Forest)
            }
        } else if (activeSubTab == 0) {
            if (volunteers.isEmpty()) {
                Text("No volunteer registrations found.", color = Muted, fontSize = 14.sp)
            } else {
                volunteers.forEach { v ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(v.fullName, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Forest)
                                Text("#${v.id ?: 1}", fontSize = 12.sp, color = Muted)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Interest: ${v.interest}", fontSize = 14.sp, color = Ink, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.height(2.dp))
                            Text("${v.email} • ${v.phoneNumber}", fontSize = 13.sp, color = Muted)
                        }
                    }
                }
            }
        } else {
            if (donations.isEmpty()) {
                Text("No donations recorded yet.", color = Muted, fontSize = 14.sp)
            } else {
                donations.forEach { d ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(d.donorName ?: "Anonymous", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Forest)
                                Text("₹${d.amount}", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = Forest)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Method: ${d.paymentMethod ?: "UPI"} • Status: ${d.status ?: "SUCCESS"}", fontSize = 13.sp, color = Muted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    onDonateClick: () -> Unit,
    onVolunteerClick: () -> Unit,
    onRaisedClick: () -> Unit,
    onVolunteersClick: () -> Unit,
    onCampaignsClick: () -> Unit,
    onStoriesClick: () -> Unit
) {
    var raisedVal by remember { mutableStateOf(LocalRepository.getFormattedRaisedAmount()) }
    var volVal by remember { mutableIntStateOf(LocalRepository.getVolunteersCount()) }
    var campVal by remember { mutableIntStateOf(LocalRepository.getCampaignsCount()) }

    LaunchedEffect(Unit) {
        raisedVal = LocalRepository.getFormattedRaisedAmount()
        volVal = LocalRepository.getVolunteersCount()
        campVal = LocalRepository.getCampaignsCount()

        try {
            val vRes = ApiClient.apiService.getAllVolunteers()
            if (vRes.isSuccessful && vRes.body() != null) {
                LocalRepository.getVolunteers(vRes.body())
                volVal = LocalRepository.getVolunteersCount()
            }
        } catch (_: Exception) {}

        try {
            val dRes = ApiClient.apiService.getAllDonations()
            if (dRes.isSuccessful && dRes.body() != null) {
                LocalRepository.getDonations(dRes.body())
                raisedVal = LocalRepository.getFormattedRaisedAmount()
            }
        } catch (_: Exception) {}

        try {
            val cRes = ApiClient.apiService.getCampaigns()
            if (cRes.isSuccessful && cRes.body() != null) {
                LocalRepository.getCampaigns(cRes.body())
                campVal = LocalRepository.getCampaignsCount()
            }
        } catch (_: Exception) {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp)
    ) {
        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NGOLogoHeader()
            TextButton(onClick = {}) {
                Text("🔔", fontSize = 20.sp)
            }
        }

        Spacer(Modifier.height(25.dp))

        Text("Hello there 👋", fontSize = 17.sp, color = Muted)
        Spacer(Modifier.height(6.dp))
        Text(
            "Small acts.\nBig impact.",
            fontSize = 35.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Ink
        )
        Spacer(Modifier.height(8.dp))
        Text("Together, we can make life better.", fontSize = 15.sp, color = Muted)

        Spacer(Modifier.height(22.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Forest)
        ) {
            Column(Modifier.padding(22.dp)) {
                Text(
                    "✦  FEATURED CAMPAIGN",
                    color = Color(0xFFBDE9CA),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Feed a Family",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(7.dp))
                Text("Every meal brings hope.", color = Color.White, fontSize = 15.sp)
                Spacer(Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = { 0.68f },
                        modifier = Modifier.weight(1f).height(8.dp),
                        color = Color(0xFF9BE5AF),
                        trackColor = Color(0xFF507B68)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("68%", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(6.dp))
                Text(
                    "Campaign funding progress · Live database",
                    color = Color(0xFFD9E9DF),
                    fontSize = 11.sp
                )
                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = onDonateClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Forest
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Support campaign  →", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Interactive Social Impact Calculator Widget
        var calcAmount by remember { mutableIntStateOf(500) }
        val impactText = when {
            calcAmount >= 5000 -> "Provides full education & nutrition kit for 10 children for an entire month!"
            calcAmount >= 2000 -> "Supports emergency medical camp and health checkups for 15 families."
            calcAmount >= 1000 -> "Sponsors school books and uniform kits for 3 children."
            else -> "Provides nutritious meals and clean water packs for 10 people in need."
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    "💡 Social Impact Calculator",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Forest
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "See how your contribution creates change",
                    fontSize = 13.sp,
                    color = Muted
                )
                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(200, 500, 1000, 2500, 5000).forEach { amt ->
                        val isSelected = calcAmount == amt
                        Button(
                            onClick = { calcAmount = amt },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Forest else Mint,
                                contentColor = if (isSelected) Color.White else Forest
                            )
                        ) {
                            Text("₹$amt", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Mint,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✨", fontSize = 20.sp)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = impactText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Forest,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(22.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ImpactCard("₹", raisedVal, "Raised", onClick = onRaisedClick, modifier = Modifier.weight(1f))
            ImpactCard("♙", "$volVal", "Volunteers", onClick = onVolunteersClick, modifier = Modifier.weight(1f))
            ImpactCard("✳", "$campVal", "Campaigns", onClick = onCampaignsClick, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(26.dp))
        Text(
            "How would you like to help?",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Ink
        )
        Spacer(Modifier.height(13.dp))

        HelpCard(
            title = "Donate",
            description = "Support people who need you.",
            symbol = "♡",
            onClick = onDonateClick
        )

        Spacer(Modifier.height(12.dp))

        HelpCard(
            title = "Volunteer",
            description = "Give your time. Create change.",
            symbol = "♧",
            onClick = onVolunteerClick
        )

        Spacer(Modifier.height(25.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Stories of impact",
                modifier = Modifier.weight(1f),
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            TextButton(onClick = onStoriesClick) {
                Text("View all →", color = Forest, fontWeight = FontWeight.Bold)
            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Mint),
            onClick = onStoriesClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(18.dp)) {
                Text(
                    "EDUCATION • COMMUNITY",
                    color = Forest,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "A little help can change a life.",
                    color = Ink,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Discover 6 community initiatives and see how people can make a difference together (Offline Available).",
                    color = Muted,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            "Made for communities, with care.",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Muted,
            fontSize = 12.sp
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun ImpactCard(
    symbol: String,
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(horizontal = 10.dp, vertical = 15.dp)) {
            Text(symbol, color = Forest, fontSize = 23.sp)
            Spacer(Modifier.height(7.dp))
            Text(value, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(label, color = Muted, fontSize = 11.sp)
        }
    }
}

@Composable
fun HelpCard(
    title: String,
    description: String,
    symbol: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(54.dp).background(Mint, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(symbol, color = Forest, fontSize = 29.sp)
            }
            Spacer(Modifier.width(13.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Forest, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(description, color = Muted, fontSize = 13.sp)
            }
            Text("→", color = Forest, fontSize = 24.sp)
        }
    }
}

@Composable
fun SimpleSection(title: String, description: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Forest)
        Spacer(Modifier.height(12.dp))
        Text(description, color = Muted)
    }
}
