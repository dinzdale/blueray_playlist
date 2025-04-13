package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlBefore
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName(value = "playlist")
data class Playlist (
    @XmlElement
    val title: String,
    @XmlSerialName(value = "trackList")
    val trackList: TrackList,
    @XmlSerialName(value = "extension")
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
    @XmlElement
    val item: List<Item>,

    //@Json(name = "_application")
    @XmlSerialName
    val application: String
)
@Serializable
@XmlSerialName("item", prefix = "vlc")
data class Item (
    //@Json(name = "_tid")
    @XmlSerialName
    val tid: String,

//    //@Json(name = "__prefix")
//    @XmlElement
//    val prefix: Prefix
)


@Serializable
@XmlSerialName(value = "tracklist")
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
    //val extension: TrackExtension
)

//@Serializable
//data class ID (
//    //@Json(name = "__prefix")
//    val prefix: Prefix,
//
//    //@Json(name = "__text")
//    val text: String
//)
