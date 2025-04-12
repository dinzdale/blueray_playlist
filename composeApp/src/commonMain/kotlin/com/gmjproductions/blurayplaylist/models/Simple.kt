package com.gmjproductions.blurayplaylist.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("person")
@XmlSerialName(value = "person")
class person(
    @XmlElement val firstname: String,
    @XmlElement val lastname: String
)