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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.gmjproductions.blurayplaylist.models.Calcit
import nl.adaptivity.xmlutil.serialization.XML
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.BufferedReader
import java.io.FileReader
import java.util.logging.Logger


@Composable
fun MainScreen() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            var inputFile by remember { mutableStateOf<String?>(null) }
            var showFilePicker by remember { mutableStateOf(false) }
            var inputSettings by remember { mutableStateOf<Calcit?>(null) }

            Column (Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                Header(inputFile) {
                    showFilePicker = true
                }
                ShowResults(inputSettings)
            }
            SelectInputFile(showFilePicker) {
                showFilePicker = false
                inputFile = it
            }
            LaunchedEffect(inputFile) {
                inputFile?.also {
                    System.out.println("Before Parse")
                    inputSettings = ParseInputFile(it)
                    System.out.println("After Parse")
                    inputSettings?.llist?.itemList?.forEach {
                        System.out.println("${it.ID}: ${it.value}")
                    }
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
    val fileReader = FileReader(filePath)
    var contents = BufferedReader(fileReader).readText()
    contents = cleanupFileContents(contents)
    XML.decodeFromString<Calcit>(contents)
}

fun cleanupFileContents(contents:String) : String {
    val date="<.*DATE.*F>".toRegex()
    val stringToRemove = date.find(contents)?.value
    val cleanedContents = contents.replace(date,"")
    return cleanedContents
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