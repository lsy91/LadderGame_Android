package com.quintet.laddergame.data.player

import com.quintet.laddergame.model.Player

/**
 * Player Repository Interface
 *
 */
interface IPlayerRepository {
    suspend fun loadPlayers(inputPlayerCount: Int): List<Player>
}