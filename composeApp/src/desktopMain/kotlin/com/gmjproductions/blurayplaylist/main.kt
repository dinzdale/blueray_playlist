package com.gmjproductions.blurayplaylist

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.vinceglb.filekit.FileKit

fun main() = application {
    FileKit.init("MultiAVCHDProject")
    val windowState = rememberWindowState(size = DpSize(1200.dp,600.dp))
    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = "MultiAVCHD Project Builder",
    ) {
        App()
    }
}

