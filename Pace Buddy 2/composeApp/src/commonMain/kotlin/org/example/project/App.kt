package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pacebuddy2.composeapp.generated.resources.Res
import pacebuddy2.composeapp.generated.resources.light


@Composable
@Preview(showBackground = true)
fun App() {
    val viewModel = remember {
        AppViewModel(calculationService = CalculationService())
    }

    var distanceText by remember { mutableStateOf("") }
    var timeText by remember { mutableStateOf("") }
    var paceText by remember { mutableStateOf("") }


    LaunchedEffect(viewModel.distance) {
        println("dist launch effect")
        distanceText = "${viewModel.distance}"
    }
    LaunchedEffect(viewModel.time) {
        println("time launch effect")
        timeText = "${viewModel.time}"
    }
    LaunchedEffect(viewModel.pace) {
        println("pace launch effect")
        paceText = "${viewModel.pace}"
    }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painterResource(Res.drawable.light),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column {
                Text(text = "Hello World")

                TextField(
                    value = distanceText,
                    onValueChange = { distanceText = it },
                    label = { Text("Enter Distance") },
                    modifier = Modifier.onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            viewModel.onBlurDistanceField(text = distanceText)
                        }
                    }
                )
                TextField(
                    value = timeText,
                    onValueChange = { timeText = it },
                    label = { Text("Enter Time") },
                    modifier = Modifier.onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            viewModel.onBlurTimeField(text = timeText)
                        }
                    }
                )
                TextField(
                    value = paceText,
                    onValueChange = { paceText = it },
                    label = { Text("Enter Pace") },
                    modifier = Modifier.onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            viewModel.onBlurPaceField(text = paceText)
                        }
                    }
                )
            }
        }
    }
}