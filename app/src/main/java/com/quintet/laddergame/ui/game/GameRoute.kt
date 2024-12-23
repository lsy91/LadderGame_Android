package com.quintet.laddergame.ui.game

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quintet.laddergame.model.Player
import com.quintet.laddergame.model.Winner
import com.quintet.laddergame.ui.components.LadderGameAppScaffold
import com.quintet.laddergame.ui.player.PlayerViewModel
import com.quintet.laddergame.ui.winner.WinnerViewModel
import com.quintet.laddergame.utils.LadderGameConstants

@Composable
fun GameRoute(
    playerViewModel: PlayerViewModel,
    winnerViewModel: WinnerViewModel,
    gameViewModel: GameViewModel,
    openDrawer: () -> Unit,
    navigateToScreen: (String) -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    // PlayerUiState
    val playerUiState by playerViewModel.playerUiState.collectAsStateWithLifecycle()
    // WinnerUiState
    val winnerUiState by winnerViewModel.winnerUiState.collectAsStateWithLifecycle()
    // UiState of the Game Screen
    val gameUiState by gameViewModel.gameUiState.collectAsStateWithLifecycle()

    GameRoute(
        uiState = gameUiState,
        players = playerUiState.players,
        winners = winnerUiState.winners,
        openDrawer = openDrawer,
        navigateToScreen = navigateToScreen,
        snackBarHostState = snackBarHostState,
        onEvent = { intent -> gameViewModel.processEvent(intent) }
    )
}

@Composable
fun GameRoute(
    uiState: GameState,
    players: List<Player>,
    winners: List<Winner>,
    openDrawer: () -> Unit,
    navigateToScreen: (String) -> Unit,
    snackBarHostState: SnackbarHostState,
    onEvent: (GameIntent) -> Unit,
) {
    LadderGameAppScaffold(
        screenId = LadderGameConstants.LADDER_GAME_SCREEN_ID,
        openDrawer = openDrawer,
        snackBarHostState = snackBarHostState
    ) {
        GameScreen(
            uiState = uiState,
            players = players,
            winners = winners,
            navigateToScreen = navigateToScreen,
            onEvent = onEvent
        )
    }
}