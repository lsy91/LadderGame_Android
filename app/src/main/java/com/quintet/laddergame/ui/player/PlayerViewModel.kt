package com.quintet.laddergame.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quintet.laddergame.data.player.impl.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
): ViewModel() {
    // StateFlow for the UI state
    private val _uiState = MutableStateFlow(
        PlayerState(
            errorMessages = emptyList(),
            playerCount = 0
        )
    )
    val playerUiState: StateFlow<PlayerState> = _uiState.asStateFlow()

    // Process user events
    fun processEvent(intent: PlayerIntent) {
        when (intent) {
            is PlayerIntent.LoadPlayers -> loadPlayers(intent.playerCount)
        }
    }

    private fun loadPlayers(playerCount: Int) {
        _uiState.update {
            it.copy(
                playerCount = playerCount
            )
        }

    }
}