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

package bluray

import java.io.IOException
import java.nio.charset.Charsets
import kotlin.experimental.ExperimentalUnsignedTypes

// Define SeekType enum used by the custom stream interface
enum class SeekType {
    SET, // Seek relative to the beginning of the stream
    CUR, // Seek relative to the current position
    END  // Seek relative to the end of the stream
}

// Define an interface that mirrors the required methods of the Vala FileInputStream
// This is necessary because standard Java/Kotlin InputStream does not support tell() or seek().
// The Vala code uses GLib.FileInputStream which has these methods.
interface CustomFileInputStream {
    // Reads up to buffer.size bytes from the stream into the buffer.
    // Returns the number of bytes read, or -1 if the end of the stream is reached.
    // The Vala code assumes this reads exactly buffer.size or throws.
    @Throws(IOException::class)
    fun read(buffer: ByteArray): Int

    // Skips over and discards n bytes of data from this input stream.
    // Returns the actual number of bytes skipped.
    // The Vala code assumes this skips exactly n or throws.
    @Throws(IOException::class)
    fun skip(n: Long): Long

    // Returns the current position in the stream.
    // The Vala code expects int64 (Long).
    fun tell(): Long

    // Sets the current position in the stream.
    // The Vala code expects int64 (Long) offset and SeekType.
    @Throws(IOException::class)
    fun seek(offset: Long, type: SeekType)
}

@OptIn(ExperimentalUnsignedTypes::class)
class BitInputStream(baseStream: CustomFileInputStream) {
    private val stream: CustomFileInputStream = baseStream

    // Vala uint8[] byte; initialized with byte += 0;
    // This creates a single-element array [0].
    private val byteBuffer: UByteArray = UByteArray(1)

    // Vala uint8 position; initialized with 0;
    // This tracks the bit mask (0x80, 0x40, ...) for the current byte in byteBuffer[0].
    private var position: UByte = 0u

    // Vala constructor
    init {
        // stream = base_stream; // Handled by primary constructor
        // byte += 0; // Handled by field initialization
        // position = 0; // Handled by field initialization
    }

    // Vala public uint8 read_bits_as_uint8 (uint32 size) throws IOError
    @Throws(IOException::class)
    fun read_bits_as_uint8(size: UInt): UByte {
        try {
            // Vala uint8 result = 0;
            var result: UByte = 0u
            // Vala uint32 size_var = size; // Use size directly

            // Use a mutable variable for size
            var currentSize = size

            // Vala while (position > 0x00 && size > 0)
            while (position > 0x00u.toUByte() && currentSize > 0u) {
                // Vala result *= 2;
                result = (result.toUInt() * 2u).toUByte()
                // Vala result += byte[0] / position;
                result = (result.toUInt() + (byteBuffer[0] / position).toUInt()).toUByte()
                // Vala byte[0] %= position;
                byteBuffer[0] = byteBuffer[0] % position
                // Vala position /= 2;
                position = position / 2u.toUByte()
                // Vala size -= 1;
                currentSize -= 1u
            }

            // Vala while (size >= 8)
            while (currentSize >= 8u) {
                // Vala result <<= 8;
                // Note: This operation on a UByte result will likely behave unexpectedly
                // if size > 8 was intended in the original Vala code, as shifting a UByte
                // by 8 bits results in 0. This loop is likely only reached if size > 8,
                // suggesting a potential issue in the original Vala logic if read_bits_as_uint8
                // is called with size > 8. Translating exactly as written.
                result = (result.toUInt() shl 8).toUByte()

                // Vala stream.read (byte);
                // Assuming read fills the buffer or throws
                stream.read(byteBuffer.asByteArray())
                // Vala result += byte[0];
                result = (result.toUInt() + byteBuffer[0].toUInt()).toUByte()
                // Vala size -= 8;
                currentSize -= 8u
            }

            // Vala if (size > 0)
            if (currentSize > 0u) {
                // Vala stream.read (byte);
                // Assuming read fills the buffer or throws
                stream.read(byteBuffer.asByteArray())
                // Vala position = 0x80;
                position = 0x80u.toUByte()
            }

            // Vala while (position > 0x00 && size > 0)
            while (position > 0x00u.toUByte() && currentSize > 0u) {
                // Vala result *= 2;
                result = (result.toUInt() * 2u).toUByte()
                // Vala result += byte[0] / position;
                result = (result.toUInt() + (byteBuffer[0] / position).toUInt()).toUByte()
                // Vala byte[0] %= position;
                byteBuffer[0] = byteBuffer[0] % position
                // Vala position /= 2;
                position = position / 2u.toUByte()
                // Vala size -= 1;
                currentSize -= 1u
            }

            // Vala return result;
            return result
        } catch (e: IOException) {
            // Vala throw new IOError.FAILED ("Couldn't read bits.");
            throw IOException("Couldn't read bits.")
        }
    }

