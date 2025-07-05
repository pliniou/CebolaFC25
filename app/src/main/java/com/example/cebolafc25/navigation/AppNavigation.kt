package com.example.cebolafc25.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.cebolafc25.R
import com.example.cebolafc25.ui.screens.*

sealed class BottomNavItem(val route: String, val icon: ImageVector, val titleResId: Int) {
    data object Home : BottomNavItem("home", Icons.Default.Home, R.string.bottom_nav_home)
    data object Matches : BottomNavItem("matches", Icons.Default.SportsEsports, R.string.bottom_nav_matches)
    data object Tournaments : BottomNavItem("tournaments", Icons.Filled.EmojiEvents, R.string.bottom_nav_tournaments)
    data object Stats : BottomNavItem("stats", Icons.Filled.QueryStats, R.string.bottom_nav_stats)
    data object Players : BottomNavItem("players", Icons.Default.People, R.string.bottom_nav_players)
}

const val PLAYER_ID_ARG = "playerId"
const val PLAYER_DETAILS_ROUTE = "player_details"
const val TOURNAMENT_ID_ARG = "tournamentId"
const val TOURNAMENT_DETAILS_ROUTE = "tournament_details"


@Composable
fun AppNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Matches,
        BottomNavItem.Tournaments,
        BottomNavItem.Stats,
        BottomNavItem.Players,
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { item ->
                    val title = stringResource(id = item.titleResId)
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = title) },
                        label = { Text(title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen(navController = navController) }
            composable(BottomNavItem.Matches.route) { PartidasScreen() }
            composable(BottomNavItem.Tournaments.route) { CampeonatosScreen(navController = navController) }
            composable(BottomNavItem.Stats.route) { EstatisticasScreen() }
            composable(BottomNavItem.Players.route) { JogadoresScreen(navController = navController) }
            composable(
                route = "$PLAYER_DETAILS_ROUTE/{$PLAYER_ID_ARG}",
                arguments = listOf(navArgument(PLAYER_ID_ARG) { type = NavType.StringType })
            ) {
                JogadorDetalhesScreen(navController = navController)
            }
            composable(
                route = "$TOURNAMENT_DETAILS_ROUTE/{$TOURNAMENT_ID_ARG}",
                arguments = listOf(navArgument(TOURNAMENT_ID_ARG) { type = NavType.StringType })
            ) {
                CampeonatoDetalhesScreen(navController = navController)
            }
        }
    }
}