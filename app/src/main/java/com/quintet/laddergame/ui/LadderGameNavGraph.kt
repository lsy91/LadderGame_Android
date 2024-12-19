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
import com.quintet.laddergame.ui.game.GameScreen
import com.quintet.laddergame.ui.game.GameViewModel
import com.quintet.laddergame.ui.player.PlayerRoute
import com.quintet.laddergame.ui.player.PlayerViewModel
import com.quintet.laddergame.ui.winner.WinnerScreen
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
    startDestination: String = LadderGameDestinations.SET_PLAYER_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = LadderGameDestinations.SET_PLAYER_ROUTE
        ) {
            PlayerRoute(
                playerViewModel = setPlayerViewModel,
                openDrawer = openDrawer
            )
        }

        composable(
            route = "SelectWinnerCount" + "/" + "{playerInfo}",
            arguments = listOf( navArgument("playerInfo") { type = NavType.StringType; defaultValue = ""} )
        ) { navBackStackEntry ->

            // TODO ViewModel 에서 State 선언

            val playerInfoJson = navBackStackEntry.arguments?.getString("playerInfo")
            val playerInfoToken = object : TypeToken<Player>() {}.type
            val selectedPlayerInfo = LadderGameUtils.convertJSONToObj<Player>(playerInfoJson, playerInfoToken)

            WinnerScreen(
                playerCount = selectedPlayerInfo?.playerIndex ?: 0,
                onSelectedGameInfo = { winnerCount, winnerTitles ->

                    val winnerInfo = Winner(
                        winnerCount = winnerCount,
                        // 당첨 설정된 만큼을 제외하고, 플레이어 수 차이만큼 남는 Prize 요소는 "꽝" 으로 미리 만들어둔다. 그래야 그릴 때 shuffle 이 먹힌다.
                        winnerPrizes = (winnerTitles + List((selectedPlayerInfo?.playerIndex ?: 0) - winnerTitles.size) { "꽝" })
                    )

                    navController.navigate("LadderGameView"
                            + "/"
                            + "${LadderGameUtils.convertObjToJSON(selectedPlayerInfo)}"
                            + "/"
                            + "${LadderGameUtils.convertObjToJSON(winnerInfo)}")
                }
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