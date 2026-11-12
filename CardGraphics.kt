package com.meldmaster.rummy.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meldmaster.rummy.game.engine.Card
import com.meldmaster.rummy.game.engine.Suit
import com.meldmaster.rummy.ui.theme.CardRed
import com.meldmaster.rummy.ui.theme.CardBlack
import com.meldmaster.rummy.ui.theme.GoldAccent

@Composable
fun PlayingCardView(
    card: Card,
    modifier: Modifier = Modifier,
    isWildJoker: Boolean = false,
    isSelected: Boolean = false
) {
    val cardColor = if (card.suit.isRed) CardRed else CardBlack

    Surface(
        modifier = modifier
            .width(58.dp)
            .height(84.dp)
            .offset(y = if (isSelected) (-12).dp else 0.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) GoldAccent else Color(0x33000000),
                shape = RoundedCornerShape(6.dp)
            ),
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFFAFAF7),
        shadowElevation = if (isSelected) 8.dp else 3.dp
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(4.dp)) {
            // Rank and mini suit top-left
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = card.rank.label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = cardColor
                )
                SuitIcon(suit = card.suit, modifier = Modifier.size(10.dp), isRed = card.suit.isRed)
            }

            // Center Suit Canvas
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (card.isPrintedJoker) {
                    JokerStarIcon(modifier = Modifier.size(24.dp))
                } else {
                    SuitIcon(suit = card.suit, modifier = Modifier.size(22.dp), isRed = card.suit.isRed)
                }
            }

            // Joker label badge
            if (isWildJoker || card.isPrintedJoker) {
                Surface(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    color = GoldAccent,
                    shape = RoundedCornerShape(3.dp)
                ) {
                    Text(
                        text = "JOKER",
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 2.dp, vertical = 1.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SuitIcon(suit: Suit, modifier: Modifier = Modifier, isRed: Boolean = false) {
    val color = if (isRed) CardRed else CardBlack
    Canvas(modifier = modifier) {
        when (suit) {
            Suit.SPADE -> drawSpade(size, color)
            Suit.HEART -> drawHeart(size, color)
            Suit.DIAMOND -> drawDiamond(size, color)
            Suit.CLUB -> drawClub(size, color)
            Suit.NONE -> {}
        }
    }
}

fun DrawScope.drawHeart(size: Size, color: Color) {
    val path = Path().apply {
        moveTo(size.width / 2f, size.height * 0.85f)
        cubicTo(
            size.width * 0.1f, size.height * 0.55f,
            0f, size.height * 0.25f,
            size.width * 0.25f, size.height * 0.1f
        )
        cubicTo(
            size.width * 0.45f, size.height * 0.05f,
            size.width / 2f, size.height * 0.3f,
            size.width / 2f, size.height * 0.35f
        )
        cubicTo(
            size.width / 2f, size.height * 0.3f,
            size.width * 0.55f, size.height * 0.05f,
            size.width * 0.75f, size.height * 0.1f
        )
        cubicTo(
            size.width, size.height * 0.25f,
            size.width * 0.9f, size.height * 0.55f,
            size.width / 2f, size.height * 0.85f
        )
        close()
    }
    drawPath(path, color)
}

fun DrawScope.drawSpade(size: Size, color: Color) {
    val path = Path().apply {
        moveTo(size.width / 2f, size.height * 0.1f)
        cubicTo(
            size.width * 0.9f, size.height * 0.45f,
            size.width * 0.8f, size.height * 0.75f,
            size.width / 2f, size.height * 0.7f
        )
        cubicTo(
            size.width * 0.2f, size.height * 0.75f,
            size.width * 0.1f, size.height * 0.45f,
            size.width / 2f, size.height * 0.1f
        )
        close()
    }
    drawPath(path, color)
    // Stem
    drawRect(
        color = color,
        topLeft = Offset(size.width * 0.45f, size.height * 0.65f),
        size = Size(size.width * 0.1f, size.height * 0.3f)
    )
}

fun DrawScope.drawDiamond(size: Size, color: Color) {
    val path = Path().apply {
        moveTo(size.width / 2f, 0f)
        lineTo(size.width, size.height / 2f)
        lineTo(size.width / 2f, size.height)
        lineTo(0f, size.height / 2f)
        close()
    }
    drawPath(path, color)
}

fun DrawScope.drawClub(size: Size, color: Color) {
    val radius = size.width * 0.22f
    drawCircle(color, radius, Offset(size.width / 2f, size.height * 0.3f))
    drawCircle(color, radius, Offset(size.width * 0.32f, size.height * 0.55f))
    drawCircle(color, radius, Offset(size.width * 0.68f, size.height * 0.55f))
    drawRect(
        color = color,
        topLeft = Offset(size.width * 0.43f, size.height * 0.5f),
        size = Size(size.width * 0.14f, size.height * 0.45f)
    )
}

@Composable
fun JokerStarIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        drawCircle(color = GoldAccent, radius = size.width / 2f, style = Fill)
        drawCircle(color = CardBlack, radius = size.width / 2.3f)
    }
}
