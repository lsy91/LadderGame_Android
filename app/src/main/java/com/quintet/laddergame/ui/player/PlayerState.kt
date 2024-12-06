package com.quintet.laddergame.ui.player

data class PlayerState(
    val playerCount: Int = 0,
    val errorMessages: List<String> = emptyList(),
)