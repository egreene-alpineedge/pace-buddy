import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import pacebuddy2.composeapp.generated.resources.MoonStars
import pacebuddy2.composeapp.generated.resources.Res
import pacebuddy2.composeapp.generated.resources.Sun
import pacebuddy2.composeapp.generated.resources.dark
import pacebuddy2.composeapp.generated.resources.light

interface AppTheme {
    val backgroundImage: DrawableResource
    val backgroundOverlay: Color
    val resetButtonColor: Color
    val containerGradient: List<Color>
    val textColor: Color
    val switchIcon: DrawableResource
}

class LightTheme : AppTheme {
    override val backgroundImage: DrawableResource = Res.drawable.light
    override val backgroundOverlay: Color = Color(0xFF097381).copy(alpha = 0.2f)
    override val resetButtonColor: Color = Color(0xFF1A60E9).copy(alpha = 0.5f)
    override val containerGradient: List<Color> = listOf(
        Color.White.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.1f)
    )
    override val textColor: Color = Color.Black
    override val switchIcon: DrawableResource = Res.drawable.MoonStars
}

class DarkTheme : AppTheme {
    override val backgroundImage: DrawableResource = Res.drawable.dark
    override val backgroundOverlay: Color = Color(0xFF097381).copy(alpha = 0.2f)
    override val resetButtonColor: Color = Color(0xFF3277FF)
    override val containerGradient: List<Color> = listOf(
        Color(0xFF797583).copy(alpha = 0.2f),
        Color(0xFF363567).copy(alpha = 0.2f)
    )
    override val textColor: Color = Color.White
    override val switchIcon: DrawableResource = Res.drawable.Sun
}

val Theme = mapOf(
    "light" to LightTheme(),
    "dark" to DarkTheme(),
)