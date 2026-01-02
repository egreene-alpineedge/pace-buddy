package org.example.project

import BaumansFontFamily
import ScreenHeightWidthWrapper
import SplitRow
import Theme
import UbuntoFontFamily
import UrbanistFontFamily
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.Services.CalculationService
import org.example.project.components.Field
import org.example.project.components.GreenPicker
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pacebuddy2.composeapp.generated.resources.Res
import pacebuddy2.composeapp.generated.resources.light
import kotlin.math.floor
import kotlin.time.Duration.Companion.convert

@Composable
fun fadedBorder(): BorderStroke {
    return BorderStroke(
        1.dp,
        Brush.linearGradient(
            colors = listOf(
                Color.White,
                Color.White.copy(alpha = 0f)   // transparent

            ),
            start = Offset(0f, 0f),     // top
            end = Offset(0f, 100f)      // bottom
        )
    )
}

fun convertValueToDistanceText(value: Double): String {
    val stringValue = value.toString()

    return if (stringValue.endsWith(".0")) {
        stringValue.dropLast(2)
    } else {
        stringValue
    }

}

fun convertValueToTimeText(value: Double): String {
    var temp = value
    val hours = floor(temp / 3600).toInt()
    temp %= 3600
    val minutes = floor(temp / 60).toInt()
    temp %= 60
    val seconds = temp.toInt()

    val hoursText = "$hours"
    val minutesText = if (minutes < 10) "0$minutes" else "$minutes"
    val secondsText = if (seconds < 10) "0$seconds" else "$seconds"

    return if (hours == 0) {
        "$minutesText:$secondsText"
    } else {
        "$hoursText:$minutesText:$secondsText"
    }

}

fun handleTimeValueChange(text: String): String {
    val input = text.filter { it != ':' }

    if (input.length > 6) {
        return text.dropLast(1)
    }

    return if (input.length > 6) {
        val head = input.take(4)
        val tail = input.drop(4).chunked(2).joinToString(":")
        if (tail.isNotEmpty()) "$head:$tail" else head
    } else {
        val padded = if (input.length % 2 != 0) "0$input" else input
        val resT = padded.chunked(2).joinToString(":")
        val res = if (input.length % 2 != 0) resT.drop(1) else resT
        res
    }

}


