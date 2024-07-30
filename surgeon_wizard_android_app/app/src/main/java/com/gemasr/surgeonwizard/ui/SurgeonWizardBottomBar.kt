package com.gemasr.surgeonwizard.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.gemasr.surgeonwizard.R
import com.gemasr.surgeonwizard.navigation.Screen

@Composable
fun SurgeonWizardBottomBar(navController: NavHostController) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.secondary
    var currentRoute by remember { mutableStateOf(Screen.ProcedureList.route) }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            destination.route?.let { currentRoute = it }
        }
    }

    NavigationBar {
        val items =
            listOf(
                Triple(Screen.ProcedureList, Icons.AutoMirrored.Filled.List, R.string.procedures),
                Triple(Screen.FavoriteList, Icons.Default.Favorite, R.string.favorites),
            )

        items.forEach { (screen, icon, labelResId) ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = stringResource(id = labelResId),
                        tint = if (selected) selectedColor else unselectedColor,
                    )
                },
                label = {
                    Text(
                        stringResource(id = labelResId),
                        color = if (selected) selectedColor else unselectedColor,
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                colors =
                NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    selectedTextColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = selectedColor.copy(alpha = 0.1f),
                ),
            )
        }
    }
}