package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

enum class MultiAVCHDItemsIDs(id: String) {
    UNCROP("UNCROP"), NAME("NAME"), IGNORE("IGNORE");

    companion object {
        fun toItem(id: String) = MultiAVCHDItemsIDs.values().find { it.name == id } ?: IGNORE

    }
}

@Serializable
data class CALCIT(val llist: List<LList>) {
    fun updateItem(index: Int, id: MultiAVCHDItemsIDs, value: String) {
        if (llist.isNotEmpty() && index <= llist.lastIndex) {
            val thisList = llist[index].itemList
            val itemIndex = thisList.indexOfFirst { it.ID == id.name }
            if (itemIndex > -1) {
                thisList[itemIndex].value = value
            }
        }
    }
}


@Serializable
@XmlSerialName(value = "L")
data class LList(val itemList: List<MultiAVCHDItem>)


@Serializable
@XmlSerialName(value = "F")
data class MultiAVCHDItem(val ID: String, @XmlValue var value: String)

@Serializable
@XmlSerialName(value = "L")
data class L2(@XmlValue(true) val value: String)

@Serializable
@XmlSerialName(value = "LTemp")
data class L3(@XmlValue(true) val value: String)



