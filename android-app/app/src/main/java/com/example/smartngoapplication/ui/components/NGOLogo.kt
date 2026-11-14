package com.example.smartngoapplication.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LogoDarkGreen = Color(0xFF145C43)
private val LogoTeal = Color(0xFF0F766E)
private val LogoBrightGreen = Color(0xFF16A34A)
private val LogoMint = Color(0xFFE5F3EA)

@Composable
fun NGOLogoImage(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(LogoMint, shape = CircleShape)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Heads
            drawCircle(
                color = LogoDarkGreen,
                radius = w * 0.09f,
                center = Offset(w * 0.38f, h * 0.28f)
            )
            drawCircle(
                color = LogoBrightGreen,
                radius = w * 0.09f,
                center = Offset(w * 0.62f, h * 0.28f)
            )

            // Left Body & Arm reaching top center
            val leftBody = Path().apply {
                moveTo(w * 0.25f, h * 0.60f)
                cubicTo(w * 0.28f, h * 0.45f, w * 0.38f, h * 0.35f, w * 0.50f, h * 0.12f)
                cubicTo(w * 0.46f, h * 0.32f, w * 0.40f, h * 0.48f, w * 0.42f, h * 0.60f)
                close()
            }
            drawPath(
                path = leftBody,
                brush = Brush.verticalGradient(listOf(LogoTeal, LogoDarkGreen))
            )

            // Right Body & Arm reaching top center
            val rightBody = Path().apply {
                moveTo(w * 0.75f, h * 0.60f)
                cubicTo(w * 0.72f, h * 0.45f, w * 0.62f, h * 0.35f, w * 0.50f, h * 0.12f)
                cubicTo(w * 0.54f, h * 0.32f, w * 0.60f, h * 0.48f, w * 0.58f, h * 0.60f)
                close()
            }
            drawPath(
                path = rightBody,
                brush = Brush.verticalGradient(listOf(LogoBrightGreen, LogoDarkGreen))
            )

            // Open Book Pages (Curved Arches)
            val leftPage1 = Path().apply {
                moveTo(w * 0.18f, h * 0.68f)
                quadraticTo(w * 0.35f, h * 0.60f, w * 0.50f, h * 0.72f)
                quadraticTo(w * 0.35f, h * 0.66f, w * 0.18f, h * 0.76f)
                close()
            }
            drawPath(path = leftPage1, color = LogoDarkGreen)

            val leftPage2 = Path().apply {
                moveTo(w * 0.14f, h * 0.76f)
                quadraticTo(w * 0.33f, h * 0.68f, w * 0.50f, h * 0.80f)
                quadraticTo(w * 0.33f, h * 0.74f, w * 0.14f, h * 0.84f)
                close()
            }
            drawPath(path = leftPage2, color = LogoDarkGreen.copy(alpha = 0.8f))

            val rightPage1 = Path().apply {
                moveTo(w * 0.82f, h * 0.68f)
                quadraticTo(w * 0.65f, h * 0.60f, w * 0.50f, h * 0.72f)
                quadraticTo(w * 0.65f, h * 0.66f, w * 0.82f, h * 0.76f)
                close()
            }
            drawPath(path = rightPage1, color = LogoBrightGreen)

            val rightPage2 = Path().apply {
                moveTo(w * 0.86f, h * 0.76f)
                quadraticTo(w * 0.67f, h * 0.68f, w * 0.50f, h * 0.80f)
                quadraticTo(w * 0.67f, h * 0.74f, w * 0.86f, h * 0.84f)
                close()
            }
            drawPath(path = rightPage2, color = LogoBrightGreen.copy(alpha = 0.8f))
        }
    }
}

@Composable
fun NGOLogoHeader(
    modifier: Modifier = Modifier,
    showSubtext: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NGOLogoImage(size = 52.dp)

        Spacer(Modifier.width(10.dp))

        Column {
            Text(
                text = "SMART",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = LogoDarkGreen,
                letterSpacing = 2.sp
            )
            if (showSubtext) {
                Text(
                    text = "NGO support",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LogoBrightGreen
                )
            }
        }
    }
}

@Composable
fun NGOLogoCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NGOLogoImage(size = 85.dp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "SMART",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = LogoDarkGreen,
                letterSpacing = 3.sp
            )
            Text(
                text = "NGO support",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = LogoBrightGreen
            )
        }
    }
}
