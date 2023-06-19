package com.carles.compose.ui.navigation

import androidx.navigation.NavHostController
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigate @Inject constructor() {

    lateinit var navController: NavHostController

    fun to(destination: Destination) {
        when (destination) {
            Destination.Back -> up()
            Destination.Settings -> toSettings()
            is Destination.MonsterDetail -> toMonsterDetail(destination.monsterId.toString())
        }
    }

    private fun toSettings() {
        navController.navigate(Screen.Settings.navigationRoute())
    }

    private fun toMonsterDetail(id: String) {
        navController.navigate(Screen.MonsterDetail.navigationRoute(id))
    }

    private fun up() {
        navController.popBackStack()
    }
}