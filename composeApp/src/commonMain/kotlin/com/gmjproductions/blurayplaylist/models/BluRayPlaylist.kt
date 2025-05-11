import com.gmjproductions.blurayplaylist.models.Playlist
import java.io.ByteArrayOutputStream
// https://github.com/lw/BluRay/wiki/PlayList
class BluRayPlaylist {
    fun fromVLCPlaylist(playlist: Playlist) : String {
        val byteOS = ByteArrayOutputStream()
        byteOS.writeBytes(getHeader().toByteArray())
        // $00-$03 (Header) 4 bytes = "MPLS"
        // $04-$07 (Version Number) 4 bytes = "0200"
        // $08-$0B AppInfoPlayList (Playlist Start Address)      4 Bytes/Word boundary - offset to Playlist section (from beginning of file)
        // $0C-$0F (Playlist Mark Start Address) 4 Bytes - offset to PMSA from beginning of file
        // $10-$13 (Extension Data Section)      4 Bytes - not included: $00000000
        // $14-$27 (Unused)                     20 Bytes = 00 00 00 00 00 00 00

        // $28-$2B (Application Info Playlist length) 4 bytes - always: 0000000E (14 bytes???)
        //    $2C (unused byte) - 1 Byte = 0
        //    $2D (Playback type) - 1 Byte - $01 = Standard, $02 = Random, $03 = Shuffle Play
        // $2E-$2F (Playback count) - $0000 if standard above
        // $30-$37 (User operation mask table) 8 bytes - Description pending
        //                                 BTIS SET: 7             6       5         4-0
        //                                       playlist      sound   lossless   reserved
        //                                       random access effects sound
        // ????vvvvv
        // $38 - (Miscillaneous flags)
        // $39 - Reserved 1 byte = 0
        // ????^^^^^

        // >>>>PLAYLIST SECTION<<<
        // >>>>PLAYLIST MARK SECTION<<<
        // >>>>EXTENSION DATA SECTION<<

    }

    private fun getHeader() = "MPLS"
    private fun getVersionNumber() = "0200"

    private fun AppInfoPlayList() {
        // PlaybackType     1 byte
        // PlaybackCount
    }
    private fun ApplicationPlayList() {
        // Length              U 4 Bytes
        // RESERVED            1 Byte
        // PLAYBACK TYPE       1 Byte =1 or 2 or 3
        // RESERVED OR PB CNT  2 Bytes
        // UOMaskTable         8 Bytes (see below)
        // Other BITS          2 Bytes = 04 00 ?
        // PLAYLIST (see below)

    }

    private fun UOMaskTable()  {
        // 8 Bytes - default? = 00 04 01 0f 40 00 00 00 00
        // every bit set, prohibits userAppInfoBDMV
    }

    private fun PlayList() {
        // $00-$03 - 4 bytes Length of play list
        // $04-$05 - 2 bytes - Reserved = 0
        // $06-$07 - 2 bytes - Number of play items
        // $08-$09 - 2 bytes - Number of sub paths
        //  n PlayItems (see below)
    }
    private fun PlayItem() {
        // length                     2 bytes
        // ClipInformationFileName    5 bytes = '0'..'9' ie: 00000 or 00009
        // ClipCodecIdentifier        4 Bytes = "M2TS" or "MP4" ? ascii
        // RESERVED                   11 Bits - 1.5 Bytes
        // is multi angle             1 bit
        // connection condition       4 Bits
        // Ref To STCID               1 byte
        // In time                    4 bytes Clip Start Time/relative to prev clip
        // Out time                   4 bytes Clip End Time/relative to prev clip
       // ---- Times are expressed in 45 KHz, so you'll need to divide by 45000 to get them in seconds.
        // UOMaskTable
        // Play item random access flag    1 bit
        // RESERVED                        7 bits
        // Still Mode                      1 byte
        // Still Time if Still Mode = 0x01 4 bytes
        // else RESERVED                   4 bytes
        // if is multi angle:
        //   number of angles              1 byte
        //   RESERVED                      6 bits
        //   is different audios           1 bit
        //   is seemless angle change      1 bit
        //   for number of angles:
        //       ClipInformationFileName   5 bytes
        //       ClipCodecIdentifier       4 bytes
        //       RegToSTCID                1 byte
        //   endif
        //   STNTTABLE
    }

