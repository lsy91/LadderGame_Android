package com.quintet.laddergame.ui.winner

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quintet.laddergame.ui.components.LadderGameAppScaffold
import com.quintet.laddergame.ui.player.PlayerViewModel
import com.quintet.laddergame.utils.LadderGameConstants

@Composable
fun WinnerRoute(
    playerViewModel: PlayerViewModel,
    winnerViewModel: WinnerViewModel,
    openDrawer: () -> Unit,
    navigateToScreen: (String) -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    // PlayerUiState
    val playerUiState by playerViewModel.playerUiState.collectAsStateWithLifecycle()
    // UiState of the Set Winner Screen
    val winnerUiState by winnerViewModel.winnerUiState.collectAsStateWithLifecycle()

    WinnerRoute(
        uiState = winnerUiState,
        playerCount = playerUiState.players.size,
        openDrawer = openDrawer,
        navigateToScreen = navigateToScreen,
        snackBarHostState = snackBarHostState,
        onEvent = { intent -> winnerViewModel.processEvent(intent) }
    )
}

@Composable
fun WinnerRoute(
    uiState: WinnerState,
    playerCount: Int,
    openDrawer: () -> Unit,
    navigateToScreen: (String) -> Unit,
    snackBarHostState: SnackbarHostState,
    onEvent: (WinnerIntent) -> Unit,
) {
    LadderGameAppScaffold(
        screenId = LadderGameConstants.SET_WINNER_SCREEN_ID,
        openDrawer = openDrawer,
        snackBarHostState = snackBarHostState
    ) {
        WinnerScreen(
            uiState = uiState,
            playerCount = playerCount,
            navigateToScreen = navigateToScreen,
            onEvent = onEvent
        )
    }
}