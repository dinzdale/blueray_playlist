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
import com.gmjproductions.blurayplaylist.theme.AppTheme
import nl.adaptivity.xmlutil.serialization.XML
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.xml.sax.XMLReader
import java.io.StringReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

@Composable
@Preview
fun App() {
    AppTheme {
        MainScreen()
    }
}

fun loadProjectDefault() {
    XMLInputFactory.newDefaultFactory()


}

private val simpleTest =
    "<person>" +
            "<firstname>Brunck</firstname>" +
            "<lastname>McGuint</lastname>" +
            "</person>"