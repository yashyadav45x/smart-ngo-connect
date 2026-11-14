package com.example.smartngoapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.smartngoapplication.network.DonationRequest
import com.example.smartngoapplication.ui.components.NGOLogoHeader
import kotlinx.coroutines.launch

private val NGOGreen = Color(0xFF236B4A)
private val NGOCream = Color(0xFFF7F5EC)

@Composable
fun DonateScreen(
    onBack: () -> Unit = {}
) {
    var amount by remember { mutableStateOf("500") }
    var selectedAmount by remember { mutableIntStateOf(500) }
    var donorName by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NGOCream)
            .padding(20.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Back", color = NGOGreen)
        }

        Spacer(modifier = Modifier.height(6.dp))

        NGOLogoHeader()

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Make a Difference",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = NGOGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your small contribution can bring a big change.",
            fontSize = 15.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Choose donation amount",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NGOGreen
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = donorName,
                    onValueChange = { donorName = it },
                    label = { Text("Your Name (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(100, 500, 1000).forEach { value ->
                        OutlinedButton(
                            onClick = {
                                selectedAmount = value
                                amount = value.toString()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = NGOGreen
                            )
                        ) {
                            Text("₹$value")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it.filter { ch -> ch.isDigit() }
                        selectedAmount = amount.toIntOrNull() ?: 0
                    },
                    label = { Text("Enter amount (₹)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        isLoading = true
                        resultMessage = null
                        scope.launch {
                            val request = DonationRequest(
                                donorName = if (donorName.isBlank()) "Anonymous Donor" else donorName.trim(),
                                amount = selectedAmount.toDouble(),
                                paymentMethod = "UPI (Demo)",
                                status = "SUCCESS"
                            )
                            try {
                                val response = ApiClient.apiService.recordDonation(request)
                                if (response.isSuccessful && response.body() != null) {
                                    val saved = response.body()!!
                                    LocalRepository.addDonation(request)
                                    isSuccess = true
                                    resultMessage = "✅ Donation Recorded!\nAmount ₹${saved.amount} saved in database with Transaction ID #${saved.id}."
                                } else {
                                    val saved = LocalRepository.addDonation(request)
                                    isSuccess = true
                                    resultMessage = "✅ Donation Recorded!\nAmount ₹${saved.amount} saved in Smart NGO Application (ID #${saved.id})."
                                }
                            } catch (e: Exception) {
                                val saved = LocalRepository.addDonation(request)
                                isSuccess = true
                                resultMessage = "✅ Donation Recorded!\nAmount ₹${saved.amount} saved in Smart NGO Application (ID #${saved.id})."
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NGOGreen
                    ),
                    enabled = !isLoading && selectedAmount > 0
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Continue to Donate (₹$selectedAmount)",
                            fontSize = 16.sp
                        )
                    }
                }

                if (resultMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSuccess) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text(
                                text = resultMessage!!,
                                color = if (isSuccess) Color(0xFF2E7D32) else Color(0xFFC62828),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Your support helps provide food, education and essential supplies to people in need.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "🔒 Secure donations • Saved to MySQL backend",
            fontSize = 13.sp,
            color = NGOGreen,
            fontWeight = FontWeight.Medium
        )
    }
}
