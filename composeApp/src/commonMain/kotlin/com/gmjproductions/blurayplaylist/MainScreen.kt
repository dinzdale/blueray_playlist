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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gmjproductions.blurayplaylist.models.CALCIT
import com.gmjproductions.blurayplaylist.models.Item
import com.gmjproductions.blurayplaylist.models.MultiAVCHDItem
import com.gmjproductions.blurayplaylist.models.MultiAVCHDItemsIDs
import com.gmjproductions.blurayplaylist.theme.BlueRaySecondary
import com.gmjproductions.blurayplaylist.ui.ActionButton
import com.gmjproductions.blurayplaylist.ui.ItemUpdate
import com.gmjproductions.blurayplaylist.ui.resolutionSelections
import com.gmjproductions.blurayplaylist.ui.resolutions
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import nl.adaptivity.xmlutil.serialization.XML
import org.jetbrains.compose.ui.tooling.preview.Preview


typealias ParsedResults = Pair<ParsedParts?, String?>
typealias FilteredLs = Pair<String, String>
typealias ParsedParts = Triple<String, CALCIT, List<FilteredLs>>

// resolutions
val uncrop1280X720 =
    XML.decodeFromString<MultiAVCHDItem>("<F ID=\"UNCROP\">3|23|0|6|1280x720&#32;(No&#32;change)|1280x720|14|0|3137|4|4|3|3|3|4|7|1|Original|1|80|2|1|0|||||||||||</F>")
val uncrop1920x1280 =
    XML.decodeFromString<MultiAVCHDItem>(
        "<F ID=\"UNCROP\">" +
                "3|23|0|52|1920x1080|1920x1080|14|0|3137|4|4|3|3|3|4|7|1|Original|1|80|2|1|0|||||||||||" +
                "</F>"
    )


val uncropResolutions: StateFlow<MultiAVCHDItem>
    field = MutableStateFlow<MultiAVCHDItem>(uncrop1280X720)

