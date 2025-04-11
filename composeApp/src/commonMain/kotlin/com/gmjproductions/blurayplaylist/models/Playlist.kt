package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.Serializable

// To parse the JSON, install Klaxon and do:
//
//   val welcome5 = Welcome5.fromJson(jsonString)

//package codebeautify

//import com.beust.klaxon.*
//
//private fun <T> Klaxon.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonValue) -> T, toJson: (T) -> String, isUnion: Boolean = false) =
//    this.converter(object: Converter {
//        @Suppress("UNCHECKED_CAST")
//        override fun toJson(value: Any)        = toJson(value as T)
//        override fun fromJson(jv: JsonValue)   = fromJson(jv) as Any
//        override fun canConvert(cls: Class<*>) = cls == k.java || (isUnion && cls.superclass == k.java)
//    })
//
//private val klaxon = Klaxon()
//    .convert(Prefix::class, { Prefix.fromValue(it.string!!) }, { "\"${it.value}\"" })
//
//data class Welcome5 (
//    val playlist: Playlist
//) {
//    public fun toJson() = klaxon.toJsonString(this)
//
//    companion object {
//        public fun fromJson(json: String) = klaxon.parse<Welcome5>(json)
//    }
//}
@Serializable
data class Playlists(val playlists:List<Playlist>)
@Serializable
data class Playlist (
    val title: String,
    val trackList: TrackList,
    val extension: PlaylistExtension,

    //@Json(name = "_xmlns")
    val xmlns: String,

    //@Json(name = "_xmlns:vlc")
    val xmlnsVlc: String,

    //@Json(name = "_version")
    val version: String
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
data class TrackList (
    val track: List<Track>
)
@Serializable
data class Track (
    val location: String,
    val duration: String,
    val extension: TrackExtension
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
