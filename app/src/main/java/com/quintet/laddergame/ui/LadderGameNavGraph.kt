package com.quintet.laddergame.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.reflect.TypeToken
import com.quintet.laddergame.model.Player
import com.quintet.laddergame.model.Winner
import com.quintet.laddergame.ui.LadderGameDestinations.SET_PLAYER_ROUTE
import com.quintet.laddergame.ui.LadderGameDestinations.SET_WINNER_ROUTE
import com.quintet.laddergame.ui.game.GameScreen
import com.quintet.laddergame.ui.game.GameViewModel
import com.quintet.laddergame.ui.player.PlayerRoute
import com.quintet.laddergame.ui.player.PlayerViewModel
import com.quintet.laddergame.ui.winner.WinnerRoute
import com.quintet.laddergame.ui.winner.WinnerViewModel
import com.quintet.laddergame.utils.LadderGameUtils

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
            route = "LadderGameView" + "/" + "{playerInfo}" + "/" + "{winnerInfo}",
            arguments = listOf(
                navArgument("playerInfo") { type = NavType.StringType; defaultValue = ""},
                navArgument("winnerInfo") { type = NavType.StringType; defaultValue = ""},
            )
        ) { navBackStackEntry ->

            // TODO ViewModel 에서 State 선언

            val playerInfoJson = navBackStackEntry.arguments?.getString("playerInfo")
            val playerInfoToken = object : TypeToken<Player>() {}.type
            val selectedPlayerInfo = LadderGameUtils.convertJSONToObj<Player>(playerInfoJson, playerInfoToken)

            val winnerInfoJson = navBackStackEntry.arguments?.getString("winnerInfo")
            val winnerInfoToken = object : TypeToken<Winner>() {}.type
            val selectedWinnerInfo = LadderGameUtils.convertJSONToObj<Winner>(winnerInfoJson, winnerInfoToken)

            GameScreen(
                playerInfo = selectedPlayerInfo ?: Player(0, 0),
                winnerInfo = selectedWinnerInfo ?: Winner()
            )
        }
    }
}