@Composable
fun MainScreen() {

    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            var inputFile by remember { mutableStateOf<PlatformFile?>(null) }
            var contents by remember { mutableStateOf<String?>(null) }
            var parsedContents by remember { mutableStateOf<String?>(null) }

            var showFilePicker by remember { mutableStateOf(false) }

            var showFileSaver by remember { mutableStateOf(false) }

            var calcIt = remember { mutableStateOf<CALCIT?>(null) }
            var errorMessage by remember { mutableStateOf<String?>(null) }
            var filterLsList = remember { mutableStateListOf<FilteredLs>() }


            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                Header(inputFile?.absolutePath(), {
                    showFilePicker = true
                }) {
                    showFileSaver = true
                }

                calcIt.value?.also {
                    ShowResults(it) { theList ->
                        theList.forEachIndexed { index, map ->
                            calcIt.value?.also {
                                it.updateItem(
                                    index,
                                    MultiAVCHDItemsIDs.NAME,
                                    map[MultiAVCHDItemsIDs.NAME]!!.value
                                )
                                it.updateItem(
                                    index,
                                    MultiAVCHDItemsIDs.UNCROP,
                                    map[MultiAVCHDItemsIDs.UNCROP]!!.value
                                )
                            }
                        }

                    }

                }
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
                val parsedResults = contents?.let {
                    parseFileContents(it)
                }
                println("parsedResults: $parsedResults")
                parsedResults?.also {
                    val (parsedParts, message) = it
                    errorMessage = message
                    parsedParts?.also {
                        val (parsedContentS, calcit, filteredLsList) = it
                        calcIt.value = calcit
                        parsedContents = parsedContentS
                        filterLsList.addAll(filteredLsList)
                    }
                }
            }
            LaunchedEffect(showFileSaver) {
                if (showFileSaver) {
                    calcIt.value?.also { calcit ->

                        val file =
                            FileKit.openFileSaver(inputFile?.name ?: "", directory = inputFile)

                        // add els back
                        calcit.llist.forEachIndexed() { index, nxtLList ->
                            // val (intapValue,chapNamesValue) = filterLsList[index]
                            var nxtGrpIndex = nxtLList.itemList.indexOfFirst { it.ID == "INTAP" }
                            nxtLList.itemList[nxtGrpIndex].value = "LLLLL${index}"
                            nxtGrpIndex = nxtLList.itemList.indexOfFirst { it.ID == "CHAPNAMES" }
                            nxtLList.itemList[nxtGrpIndex].value = "CCCCC${index}"
                        }
                        var result = XML.encodeToString<CALCIT>(calcit)
                        result = result.replace("*****", "<E/>")
                        result = result.replace("^\\s*$", "")
                        filterLsList.forEachIndexed { index, nxtEntry ->
                            val (intapValue, chapNamesValue) = nxtEntry
                            result = result.replace("LLLLL${index}", intapValue)
                            result = result.replace("CCCCC${index}", chapNamesValue)
                        }
                        //result = prettyPrintXml(result,2)
                        println(result)

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


fun parseFileContents(contents: String): ParsedResults {
    val E = "<E/>".toRegex()
    val L = "(<L>(.*)</L>)".toRegex()
    val inTapL = "<F ID=\"INTAP\">[\\n\\s]+(.*)[\\n\\s]+</F>".toRegex()
    val chapNamesL = "<F ID=\"CHAPNAMES\">[\\n\\s]+(.*)[\\n\\s]+<\\/F>".toRegex()

    var cleanedContents = E.replace(contents, "*****")
    val inTapLList = inTapL.findAll(cleanedContents).map { it.groups[1]?.value }.toList()
    val chapNamesLList = chapNamesL.findAll(cleanedContents).map { it.groups[1]?.value }.toList()

    cleanedContents = L.replace(cleanedContents, "")

    val filteredLList = mutableListOf<FilteredLs>()
    inTapLList.forEachIndexed { index, nxtTapL ->
        filteredLList.add(FilteredLs(inTapLList[index] ?: "", chapNamesLList[index] ?: ""))
    }
    try {
        val calcit = XML.decodeFromString<CALCIT>(cleanedContents)
        return ParsedResults(
            ParsedParts(
                cleanedContents,
                calcit, filteredLList
            ), null
        )

    } catch (ex: Exception) {
        return ParsedResults(null, ex.message)
    }
}


@Composable
fun Header(filePath: String?, onOpenFileClick: () -> Unit, onSaveFile: () -> Unit) {


    Column(
        Modifier.fillMaxWidth().wrapContentHeight().background(color = BlueRaySecondary).height(150.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            Modifier.fillMaxWidth().padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ActionButton("Open File", "Open MultiAVCHD project file to modify", onClick = {
                onOpenFileClick()
                true
            })
            Spacer(Modifier.width(10.dp))
            Text(filePath ?: "")
        }
        resolutionSelections {
            when (it) {
                resolutions[0] -> uncropResolutions.tryEmit(uncrop1280X720)
                resolutions[1] -> uncropResolutions.tryEmit(uncrop1920x1280)
            }
        }
        Row(
            Modifier.wrapContentSize().padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(Modifier.width(10.dp))
            ActionButton(
                "Save",
                "Save updated project file",
                filePath?.isNotBlank() ?: false,
                {
                    onSaveFile()
                    true
                }
            )
        }
    }
}

@Composable
fun ShowResults(calcit: CALCIT, onSave: (List<Map<MultiAVCHDItemsIDs, MultiAVCHDItem>>) -> Unit) {
    val lazyListState = rememberLazyListState()

    val theList = remember {
        mutableStateListOf<SnapshotStateMap<MultiAVCHDItemsIDs, MultiAVCHDItem>>().also {
            it.addAll(calcit.asList())
        }
    }


    LazyColumn(state = lazyListState, userScrollEnabled = true) {

        itemsIndexed(theList) { index, item ->

            ItemUpdate(theList[index][MultiAVCHDItemsIDs.NAME]!!.value, onConvert = {
                theList[index][MultiAVCHDItemsIDs.NAME] =
                    item[MultiAVCHDItemsIDs.NAME]!!.copy(value = convertFilename(it))
            }, onUnDo = {
                theList[index][MultiAVCHDItemsIDs.NAME]?.value.toString()
            }, onSave = {
                onSave(theList)
            }, onGlobalConvert = {
                theList.forEachIndexed { index, nxtItem ->
                    nxtItem[MultiAVCHDItemsIDs.NAME]?.also {
                        theList[index][MultiAVCHDItemsIDs.NAME] =
                            it.copy(value = convertFilename(it.value))
                    }
                }
            })
            ItemUpdate(theList[index][MultiAVCHDItemsIDs.UNCROP]!!.value, onConvert = {
                theList[index][MultiAVCHDItemsIDs.UNCROP] =
                    item[MultiAVCHDItemsIDs.UNCROP]!!.copy(value = uncropResolutions.value.value)
            }, onUnDo = {
                theList[index][MultiAVCHDItemsIDs.UNCROP]?.value.toString()
            }, onSave = {
                theList[index][MultiAVCHDItemsIDs.UNCROP]?.value = it
                onSave(theList)
            }, onGlobalConvert = {
                theList.forEachIndexed { index, nxtItem ->
                    nxtItem[MultiAVCHDItemsIDs.UNCROP]?.also {
                        theList[index][MultiAVCHDItemsIDs.UNCROP] =
                            it.copy(value = uncropResolutions.value.value)
                    }
                }
            })
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

fun convertFilename(filename: String) = "^.* - (.*)\$".toRegex().replace(filename, "$1")

@Composable
@Preview
fun PreviewMainScreen() {
    MainScreen()
}