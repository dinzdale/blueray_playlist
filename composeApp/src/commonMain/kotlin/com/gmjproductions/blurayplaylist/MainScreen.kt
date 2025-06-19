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
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
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
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.gmjproductions.blurayplaylist.models.Calcit
import nl.adaptivity.xmlutil.serialization.XML
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.BufferedReader
import java.io.FileReader


@Composable
fun MainScreen() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            var inputFile by remember { mutableStateOf<String?>(null) }
            var saveFile by remember { mutableStateOf<String?>(null) }

            var showFilePicker by remember { mutableStateOf(false) }
            var showDirectoryPicker by remember { mutableStateOf(false) }
            var parsedContents by remember { mutableStateOf<String?>(null) }
            var inputSettings by remember { mutableStateOf<Calcit?>(null) }
            var saveContentsPath by remember { mutableStateOf<String?>(null) }

            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                Header(inputFile, {
                    showFilePicker = true
                }) {
                    saveFile = it
                }
                ShowResults(inputSettings)
            }
            SelectInputFile(showFilePicker) {
                showFilePicker = false
                inputFile = it
            }
            SelectSaveDirectory(saveFile?.isNotBlank() ?: false, "", saveFile ?: "") {
                saveContentsPath = it
            }
            LaunchedEffect(inputFile) {
                inputFile?.also {
                    System.out.println("Before Parse")
                    parsedContents = ParseInputFile(it)
//                    inputSettings = ParseInputFile(it)
//                    System.out.println("After Parse")
//                    inputSettings?.llist?.itemList?.forEach {
//                        System.out.println("${it.ID}: ${it.value}")
//                    }
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

@Composable
fun SelectSaveDirectory(
    show: Boolean,
    initialDirectory: String,
    title: String,
    onDirectorySelected: (String?) -> Unit
) {
    DirectoryPicker(show, initialDirectory, title) {
        onDirectorySelected(it)
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
    val date = "<.*DATE.*F>".toRegex()
    val e = "(<E/>)+".toRegex()
    val l = "(<L>.*</L>)+".toRegex()
    val stringToRemove = date.find(contents)?.value
    var cleanedContents = contents.replace(date, "")
    cleanedContents = contents.replace(e, "")
    val allLs = l.findAll(cleanedContents).map { it.value }.toList()
    cleanedContents = cleanedContents.replace(l, "")

    return cleanedContents
}

@Composable
fun Header(filePath: String?, onOpenFileClick: () -> Unit, onSaveFile: (String) -> Unit) {
    var saveFileName by remember { mutableStateOf("") }
    val tfState = rememberTextFieldState()


    Column(
        Modifier.fillMaxWidth().wrapContentHeight().background(color = Color.Yellow).height(100.dp),
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
            TextField(saveFileName, { saveFileName = it }, Modifier.width(300.dp),colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White))
           Spacer(Modifier.width(10.dp))
            Button(onClick = { onSaveFile(saveFileName) }, enabled = saveFileName.isNotBlank()) {
                Text("Save")
            }

        }
    }
}

@Composable
fun ShowResults(calcit: Calcit?) {
    calcit?.llist?.itemList?.also {
        val lazyListState = rememberLazyListState()
        LazyColumn(state = lazyListState, userScrollEnabled = true) {
            items(it) { nxtItem ->
                Row(Modifier.fillMaxWidth().wrapContentHeight()) {
                    Text("${nxtItem.ID}: ${nxtItem.value}")
                }
            }
        }
    } ?: Row(Modifier.fillMaxWidth().wrapContentHeight()) {
        Text("Empty")
    }
}


@Composable
@Preview
fun PreviewMainScreen() {
    MainScreen()
}