@Composable
@Preview(showBackground = true)
fun App() {
    val viewModel = remember {
        AppViewModel(calculationService = CalculationService())
    }

    var theme by remember { mutableStateOf("light") }

    var distanceText by remember { mutableStateOf("") }
    var timeText by remember { mutableStateOf("") }
    var paceText by remember { mutableStateOf("") }
    var speedText by remember { mutableStateOf("") }

    var fieldScreenPosition by remember { mutableStateOf(Offset.Zero) }
    var splitScreenPosition by remember { mutableStateOf(Offset.Zero) }

    var count by remember { mutableStateOf(3) }


//    LaunchedEffect(viewModel.distance) {
//        println("dist launch effect")
//        if (viewModel.distance != null) {
//            distanceText = convertValueToDistanceText(viewModel.distance!!)
//        }
//    }
    LaunchedEffect(viewModel.time) {
        println("time launch effect")
        if (viewModel.time != null) {
            timeText = convertValueToTimeText(viewModel.time!!)
        }

    }
    LaunchedEffect(viewModel.pace) {
        println("pace launch effect")
        paceText = "${viewModel.pace}"
    }
    LaunchedEffect(viewModel.speed) {
        println("speed launch effect")
        speedText = "${viewModel.speed}"
    }

    MaterialTheme {
        ScreenHeightWidthWrapper { screenWidth, screenHeight ->
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painterResource(Theme[theme]!!.backgroundImage),
                    contentDescription = null,
                    modifier = Modifier
                        .width(screenWidth)
                        .height(screenHeight),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .background(Theme[theme]!!.backgroundOverlay)
                        .fillMaxSize()
                ) { }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                    verticalArrangement = Arrangement.spacedBy(20.dp),

                    ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Pace Buddy",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = BaumansFontFamily(),
                            color = Color.White
                        )

                        Spacer(Modifier.weight(1f))

                        Button(
                            modifier = Modifier.size(width = 80.dp, height = 40.dp),
                            onClick = {
                                viewModel.onReset()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Theme[theme]!!.resetButtonColor,
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            border = fadedBorder()
                        ) {
                            Text(
                                text = "Reset",
                                fontFamily = UrbanistFontFamily(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White,
                            )

                        }
                    }

                    Box (
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                brush = Brush.verticalGradient(
                                    listOf(Color.White, Color.Transparent)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.3f),
                                        Color.White.copy(alpha = 0.1f)
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                                )
                            )
                            .height(300.dp)
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates: LayoutCoordinates ->
                                // Converts the top-left corner (Offset.Zero) of the composable
                                // to its position relative to the window/screen.
                                val screenPosition = coordinates.localToWindow(Offset.Zero)
                                fieldScreenPosition = screenPosition
                            },

                    ) {

                        Image(
                            imageResource(Theme[theme]!!.backgroundImage),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .wrapContentSize(unbounded = true, align = Alignment.TopStart)
                                .width(screenWidth)
                                .height(screenHeight)
                                .offset {
                                    IntOffset(
                                        -1 * fieldScreenPosition.x.toInt(),
                                        -1 * fieldScreenPosition.y.toInt()
                                    )
                                }
                                .blur(15.dp)
                        )

                        Column(
                            modifier = Modifier
                                .background(Color(0xFF097381).copy(alpha = 0.2f))
                                .height(screenHeight)
                                .width(screenWidth)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(
                                    Brush.linearGradient(
                                        colors = Theme[theme]!!.containerGradient,
                                        start = Offset(0f, 0f),     // top
                                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)      // bottom
                                    )
                                )
                                .height(screenHeight)
                                .width(screenWidth)
                        ) { }

                        Column (
                            modifier = Modifier
                                .padding(vertical = 20.dp, horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                            Field(
                                label = "Time",
                                value = timeText,
                                transform = { handleTimeValueChange(it) },
                                onValueChange = {
                                    timeText = it
                                },
                                onBlur = {
                                    viewModel.onBlurTimeField(text = timeText)
                                }
                            )

                            Field(
                                label = "Distance",
                                value = distanceText,
                                onValueChange = {
                                    distanceText = it
                                },
                                onBlur = {
                                    viewModel.onBlurDistanceField(text = distanceText)
                                    val distanceValue = distanceText.toDoubleOrNull()
                                    if (distanceValue != null) {
                                        val texttt = convertValueToDistanceText(distanceValue)
                                        distanceText = texttt
                                    }

//                                    if (viewModel.distance != null) {
//                                        distanceText = convertValueToDistanceText(viewModel.distance!!)
//                                    }
                                }
                            )
//
//                            Field(
//                                label = "Pace",
//                                value = paceText,
//                                onValueChange = {
//                                    paceText = it
//                                },
//                                onBlur = {
//                                    viewModel.onBlurPaceField(text = paceText)
//                                }
//                            )
//
                            Field(
                                label = "Speed",
                                value = speedText,
                                onValueChange = {
                                    speedText = it
                                },
                                onBlur = {
                                    viewModel.onBlurSpeedField(text = speedText)
                                }
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                brush = Brush.verticalGradient(
                                    listOf(Color.White, Color.Transparent)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .height((24 * 2 + 40 * count).dp)
                            .onGloballyPositioned { coordinates: LayoutCoordinates ->
                                // Converts the top-left corner (Offset.Zero) of the composable
                                // to its position relative to the window/screen.
                                val screenPosition = coordinates.localToWindow(Offset.Zero)
                                splitScreenPosition = screenPosition
                            },
                    ) {

                        Image(
                            imageResource(Theme[theme]!!.backgroundImage),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .wrapContentSize(unbounded = true, align = Alignment.TopStart)
                                .width(screenWidth)
                                .height(screenHeight)
                                .offset {
                                    IntOffset(
                                        -1 * splitScreenPosition.x.toInt(),
                                        -1 * splitScreenPosition.y.toInt()
                                    )
                                }
                                .blur(15.dp)
                        )

                        Column(
                            modifier = Modifier
                                .background(Color(0xFF097381).copy(alpha = 0.2f))
                                .height(screenHeight)
                                .width(screenWidth)
                        ) { }

                        Column(
                            modifier = Modifier
                                .background(
                                    Brush.linearGradient(
                                        colors = Theme[theme]!!.containerGradient,
                                        start = Offset(0f, 0f),
                                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)      // bottom
                                    )
                                )
                                .height(screenHeight)
                                .width(screenWidth)
                        ) { }


                        Column(
                            modifier = Modifier
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SplitRow(split = "1 mi", time = "7m 12s")
                            SplitRow(split = "2 mi", time = "14m 24s")
                            SplitRow(split = "3 mi", time = "21m 36s")
                            SplitRow(split = "1 mi", time = "7m 12s")
                            SplitRow(split = "2 mi", time = "14m 24s")
                            SplitRow(split = "3 mi", time = "21m 36s")
                        }
                    }

                }
            }
        }
    }
}



