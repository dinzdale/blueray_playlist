package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName(value = "CALCIT")
@XmlElement
class Calcit() {
    @XmlValue
    val llist: LList? = null

}

@Serializable
@XmlSerialName(value = "L")
class LList() {

    val itemList: List<MultiAVCHDItem> = listOf()

}

@Serializable
@XmlSerialName(value = "F")
class MultiAVCHDItem() {
    @XmlElement
    val ID: String = ""

    @XmlValue
    val value: String? = null
}



