package com.meldmaster.rummy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meldmaster.rummy.game.engine.Card
import com.meldmaster.rummy.game.engine.MeldEvaluation
import com.meldmaster.rummy.game.engine.MeldValidator
import com.meldmaster.rummy.ui.canvas.PlayingCardView
import com.meldmaster.rummy.ui.canvas.ProceduralAvatar
import com.meldmaster.rummy.ui.theme.*

@Composable
fun GameTableScreen(
    userHand: List<List<Card>>,
    wildJoker: Card?,
    discardTop: Card?,
    isUserTurn: Boolean,
    onDrawClosed: () -> Unit,
    onDrawDiscard: () -> Unit,
    onDiscardCard: (Card) -> Unit,
    onDrop: () -> Unit,
    onShow: () -> Unit
) {
    var selectedCard by remember { mutableStateOf<Card?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF165B54), Color(0xFF092928), Color(0xFF041416))
                )
            )
    ) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(color = SurfaceCardColor, shape = RoundedCornerShape(8.dp)) {
                Text(
                    text = "Pool 101 | Practice Mode",
                    color = TextLight,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Text(
                text = if (isUserTurn) "Your Turn - Pick or Discard" else "Bot Thinking...",
                color = if (isUserTurn) GoldAccent else TextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Surface(color = SurfaceCardColor, shape = RoundedCornerShape(8.dp)) {
                Text(
                    text = "Points: 0",
                    color = TealSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // Center Area: Closed Deck, Wild Joker, Discard Pile
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Closed Stock Deck
            Box(
                modifier = Modifier
                    .width(58.dp)
                    .height(84.dp)
                    .border(1.dp, GoldAccent, RoundedCornerShape(6.dp))
                    .background(Color(0xFF1E3A40), RoundedCornerShape(6.dp))
                    .clickable(enabled = isUserTurn) { onDrawClosed() },
                contentAlignment = Alignment.Center
            ) {
                Text("DECK", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }

            // Open Discard Deck
            Box(
                modifier = Modifier
                    .width(58.dp)
                    .height(84.dp)
                    .clickable(enabled = isUserTurn && discardTop != null) { onDrawDiscard() }
            ) {
                if (discardTop != null) {
                    PlayingCardView(card = discardTop)
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Gray, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("DISCARD", fontSize = 9.sp, color = TextMuted)
                    }
                }
            }

            // Open Wild Joker indicator
            wildJoker?.let {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PlayingCardView(card = it, isWildJoker = true)
                    Text("WILD", fontSize = 9.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Player Hand & Melds at Bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            // Live Group Evaluations & Grouped Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                userHand.forEach { meldCards ->
                    val eval = MeldValidator.evaluateMeld(meldCards, wildJoker?.rank)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (eval) {
                                MeldEvaluation.PURE_SEQUENCE -> SuccessGreen
                                MeldEvaluation.SEQUENCE -> TealSecondary
                                MeldEvaluation.SET -> Color(0xFF3B82F6)
                                MeldEvaluation.INVALID -> DangerCoral
                            }
                        ) {
                            Text(
                                text = when (eval) {
                                    MeldEvaluation.PURE_SEQUENCE -> "Pure Seq"
                                    MeldEvaluation.SEQUENCE -> "Seq"
                                    MeldEvaluation.SET -> "Set"
                                    MeldEvaluation.INVALID -> "Invalid"
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy((-30).dp)) {
                            meldCards.forEach { card ->
                                PlayingCardView(
                                    card = card,
                                    isWildJoker = card.isJoker(wildJoker?.rank),
                                    isSelected = selectedCard == card,
                                    modifier = Modifier.clickable {
                                        selectedCard = if (selectedCard == card) null else card
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDrop,
                    colors = ButtonDefaults.buttonColors(containerColor = DangerCoral),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("DROP (20)", fontSize = 12.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            selectedCard?.let {
                                onDiscardCard(it)
                                selectedCard = null
                            }
                        },
                        enabled = isUserTurn && selectedCard != null,
                        colors = ButtonDefaults.buttonColors(containerColor = TealSecondary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("DISCARD", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onShow,
                        enabled = isUserTurn,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("SHOW / DECLARE", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