    private fun ClipInformationFile() {
        // Type indicator                   4 bytes
        // Version Number                   4 bytes
        // sequence info start              8 bytes
        // program info start address       8 bytes
        // cpi start address                8 bytes
        // clipmark start address           8 bytes
        // extension start address == 0     8
        // RESERVED                         96 bits - 12 bytes
        // CLIPINFO
        // starting byte SequenceInfo
        // starting byte ProgramInfo
        // starting byte CPI
        // starting byte ClipMarkStart address
        // CLIPMARK
        // starting byte Extension Data address
        // extension start address
        // if (extension start address != 0):
        //    ExtensionData
    }
    private fun ClipInfo() {
        // length                            4 bytes
        // RESERVED                          2 bytes
        // ClipStream type                   1 byte
        // Application type                  1 byte
        // RESERVED                          31 bits = 3 bytes, 7 bits
        // isCC5 or isAtCDelta               1 bit
        // TSTypeInfoBlock                   256 bits = 32 bytes
        // if (isCC5 == 1) :
        //   RESERVED                        1 byte
        //   FollowingClipStreamType         1 byte
        //   RESERVED                        4 bytes
        //   FollowingClipInformationName    5 bytes
        //   FollowingClipCodeIdentifier     4 bytes
        //   RESERVED                        1 byte
        // endif
    }
    private fun SequenceInfo() {
        // length                             4 bytes
        // RESERVED                           1 byte
        // number of atc sequences            1 byte
        // for each atc sequences:
        //    SPNATCStart                     4 bytes
        //    number of stc sequences         1 byte
        //    offset stc id                   1 byte
        //    for each stc sequences:
        //
        //    endif
        // end
    }

    private fun ProgramInfo() {
        // length                              4 bytes
        // RESERVED                            1 byte
        // number of programs                  1 byte
        // for 1...number of programs
        //     SPNProgramSequenceStart          4 bytes
        //     ProgramMapPID                    2 bytes
        //     NumberOfStreamsInPS              1 byte
        //     reserved (may be NumberOfGroups) 1 byte
        //     for NumberOfStreamsInPS
        //        StreamPID                      2 bytes
        //     end
        //     StreamConfigInfo
        // end
    }
    private fun StreamConfigInfo() {
        // Length                                8 bytes
        // StreamCodingType  01, 02, 1B, EA
        // if StreamCodingType =  0x01 | 0x02 | 0x1B | 0xEA
        //    VideoFormat                           4 bits
        //    FrameRate                             4 bits
        //    VideoAspect                           4 bits
        //    RESERVED                              2 bits
        //    OCFlag                                1 bit
        //    RESERVED                              17 bits
        //  else if StreamCodingType =  0x024
        //    VideoFormat                           4 bits
        //    FrameRate                             4 bits
        //    VideoAspect                           4 bits
        //    RESERVED                              2 bits
        //    OCFlag                                1 bit
        //    CRFlag                                1 bit
        //    DynamicRange                          4 bits
        //    ColorSpace                            4 bits
        //    HDRPlusFlag                           1 bit
        //    RESERVED                              7 bits
        //  StreamCodingType in {0x03, 0x04, 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0xA1, 0xA2}
        //      AudioFormat                          4 bits
        //      SampleRate                           4 bits
        //      Language                             3 bytes
        //      if StreamCodingType = {0x90, 0x91}:
        //         Language                          3 bytes
        //      else if  StreamCodingType =  {0x92}:
        //         CharCode                          1 bytes
        //         Language                          3 bytes
        //      end if
        // end
    }

}