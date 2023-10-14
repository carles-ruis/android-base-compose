package com.carles.compose.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.carles.compose.R
import com.carles.compose.ui.Arguments.monsterId

sealed class Screen(
    @StringRes val label: Int,
    private val baseRoute: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val navigationItems: List<NavigationItem> = emptyList()
) {

    val route = buildString {
        append(baseRoute)
        arguments.forEach {
            append("/{")
            append(it.name)
            append("}")
        }
    }

    fun navigationRoute(vararg arguments: String) = buildString {
        append(baseRoute)
        arguments.forEach { arg ->
            append("/")
            append(arg)
        }
    }

    object Monsters : Screen(
        label = R.string.appname,
        baseRoute = "monsters_path",
        navigationItems = listOf(NavigationItem(Icons.Filled.Settings, R.string.settings, Settings))
    )

    object MonsterDetail : Screen(
        label = R.string.appname,
        baseRoute = "monster_detail_path",
        arguments = listOf(navArgument(monsterId) { NavType.StringType })
    )

    object Settings : Screen(label = R.string.settings, baseRoute = "settings_path")

}

object Arguments {
    const val monsterId = "monster_id"
}

