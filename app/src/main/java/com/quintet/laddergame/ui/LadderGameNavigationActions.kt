package com.quintet.laddergame.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Destinations used in the [LadderGame].
 */
object LadderGameDestinations {
    const val SET_PLAYER_ROUTE = "set_player"
    const val SET_WINNER_ROUTE = "set_winner"
    const val GAME_ROUTE = "game"
}

/**
 * Models the navigation actions in the app.
 */
class LadderGameNavigationActions(navController: NavHostController) {
    /**
     * 홈 이동
     */
    val navigateToHome: () -> Unit = {
        navController.navigate(LadderGameDestinations.SET_PLAYER_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id)

            launchSingleTop = true
        }
    }

    /**
     * 화면 간 자유 이동
     *
     * @param route
     */
    val navigateToScreen: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}