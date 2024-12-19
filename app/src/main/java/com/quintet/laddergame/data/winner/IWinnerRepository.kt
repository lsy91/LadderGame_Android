package com.quintet.laddergame.data.winner

import com.quintet.laddergame.model.Winner

/**
 * Winner Repository Interface
 *
 */
interface IWinnerRepository {
    suspend fun loadWinners(inputPlayerCount: Int, nothingCount: Int): List<Winner>
}