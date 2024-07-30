package com.gemasr.surgeonwizard.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gemasr.surgeonwizard.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SurgeonWizardTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.app_name))
        },
        colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    )
}