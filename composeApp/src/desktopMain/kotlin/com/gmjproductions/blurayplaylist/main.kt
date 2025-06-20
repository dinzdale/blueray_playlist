package com.gmjproductions.blurayplaylist

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit

fun main() = application {
    FileKit.init("MultiAVCHDProject")
    Window(
        onCloseRequest = ::exitApplication,
        title = "BluRay Playlist",
    ) {
        App()
    }
}