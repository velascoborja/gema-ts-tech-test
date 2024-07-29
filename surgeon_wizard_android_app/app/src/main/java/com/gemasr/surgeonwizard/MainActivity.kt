package com.gemasr.surgeonwizard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gemasr.surgeonwizard.design.theme.SurgeonWizardTheme
import com.gemasr.surgeonwizard.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SurgeonWizardTheme {
                MainScreen()
            }
        }
    }
}