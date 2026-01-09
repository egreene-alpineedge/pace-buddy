package org.example.project

import BaumansFontFamily
import ScreenHeightWidthWrapper
import SplitRow
import Theme
import UrbanistFontFamily
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.example.project.Services.CalculationService
import org.example.project.Services.ConversionService
import org.example.project.components.Field
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.text.contains
import kotlin.text.get

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
    if (value == 0.0) return ""

    val roundedValue = (value * 1000).roundToInt().toDouble() / 1000

    val stringValue = roundedValue.toString()

    return if (stringValue.endsWith(".0")) {
        stringValue.dropLast(2)
    } else {
        stringValue
    }
}
fun convertValueToSpeedText(value: Double): String {
    if (value == 0.0) return ""

    val roundedValue = (value * 1000).roundToInt().toDouble() / 1000

    val stringValue = roundedValue.toString()

    return if (stringValue.endsWith(".0")) {
        stringValue.dropLast(2)
    } else {
        stringValue
    }
}
fun convertValueToTimeText(value: Double): String {
    if (value == 0.0) return ""

    var temp = value
    val hours = floor(temp / 3600).toInt()
    temp %= 3600
    val minutes = floor(temp / 60).toInt()
    temp %= 60
    val seconds = temp.toInt()

    val hoursText = "$hours"
    val minutesText = if (minutes < 10 && hours != 0) "0$minutes" else "$minutes"
    val secondsText = if (seconds < 10) "0$seconds" else "$seconds"

    return if (hours == 0) {
        "$minutesText:$secondsText"
    } else {
        "$hoursText:$minutesText:$secondsText"
    }

}
fun handleDecimalValueChange(text: String): String {

    if (text == "") return ""

    val newCharIsValid = text.last().isDigit() || text.last() == '.'
    if (!newCharIsValid) {
       return text.dropLast(1)
    }

    return if (text.last() == '.' && text.dropLast(1).contains('.')) {
        text.dropLast(1)
    } else {
        text
    }


}
fun handleTimeValueChange(text: String): String {
    val input = text.filter { it.isDigit() }

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
fun convertTimeTextToValue(text: String): Double {
    val parts = text.split(":")
    if (parts.count() == 2) {
        val minutes = parts[0].toDouble()
        val seconds = parts[1].toDouble()
        return (minutes*60) + seconds
    }
    else if (parts.count() == 3) {
        val hours = parts[0].toDouble()
        val minutes = parts[1].toDouble()
        val seconds = parts[2].toDouble()
        return (hours*60*60) + (minutes*60) + seconds
    }
    else {
        val seconds = parts[0].toDouble()
        return seconds
    }
}


@Composable
@Preview(showBackground = true)
fun App(
    prefs: DataStore<Preferences>
) {
    val scope = rememberCoroutineScope()
    val viewModel = remember {
        AppViewModel(
            calculationService = CalculationService(),
            conversionService = ConversionService()
        )
    }


    val theme by prefs
        .data
        .map {
            val themeKey = stringPreferencesKey("theme")
            it[themeKey] ?: "light"
        }
        .collectAsState("light")

    var distanceTextField by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var timeTextField by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var speedTextField by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var paceTextField by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var fieldScreenPosition by remember { mutableStateOf(Offset.Zero) }
    var splitScreenPosition by remember { mutableStateOf(Offset.Zero) }

    var changesMade by remember {
        mutableStateOf(false)
    }


    fun updateFields() {

        // Don't update if no changes were made
        if (!changesMade) {
            return
        }

        val distanceValue = viewModel.distance
        if (distanceValue != null) {
            distanceTextField = distanceTextField.copy(
                text = convertValueToDistanceText(distanceValue),
                selection = TextRange(distanceTextField.text.length)
            )
        }

        val timeValue = viewModel.time
        if (timeValue != null) {
            timeTextField = timeTextField.copy(
                text = convertValueToTimeText(timeValue),
                selection = TextRange(timeTextField.text.length)
            )
        }

        val speedValue = viewModel.speed
        if (speedValue != null) {
            speedTextField = speedTextField.copy(
                text = convertValueToSpeedText(speedValue),
                selection = TextRange(speedTextField.text.length)
            )
        }

        val paceValue = viewModel.pace
        if (paceValue != null) {
            paceTextField = paceTextField.copy(
                text = convertValueToTimeText(paceValue),
                selection = TextRange(paceTextField.text.length)
            )
        }

        changesMade = false
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

                        Image(
                            imageResource(Theme[theme]!!.switchIcon),
                            contentDescription = null,
                            modifier = Modifier
                                .width(32.dp)
                                .height(32.dp)
                                .clickable {
                                    scope.launch {
                                        prefs.edit { dataStore ->
                                            val themeKey = stringPreferencesKey("theme")

                                            val currentTheme = theme

                                            if (currentTheme == "light") {
                                                dataStore[themeKey] = "dark"
                                            } else {
                                                dataStore[themeKey] = "light"
                                            }
                                        }
                                    }
                                }
                        )

                        Button(
                            modifier = Modifier.size(width = 80.dp, height = 40.dp),
                            onClick = {
                                viewModel.onReset()

                                distanceTextField = distanceTextField.copy(text = "", selection = TextRange(0))
                                timeTextField = timeTextField.copy(text = "", selection = TextRange(0))
                                speedTextField = speedTextField.copy(text = "", selection = TextRange(0))
                                paceTextField = paceTextField.copy(text = "", selection = TextRange(0))
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
                                label = "Distance",
                                value = distanceTextField,
                                transform = { handleDecimalValueChange(it) },
                                onValueChange = {
                                    changesMade = true
                                    distanceTextField = it
                                },
                                onBlur = {
                                    val distanceValue = distanceTextField.text.toDoubleOrNull()
                                    if (distanceValue != null) {
                                        viewModel.onBlurDistanceField(value = distanceValue)
                                        updateFields()
                                    }
                                },
                                leftLabel = "mi",
                                rightLabel = "km",
                                onToggle = { unitString ->
                                    changesMade = true
                                    viewModel.onToggleDistanceUnit(unitString)
                                    updateFields()
                                },
                                keyboardType = KeyboardType.Decimal,
                                prefs = prefs
                            )

                            Field(
                                label = "Time",
                                value = timeTextField,
                                transform = { handleTimeValueChange(it) }, // As we're typing
                                onValueChange = {
                                    changesMade = true
                                    timeTextField = it
                                },
                                onBlur = {
                                    if (timeTextField.text != "") {
                                        val timeValue = convertTimeTextToValue(timeTextField.text)
                                        viewModel.onBlurTimeField(value = timeValue)
                                        updateFields()
                                    }
                                },
                                keyboardType = KeyboardType.Number,
                                prefs = prefs
                            )


                            Field(
                                label = "Pace",
                                value = paceTextField,
                                transform = { handleTimeValueChange(it) }, // As we're typing
                                onValueChange = {
                                    changesMade = true
                                    paceTextField = it
                                },
                                onBlur = {
                                    if (paceTextField.text != "") {
                                        val paceValue = convertTimeTextToValue(paceTextField.text)
                                        viewModel.onBlurPaceField(value = paceValue)
                                        updateFields()
                                    }
                                },
                                leftLabel = "/mi",
                                rightLabel = "/km",
                                onToggle = { unitString ->
                                    changesMade = true
                                    viewModel.onTogglePaceUnit(unitString)
                                    updateFields()
                                },
                                keyboardType = KeyboardType.Number,
                                prefs = prefs
                            )
                            Field(
                                label = "Speed",
                                value = speedTextField,
                                transform = { handleDecimalValueChange(it) },
                                onValueChange = {
                                    changesMade = true
                                    speedTextField = it
                                },
                                onBlur = {
                                    val speedValue = speedTextField.text.toDoubleOrNull()
                                    if (speedValue != null) {
                                        viewModel.onBlurSpeedField(value = speedValue)
                                        updateFields()
                                    }
                                },
                                leftLabel = "mph",
                                rightLabel = "kph",
                                onToggle = { unitString ->
                                    changesMade = true
                                    viewModel.onToggleSpeedUnit(unitString)
                                    updateFields()
                                },
                                keyboardType = KeyboardType.Decimal,
                                prefs = prefs
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
                            .height(
                                if (viewModel.splits.count() == 0) 0.dp
                                else (28*viewModel.splits.count() + 12*(viewModel.splits.count()-1) + 24 + 24 + 2).dp
                            )
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
                                .padding(24.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            viewModel.splits.forEach { split ->
                                SplitRow(
                                    split = "${convertValueToDistanceText(split.length)} mi",
                                    time = convertValueToTimeText(split.time),
                                    textColor = Theme[theme]!!.textColor
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}



