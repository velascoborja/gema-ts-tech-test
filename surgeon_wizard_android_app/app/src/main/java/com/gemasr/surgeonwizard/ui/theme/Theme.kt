package com.gemasr.surgeonwizard.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

// Light theme colors
val LightPrimary = Color(0xFF00A86B)
val LightOnPrimary = Color.White
val LightPrimaryContainer = Color(0xFFB3F2D6)
val LightOnPrimaryContainer = Color(0xFF002117)
val LightSecondary = Color(0xFF4E6356)
val LightOnSecondary = Color.White
val LightSecondaryContainer = Color(0xFFD0E8D8)
val LightOnSecondaryContainer = Color(0xFF0B1F14)
val LightTertiary = Color(0xFF3D6373)
val LightOnTertiary = Color.White
val LightTertiaryContainer = Color(0xFFC1E8FB)
val LightOnTertiaryContainer = Color(0xFF001F2A)
val LightError = Color(0xFFBA1A1A)
val LightOnError = Color.White
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnErrorContainer = Color(0xFF410002)
val LightBackground = Color(0xFFF8FAF6)
val LightOnBackground = Color(0xFF191C1A)
val LightSurface = Color(0xFFF8FAF6)
val LightOnSurface = Color(0xFF191C1A)
val LightSurfaceVariant = Color(0xFFDBE5DB)
val LightOnSurfaceVariant = Color(0xFF404943)
val LightOutline = Color(0xFF707973)
val LightInverseOnSurface = Color(0xFFEFF1ED)
val LightInverseSurface = Color(0xFF2E312E)
val LightInversePrimary = Color(0xFF96D6BB)
val LightShadow = Color.Black
val LightSurfaceTint = LightPrimary
val LightOutlineVariant = Color(0xFFBFC9C1)
val LightScrim = Color.Black

// Dark theme colors
val DarkPrimary = Color(0xFF96D6BB)
val DarkOnPrimary = Color(0xFF00382A)
val DarkPrimaryContainer = Color(0xFF00513D)
val DarkOnPrimaryContainer = Color(0xFFB3F2D6)
val DarkSecondary = Color(0xFFB4CCBC)
val DarkOnSecondary = Color(0xFF213528)
val DarkSecondaryContainer = Color(0xFF374B3E)
val DarkOnSecondaryContainer = Color(0xFFD0E8D8)
val DarkTertiary = Color(0xFFA5CCDF)
val DarkOnTertiary = Color(0xFF063544)
val DarkTertiaryContainer = Color(0xFF244C5B)
val DarkOnTertiaryContainer = Color(0xFFC1E8FB)
val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)
val DarkBackground = Color(0xFF191C1A)
val DarkOnBackground = Color(0xFFE1E3DF)
val DarkSurface = Color(0xFF191C1A)
val DarkOnSurface = Color(0xFFE1E3DF)
val DarkSurfaceVariant = Color(0xFF404943)
val DarkOnSurfaceVariant = Color(0xFFBFC9C1)
val DarkOutline = Color(0xFF8A938C)
val DarkInverseOnSurface = Color(0xFF191C1A)
val DarkInverseSurface = Color(0xFFE1E3DF)
val DarkInversePrimary = Color(0xFF00A86B)
val DarkShadow = Color.Black
val DarkSurfaceTint = DarkPrimary
val DarkOutlineVariant = Color(0xFF404943)
val DarkScrim = Color.Black

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    inverseOnSurface = LightInverseOnSurface,
    inverseSurface = LightInverseSurface,
    inversePrimary = LightInversePrimary,
    surfaceTint = LightSurfaceTint,
    outlineVariant = LightOutlineVariant,
    scrim = LightScrim,
)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    inverseOnSurface = DarkInverseOnSurface,
    inverseSurface = DarkInverseSurface,
    inversePrimary = DarkInversePrimary,
    surfaceTint = DarkSurfaceTint,
    outlineVariant = DarkOutlineVariant,
    scrim = DarkScrim,
)

@Composable
fun SurgeonWizardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}