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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import pacebuddy2.composeapp.generated.resources.Res
import pacebuddy2.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    val viewModel = remember {
        AppViewModel(calculationService = CalculationService())
    }

    var distanceText by remember { mutableStateOf("") }
    var distanceTextFocused by remember { mutableStateOf(false) }
    var timeText by remember { mutableStateOf("") }
    var timeTextFocused by remember { mutableStateOf(false) }
    var paceText by remember { mutableStateOf("") }
    var paceTextFocused by remember { mutableStateOf(false) }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Hello World")

            TextField(
                value = distanceText,
                onValueChange = { distanceText = it },
                label = { Text("Enter Distance") },
                modifier = Modifier.onFocusChanged { focusState ->
                    distanceTextFocused = focusState.isFocused
                    if (!focusState.isFocused) {
                        // 👇 onBlur logic here
                        println("TextField lost focus, value = $text")
                    }
                }
            )
            TextField(
                value = timeText,
                onValueChange = { timeText = it },
                label = { Text("Enter Time") }
            )
            TextField(
                value = paceText,
                onValueChange = { paceText = it },
                label = { Text("Enter Pace") }

            )
        }
    }
}