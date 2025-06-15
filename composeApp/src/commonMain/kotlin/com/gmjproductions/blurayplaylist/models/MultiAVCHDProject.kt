package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName(value = "CALCIT")
class Calcit() {
    @XmlElement
    val llist : LList? = null
}

@Serializable
@XmlSerialName(value = "L")
class LList() {
    val itemList : ArrayList<MultiAVCHDItem>? = null
}

@Serializable
@XmlSerialName(value = "F")
class MultiAVCHDItem() {
    val ID : String = ""
    @XmlValue
    val value : String = ""
}

@Serializable
@XmlSerialName(value = "E")
class E() {}

