package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

const val ns1="http://xspf.org/ns/0/"
const val ns2="http://www.videolan.org/vlc/playlist/ns/0/"

@Serializable
@XmlSerialName(value = "playlist", namespace = ns1)
data class Playlist (
    val version:String,
    @XmlElement
    val title: String,
    @XmlElement
    val trackList: TrackList,
    @XmlElement
    val extension: PlaylistExtension,
)
@Serializable
@XmlSerialName(value = "extension")
data class PlaylistExtension (
    @XmlSerialName("application")
    val application: String,
    val item: List<Item>
)
@Serializable
@XmlSerialName(value = "item", namespace = ns2, prefix = "vlc")
data class Item(
    val tid: String,
)

@Serializable
@XmlSerialName("trackList")
data class TrackList (
    val track: List<Track>
)
@Serializable
@XmlSerialName(value = "track")
data class Track (
    @XmlElement
    val location: String,
    @XmlElement
    val duration: String,
    val extension: TrackExtension
)
@Serializable
@XmlSerialName("extension")
data class TrackExtension (
    val application: String,
    val id : ID
)

@Serializable
@XmlSerialName("id",ns2)
data class ID(
    @XmlValue
    val id : String
)

