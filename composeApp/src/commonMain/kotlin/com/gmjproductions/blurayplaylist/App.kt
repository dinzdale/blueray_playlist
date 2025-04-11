package com.gmjproductions.blurayplaylist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import org.jetbrains.compose.ui.tooling.preview.Preview

import blurayplaylist.composeapp.generated.resources.Res
import blurayplaylist.composeapp.generated.resources.compose_multiplatform
import com.gmjproductions.blurayplaylist.models.Playlists
import nl.adaptivity.xmlutil.serialization.XML
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.io.File
import java.nio.file.DirectoryStream
import java.nio.file.FileSystem
import java.nio.file.Files

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
    val byteArray = Res.readBytes("files/m2ts.xspf")
    val result = String(byteArray)
    return result
}