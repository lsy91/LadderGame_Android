package com.quintet.laddergame.ui.winner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quintet.laddergame.data.winner.impl.WinnerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WinnerViewModel @Inject constructor(
    private val winnerRepository: WinnerRepository
): ViewModel() {
    // StateFlow for the UI state
    private val _uiState = MutableStateFlow(
        WinnerState(
            winners = emptyList(),
            errorMessages = emptyList()
        )
    )
    val winnerUiState: StateFlow<WinnerState> = _uiState.asStateFlow()

    // Process user events
    fun processEvent(intent: WinnerIntent) {
        viewModelScope.launch {
            when (intent) {
                is WinnerIntent.LoadWinners -> {
                    _uiState.update {
                        it.copy(
                            winners = winnerRepository.loadWinners(intent.inputWinnerCount, intent.nothingCount)
                        )
                    }
                }
                is WinnerIntent.ClearWinners -> {
                    _uiState.update {
                        it.copy(
                            winners = emptyList()
                        )
                    }
                }
            }
        }
    }
}