package com.example.mymovieapp.ui.theme



import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 الألوان من الباليت بتاعك
val RaisinBlack = Color(0xFF272838)
val Black =     Color(0xFF1A1A23)
val SemiBlack = Color(0xFF1A1B1E)
val White = Color(0xFFFBFBFD)         // فاتح
val OrangeCrayola: Color = Color(0xFFFF7329) // أساسي
val RaisinBlack2 = Color(0xFF1E1D2B)  // غامق تاني
val CoolGray = Color(0xFF9A9BA6)      // رمادي

// 🌞 Light Theme Colors
private val LightColors = lightColorScheme(
    primary = OrangeCrayola,
    onPrimary = White,
    secondary = CoolGray,
    onSecondary = RaisinBlack,
    background = White,
    onBackground = RaisinBlack,
    surface = White,
    onSurface = RaisinBlack
)

// 🌚 Dark Theme Colors
private val DarkColors = darkColorScheme(
    primary = OrangeCrayola,
    onPrimary = RaisinBlack2,
    secondary = CoolGray,
    onSecondary = White,
    background = RaisinBlack2,
    onBackground = White,
    surface = RaisinBlack,
    onSurface = White
)

// ✨ Theme Composable
@Composable
fun MyAppTheme(
    darkTheme: Boolean = false, // ممكن تخليها isSystemInDarkTheme()
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content
    )
}
