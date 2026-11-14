package com.example.smartngoapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.smartngoapplication.network.ApiClient
import com.example.smartngoapplication.ui.components.NGOLogoHeader

private val ProfileGreen = Color(0xFF145C43)
private val ProfileMint = Color(0xFFE5F3EA)
private val ProfileCream = Color(0xFFFAF9F5)
private val ProfileTextDark = Color(0xFF1B3028)
private val ProfileMuted = Color(0xFF6B756F)

@Composable
fun ProfileScreen(
    onNavigateToTab: (Int) -> Unit = {},
    onLogout: () -> Unit = {},
    userName: String = "Yash Yadav",
    userEmail: String = "yashyadav123@gmail.com",
    isAdmin: Boolean = false
) {
    var name by remember { mutableStateOf(userName) }
    var email by remember { mutableStateOf(userEmail) }
    var phone by remember { mutableStateOf("9876543210") }
    var memberRole by remember { mutableStateOf(if (isAdmin) "Administrator (Full Access)" else "Active Donor & Supporter") }

    var serverIp by remember { mutableStateOf("127.0.0.1") }
    var showIpDialog by remember { mutableStateOf(false) }

    var editDialog by remember { mutableStateOf(false) }
    var helpDialog by remember { mutableStateOf(false) }
    var aboutDialog by remember { mutableStateOf(false) }
    var logoutDialog by remember { mutableStateOf(false) }

    var editName by remember { mutableStateOf(name) }
    var editEmail by remember { mutableStateOf(email) }
    var editPhone by remember { mutableStateOf(phone) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileCream)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NGOLogoHeader()

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = ProfileMint,
                modifier = Modifier.padding(2.dp)
            ) {
                Text(
                    text = "VERIFIED MEMBER ✓",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProfileGreen,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Main User Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(88.dp),
                    shape = CircleShape,
                    color = ProfileMint
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = name.firstOrNull()?.uppercase() ?: "P",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProfileGreen
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProfileTextDark
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = memberRole,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ProfileGreen
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "$email • +91 $phone",
                    color = ProfileMuted,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        editName = name
                        editEmail = email
                        editPhone = phone
                        editDialog = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ProfileGreen)
                ) {
                    Text("✎ Edit Details", fontSize = 14.sp)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Quick Impact Stats
        Text(
            text = "Your Contribution Impact",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ProfileTextDark
        )

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ImpactStatCard(
                modifier = Modifier.weight(1f),
                title = "Total Donated",
                value = "₹5,000",
                icon = "💚",
                onClick = { onNavigateToTab(2) }
            )

            ImpactStatCard(
                modifier = Modifier.weight(1f),
                title = "Events Joined",
                value = "2 Causes",
                icon = "🤝",
                onClick = { onNavigateToTab(2) }
            )

            ImpactStatCard(
                modifier = Modifier.weight(1f),
                title = "NGO Badge",
                value = "Bronze",
                icon = "🏆",
                onClick = { helpDialog = true }
            )
        }

        Spacer(Modifier.height(24.dp))

        // Account Options Section
        Text(
            text = "Account Settings",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ProfileTextDark
        )

        Spacer(Modifier.height(12.dp))

        ProfileOptionItem(
            icon = "👤",
            title = "Personal Information",
            subtitle = "Manage name, email, and mobile number",
            onClick = {
                editName = name
                editEmail = email
                editPhone = phone
                editDialog = true
            }
        )

        ProfileOptionItem(
            icon = "📜",
            title = "My Activities & History",
            subtitle = "View your donations and volunteer applications",
            onClick = { onNavigateToTab(2) }
        )

        ProfileOptionItem(
            icon = "⚙",
            title = "Backend Server IP Configuration",
            subtitle = "Current Target: http://$serverIp:8080/",
            onClick = { showIpDialog = true }
        )

        ProfileOptionItem(
            icon = "❓",
            title = "Help & Support",
            subtitle = "Get assistance and contact NGO team",
            onClick = { helpDialog = true }
        )

        ProfileOptionItem(
            icon = "ℹ",
            title = "About Smart NGO Connect",
            subtitle = "Version 1.0.0 • BSc IT Major Project",
            onClick = { aboutDialog = true }
        )

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { logoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFB3261E)
            )
        ) {
            Text("🚪 Log Out", fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(20.dp))
    }

    // Edit Profile Dialog
    if (editDialog) {
        AlertDialog(
            onDismissRequest = { editDialog = false },
            title = { Text("Edit Profile Information", fontWeight = FontWeight.Bold, color = ProfileGreen) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Mobile Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editName.isNotBlank() && editEmail.isNotBlank()) {
                            name = editName.trim()
                            email = editEmail.trim()
                            phone = editPhone.trim()
                            editDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfileGreen)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { editDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    // IP Config Dialog
    if (showIpDialog) {
        AlertDialog(
            onDismissRequest = { showIpDialog = false },
            title = { Text("Configure Backend Server IP", fontWeight = FontWeight.Bold, color = ProfileGreen) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Enter your PC/laptop IP address or 127.0.0.1 for USB ADB connection.", fontSize = 13.sp, color = Color.DarkGray)
                    OutlinedTextField(
                        value = serverIp,
                        onValueChange = {
                            serverIp = it
                            ApiClient.baseUrl = "http://$serverIp:8080/"
                        },
                        label = { Text("Server IP (e.g. 127.0.0.1 or 10.159.41.160)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Current URL: http://$serverIp:8080/", fontSize = 12.sp, color = ProfileGreen)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showIpDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfileGreen)
                ) {
                    Text("Done")
                }
            }
        )
    }

    // Help Dialog
    if (helpDialog) {
        AlertDialog(
            onDismissRequest = { helpDialog = false },
            title = { Text("Help & Support", fontWeight = FontWeight.Bold, color = ProfileGreen) },
            text = {
                Text(
                    "Smart NGO Connect helps NGOs manage volunteers, track donations, and coordinate campaigns.\n\n" +
                            "📧 Support Email: support@smartngo.org\n" +
                            "📞 Helpline: +91 1800-123-4567\n" +
                            "🌐 Website: www.smartngoconnect.org"
                )
            },
            confirmButton = {
                Button(
                    onClick = { helpDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfileGreen)
                ) {
                    Text("Close")
                }
            }
        )
    }

    // About Dialog
    if (aboutDialog) {
        AlertDialog(
            onDismissRequest = { aboutDialog = false },
            title = { Text("About Smart NGO Connect", fontWeight = FontWeight.Bold, color = ProfileGreen) },
            text = {
                Text(
                    "Smart NGO Application v1.0.0\n\n" +
                            "Built with Jetpack Compose (Android Front-End), Spring Boot REST API, and MySQL Database.\n\n" +
                            "BSc IT Major Academic Project."
                )
            },
            confirmButton = {
                Button(
                    onClick = { aboutDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ProfileGreen)
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Logout Dialog
    if (logoutDialog) {
        AlertDialog(
            onDismissRequest = { logoutDialog = false },
            title = { Text("Confirm Log Out", fontWeight = FontWeight.Bold, color = Color(0xFFB3261E)) },
            text = { Text("Are you sure you want to log out of your Smart NGO Connect account?") },
            confirmButton = {
                Button(
                    onClick = {
                        logoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E))
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { logoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ImpactStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 20.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ProfileGreen)
            Spacer(Modifier.height(2.dp))
            Text(title, fontSize = 11.sp, color = ProfileMuted)
        }
    }
}

@Composable
private fun ProfileOptionItem(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = ProfileMint,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(icon, fontSize = 18.sp)
                }
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ProfileTextDark
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = ProfileMuted
                )
            }

            Text("›", fontSize = 22.sp, color = ProfileMuted, fontWeight = FontWeight.Bold)
        }
    }
}
