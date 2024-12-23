package com.quintet.laddergame.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quintet.laddergame.data.game.impl.GameRepository
import com.quintet.laddergame.model.PlayerPosition
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

                    val ladders = gameRepository.generateLadders(intent.playerCount)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            ladders = ladders
                        )
                    }
                }
                is GameIntent.SetPlayerPosition -> {

                    val playerPositionList = mutableListOf<PlayerPosition>()

                    // ladderData에서 각 플레이어의 세로줄을 추출하여 PlayerGameInfo 생성
                    intent.ladders.filter { it.vertical != null } // 세로줄만 선택
                        .forEach { ladderLine ->
                            // 각 세로줄의 시작점과 끝점 정보를 가져옴
                            val startPoint = ladderLine.vertical?.start
                            val endPoint = ladderLine.vertical?.end

                            // PlayerGameInfo 생성하여 리스트에 추가
                            if (startPoint != null && endPoint != null) {
                                playerPositionList.add(
                                    PlayerPosition(
                                        startPoint = startPoint,
                                        endPoint = endPoint
                                    )
                                )
                            }
                        }

                    // TODO
                }
            }
        }
    }
}