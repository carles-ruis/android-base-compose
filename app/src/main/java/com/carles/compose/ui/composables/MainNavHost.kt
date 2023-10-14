package com.carles.compose.ui.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.carles.compose.ui.Screen
import com.carles.compose.ui.hyrule.MonsterDetailScreen
import com.carles.compose.ui.hyrule.MonstersScreen
import com.carles.compose.ui.settings.SettingsScreen

@Composable
fun MainNavHost(navController: NavHostController, changeTitle: (String) -> Unit, modifier: Modifier = Modifier) {
    val toMonsterDetail: (Int) -> Unit = { id -> navController.navigate(Screen.MonsterDetail.navigationRoute(id.toString())) }
    val navigateUp: () -> Unit = { navController.popBackStack() }

    NavHost(
        navController = navController,
        startDestination = Screen.Monsters.route,
        modifier = modifier
    ) {
        monstersDestination { id -> toMonsterDetail(id) }
        monsterDetailDestination(changeTitle) { navigateUp() }
        settingsDestination()
    }
}

private fun NavGraphBuilder.monstersDestination(onMonsterClick: (Int) -> Unit) {
    composableWithTransition(Screen.Monsters.route, Screen.Monsters.arguments) {
        MonstersScreen(
            viewModel = hiltViewModel(),
            onMonsterClick = onMonsterClick
        )
    }
}

private fun NavGraphBuilder.monsterDetailDestination(changeTitle: (String) -> Unit, navigateUp: () -> Unit) {
    composableWithTransition(Screen.MonsterDetail.route, Screen.MonsterDetail.arguments) {
        MonsterDetailScreen(
            viewModel = hiltViewModel(),
            changeTitle = changeTitle,
            navigateUp = navigateUp
        )
    }
}

private fun NavGraphBuilder.settingsDestination() {
    composableWithTransition(Screen.Settings.route, Screen.Settings.arguments) {
        SettingsScreen(viewModel = hiltViewModel())
    }
}

private fun NavGraphBuilder.composableWithTransition(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
    ) { backStackEntry ->
        content(backStackEntry)
    }
}
