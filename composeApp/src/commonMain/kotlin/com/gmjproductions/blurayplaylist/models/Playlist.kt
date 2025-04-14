package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlBefore
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
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
//
//    //@Json(name = "_xmlns")
//    val xmlns: String,
//
//    //@Json(name = "_xmlns:vlc")
//    val xmlnsVlc: String,
//
//    //@Json(name = "_version")
//    val version: String
)
@Serializable
@XmlSerialName(value = "extension")
data class PlaylistExtension (
    @XmlSerialName("application")
    val application: String,

    @XmlElement
    val item: List<Item>,

    //@Json(name = "_application")

)
@Serializable
@XmlSerialName(value = "item", namespace = "http://www.videolan.org/vlc/playlist/ns/0/", prefix = "vlc")
data class Item(
    @XmlElement
    val tid: String,
)

@Serializable
@XmlSerialName("trackList")
data class TrackList (
    @XmlElement
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
    @XmlSerialName("application")
    val application: String,
    @XmlSerialName("id",ns2,"vlc")
    val id : String
)

