package com.quintet.laddergame.ui.player

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.quintet.laddergame.ui.components.LadderGameAppScaffold

@Composable
fun PlayerRoute(
    playerViewModel: PlayerViewModel,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    // UiState of the Set Player Screen
    val playerUiState by playerViewModel.playerUiState.collectAsStateWithLifecycle()

    PlayerRoute(
        uiState = playerUiState,
        openDrawer = openDrawer,
        snackBarHostState = snackBarHostState
    )
}

@Composable
fun PlayerRoute(
    uiState: PlayerState,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    LadderGameAppScaffold(
        openDrawer = openDrawer,
        snackBarHostState = snackBarHostState
    ) {
        PlayerScreen(
            uiState = uiState,
            openDrawer = openDrawer,
            snackBarHostState = snackBarHostState
        )
    }
}

