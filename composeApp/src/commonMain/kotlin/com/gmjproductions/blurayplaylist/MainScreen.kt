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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.gmjproductions.blurayplaylist.models.L3
import com.gmjproductions.blurayplaylist.models.MultiAVCHDItem
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import nl.adaptivity.xmlutil.serialization.XML
import org.jetbrains.compose.ui.tooling.preview.Preview


val uncrop =
    XML.decodeFromString<MultiAVCHDItem>("<F ID=\"UNCROP\">3|23|0|6|1280x720&#32;(No&#32;change)|1280x720|14|0|2895|4|4|3|3|3|4|7|1|Original|1|80|2|1|0|||||||||||</F>")

@Composable
fun MainScreen() {
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            var inputFile by remember { mutableStateOf<PlatformFile?>(null) }
            var contents by remember { mutableStateOf<String?>(null) }
            var parsedContents by remember { mutableStateOf<String?>(null) }

            var showFilePicker by remember { mutableStateOf(false) }

            var showFileSaver by remember { mutableStateOf(false) }

            var calcIt = remember { mutableStateOf<Calcit?>(null) }
            var errorMessage by remember { mutableStateOf<String?>(null) }
            var els = remember { mutableListOf<L2>() }

            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                Header(inputFile?.path, {
                    showFilePicker = true
                }) {
                    showFileSaver = true
                }
                ShowResults(calcIt)
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
                val result = contents?.let {
                    parseFileContents(it)
                }
                result?.also {
                    val (x, message) = it
                    errorMessage = message
                    x?.also {
                        val (p, c, l) = x
                        calcIt.value = c
                        parsedContents = p
                        els.addAll(l)
                    }
                }
            }
            LaunchedEffect(showFileSaver) {
                if (showFileSaver) {
                    calcIt.value?.also {

                        val file =
                            FileKit.openFileSaver(inputFile?.name ?: "", directory = inputFile)

                        // add els back

                        var result = XML.encodeToString<Calcit>(it)

                        println(result)
                        result = "(</F>)+".toRegex().replace(result, "$1\n")

                        //result = "(<F.*</F>)+".toRegex().replace(result,"$1*****")


                        file?.writeString(result)

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


fun parseFileContents(contents: String): Pair<Triple<String, Calcit, List<L2>>?, String?> {
    val E = "<E/>".toRegex()
    val L = "(<L>(.*)+</L>)".toRegex()
    val inTapL = "<F ID=\"INTAP\">[\\n\\s]+(.*)[\\n\\s]+</F>".toRegex()
    val chapNamesL = "<F ID=\"CHAPNAMES\">[\\n\\s]+(.*)[\\n\\s]+<\\/F>".toRegex()

    var cleanedContents = E.replace(contents, "")
    val inTapLList = inTapL.findAll(cleanedContents).map { it.groups[1]?.value }.toList()
    val chapNamesLList = chapNamesL.findAll(cleanedContents).map { it.groups[1]?.value }.toList()

    var els = L.findAll(cleanedContents).map { it.value }.toList()
    var el2s = els.map { XML.decodeFromString<L2>(it) }

    cleanedContents = L.replace(cleanedContents,"")

    try {
        val calcit = XML.decodeFromString<Calcit>(cleanedContents)
        return Pair(Triple(cleanedContents, calcit, el2s), null)
    } catch (ex: Exception) {
        return Pair(null, ex.message)
    }
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
fun ShowResults(calcit: MutableState<Calcit?>) {
    val lazyListState = rememberLazyListState()


    calcit?.value?.llist?.also { llist ->
        LazyColumn(state = lazyListState, userScrollEnabled = true) {
            itemsIndexed(llist) { index, nxtList ->
                var nxtmultiAVCHDItem = nxtList.itemList.filter { it.ID == "UNCROP" }.first()

                MultiAVCHDItemRow(nxtmultiAVCHDItem) { newItem ->
                    val indexItemToUpdate =
                        calcit.value!!.llist[index].itemList.indexOf(nxtmultiAVCHDItem)
                    calcit.value!!.llist[index].itemList[indexItemToUpdate].value = newItem.value
                }


            }
        }
    }
}

@Composable
fun MultiAVCHDItemRow(multiAVCHDItem: MultiAVCHDItem, onUpdate: (MultiAVCHDItem) -> Unit) {
    var textFieldState = rememberTextFieldState(multiAVCHDItem.value)
    var text by remember { mutableStateOf(multiAVCHDItem.value) }
    Row(
        Modifier.fillMaxWidth().wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(multiAVCHDItem.ID, Modifier.width(100.dp))
        TextField(text, { text = it })
        Button({
            text = uncrop.value
        }) {
            Text("correct")
        }
        Button({
            onUpdate(MultiAVCHDItem(multiAVCHDItem.ID, text))
        }) {
            Text("save")
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
            title = @Composable {
                Text(
                    "Serializing Error",
                    style = TextStyle(color = Color.Black, fontWeight = FontWeight.Medium)
                )
            },
            text = @Composable {
                Text(
                    it,
                    style = TextStyle(color = Color.Red, fontWeight = FontWeight.Bold)
                )
            }
        )
    }

}

@Composable
@Preview
fun PreviewMainScreen() {
    MainScreen()
}