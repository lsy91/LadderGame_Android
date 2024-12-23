package com.quintet.laddergame.data.game

import com.quintet.laddergame.model.LadderLine

/**
 * Game Repository Interface
 *
 */
interface IGameRepository {
    suspend fun generateLadders(playerCount: Int): List<LadderLine>
}