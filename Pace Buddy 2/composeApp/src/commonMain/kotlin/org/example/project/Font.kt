import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import pacebuddy2.composeapp.generated.resources.Baumans_Regular
import pacebuddy2.composeapp.generated.resources.Res
import pacebuddy2.composeapp.generated.resources.UbuntuMono_Regular
import pacebuddy2.composeapp.generated.resources.Urbanist_Bold
import pacebuddy2.composeapp.generated.resources.Urbanist_Medium
import pacebuddy2.composeapp.generated.resources.Urbanist_Regular
import pacebuddy2.composeapp.generated.resources.Urbanist_SemiBold

@Composable
fun BaumansFontFamily() = FontFamily(
    Font(Res.font.Baumans_Regular, weight = FontWeight.Normal)
)

@Composable
fun UrbanistFontFamily() = FontFamily(
    Font(Res.font.Urbanist_Regular, weight = FontWeight.Normal),
    Font(Res.font.Urbanist_Bold, weight = FontWeight.Bold),
    Font(Res.font.Urbanist_Medium, weight = FontWeight.Medium),
    Font(Res.font.Urbanist_SemiBold, weight = FontWeight.SemiBold),
)

@Composable
fun UbuntoFontFamily() = FontFamily(
    Font(Res.font.UbuntuMono_Regular, weight = FontWeight.Normal)
)