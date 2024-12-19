package com.quintet.laddergame.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quintet.laddergame.data.player.impl.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
): ViewModel() {
    // StateFlow for the UI state
    private val _uiState = MutableStateFlow(
        PlayerState(
            players = emptyList(),
            errorMessages = emptyList()
        )
    )
    val playerUiState: StateFlow<PlayerState> = _uiState.asStateFlow()

    // Process user events
    fun processEvent(intent: PlayerIntent) {
        viewModelScope.launch {
            when (intent) {
                is PlayerIntent.LoadPlayers -> {
                    _uiState.update {
                        it.copy(
                            players = playerRepository.loadPlayers(intent.inputPlayerCount)
                        )
                    }
                }
                is PlayerIntent.ClearPlayers -> {
                    _uiState.update {
                        it.copy(
                            players = emptyList()
                        )
                    }
                }
            }
        }
    }
}