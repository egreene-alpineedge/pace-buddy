package org.example.project.components

import UrbanistFontFamily
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true)
fun Field(
    label: String,
    value: String?,
    transform: (String) -> String = { it },
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit,
) {

    var text by remember(value) {
        mutableStateOf(
            TextFieldValue(text = value.orEmpty())
        )
    }

    Row (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp)

    ) {
        Text(
            modifier = Modifier
                .width(100.dp),
            text = label,
            fontSize = 22.sp,
            fontFamily = UrbanistFontFamily(),
            fontWeight = FontWeight.Medium,
        )
        Box(
            modifier = Modifier
                .width(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.55f))
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(Color.White, Color.Transparent)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 22.dp, vertical = 5.dp)

        ) {
            BasicTextField(
                value = text,
                singleLine = true,
                onValueChange = {

                    val transformedText = transform(it.text)

                    text = TextFieldValue(
                        text = transformedText,
                        selection = TextRange(transformedText.length) // cursor at end
                    )

//                    text = it
//                    onValueChange(transformedText)
                },
                textStyle = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = UrbanistFontFamily(),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .width(120.dp)
                    .background(Color.Transparent)
                    .padding(0.dp)
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            onValueChange(text.text)
                            onBlur()
                        }

                    }
            )
        }

        Spacer(Modifier.weight(1f))

        GreenPicker()
    }
}