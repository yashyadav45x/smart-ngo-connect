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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartngoapplication.ui.components.NGOLogoImage

private val ThemeGreen = Color(0xFF145C43)
private val ThemeMint = Color(0xFFE5F3EA)
private val ThemeCream = Color(0xFFFAF9F5)
private val ThemeMuted = Color(0xFF6B756F)

object DonorAuthStore {
    val registeredDonors = mutableMapOf(
        "yashyadav123@gmail.com" to "password123",
        "prakash@gmail.com" to "123456"
    )
    var currentUserName = "Yash Yadav"
}

@Composable
fun LoginScreen(
    onLoginSuccess: (email: String, name: String, isAdmin: Boolean) -> Unit
) {
    var selectedRole by remember { mutableIntStateOf(0) } // 0 = Donor, 1 = Admin
    var isRegisterMode by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeCream)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(20.dp))

        // Logo
        NGOLogoImage(size = 90.dp)

        Spacer(Modifier.height(14.dp))

        Text(
            text = "Smart NGO Portal",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ThemeGreen
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Empowering communities together.",
            fontSize = 14.sp,
            color = ThemeMuted
        )

        Spacer(Modifier.height(24.dp))

        // Role Tab Switcher (Donor vs Admin)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            ) {
                // Donor Tab
                Button(
                    onClick = {
                        selectedRole = 0
                        isRegisterMode = false
                        errorMessage = null
                        successMessage = null
                        email = ""
                        password = ""
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRole == 0) ThemeGreen else Color.Transparent,
                        contentColor = if (selectedRole == 0) Color.White else ThemeMuted
                    )
                ) {
                    Text("Donor Login", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                // Admin Tab
                Button(
                    onClick = {
                        selectedRole = 1
                        isRegisterMode = false
                        errorMessage = null
                        successMessage = null
                        email = "yashyadav123@gmail.com"
                        password = "1234567890"
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRole == 1) ThemeGreen else Color.Transparent,
                        contentColor = if (selectedRole == 1) Color.White else ThemeMuted
                    )
                ) {
                    Text("Admin Login", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Text(
                    text = if (selectedRole == 1) "Admin Portal Access" else if (isRegisterMode) "Create Donor Account" else "Welcome Back, Donor",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ThemeGreen
                )

                Spacer(Modifier.height(16.dp))

                if (selectedRole == 0 && isRegisterMode) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(Modifier.height(14.dp))
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (errorMessage != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(text = errorMessage!!, color = Color(0xFFB3261E), fontSize = 13.sp)
                }

                if (successMessage != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(text = successMessage!!, color = Color(0xFF2E7D32), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(22.dp))

                Button(
                    onClick = {
                        errorMessage = null
                        successMessage = null
                        val cleanEmail = email.trim().lowercase()
                        val cleanPassword = password.trim()

                        if (selectedRole == 1) {
                            // Admin Login (Hardcoded credentials requested by user)
                            if (cleanEmail == "yashyadav123@gmail.com" && cleanPassword == "1234567890") {
                                onLoginSuccess("yashyadav123@gmail.com", "Admin Yash", true)
                            } else {
                                errorMessage = "Invalid Admin Credentials!\nEmail: yashyadav123@gmail.com\nPassword: 1234567890"
                            }
                        } else {
                            // Donor Login / Register
                            if (isRegisterMode) {
                                if (name.isBlank() || cleanEmail.isBlank() || cleanPassword.isBlank()) {
                                    errorMessage = "Please fill all fields to register."
                                } else {
                                    DonorAuthStore.registeredDonors[cleanEmail] = cleanPassword
                                    DonorAuthStore.currentUserName = name.trim()
                                    successMessage = "Registration successful! Please login now."
                                    isRegisterMode = false
                                    password = ""
                                }
                            } else {
                                // Donor Login
                                val storedPass = DonorAuthStore.registeredDonors[cleanEmail]
                                if (storedPass != null && storedPass == cleanPassword) {
                                    onLoginSuccess(cleanEmail, name.ifBlank { "Yash Yadav" }, false)
                                } else {
                                    errorMessage = "Account not found or incorrect password. Please register first if you are new."
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ThemeGreen)
                ) {
                    Text(
                        text = if (selectedRole == 1) "→ Admin Login" else if (isRegisterMode) "Register Account" else "→ Donor Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (selectedRole == 0) {
                    Spacer(Modifier.height(14.dp))
                    TextButton(
                        onClick = {
                            isRegisterMode = !isRegisterMode
                            errorMessage = null
                            successMessage = null
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = if (isRegisterMode) "Already have an account? Login here" else "New donor? Create an account",
                            color = ThemeGreen,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}
