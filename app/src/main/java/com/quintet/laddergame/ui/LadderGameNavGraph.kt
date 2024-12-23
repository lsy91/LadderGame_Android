package com.quintet.laddergame.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quintet.laddergame.ui.LadderGameDestinations.GAME_ROUTE
import com.quintet.laddergame.ui.LadderGameDestinations.SET_PLAYER_ROUTE
import com.quintet.laddergame.ui.LadderGameDestinations.SET_WINNER_ROUTE
import com.quintet.laddergame.ui.game.GameRoute
import com.quintet.laddergame.ui.game.GameViewModel
import com.quintet.laddergame.ui.player.PlayerRoute
import com.quintet.laddergame.ui.player.PlayerViewModel
import com.quintet.laddergame.ui.winner.WinnerRoute
import com.quintet.laddergame.ui.winner.WinnerViewModel

@Composable
fun LadderGameNavGraph(
    modifier: Modifier = Modifier,
    setPlayerViewModel: PlayerViewModel = hiltViewModel(),
    setWinnerViewModel: WinnerViewModel = hiltViewModel(),
    gameViewModel: GameViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    navigateToScreen: (String) -> Unit,
    startDestination: String = SET_PLAYER_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = SET_PLAYER_ROUTE
        ) {
            PlayerRoute(
                playerViewModel = setPlayerViewModel,
                openDrawer = openDrawer,
                navigateToScreen = navigateToScreen
            )
        }

        composable(
            route = SET_WINNER_ROUTE
        ) {
            WinnerRoute(
                playerViewModel = setPlayerViewModel,
                winnerViewModel = setWinnerViewModel,
                openDrawer = openDrawer,
                navigateToScreen = navigateToScreen
            )
        }

        composable(
            route = GAME_ROUTE,
        ) {
            GameRoute(
                playerViewModel = setPlayerViewModel,
                winnerViewModel = setWinnerViewModel,
                gameViewModel = gameViewModel,
                openDrawer = openDrawer,
                navigateToScreen = navigateToScreen
            )
        }
    }
}