    // Vala public uint16 read_bits_as_uint16 (uint32 size) throws IOError
    @Throws(IOException::class)
    fun read_bits_as_uint16(size: UInt): UShort {
        try {
            // Vala uint16 result = 0;
            var result: UShort = 0u
            // Vala uint32 size_var = size; // Use size directly

            // Use a mutable variable for size
            var currentSize = size

            // Vala while (position > 0x00 && size > 0)
            while (position > 0x00u.toUByte() && currentSize > 0u) {
                // Vala result *= 2;
                result = (result.toUInt() * 2u).toUShort()
                // Vala result += byte[0] / position;
                result = (result.toUInt() + (byteBuffer[0] / position).toUInt()).toUShort()
                // Vala byte[0] %= position;
                byteBuffer[0] = byteBuffer[0] % position
                // Vala position /= 2;
                position = position / 2u.toUByte()
                // Vala size -= 1;
                currentSize -= 1u
            }

            // Vala while (size >= 8)
            while (currentSize >= 8u) {
                // Vala result <<= 8;
                result = (result.toUInt() shl 8).toUShort()
                // Vala stream.read (byte);
                // Assuming read fills the buffer or throws
                stream.read(byteBuffer.asByteArray())
                // Vala result += byte[0];
                result = (result.toUInt() + byteBuffer[0].toUInt()).toUShort()
                // Vala size -= 8;
                currentSize -= 8u
            }

            // Vala if (size > 0)
            if (currentSize > 0u) {
                // Vala stream.read (byte);
                // Assuming read fills the buffer or throws
                stream.read(byteBuffer.asByteArray())
                // Vala position = 0x80;
                position = 0x80u.toUByte()
            }

            // Vala while (position > 0x00 && size > 0)
            while (position > 0x00u.toUByte() && currentSize > 0u) {
                // Vala result *= 2;
                result = (result.toUInt() * 2u).toUShort()
                // Vala result += byte[0] / position;
                result = (result.toUInt() + (byteBuffer[0] / position).toUInt()).toUShort()
                // Vala byte[0] %= position;
                byteBuffer[0] = byteBuffer[0] % position
                // Vala position /= 2;
                position = position / 2u.toUByte()
                // Vala size -= 1;
                currentSize -= 1u
            }

            // Vala return result;
            return result
        } catch (e: IOException) {
            // Vala throw new IOError.FAILED ("Couldn't read bits.");
            throw IOException("Couldn't read bits.")
        }
    }

