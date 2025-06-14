package com.gmjproductions.blurayplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.gmjproductions.blurayplaylist.models.Calcit
import com.gmjproductions.blurayplaylist.models.MultiAVCHDItem
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.XML
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.xml.sax.XMLReader
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.FileReader
import java.io.StringReader
import javax.xml.stream.XMLStreamReader


@Composable
fun MainScreen() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            var inputFile by remember { mutableStateOf<String?>(null) }
            var showFilePicker by remember { mutableStateOf(false) }
            var inputSettings by remember { mutableStateOf<Calcit?>(null) }
            Row(Modifier.fillMaxSize()) {
                Header(inputFile) {
                    showFilePicker = true
                }
            }
            SelectInputFile(showFilePicker) {
                showFilePicker = false
                inputFile = it ?: ""
            }
            LaunchedEffect(inputFile) {
                inputFile?.also {
                    inputSettings = ParseInputFile(it)
                }
            }
        }
    }
}

@Composable
fun SelectInputFile(showFilePicker: Boolean, onFileSelected: (String?) -> Unit) {
    FilePicker(showFilePicker, fileExtensions = listOf("mpf")) {
        onFileSelected(it?.path)
    }
}

fun ParseInputFile(filePath: String?) = filePath?.let {
    val fis = FileInputStream(filePath)
    val contents = fis.readAllBytes().toString()
    XML.decodeFromString<Calcit>(contents)

}

@Composable
fun Header(filePath: String?, onOpenFileClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(100.dp).padding(start = 10.dp)
            .background(color = Color.Yellow),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Button(onClick = onOpenFileClick) {
            Text("Open File")
        }
        Spacer(Modifier.width(10.dp))
        Text(filePath ?: "")

    }
}


@Composable
@Preview
fun PreviewMainScreen() {
    MainScreen()
}