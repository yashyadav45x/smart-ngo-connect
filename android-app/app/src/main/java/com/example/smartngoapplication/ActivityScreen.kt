package com.example.smartngoapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val NGOGreen = Color(0xFF145C43)
private val NGOMint = Color(0xFFE5F3EA)

private data class NGOActivity(
    val title: String,
    val description: String,
    val date: String,
    val status: String,
    val category: String
)

@Composable
fun ActivityScreen() {
    var selectedFilter by remember { mutableStateOf("All") }

    val activities = listOf(
        NGOActivity(
            "Education Donation",
            "Support for educational supplies",
            "12 Oct 2026",
            "Completed",
            "Donation"
        ),
        NGOActivity(
            "Community Clean-up",
            "Community volunteering event",
            "28 Oct 2026",
            "Upcoming",
            "Volunteer"
        ),
        NGOActivity(
            "Food Donation",
            "Food support for families",
            "20 Oct 2026",
            "Pending",
            "Donation"
        )
    )

    val filters = listOf("All", "Donation", "Volunteer")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "My Activity",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = NGOGreen
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Track your contributions and registrations.",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            filters.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        val filteredActivities = activities.filter {
            selectedFilter == "All" || it.category == selectedFilter
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            if (filteredActivities.isEmpty()) {
                item {
                    Text("No activities found.")
                }
            }

            items(filteredActivities) { activity ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            text = activity.category.uppercase(),
                            color = NGOGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = activity.title,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(
                            text = activity.description,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = activity.date,
                            color = Color.DarkGray,
                            fontSize = 13.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = NGOMint
                        ) {
                            Text(
                                text = activity.status,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                ),
                                color = NGOGreen,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}