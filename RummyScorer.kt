package com.meldmaster.rummy.game.engine

object RummyScorer {
    const val MAX_PENALTY = 80
    const val FIRST_DROP_PENALTY = 20
    const val MIDDLE_DROP_PENALTY = 40
    const val WRONG_SHOW_PENALTY = 80

    fun calculateHandScore(melds: List<List<Card>>, wildJokerRank: Rank?): Int {
        val evaluations = melds.map { MeldValidator.evaluateMeld(it, wildJokerRank) }
        val hasPureSequence = evaluations.contains(MeldEvaluation.PURE_SEQUENCE)
        val sequenceCount = evaluations.count { 
            it == MeldEvaluation.PURE_SEQUENCE || it == MeldEvaluation.SEQUENCE 
        }

        // If no pure sequence, player counts all cards (except actual jokers)
        if (!hasPureSequence) {
            val total = melds.flatten().sumOf { it.getPoints(wildJokerRank) }
            return total.coerceAtMost(MAX_PENALTY)
        }

        // If at least one pure sequence, pure sequences are exempt from score
        var totalScore = 0
        melds.forEachIndexed { index, cards ->
            val eval = evaluations[index]
            if (eval == MeldEvaluation.INVALID || (eval == MeldEvaluation.SET && sequenceCount < 2)) {
                totalScore += cards.sumOf { it.getPoints(wildJokerRank) }
            }
        }
        return totalScore.coerceAtMost(MAX_PENALTY)
    }
}
