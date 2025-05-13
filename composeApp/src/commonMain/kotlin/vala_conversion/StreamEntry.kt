package BluRay

import java.io.IOException
import kotlin.experimental.ExperimentalUnsignedTypes

/*
 * BluRay - a simple sample implementation of Blu-ray Disc specifications
 * Copyright (C) 2011  Luca Wehrstedt

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

// Define the necessary exceptions and interfaces used by StreamEntry

// Custom exception for parsing errors, mapping Vala's ParseError
sealed class ParseError(message: String, cause: Throwable? = null) : Exception(message, cause) {
    // Represents an error during input processing, mapping Vala's ParseError.INPUT_ERROR
    class InputError(message: String, cause: Throwable? = null) : `ParseError.kt`(message, cause)
    // Add other specific error types if needed based on Vala's ParseError definition
}

// Interface representing the BitInputStream used by StreamEntry
// This interface defines the methods called in the Vala code
interface BitInputStream {
    // Reads the specified number of bits and returns them as a UByte (uint8)
    fun read_bits_as_uint8(bits: Int): UByte

    // Reads the specified number of bits and returns them as a UShort (uint16)
    fun read_bits_as_uint16(bits: Int): UShort

    // Returns the current position in the stream as a Long (int64)
    fun tell(): Long

    // Seeks to the specified position in the stream (int64)
    fun seek(position: Long): Unit
}

// Opt-in for using unsigned types (UByte, UShort)
@OptIn(ExperimentalUnsignedTypes::class)
class StreamEntry { // In Kotlin, classes implicitly inherit from Any (similar to Vala's Object)

    // Public properties with getters and setters, mapping Vala's public properties
    var StreamType: UByte = 0u // uint8 maps to UByte
    var RefToSubPathID: UByte = 0u // uint8 maps to UByte
    var RefToSubClipID: UByte = 0u // uint8 maps to UByte
    var RefToStreamPID: UShort = 0u // uint16 maps to UShort

    // Constructor equivalent to Vala's named constructor 'from_bit_input_stream'
    // It takes a BitInputStream and is designed to parse data from it
    constructor(input_stream: BitInputStream) {
        try {
            // Read the Length field (8 bits) as uint8
            val Length: UByte = input_stream.read_bits_as_uint8(8)

            // Get the current position in the stream (int64)
            val Position: Long = input_stream.tell() // Needed to seek later

            // Read the StreamType (8 bits) as uint8
            StreamType = input_stream.read_bits_as_uint8(8)

            // Conditional logic based on the StreamType value
            if (StreamType == 0x01.toUByte()) {
                // If StreamType is 0x01, read RefToStreamPID (16 bits)
                RefToStreamPID = input_stream.read_bits_as_uint16(16)
            } else if (StreamType == 0x02.toUByte()) {
                // If StreamType is 0x02, read RefToSubPathID (8 bits), RefToSubClipID (8 bits), and RefToStreamPID (16 bits)
                RefToSubPathID = input_stream.read_bits_as_uint8(8)
                RefToSubClipID = input_stream.read_bits_as_uint8(8)
                RefToStreamPID = input_stream.read_bits_as_uint16(16)
            } else if (StreamType == 0x03.toUByte()) {
                // If StreamType is 0x03, read RefToStreamPID (16 bits)
                RefToStreamPID = input_stream.read_bits_as_uint16(16)
            } else if (StreamType == 0x04.toUByte()) {
                // If StreamType is 0x04, read RefToSubPathID (8 bits), RefToSubClipID (8 bits), and RefToStreamPID (16 bits)
                RefToSubPathID = input_stream.read_bits_as_uint8(8)
                RefToSubClipID = input_stream.read_bits_as_uint8(8)
                RefToStreamPID = input_stream.read_bits_as_uint16(16)
            }

            // Seek the stream forward by the Length read initially
            // Convert UByte Length to Long for addition with Position (Long)
            input_stream.seek(Position + Length.toLong())

        } catch (e: IOException) { // Catch IOError, which is mapped to java.io.IOException
            // Throw a new ParseError.InputError, wrapping the original IOException
            throw `ParseError.kt`.InputError("Couldn't parse StreamEntry.", e)
        }
    }

    // Commented-out method from the original Vala code is preserved as a comment
//    fun write (output_stream: BitOutputStream) {
//    }
}
