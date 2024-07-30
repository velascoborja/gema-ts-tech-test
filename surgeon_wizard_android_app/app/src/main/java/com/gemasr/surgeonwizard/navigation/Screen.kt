package com.gemasr.surgeonwizard.navigation

sealed class Screen(val route: String) {
    data object ProcedureList : Screen("procedureList")

    data object FavoriteList : Screen("favoriteList")
}