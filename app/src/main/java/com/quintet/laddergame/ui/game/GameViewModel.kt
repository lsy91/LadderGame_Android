package com.quintet.laddergame.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quintet.laddergame.data.game.impl.GameRepository
import com.quintet.laddergame.model.Player
import com.quintet.laddergame.model.Winner
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
            players = emptyList(),
            winners = emptyList(),
            ladders = emptyList(),
            errorMessages = emptyList()
        )
    )
    val gameUiState: StateFlow<GameState> = _uiState.asStateFlow()

    // Process user events
    fun processEvent(intent: GameIntent) {
        viewModelScope.launch {
            when (intent) {
                is GameIntent.InitData -> {
                    _uiState.update {
                        it.copy(
                            players = intent.players,
                            winners = intent.winners
                        )
                    }
                }
                is GameIntent.LoadLadders -> {

                    val ladders = gameRepository.generateLadders(intent.playerCount)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            ladders = ladders
                        )
                    }
                }
                is GameIntent.SetPlayerAndWinnerPosition -> {

                    val updatedPlayers = mutableListOf<Player>()
                    val updatedWinners = mutableListOf<Winner>()

                    intent.ladders.filter { it.vertical != null } // 세로줄만 선택
                        .forEachIndexed { index, ladderLine ->
                            val startPoint = ladderLine.vertical?.start
                            val endPoint = ladderLine.vertical?.end

                            // players 리스트 병합
                            if (index < _uiState.value.players.size && startPoint != null) {
                                updatedPlayers.add(_uiState.value.players[index].copy(startPoint = startPoint))
                            } else {
                                updatedPlayers.add(_uiState.value.players.getOrElse(index) { Player(0, 0) })
                            }

                            // winners 리스트 병합
                            if (index < _uiState.value.winners.size && endPoint != null) {
                                updatedWinners.add(_uiState.value.winners[index].copy(point = endPoint))
                            } else {
                                updatedWinners.add(_uiState.value.winners.getOrElse(index) { Winner() })
                            }
                        }

                    // 상태 업데이트
                    _uiState.update {
                        it.copy(
                            players = updatedPlayers,
                            winners = updatedWinners
                        )
                    }
                }
            }
        }
    }
}