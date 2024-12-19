package com.quintet.laddergame.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.quintet.laddergame.ui.theme.LadderGameTheme
import kotlinx.coroutines.launch

@Composable
fun LadderGameApp() {

    LadderGameTheme {
        val navController = rememberNavController()
        val ladderGameActions = remember(navController) {
            LadderGameNavigationActions(navController)
        }

        // Coroutines Scope
        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: LadderGameDestinations.SET_PLAYER_ROUTE

        val drawerState = rememberDrawerState(DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerContent = {
                AppDrawer(
                    drawerState = drawerState,
                    closeDrawer = { coroutineScope.launch { drawerState.close() } }
                )
            },
            drawerState = drawerState
        ) {
            LadderGameNavGraph(
                navController = navController,
                openDrawer = { coroutineScope.launch { drawerState.open() } },
                navigateToScreen = ladderGameActions.navigateToScreen
            )
        }
    }
}