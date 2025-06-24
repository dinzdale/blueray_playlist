package com.gmjproductions.blurayplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gmjproductions.blurayplaylist.models.Calcit
import com.gmjproductions.blurayplaylist.models.L2
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.XML
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.BufferedReader
import java.io.FileReader


@Composable
fun MainScreen() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            var inputFile by remember { mutableStateOf<PlatformFile?>(null) }
            var contents by remember { mutableStateOf<String?>(null) }

            var showFilePicker by remember { mutableStateOf(false) }
            var showFileSaver by remember { mutableStateOf(false) }

            var inputSettings by remember { mutableStateOf<Calcit?>(null) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                Header(inputFile?.path, {
                    showFilePicker = true
                }) {
                    showFileSaver = true
                }
                ShowResults(inputSettings)
            }
            LaunchedEffect(showFilePicker) {
                if (showFilePicker) {
                    val file = FileKit.openFilePicker()
                    file?.also {
                        inputFile = it
                        contents = it.readString()
                    }
                    showFilePicker = false
                }
            }

            LaunchedEffect(contents) {
                contents = contents?.let {
                    cleanupFileContents(it)
                }
                contents?.also {
                    try {
                        inputSettings = XML.decodeFromString(it)
                    } catch (ex: Exception) {
                        errorMessage = ex.message
                    }
                }
            }
            LaunchedEffect(showFileSaver) {
                if (showFileSaver) {
                    contents?.also {
                        if (it.isNotBlank()) {
                            val file =
                                FileKit.openFileSaver(inputFile?.name ?: "", directory = inputFile)
                            file?.writeString(it)
                        }
                    }
                    showFileSaver = false
                }
            }
            showAlert(errorMessage, { errorMessage = null }) {
                errorMessage = null
            }
        }
    }
}


fun ParseInputFile(filePath: String?) = filePath?.let {
    val fileReader = FileReader(filePath)
    var contents = BufferedReader(fileReader).readText()
    contents = cleanupFileContents(contents)
    //XML.decodeFromString<Calcit>(contents)
    contents
}

fun cleanupFileContents(contents: String): String {
    val E = "<E/>".toRegex()
    val L = "(<L>(.*)+</L>)".toRegex()

    var cleanedContents = E.replace(contents,"")
    val els = L.findAll(cleanedContents).toList().map { it.value }.map { XML.decodeFromString<L2>(it) }

    cleanedContents = L.replace(cleanedContents,"")
    return cleanedContents
}


@Composable
fun Header(filePath: String?, onOpenFileClick: () -> Unit, onSaveFile: () -> Unit) {


    Column(
        Modifier.fillMaxWidth().wrapContentHeight().background(color = Color.Yellow).height(150.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            Modifier.fillMaxWidth().padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Button(onClick = onOpenFileClick) {
                Text("Open File")
            }
            Spacer(Modifier.width(10.dp))
            Text(filePath ?: "")
        }
        Row(
            Modifier.wrapContentSize().padding(start = 10.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(Modifier.width(10.dp))
            Button(onClick = onSaveFile, enabled = filePath?.isNotBlank() ?: false) {
                Text("Save")
            }

        }
    }
}

@Composable
fun ShowResults(calcit: Calcit?) {
    val lazyListState = rememberLazyListState()
    calcit?.llist?.also {
        LazyColumn(state = lazyListState, userScrollEnabled = true) {
            items(it) { nxtList ->
                nxtList.itemList.forEach {
                    Row(Modifier.fillMaxWidth().wrapContentHeight()) {
                        Text("${it.ID}: ${it.value}")
                    }
                }
            }
        } ?: Row(Modifier.fillMaxWidth().wrapContentHeight()) {
            Text("Empty")
        }
    }
}


@Composable
fun showAlert(message: String?, onExit: () -> Unit, onConfirm: () -> Unit) {
    message?.also {
        AlertDialog(
            onExit,
            confirmButton = @Composable {
                Button(onConfirm) {
                    Text("OK")
                }
            },
            title = @Composable { Text("Serializing Error", style = TextStyle(color = Color.Black, fontWeight = FontWeight.Medium)) },
            text = @Composable { Text(it, style = TextStyle(color=Color.Red, fontWeight = FontWeight.Bold )) }
        )
    }
}

@Composable
@Preview
fun PreviewMainScreen() {
    MainScreen()
}