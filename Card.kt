package com.meldmaster.rummy.game.engine

enum class Suit(val symbol: String, val isRed: Boolean) {
    SPADE("♠", false),
    HEART("♥", true),
    CLUB("♣", false),
    DIAMOND("♦", true),
    NONE("", false)
}

enum class Rank(val value: Int, val label: String) {
    TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"),
    SIX(6, "6"), SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"),
    TEN(10, "10"), JACK(11, "J"), QUEEN(12, "Q"), KING(13, "K"), ACE(14, "A"),
    JOKER(0, "★")
}

data class Card(
    val id: Int,
    val suit: Suit,
    val rank: Rank,
    val isPrintedJoker: Boolean = false
) {
    fun getPoints(wildJokerRank: Rank?): Int {
        if (isPrintedJoker || rank == wildJokerRank) return 0
        return when (rank) {
            Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK, Rank.TEN -> 10
            else -> rank.value
        }
    }

    fun isJoker(wildJokerRank: Rank?): Boolean {
        return isPrintedJoker || (wildJokerRank != null && rank == wildJokerRank)
    }
}
