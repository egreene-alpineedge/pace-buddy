import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
fun ScreenHeightWidthWrapper(content: @Composable (width: Dp, height: Dp) -> Unit) {
    val density = LocalDensity.current
    var screenWidth by remember { mutableStateOf(0.dp) }
    var screenHeight by remember { mutableStateOf(0.dp) }

    Box(
        modifier = Modifier
            .fillMaxSize() // Takes up the entire screen/window space
            .onGloballyPositioned { coordinates ->
                // Get the size of the root Box in pixels
                val size = coordinates.size

                // Convert pixels to Dp
                screenWidth = with(density) { size.width.toDp() }
                screenHeight = with(density) { size.height.toDp() }
            }
    ) {
        // Pass the dimensions to the rest of your UI
        content(screenWidth, screenHeight)
    }
}