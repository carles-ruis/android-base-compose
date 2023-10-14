package com.carles.compose.ui.composables

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
import androidx.navigation.compose.rememberNavController
import com.carles.compose.ui.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val context = LocalContext.current

    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val screen = screens.find { it.route == currentBackStackEntry?.destination?.route } ?: Screen.Monsters

    val showBackButton by remember(currentBackStackEntry) {
        derivedStateOf { navController.previousBackStackEntry != null }
    }
    val navigateUp: () -> Unit = { navController.popBackStack() }

    var topBarTitle by remember(screen) {
        mutableStateOf(context.getString(screen.label))
    }
    val changeTitle: (String) -> Unit = { topBarTitle = it }

    Scaffold(
        topBar = {
            TopBar(
                title = topBarTitle,
                showBackButton = showBackButton,
                navigationItems = screen.navigationItems,
                navigateUp = navigateUp,
                navigateTo = { screen, arguments -> navController.navigate(screen.navigationRoute(*arguments)) }
            )
        }) { innerPadding ->
        MainNavHost(navController, changeTitle, Modifier.padding(innerPadding))
    }
}

private val screens = listOf(Screen.Monsters, Screen.MonsterDetail, Screen.Settings)