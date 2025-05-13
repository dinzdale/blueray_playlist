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

package BluRay

import java.io.IOException // Needed for catching IOError equivalent

class UOMaskTable { // In Kotlin, inheriting from Any (equivalent to Vala Object) is implicit

    // Properties translated from Vala uint8 to Kotlin Byte
    // uint8 is an unsigned 8-bit integer. Since the original code reads only 1 bit
    // into these properties, a Byte (signed 8-bit) is sufficient to hold 0 or 1.
    var MenuCall: Byte = 0
    var TitleSearch: Byte = 0
    var ChapterSearch: Byte = 0
    var TimeSearch: Byte = 0
    var SkipToNextPoint: Byte = 0
    var SkipToPrevPoint: Byte = 0

    var Stop: Byte = 0
    var PauseOn: Byte = 0

    var StillOff: Byte = 0
    var ForwardPlay: Byte = 0
    var BackwardPlay: Byte = 0
    var Resume: Byte = 0
    var MoveUpSelectedButton: Byte = 0
    var MoveDownSelectedButton: Byte = 0
    var MoveLeftSelectedButton: Byte = 0
    var MoveRightSelectedButton: Byte = 0
    var SelectButton: Byte = 0
    var ActivateButton: Byte = 0
    var SelectAndActivateButton: Byte = 0
    var PrimaryAudioStreamNumberChange: Byte = 0

    var AngleNumberChange: Byte = 0
    var PopupOn: Byte = 0
    var PopupOff: Byte = 0
    var PGEnableDisable: Byte = 0
    var PGStreamNumberChange: Byte = 0
    var SecondaryVideoEnableDisable: Byte = 0
    var SecondaryVideoStreamNumberChange: Byte = 0
    var SecondaryAudioEnableDisable: Byte = 0
    var SecondaryAudioStreamNumberChange: Byte = 0

    var SecondaryPGStreamNumberChange: Byte = 0

    // Constructor translated from Vala
    // Vala: UOMaskTable.from_bit_input_stream (BitInputStream input_stream) throws ParseError
    // Kotlin: constructor(inputStream: BitInputStream) throws ParseError
    @Throws(ParseError::class) // Explicitly declare that this constructor can throw ParseError
    constructor (inputStream: BitInputStream) {
        try {
            // Read 1 bit for each property
            MenuCall = inputStream.readBitsAsUint8(1)
            TitleSearch = inputStream.readBitsAsUint8(1)
            ChapterSearch = inputStream.readBitsAsUint8(1)
            TimeSearch = inputStream.readBitsAsUint8(1)
            SkipToNextPoint = inputStream.readBitsAsUint8(1)
            SkipToPrevPoint = inputStream.readBitsAsUint8(1)

            // Skip 1 bit as per original code
            inputStream.skipBits(1)

            Stop = inputStream.readBitsAsUint8(1)
            PauseOn = inputStream.readBitsAsUint8(1)

            // Skip 1 bit as per original code
            inputStream.skipBits(1)

            StillOff = inputStream.readBitsAsUint8(1)
            ForwardPlay = inputStream.readBitsAsUint8(1)
            BackwardPlay = inputStream.readBitsAsUint8(1)
            Resume = inputStream.readBitsAsUint8(1)
            MoveUpSelectedButton = inputStream.readBitsAsUint8(1)
            MoveDownSelectedButton = inputStream.readBitsAsUint8(1)
            MoveLeftSelectedButton = inputStream.readBitsAsUint8(1)
            MoveRightSelectedButton = inputStream.readBitsAsUint8(1)
            SelectButton = inputStream.readBitsAsUint8(1)
            ActivateButton = inputStream.readBitsAsUint8(1)
            SelectAndActivateButton = inputStream.readBitsAsUint8(1)
            PrimaryAudioStreamNumberChange = inputStream.readBitsAsUint8(1)

            // Skip 1 bit as per original code
            inputStream.skipBits(1)

            AngleNumberChange = inputStream.readBitsAsUint8(1)
            PopupOn = inputStream.readBitsAsUint8(1)
            PopupOff = inputStream.readBitsAsUint8(1)
            PGEnableDisable = inputStream.readBitsAsUint8(1)
            PGStreamNumberChange = inputStream.readBitsAsUint8(1)
            SecondaryVideoEnableDisable = inputStream.readBitsAsUint8(1)
            SecondaryVideoStreamNumberChange = inputStream.readBitsAsUint8(1)
            SecondaryAudioEnableDisable = inputStream.readBitsAsUint8(1)
            SecondaryAudioStreamNumberChange = inputStream.readBitsAsUint8(1)

            // Skip 1 bit as per original code
            inputStream.skipBits(1)

            SecondaryPGStreamNumberChange = inputStream.readBitsAsUint8(1)

            // Skip 30 bits as per original code
            inputStream.skipBits(30)
        } catch (e: IOException) { // Catch IOException, equivalent to Vala IOError
            // Throw a new ParseError with a specific message
            // Vala: throw new ParseError.INPUT_ERROR ("Couldn't parse UOMaskTable.");
            // Kotlin: throw ParseError("Couldn't parse UOMaskTable.")
            throw ParseError("Couldn't parse UOMaskTable.")
        }
    }

//		public void write (BitOutputStream output_stream)
//		{
//		}
}
