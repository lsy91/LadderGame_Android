package com.quintet.laddergame.ui.player

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun PlayerRoute(
    playerViewModel: PlayerViewModel,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    // UiState of the Set Player Screen
    val playerUiState by playerViewModel.playerUiState.collectAsState()

}