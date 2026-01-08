package org.example.project.components

import UrbanistFontFamily
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true)
fun GreenPicker(
    leftLabel: String,
    rightLabel: String,
    onToggle: (String) -> Unit
) {
    var left by remember { mutableStateOf(true) }

    val offset by animateDpAsState(
        targetValue = if (left) 0.dp else 36.dp,
        animationSpec = spring()
    )

    Row(
        modifier = Modifier
            .width(90.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF4FEE5F).copy(alpha = if (leftLabel == "") 0f else 0.5f))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = if (leftLabel == "") 0f else 0.5f), Color.Transparent)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable {
                val newLeft = !left
                left = newLeft
                if (newLeft) {
                    onToggle(leftLabel)
                } else {
                    onToggle(rightLabel)
                }

            }
            .alpha(if (leftLabel == "") 0f else 1f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = offset.value.dp, y = 0.dp)
                        .width(38.dp)
                        .height(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFFFFF).copy(alpha = 0.4f))
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                listOf(Color.White, Color.Transparent)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                ) {}
            }
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(30.dp)
                        .fillMaxHeight()
                        .padding(horizontal = 1.dp, vertical = 2.dp)

                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = leftLabel,
                        textAlign = TextAlign.Center,
                        fontFamily = UrbanistFontFamily(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                    )
                    Spacer(Modifier.weight(1f))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(30.dp)
                        .fillMaxHeight()
                        .padding(horizontal = 1.dp, vertical = 2.dp)

                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = rightLabel,
                        textAlign = TextAlign.Center,
                        fontFamily = UrbanistFontFamily(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,

                        )
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}