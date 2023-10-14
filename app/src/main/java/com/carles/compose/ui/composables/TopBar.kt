package com.carles.compose.ui.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.carles.compose.R
import com.carles.compose.ui.NavigationItem
import com.carles.compose.ui.Screen
import com.carles.compose.ui.theme.HyruleTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun TopBar(
    title: String,
    showBackButton: Boolean,
    navigationItems: List<NavigationItem>,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    navigateTo: (Screen, Array<String>) -> Unit = { _, _ -> },
) {
    TopAppBar(
        title = {
            AnimatedContent(
                targetState = title,
                transitionSpec = { fadeIn(tween()) with fadeOut(tween()) },
                label = "AnimatedContent:TopBar"
            ) { target ->
                Text(
                    text = target,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.testTag(stringResource(R.string.tag_top_bar_title))
                )
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.Filled.ArrowBack, stringResource(R.string.description_back_arrow))
                }
            }
        },
        actions = {
            navigationItems.forEach { item ->
                IconButton(
                    onClick = { navigateTo(item.screen, item.arguments) }) {
                    Icon(item.icon, stringResource(item.description))
                }
            }
        },
        modifier = modifier.testTag(stringResource(R.string.tag_top_bar))
    )
}

@Preview
@Composable
private fun TopBar_SettingsScreen() {
    HyruleTheme {
        TopBar(
            title = stringResource(id = R.string.settings),
            showBackButton = true,
            navigationItems = Screen.Settings.navigationItems
        )
    }
}

@Preview
@Composable
private fun TopBar_MonstersScreen() {
    HyruleTheme {
        TopBar(
            title = stringResource(id = R.string.appname),
            showBackButton = false,
            navigationItems = emptyList()
        )
    }
}