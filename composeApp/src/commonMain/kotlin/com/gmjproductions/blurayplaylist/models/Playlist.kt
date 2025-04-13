package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("playlist")
@XmlSerialName(value = "playlist")
data class Playlist (
    @XmlElement
    val title: String,
    @XmlSerialName(value = "trackList")
    val trackList: TrackList
//    val extension: PlaylistExtension,
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
data class PlaylistExtension (
    val item: List<Item>,

    //@Json(name = "_application")
    val application: String
)
@Serializable
data class Item (
    //@Json(name = "_tid")
    val tid: String,

    //@Json(name = "__prefix")
    val prefix: Prefix
)
@Serializable
enum class Prefix(val value: String) {
    Vlc("vlc");

    companion object {
        public fun fromValue(value: String): Prefix = when (value) {
            "vlc" -> Vlc
            else  -> throw IllegalArgumentException()
        }
    }
}
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
@Serializable
data class TrackExtension (
    val id: ID,

    //@Json(name = "_application")
    val application: String
)
@Serializable
data class ID (
    //@Json(name = "__prefix")
    val prefix: Prefix,

    //@Json(name = "__text")
    val text: String
)
