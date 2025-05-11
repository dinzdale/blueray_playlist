package BluRay

import java.io.IOException
https://app.codeconvert.ai/code-converter?inputLang=Vala&outputLang=Kotlin
class PlayItem {
    var clipInformationFileName: String? = null
    var clipCodecIdentifier: String? = null
    var isMultiAngle: UByte = 0u
    var connectionCondition: UByte = 0u
    var refToSTCID: UByte = 0u
    var inTime: UInt = 0u
    var outTime: UInt = 0u
    var uOMaskTable: UOMaskTable? = null
    var playItemRandomAccessFlag: UByte = 0u
    var stillMode: UByte = 0u
    var stillTime: UShort = 0u
    var stnTable: STNTable? = null

    @Throws(ParseError::class)
    constructor(inputStream: BitInputStream) {
        try {
            val length: UShort = inputStream.readBitsAsUInt16(16)
            val position: Long = inputStream.tell() // Needed to seek

            clipInformationFileName = inputStream.readString(5)
            clipCodecIdentifier = inputStream.readString(4)

            inputStream.skipBits(11)

            isMultiAngle = inputStream.readBitsAsUInt8(1)
            connectionCondition = inputStream.readBitsAsUInt8(4)
            refToSTCID = inputStream.readBitsAsUInt8(8)
            inTime = inputStream.readBitsAsUInt32(32)
            outTime = inputStream.readBitsAsUInt32(32)

            // UOMaskTable
            uOMaskTable = UOMaskTable.fromBitInputStream(inputStream)

            playItemRandomAccessFlag = inputStream.readBitsAsUInt8(1)

            inputStream.skipBits(7)

            stillMode = inputStream.readBitsAsUInt8(8)

            if (stillMode.toInt() == 0x01) {
                stillTime = inputStream.readBitsAsUInt16(16)
            } else {
                inputStream.skipBits(16)
            }

            // TODO: MultiAngle

            // STNTable
            stnTable = STNTable.fromBitInputStream(inputStream)

            inputStream.seek(position + length.toLong())
        } catch (e: ParseError) {
            throw e
        } catch (e: IOException) {
            throw ParseError.InputError("Couldn't parse PlayItem.")
        }
    }

//    fun write(outputStream: BitOutputStream) {
//    }
}

