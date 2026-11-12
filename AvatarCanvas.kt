package com.meldmaster.rummy.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProceduralAvatar(
    avatarIndex: Int,
    modifier: Modifier = Modifier.size(46.dp)
) {
    val skinTones = listOf(Color(0xFFFCD0A1), Color(0xFFE0AC69), Color(0xFFC68642), Color(0xFF8D5524))
    val shirtColors = listOf(Color(0xFF26C6A6), Color(0xFFD94452), Color(0xFF3B82F6), Color(0xFFEAB308))

    val skin = skinTones[avatarIndex % skinTones.size]
    val shirt = shirtColors[avatarIndex % shirtColors.size]

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Background Circle
        drawCircle(color = Color(0xFF163F47), radius = w / 2f)

        // Torso/Shirt
        drawArc(
            color = shirt,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(w * 0.15f, h * 0.55f),
            size = Size(w * 0.7f, h * 0.6f)
        )

        // Head/Face
        drawCircle(
            color = skin,
            radius = w * 0.28f,
            center = Offset(w / 2f, h * 0.42f)
        )

        // Eyes
        drawCircle(color = Color(0xFF1B1B1F), radius = 2f, center = Offset(w * 0.42f, h * 0.40f))
        drawCircle(color = Color(0xFF1B1B1F), radius = 2f, center = Offset(w * 0.58f, h * 0.40f))

        // Hair / Accessory Variation
        if (avatarIndex % 2 == 0) {
            // Cap / Hair top
            drawArc(
                color = Color(0xFF1B1B1F),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(w * 0.22f, h * 0.14f),
                size = Size(w * 0.56f, h * 0.4f)
            )
        } else {
            // Turban / Band
            drawRoundRect(
                color = GoldAccent,
                topLeft = Offset(w * 0.2f, h * 0.16f),
                size = Size(w * 0.6f, h * 0.18f)
            )
        }
    }
}
