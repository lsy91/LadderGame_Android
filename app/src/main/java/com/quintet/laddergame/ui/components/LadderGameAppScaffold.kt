package com.quintet.laddergame.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.quintet.laddergame.R
import com.quintet.laddergame.ui.utils.BaseText
import com.quintet.laddergame.utils.LadderGameConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LadderGameAppScaffold(
    modifier: Modifier = Modifier,
    screenId: String,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState,
    content: @Composable () -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)

    Scaffold(
        snackbarHost = {
            LadderGameSnackBarHost(hostState = snackBarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val topBarTitle = when {
                        screenId.equals(LadderGameConstants.SET_PLAYER_SCREEN_ID, ignoreCase = true) -> {
                            stringResource(R.string.set_player_screen_title)
                        }
                        screenId.equals(LadderGameConstants.SET_WINNER_SCREEN_ID, ignoreCase = true) -> {
                            stringResource(R.string.set_winner_screen_title)
                        }
                        screenId.equals(LadderGameConstants.LADDER_GAME_SCREEN_ID, ignoreCase = true) -> {
                            stringResource(R.string.ladder_game_screen_title)
                        }
                        else -> { "" }
                    }

                    BaseText(
                        text = topBarTitle,
                        fontSize = 20
                    )
                },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            painter = painterResource(R.drawable.ic_menu),
                            contentDescription = stringResource(R.string.menu_icon_description)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /**TODO 게임 다시 하기 버튼 팝업 */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_more_vertical),
                            contentDescription = stringResource(R.string.more_icon_description)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                modifier = modifier
            )
        }
    ) { innerPadding ->
        Box(
           modifier = Modifier
               .padding(innerPadding)
               .fillMaxSize()
        ) {
            content()
        }
    }
}