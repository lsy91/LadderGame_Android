package com.quintet.laddergame.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quintet.laddergame.data.game.impl.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {
    // StateFlow for the UI state
    private val _uiState = MutableStateFlow(
        GameState(
            ladders = emptyList(),
            errorMessages = emptyList()
        )
    )
    val gameUiState: StateFlow<GameState> = _uiState.asStateFlow()

    // Process user events
    fun processEvent(intent: GameIntent) {
        viewModelScope.launch {
            when (intent) {
                is GameIntent.LoadLadders -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}