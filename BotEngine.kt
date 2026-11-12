package com.meldmaster.rummy.game.ai

import com.meldmaster.rummy.game.engine.*
import kotlin.random.Random

class BotEngine(private val difficulty: Difficulty = Difficulty.MEDIUM) {

    enum class Difficulty { EASY, MEDIUM, HARD }

    fun decideTurnAction(
        botHand: List<Card>,
        discardTop: Card?,
        wildJoker: Rank?
    ): DrawChoice {
        if (discardTop == null) return DrawChoice.DRAW_CLOSED

        val improvesHand = wouldCardBenefit(botHand, discardTop, wildJoker)
        return when (difficulty) {
            Difficulty.EASY -> if (Random.nextFloat() > 0.6f) DrawChoice.DRAW_DISCARD else DrawChoice.DRAW_CLOSED
            Difficulty.MEDIUM -> if (improvesHand || discardTop.isJoker(wildJoker)) DrawChoice.DRAW_DISCARD else DrawChoice.DRAW_CLOSED
            Difficulty.HARD -> if (improvesHand) DrawChoice.DRAW_DISCARD else DrawChoice.DRAW_CLOSED
        }
    }

    fun chooseDiscardCard(hand: List<Card>, wildJoker: Rank?): Card {
        // Never discard a joker unless impossible
        val nonJokers = hand.filter { !it.isJoker(wildJoker) }
        if (nonJokers.isEmpty()) return hand.first()

        // Discard high-point, isolated cards
        val isolatedCards = nonJokers.filter { card ->
            nonJokers.none { other -> other != card && other.suit == card.suit && kotlin.math.abs(other.rank.value - card.rank.value) <= 2 }
        }

        return if (isolatedCards.isNotEmpty()) {
            isolatedCards.maxByOrNull { it.getPoints(wildJoker) } ?: nonJokers.first()
        } else {
            nonJokers.maxByOrNull { it.getPoints(wildJoker) } ?: nonJokers.first()
        }
    }

    private fun wouldCardBenefit(hand: List<Card>, target: Card, wildJoker: Rank?): Boolean {
        if (target.isJoker(wildJoker)) return true
        val matchedSuit = hand.filter { it.suit == target.suit }
        return matchedSuit.any { kotlin.math.abs(it.rank.value - target.rank.value) == 1 }
    }

    enum class DrawChoice { DRAW_CLOSED, DRAW_DISCARD }
}
