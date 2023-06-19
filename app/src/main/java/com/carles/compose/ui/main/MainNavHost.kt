package com.carles.compose.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.carles.compose.ui.hyrule.MonstersScreen
import com.carles.compose.ui.navigation.Destination
import com.carles.compose.ui.navigation.Navigate
import com.carles.compose.ui.navigation.Screen
import com.carles.compose.ui.navigation.defaultComposable
import com.carles.hyrule.ui.MonsterDetailScreen
import com.carles.compose.ui.settings.SettingsScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost

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