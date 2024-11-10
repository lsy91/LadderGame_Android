package com.quintet.laddergame.bean

import androidx.annotation.Keep

/**
 *  Player Info Data Class
 *
 * 2024-07-07 Created by sy.lee
 */
@Keep
data class Player(
    val playerCount: Int = 0,
    val playerNames: List<String> = listOf()
)