    // Vala public uint32 read_bits_as_uint32 (uint32 size) throws IOError
    @Throws(IOException::class)
    fun read_bits_as_uint32(size: UInt): UInt {
        try {
            // Vala uint32 result = 0;
            var result: UInt = 0u
            // Vala uint32 size_var = size; // Use size directly

            // Use a mutable variable for size
            var currentSize = size

            // Vala while (position > 0x00 && size > 0)
            while (position > 0x00u.toUByte() && currentSize > 0u) {
                // Vala result *= 2;
                result *= 2u
                // Vala result += byte[0] / position;
                result += (byteBuffer[0] / position).toUInt()
                // Vala byte[0] %= position;
                byteBuffer[0] = byteBuffer[0] % position
                // Vala position /= 2;
                position = position / 2u.toUByte()
                // Vala size -= 1;
                currentSize -= 1u
            }

            // Vala while (size >= 8)
            while (currentSize >= 8u) {
                // Vala result <<= 8;
                result = result shl 8
                // Vala stream.read (byte);
                // Assuming read fills the buffer or throws
                stream.read(byteBuffer.asByteArray())
                // Vala result += byte[0];
                result += byteBuffer[0].toUInt()
                // Vala size -= 8;
                currentSize -= 8u
            }

            // Vala if (size > 0)
            if (currentSize > 0u) {
                // Vala stream.read (byte);
                // Assuming read fills the buffer or throws
                stream.read(byteBuffer.asByteArray())
                // Vala position = 0x80;
                position = 0x80u.toUByte()
            }

            // Vala while (position > 0x00 && size > 0)
            while (position > 0x00u.toUByte() && currentSize > 0u) {
                // Vala result *= 2;
                result *= 2u
                // Vala result += byte[0] / position;
                result += (byteBuffer[0] / position).toUInt()
                // Vala byte[0] %= position;
                byteBuffer[0] = byteBuffer[0] % position
                // Vala position /= 2;
                position = position / 2u.toUByte()
                // Vala size -= 1;
                currentSize -= 1u
            }

            // Vala return result;
            return result
        } catch (e: IOException) {
            // Vala throw new IOError.FAILED ("Couldn't read bits.");
            throw IOException("Couldn't read bits.")
        }
    }

    // Vala public uint64 read_bits_as_uint64 (uint32 size) throws IOError
    @Throws(IOException::class)
    fun read_bits_as_uint64(size: UInt): ULong {
        try {
            // Vala uint64 result = 0;
            var result: ULong = 0u
            // Vala uint32 size_var = size; // Use size directly

            // Use a mutable variable for size
            var currentSize = size

            // Vala while (position > 0x00 && size > 0)
            while (position > 0x00u.toUByte() && currentSize > 0u) {
                // Vala result *= 2;
                result *= 2u
                // Vala result += byte[0] / position;
                result += (byteBuffer[0] / position).toULong()
                // Vala byte[0] %= position;
                byteBuffer[0] = byteBuffer[0] % position
                // Vala position /= 2;
                position = position / 2u.toUByte()
                // Vala size -= 1;
                currentSize -= 1u
            }

            // Vala while (size >= 8)
            while (currentSize >= 8u) {
                // Vala result <<= 8;
                result = result shl 8
                // Vala stream.read (byte);
                // Assuming read fills the buffer or throws
                stream.read(byteBuffer.asByteArray())
                // Vala result += byte[0];
                result += byteBuffer[0].toULong()
                // Vala size -= 8;
                currentSize -= 8u
            }

            // Vala if (size > 0)
            if (currentSize > 0u) {
                // Vala stream.read (byte);
                // Assuming read fills the buffer or throws
                stream.read(byteBuffer.asByteArray())
                // Vala position = 0x80;
                position = 0x80u.toUByte()
            }

            // Vala while (position > 0x00 && size > 0)
            while (position > 0x00u.toUByte() && currentSize > 0u) {
                // Vala result *= 2;
                result *= 2u
                // Vala result += byte[0] / position;
                result += (byteBuffer[0] / position).toULong()
                // Vala byte[0] %= position;
                byteBuffer[0] = byteBuffer[0] % position
                // Vala position /= 2;
                position = position / 2u.toUByte()
                // Vala size -= 1;
                currentSize -= 1u
            }

            // Vala return result;
            return result
        } catch (e: IOException) {
            // Vala throw new IOError.FAILED ("Couldn't read bits.");
            throw IOException("Couldn't read bits.")
        }
    }

