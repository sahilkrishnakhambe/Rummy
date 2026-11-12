package com.meldmaster.rummy.game.engine

enum class MeldEvaluation {
    PURE_SEQUENCE,
    SEQUENCE,
    SET,
    INVALID
}

object MeldValidator {

    fun evaluateMeld(cards: List<Card>, wildJokerRank: Rank?): MeldEvaluation {
        if (cards.size < 3) return MeldEvaluation.INVALID
        if (isPureSequence(cards)) return MeldEvaluation.PURE_SEQUENCE
        if (isImpureSequence(cards, wildJokerRank)) return MeldEvaluation.SEQUENCE
        if (isValidSet(cards, wildJokerRank)) return MeldEvaluation.SET
        return MeldEvaluation.INVALID
    }

    fun isPureSequence(cards: List<Card>): Boolean {
        if (cards.size < 3) return false
        if (cards.any { it.isPrintedJoker }) return false
        val suit = cards.first().suit
        if (cards.any { it.suit != suit }) return false

        val sortedRanks = cards.map { it.rank.value }.sorted()
        var consecutive = true
        for (i in 0 until sortedRanks.size - 1) {
            if (sortedRanks[i + 1] != sortedRanks[i] + 1) {
                consecutive = false
                break
            }
        }
        if (consecutive) return true

        // Handle A-2-3 low Ace sequence (Ace value is 14 by default)
        if (sortedRanks.contains(Rank.ACE.value)) {
            val aceLowRanks = cards.map { if (it.rank == Rank.ACE) 1 else it.rank.value }.sorted()
            for (i in 0 until aceLowRanks.size - 1) {
                if (aceLowRanks[i + 1] != aceLowRanks[i] + 1) return false
            }
            return true
        }
        return false
    }

    fun isImpureSequence(cards: List<Card>, wildJokerRank: Rank?): Boolean {
        if (cards.size < 3) return false
        val regularCards = cards.filter { !it.isJoker(wildJokerRank) }
        val jokerCount = cards.size - regularCards.size
        if (regularCards.isEmpty()) return false

        val primarySuit = regularCards.first().suit
        if (regularCards.any { it.suit != primarySuit }) return false

        fun canFormChain(vals: List<Int>, jokers: Int): Boolean {
            val sorted = vals.distinct().sorted()
            if (sorted.size != vals.size) return false // Duplicate card values can't form a run
            var neededJokers = 0
            for (i in 0 until sorted.size - 1) {
                val gap = sorted[i + 1] - sorted[i] - 1
                if (gap < 0) return false
                neededJokers += gap
            }
            return neededJokers <= jokers
        }

        val normalValues = regularCards.map { it.rank.value }
        if (canFormChain(normalValues, jokerCount)) return true

        // Check Low Ace possibility
        val lowAceValues = regularCards.map { if (it.rank == Rank.ACE) 1 else it.rank.value }
        return canFormChain(lowAceValues, jokerCount)
    }

    fun isValidSet(cards: List<Card>, wildJokerRank: Rank?): Boolean {
        if (cards.size !in 3..4) return false
        val regularCards = cards.filter { !it.isJoker(wildJokerRank) }
        if (regularCards.isEmpty()) return true

        val targetRank = regularCards.first().rank
        if (regularCards.any { it.rank != targetRank }) return false

        // In standard 2-deck rummy, a set cannot have two identical cards of the same suit
        val suits = regularCards.map { it.suit }
        return suits.distinct().size == suits.size
    }

    fun validateDeclaration(melds: List<List<Card>>, wildJokerRank: Rank?): Boolean {
        if (melds.isEmpty()) return false
        val evaluations = melds.map { evaluateMeld(it, wildJokerRank) }
        val hasPure = evaluations.contains(MeldEvaluation.PURE_SEQUENCE)
        val sequenceCount = evaluations.count { 
            it == MeldEvaluation.PURE_SEQUENCE || it == MeldEvaluation.SEQUENCE 
        }

        return hasPure && sequenceCount >= 2 && evaluations.none { it == MeldEvaluation.INVALID }
    }
}
