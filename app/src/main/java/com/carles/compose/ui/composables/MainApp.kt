package com.carles.compose.ui.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import com.carles.compose.ui.navigation.Navigate
import com.carles.compose.ui.navigation.Screen
import com.carles.compose.ui.navigation.screens
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainApp(navigate: Navigate) {
    val context = LocalContext.current

    val navController = rememberAnimatedNavController()
    navigate.navController = navController
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = screens.find { it.route == currentBackStackEntry?.destination?.route } ?: Screen.Monsters

    val showBackButton by remember(currentBackStackEntry) {
        derivedStateOf { navController.previousBackStackEntry != null }
    }

    var topBarTitle by remember(currentScreen) {
        mutableStateOf(context.getString(currentScreen.label))
    }
    val changeTitle: (String) -> Unit = { topBarTitle = it }

    Scaffold(
        topBar = {
            TopBar(
                title = topBarTitle,
                showBackButton = showBackButton,
                menuItems = currentScreen.menuItems
            ) { destination ->
                navigate.to(destination)
            }
        }) { innerPadding ->
        MainNavHost(navigate, changeTitle, Modifier.padding(innerPadding))
    }
}