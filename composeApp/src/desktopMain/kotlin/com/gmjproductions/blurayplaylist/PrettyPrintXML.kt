package com.gmjproductions.blurayplaylist


import java.io.StringReader
import java.io.StringWriter

import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

actual fun prettyPrintXml(xmlString: String, indent: Int): String {
    return try {
        val src = StreamSource(StringReader(xmlString))
        val res = StreamResult(StringWriter())
        val transformer = TransformerFactory.newInstance().newTransformer()

        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indent.toString())
        // For standalone XML, you might want to omit the XML declaration
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")

        transformer.transform(src, res)
        res.writer.toString()
    } catch (e: Exception) {
        // Handle transformation error, e.g., return original string or throw
        e.printStackTrace()
        xmlString
    }
}