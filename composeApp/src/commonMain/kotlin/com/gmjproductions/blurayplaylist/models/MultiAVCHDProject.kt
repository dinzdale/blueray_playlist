package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
data class CALCIT(val llist: List<LList>)

@Serializable
@XmlSerialName(value = "L")
data class LList(val itemList: List<MultiAVCHDItem> )


@Serializable
@XmlSerialName(value = "F")
data class MultiAVCHDItem(val ID: String, @XmlValue var value:  String )

@Serializable
@XmlSerialName(value = "L")
data class L2(@XmlValue(true) val value : String)

@Serializable
@XmlSerialName(value = "LTemp")
data class L3(@XmlValue(true) val value : String)



