package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName(value = "CALCIT")
class Calcit() {
    @XmlElement
    val l : TheList = TheList()
}

@Serializable
@XmlSerialName(value = "L")
class TheList() {
    val itemList : ArrayList<MultiAVCHDItem> = arrayListOf()
}

@Serializable
@XmlSerialName(value = "F")
class MultiAVCHDItem() {
    val ID : String = ""
}