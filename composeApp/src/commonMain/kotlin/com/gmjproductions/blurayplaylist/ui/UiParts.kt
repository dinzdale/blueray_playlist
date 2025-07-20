package com.gmjproductions.blurayplaylist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text


import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ActionButton(label: String, onHover: @Composable (() -> Unit)? = null, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val showHover by interactionSource.collectIsHoveredAsState()

    Box(modifier = Modifier.wrapContentSize()) {
        if (showHover) {
            onHover?.invoke()
        }
        Button(
            onClick = onClick,
            modifier = Modifier.size(width = 50.dp, height = 50.dp)
        ) {
            Text(text = label.first().toString())
        }
    }
}

@Composable
@Preview
fun PreviewButton() {
    MaterialTheme {
        ActionButton("A", { HintBox("Apply conversion") }) {

        }
    }
}

@Composable
fun HintBox(text: String) {
    Box(
        modifier = Modifier.width(80.dp).wrapContentHeight().background(Color.Black),
        Alignment.Center
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun ItemTextField(text: String, width: Dp) {

    var value by remember { mutableStateOf(text) }
    TextField(
        value,
        { value = it },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.LightGray,
            textColor = Color.Black
        ),
        modifier = Modifier.width(width).wrapContentHeight()
    )
}


@Composable
@Preview
fun PreviewItemTextField() {
    MaterialTheme {
        Surface() {
            Box(Modifier.fillMaxSize().background(Color.Yellow), contentAlignment = Alignment.CenterStart) {
                ItemTextField("Initial value here", 200.dp)
            }
        }
    }
}
