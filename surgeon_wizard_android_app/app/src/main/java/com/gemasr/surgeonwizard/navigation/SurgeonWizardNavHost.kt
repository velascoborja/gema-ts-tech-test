package com.gemasr.surgeonwizard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gemasr.surgeonwizard.procedures.list.ui.ProcedureListScreen

@Composable
fun SurgeonWizardNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ProcedureList.route,
        modifier = Modifier.padding(innerPadding),
    ) {
        composable(Screen.ProcedureList.route) {
            ProcedureListScreen(snackbarHostState = snackbarHostState)
        }
        composable(Screen.FavoriteList.route) {
            ProcedureListScreen(onlyFavorites = true, snackbarHostState = snackbarHostState)
        }
    }
}