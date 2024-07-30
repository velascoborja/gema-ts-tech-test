package com.gemasr.surgeonwizard.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gemasr.surgeonwizard.navigation.SurgeonWizardNavHost

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { SurgeonWizardTopBar() },
        bottomBar = { SurgeonWizardBottomBar(navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        SurgeonWizardNavHost(
            navController = navController,
            innerPadding = innerPadding,
            snackbarHostState = snackbarHostState,
        )
    }
}