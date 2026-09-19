package espaco.artesao.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Terracotta,
    onPrimary = LightText,
    secondary = BurntYellow,
    onSecondary = DarkText,
    tertiary = BurntOrange,
    onTertiary = LightText,
    background = OffWhite,
    onBackground = DarkText,
    surface = OffWhite,
    onSurface = DarkText,
    surfaceVariant = Beige,
    onSurfaceVariant = SecondaryText,
)

// We'll use the same for dark theme for now, but in reality we might want a proper dark palette
private val DarkColorScheme = LightColorScheme

@Composable
fun ArtesaoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disabled to keep the artisanal theme consistent
    dynamicColor: Boolean = false,
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
