package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlId
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
data class Calcit(val llist: List<LList>)

@Serializable
@XmlSerialName(value = "L")
data class LList(val itemList: List<MultiAVCHDItem> )


@Serializable
@XmlSerialName(value = "F")
data class MultiAVCHDItem(val ID: String, @XmlValue var value:  String )

@Serializable
@XmlSerialName(value = "L")
data class L2(@XmlValue(true) val value : String)



