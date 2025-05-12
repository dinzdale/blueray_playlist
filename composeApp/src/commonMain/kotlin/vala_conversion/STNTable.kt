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

import java.util.ArrayList
import java.lang.Exception





// Placeholder for StreamAttributes
// Assume it has a constructor that takes BitInputStream
// This is a minimal implementation to allow the STNTable class to compile.
class StreamAttributes {
    constructor(inputStream: BitInputStream) {
        // Empty constructor body
    }
}

// Placeholder for ParseError
// Assume it's an Exception and has a constructor that takes a message and optionally a cause
// This is a minimal implementation to allow the STNTable class to compile.
class ParseError : Exception {
    constructor(message: String) : super(message) { }
    constructor(message: String, cause: Throwable?) : super(message, cause) { }
}

// Placeholder for IOError
// Assume it's an Exception
// This is a minimal implementation to allow the STNTable class to compile.
class IOError : Exception {
    constructor(message: String) : super(message) { }
    constructor(message: String, cause: Throwable?) : super(message, cause) { }
}

class STNTable
{
    var PrimaryVideoStreamEntry: ArrayList<StreamEntry>
    var PrimaryVideoStreamAttributes: ArrayList<StreamAttributes>

    var PrimaryAudioStreamEntry: ArrayList<StreamEntry>
    var PrimaryAudioStreamAttributes: ArrayList<StreamAttributes>

    var PrimaryPGStreamEntry: ArrayList<StreamEntry>
    var PrimaryPGStreamAttributes: ArrayList<StreamAttributes>

    var PrimaryIGStreamEntry: ArrayList<StreamEntry>
    var PrimaryIGStreamAttributes: ArrayList<StreamAttributes>

    var SecondaryVideoStreamEntry: ArrayList<StreamEntry>
    var SecondaryVideoStreamAttributes: ArrayList<StreamAttributes>

    var SecondaryAudioStreamEntry: ArrayList<StreamEntry>
    var SecondaryAudioStreamAttributes: ArrayList<StreamAttributes>

    var SecondaryPGStreamEntry: ArrayList<StreamEntry>
    var SecondaryPGStreamAttributes: ArrayList<StreamAttributes>

    @Throws(ParseError::class)
    constructor (input_stream: BitInputStream)
    {
        try
        {
            val Length = input_stream.readBitsAsUint16 (16)

            val Position = input_stream.tell () // Needed to seek

            input_stream.skipBits (16)

            val NumberOfPrimaryVideoStreams = input_stream.readBitsAsUint8 (8)
            val NumberOfPrimaryAudioStreams = input_stream.readBitsAsUint8 (8)
            val NumberOfPrimaryPGStreams = input_stream.readBitsAsUint8 (8)
            val NumberOfPrimaryIGStreams = input_stream.readBitsAsUint8 (8)
            val NumberOfSecondaryAudioStreams = input_stream.readBitsAsUint8 (8)
            val NumberOfSecondaryVideoStreams = input_stream.readBitsAsUint8 (8)
            val NumberOfSecondaryPGStreams = input_stream.readBitsAsUint8 (8)

            input_stream.skipBits (40)

            PrimaryVideoStreamEntry = ArrayList<StreamEntry> ()
            PrimaryVideoStreamAttributes = ArrayList<StreamAttributes> ()

            for (i in 0 until NumberOfPrimaryVideoStreams.toInt())
            {
                // StreamEntry
                PrimaryVideoStreamEntry.add (StreamEntry (input_stream))

                // StreamAttributes
                PrimaryVideoStreamAttributes.add (StreamAttributes (input_stream))
            }

            PrimaryAudioStreamEntry = ArrayList<StreamEntry> ()
            PrimaryAudioStreamAttributes = ArrayList<StreamAttributes> ()

            for (i in 0 until NumberOfPrimaryAudioStreams.toInt())
            {
                // StreamEntry
                PrimaryAudioStreamEntry.add (StreamEntry (input_stream))

                // StreamAttributes
                PrimaryAudioStreamAttributes.add (StreamAttributes (input_stream))
            }

            PrimaryPGStreamEntry = ArrayList<StreamEntry> ()
            PrimaryPGStreamAttributes = ArrayList<StreamAttributes> ()

            for (i in 0 until NumberOfPrimaryPGStreams.toInt())
            {
                // StreamEntry
                PrimaryPGStreamEntry.add (StreamEntry (input_stream))

                // StreamAttributes
                PrimaryPGStreamAttributes.add (StreamAttributes (input_stream))
            }

            SecondaryPGStreamEntry = ArrayList<StreamEntry> ()
            SecondaryPGStreamAttributes = ArrayList<StreamAttributes> ()

            for (i in 0 until NumberOfSecondaryPGStreams.toInt())
            {
                // StreamEntry
                SecondaryPGStreamEntry.add (StreamEntry (input_stream))

                // StreamAttributes
                SecondaryPGStreamAttributes.add (StreamAttributes (input_stream))
            }

            PrimaryIGStreamEntry = ArrayList<StreamEntry> ()
            PrimaryIGStreamAttributes = ArrayList<StreamAttributes> ()

            for (i in 0 until NumberOfPrimaryIGStreams.toInt())
            {
                // StreamEntry
                PrimaryIGStreamEntry.add (StreamEntry (input_stream))

                // StreamAttributes
                PrimaryIGStreamAttributes.add (StreamAttributes (input_stream))
            }

            SecondaryAudioStreamEntry = ArrayList<StreamEntry> ()
            SecondaryAudioStreamAttributes = ArrayList<StreamAttributes> ()

            for (i in 0 until NumberOfSecondaryAudioStreams.toInt())
            {
                // StreamEntry
                SecondaryAudioStreamEntry.add (StreamEntry (input_stream))

                // StreamAttributes
                SecondaryAudioStreamAttributes.add (StreamAttributes (input_stream))
            }

            SecondaryVideoStreamEntry = ArrayList<StreamEntry> ()
            SecondaryVideoStreamAttributes = ArrayList<StreamAttributes> ()

            for (i in 0 until NumberOfSecondaryVideoStreams.toInt())
            {
                // StreamEntry
                SecondaryVideoStreamEntry.add (StreamEntry (input_stream))

                // StreamAttributes
                SecondaryVideoStreamAttributes.add (StreamAttributes (input_stream))
            }

            input_stream.seek (Position + Length.toLong())
        }
        catch (e: ParseError)
        {
            throw e
        }
        catch (e: IOError)
        {
            throw ParseError ("Couldn't parse STNTable.")
        }
    }

//		public void write (BitOutputStream output_stream)
//		{
//		}
}
