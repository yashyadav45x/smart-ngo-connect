package com.example.smartngoapplication

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
import com.example.smartngoapplication.network.VolunteerRequest
import com.example.smartngoapplication.ui.components.NGOLogoHeader
import kotlinx.coroutines.launch

private val VolunteerGreen = Color(0xFF145C43)
private val VolunteerCream = Color(0xFFFAF9F5)

@Composable
fun VolunteerScreen(
    onBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var interest by remember { mutableStateOf("Teaching") }

    var isLoading by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    var serverIp by remember { mutableStateOf("127.0.0.1") }
    var showIpSettings by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VolunteerCream)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("← Back", color = VolunteerGreen)
            }
            TextButton(onClick = { showIpSettings = !showIpSettings }) {
                Text(if (showIpSettings) "Hide Server IP" else "⚙ Backend IP", color = VolunteerGreen, fontSize = 12.sp)
            }
        }

        if (showIpSettings) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF))
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("Backend Server IP Configuration", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = VolunteerGreen)
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = serverIp,
                        onValueChange = {
                            serverIp = it
                            ApiClient.baseUrl = "http://$serverIp:8080/"
                        },
                        label = { Text("Laptop Wi-Fi IP") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Text("Target: http://$serverIp:8080/api/volunteers", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        NGOLogoHeader()

        Spacer(Modifier.height(12.dp))

        Text(
            "Become a Volunteer",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = VolunteerGreen
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Share your time and skills to make a difference.",
            fontSize = 15.sp,
            color = Color.DarkGray
        )

        Spacer(Modifier.height(22.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    "Your Details",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = VolunteerGreen
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it.filter { ch -> ch.isDigit() }.take(10)
                    },
                    label = { Text("Mobile Number (10 digits)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(18.dp))

                Text(
                    "I'm interested in",
                    fontWeight = FontWeight.SemiBold,
                    color = VolunteerGreen
                )

                Spacer(Modifier.height(8.dp))

                listOf(
                    "Teaching",
                    "Food Distribution",
                    "Healthcare",
                    "Animal Welfare",
                    "Event Management"
                ).forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = interest == option,
                            onClick = { interest = option },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = VolunteerGreen
                            )
                        )
                        Text(option, color = Color.DarkGray)
                    }
                }

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        isLoading = true
                        resultMessage = null
                        scope.launch {
                            val request = VolunteerRequest(
                                fullName = name.trim(),
                                phoneNumber = phone.trim(),
                                email = email.trim(),
                                interest = interest
                            )
                            try {
                                ApiClient.baseUrl = "http://$serverIp:8080/"
                                val response = ApiClient.apiService.registerVolunteer(request)
                                if (response.isSuccessful && response.body() != null) {
                                    val saved = response.body()!!
                                    LocalRepository.addVolunteer(request)
                                    isSuccess = true
                                    resultMessage = "✅ Registration Successful!\nSaved in MySQL database with ID #${saved.id}."
                                    name = ""
                                    phone = ""
                                    email = ""
                                } else {
                                    val saved = LocalRepository.addVolunteer(request)
                                    isSuccess = true
                                    resultMessage = "✅ Registration Successful!\nRegistered in Smart NGO Application (ID #${saved.id})."
                                    name = ""
                                    phone = ""
                                    email = ""
                                }
                            } catch (e: Exception) {
                                val saved = LocalRepository.addVolunteer(request)
                                isSuccess = true
                                resultMessage = "✅ Registration Successful!\nRegistered in Smart NGO Application (ID #${saved.id})."
                                name = ""
                                phone = ""
                                email = ""
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading && name.isNotBlank() && phone.length == 10 && email.contains("@"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VolunteerGreen
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Submit Registration",
                            fontSize = 16.sp
                        )
                    }
                }

                if (resultMessage != null) {
                    Spacer(Modifier.height(16.dp))

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

        Spacer(Modifier.height(18.dp))

        Text(
            "Every helping hand can create a positive change.",
            color = VolunteerGreen,
            fontSize = 14.sp
        )
    }

    if (isSuccess && resultMessage != null) {
        AlertDialog(
            onDismissRequest = { isSuccess = false },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("🎉", fontSize = 42.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Welcome aboard!", fontWeight = FontWeight.Bold, color = VolunteerGreen, fontSize = 20.sp)
                }
            },
            text = {
                Text(
                    text = resultMessage ?: "",
                    fontSize = 15.sp,
                    color = Color.DarkGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = { isSuccess = false },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = VolunteerGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Awesome! Continue")
                }
            }
        )
    }
}
