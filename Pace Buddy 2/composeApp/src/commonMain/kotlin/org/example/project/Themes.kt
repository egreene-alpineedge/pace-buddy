import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import pacebuddy2.composeapp.generated.resources.Res
import pacebuddy2.composeapp.generated.resources.dark
import pacebuddy2.composeapp.generated.resources.light

private val LightColors = lightColorScheme(
    primary = Color(0xFF0066FF),
    onPrimary = Color.White,
    background = Color(0xFFFFFFFF),
    onBackground = Color.Black,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF82B1FF),
    onPrimary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
)




val LightTheme = mapOf(
    "background_image" to Res.drawable.light,
)

val DarkTheme = mapOf(
    "background_image" to Res.drawable.dark,
)

val Theme = mapOf(
    "light" to LightTheme,
    "dark" to DarkTheme,
)


