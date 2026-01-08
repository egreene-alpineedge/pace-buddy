import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplitRow(split: String, time: String) {
    Row (
        modifier = Modifier
            .height(28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = split,
            fontFamily = UrbanistFontFamily(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = time,
            fontFamily = UbuntoFontFamily(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
        )
    }
}