    // Vala public uint8[] read_bytes (uint32 size) throws IOError
    @Throws(IOException::class)
    fun read_bytes(size: UInt): UByteArray {
        try {
            // Vala uint8[] result = new uint8[size];
            // size is uint32, needs toInt() for array size. Assumes size fits in Int.
            val result = UByteArray(size.toInt())

            // Vala if (position == 0x00)
            if (position == 0x00u.toUByte()) {
                // Vala stream.read (result);
                // Assuming read fills the buffer or throws
                stream.read(result.asByteArray())
            } else {
                // Vala for (int i = 0; i < size; i += 1)
                for (i in 0 until size.toInt()) {
                    // Vala result[i] = read_bits_as_uint8 (8);
                    result[i] = read_bits_as_uint8(8u)
                }
            }

            // Vala return result;
            return result
        } catch (e: IOException) {
            // Vala throw new IOError.FAILED ("Couldn't read bytes.");
            throw IOException("Couldn't read bytes.")
        }
    }

    // Vala public string read_string (uint32 size) throws IOError
    @Throws(IOException::class)
    fun read_string(size: UInt): String {
        try {
            // Vala return (string) read_bytes (size);
            // Assuming UTF-8 encoding for string conversion from byte array
            return String(read_bytes(size).asByteArray(), Charsets.UTF_8)
        } catch (e: IOException) {
            // Vala throw new IOError.FAILED ("Couldn't read string.");
            throw IOException("Couldn't read string.")
        }
    }

    // Vala public void skip_bits (uint32 size) throws IOError
    @Throws(IOException::class)
    fun skip_bits(size: UInt) {
        try {
            // Use a mutable variable for size
            var currentSize = size

            // Vala while (position > 0x00 && size > 0)
            while (position > 0x00u.toUByte() && currentSize > 0u) {
                // Vala byte[0] %= position;
                byteBuffer[0] = byteBuffer[0] % position
                // Vala position /= 2;
                position = position / 2u.toUByte()
                // Vala size -= 1;
                currentSize -= 1u
            }

            // Vala stream.skip (size / 8);
            // Assuming skip skips the requested amount or throws
            stream.skip((currentSize / 8u).toLong())
            // Vala size %= 8;
            currentSize %= 8u

            // Vala if (size > 0)
            if (currentSize > 0u) {
                // Vala stream.read (byte);
                // Assuming read fills the buffer or throws
                stream.read(byteBuffer.asByteArray())
                // Vala position = 0x80;
                position = 0x80u.toUByte()
            }

            // Vala while (position > 0x00 && size > 0)
            while (position > 0x00u.toUByte() && currentSize > 0u) {
                // Vala byte[0] %= position;
                byteBuffer[0] = byteBuffer[0] % position
                // Vala position /= 2;
                position = position / 2u.toUByte()
                // Vala size -= 1;
                currentSize -= 1u
            }
        } catch (e: IOException) {
            // Vala throw new IOError.FAILED ("Couldn't skip.");
            throw IOException("Couldn't skip.")
        }
    }

    // Vala public void skip_bytes (uint32 size) throws IOError
    @Throws(IOException::class)
    fun skip_bytes(size: UInt) {
        try {
            // Vala if (position == 0x00)
            if (position == 0x00u.toUByte()) {
                // Vala stream.skip (size);
                // Assuming skip skips the requested amount or throws
                stream.skip(size.toLong())
            } else {
                // Vala skip_bits (size * 8);
                skip_bits(size * 8u)
            }
        } catch (e: IOException) {
            // Vala throw new IOError.FAILED ("Couldn't skip.");
            throw IOException("Couldn't skip.")
        }
    }

    // Vala public int64 tell ()
    fun tell(): Long {
        // Vala return stream.tell ();
        return stream.tell()
    }

    // Vala public void seek (int64 size) throws IOError
    @Throws(IOException::class)
    fun seek(size: Long) {
        try {
            // Vala stream.seek (size, SeekType.SET);
            stream.seek(size, SeekType.SET)
        } catch (e: IOException) { // Vala catch (Error e) - using IOException as stream methods throw IOError
            // Vala throw new IOError.FAILED ("Couldn't seek.");
            throw IOException("Couldn't seek.")
        }
    }
}
