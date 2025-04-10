package com.gmjproductions.blurayplaylist

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform