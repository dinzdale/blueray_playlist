package com.gmjproductions.blurayplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun MainScreen() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            val inputFile by remember { mutableStateOf("dummy file path") }
            val showFilePicker by remember { mutableStateOf(false) }
            Row(Modifier.fillMaxSize()) {
                Header(inputFile) {

                }
            }
        }
    }
}

@Composable
fun SelectInputFile(showFilePicker: Boolean, onFileSelected: (String) -> Unit) {
    FilePicker(showFilePicker, fileExtensions = listOf("*")) {

    }
}

@Composable
fun Header(filePath: String, onOpenFileClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(200.dp).background(color = Color.Yellow),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Button(onClick = onOpenFileClick) {
            Text("Open File")
        }
        Text(filePath)

    }
}


@Composable
@Preview
fun PreviewMainScreen() {
    MainScreen()
}