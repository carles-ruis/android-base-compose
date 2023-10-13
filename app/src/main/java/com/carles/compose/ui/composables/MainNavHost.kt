package com.carles.compose.ui.composables

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.carles.compose.ui.hyrule.MonstersScreen
import com.carles.compose.ui.navigation.Destination
import com.carles.compose.ui.navigation.Navigate
import com.carles.compose.ui.navigation.Screen
import com.carles.compose.ui.settings.SettingsScreen
import com.carles.compose.ui.hyrule.MonsterDetailScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(navigate: Navigate, changeTitle: (String) -> Unit, modifier: Modifier = Modifier) {

    AnimatedNavHost(
        navController = navigate.navController,
        startDestination = Screen.Monsters.route,
        modifier = modifier
    ) {
        monstersDestination { id -> navigate.to(Destination.MonsterDetail(id)) }
        monsterDetailDestination(changeTitle) { navigate.to(Destination.Back) }
        settingsDestination()
    }
}

private fun NavGraphBuilder.monstersDestination(onMonsterClick: (Int) -> Unit) {
    defaultComposable(Screen.Monsters.route, Screen.Monsters.arguments) {
        MonstersScreen(
            viewModel = hiltViewModel(),
            onMonsterClick = onMonsterClick
        )
    }
}

private fun NavGraphBuilder.monsterDetailDestination(changeTitle: (String) -> Unit, navigateUp: () -> Unit) {
    defaultComposable(Screen.MonsterDetail.route, Screen.MonsterDetail.arguments) {
        MonsterDetailScreen(
            viewModel = hiltViewModel(),
            changeTitle = changeTitle,
            navigateUp = navigateUp
        )
    }
}

private fun NavGraphBuilder.settingsDestination() {
    defaultComposable(Screen.Settings.route, Screen.Settings.arguments) {
        SettingsScreen(viewModel = hiltViewModel())
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.defaultComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = { slideInFromRightToLeft() },
        exitTransition = { slideOutFromRightToLeft() },
        popEnterTransition = { slideInFromLeftToRight() },
        popExitTransition = { slideOutFromLeftToRight() }
    ) { backStackEntry ->
        content(backStackEntry)
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.slideInFromRightToLeft() = slideInHorizontally(initialOffsetX = { it })

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.slideOutFromRightToLeft() = slideOutHorizontally(targetOffsetX = { -it })

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.slideInFromLeftToRight() = slideInHorizontally(initialOffsetX = { -it })

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.slideOutFromLeftToRight() = slideOutHorizontally(targetOffsetX = { it })
