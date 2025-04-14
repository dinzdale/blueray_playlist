package com.gmjproductions.blurayplaylist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import blurayplaylist.composeapp.generated.resources.Res
import com.gmjproductions.blurayplaylist.models.Playlist
import nl.adaptivity.xmlutil.serialization.XML
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var xspfContents by remember { mutableStateOf<String?>(null) }
        LaunchedEffect(Unit) {
            xspfContents = loadPlaylist()
        }
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
           xspfContents?.also {
               Text("playlist: $xspfContents")
           }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
suspend fun loadPlaylist() : String {
    val byteArray = Res.readBytes("files/m2ts_2.xspf")
    val result = String(byteArray)

    val o = XML.decodeFromString<Playlist>(result)
    return o.toString()
}

private val simpleTest =
"<person>"+
"<firstname>Brunck</firstname>"+
"<lastname>McGuint</lastname>"+
"</person>"