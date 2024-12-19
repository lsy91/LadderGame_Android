package com.quintet.laddergame.ui.player

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quintet.laddergame.ui.components.LadderGameAppScaffold
import com.quintet.laddergame.utils.LadderGameConstants

@Composable
fun PlayerRoute(
    playerViewModel: PlayerViewModel,
    openDrawer: () -> Unit,
    navigateToScreen: (String) -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    // UiState of the Set Player Screen
    val playerUiState by playerViewModel.playerUiState.collectAsStateWithLifecycle()

    PlayerRoute(
        uiState = playerUiState,
        openDrawer = openDrawer,
        navigateToScreen = navigateToScreen,
        snackBarHostState = snackBarHostState,
        onEvent = { intent -> playerViewModel.processEvent(intent) }
    )
}

@Composable
fun PlayerRoute(
    uiState: PlayerState,
    openDrawer: () -> Unit,
    navigateToScreen: (String) -> Unit,
    snackBarHostState: SnackbarHostState,
    onEvent: (PlayerIntent) -> Unit,
) {
    LadderGameAppScaffold(
        screenId = LadderGameConstants.SET_PLAYER_SCREEN_ID,
        openDrawer = openDrawer,
        snackBarHostState = snackBarHostState
    ) {
        PlayerScreen(
            uiState = uiState,
            navigateToScreen = navigateToScreen,
            onEvent = onEvent
        )
    }
}

