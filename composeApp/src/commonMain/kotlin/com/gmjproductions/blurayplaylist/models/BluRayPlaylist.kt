import com.gmjproductions.blurayplaylist.models.Playlist
import java.io.ByteArrayOutputStream
// https://github.com/lw/BluRay/wiki/PlayList
class BluRayPlaylist {
    fun fromVLCPlaylist(playlist: Playlist) : String {
        val byteOS = ByteArrayOutputStream()
        byteOS.writeBytes(getHeader().toByteArray())
        // $00-$03 (Header) 4 bytes = "MPLS"
        // $04-$07 (Version Number) 4 bytes = "0200"
        // $08-$0B (Playlist Start Address)      4 Bytes/Word boundary - offset to Playlist section (from beginning of file)
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

    private fun ApplicationPlayList() {
        // Length              4 Bytes
        // RESERVED            1 Byte
        // PLAYBACK TYPE       1 Byte =1 or 2 or 3
        // RESERVED OR PB CNT  2 Bytes
        // UOMaskTable         8 Bytes (see below)
        // Other BITS          2 Bytes = 04 00 ?
        // PLAYLIST (see below)

    }

    private fun UOMaskTable()  {
        // 8 Bytes - default? = 00 04 01 0f 40 00 00 00 00
        // every bit set, prohibits user
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
        // ClipInformationFileName    5 bytes = '0'..'9' only ? ascii
        // ClipCodecIdentifier        4 Bytes = "M2TS" or "MP4" ? ascii
        // RESERVED                   11 Bits - 1.5 Bytes
        // is multi angle             1 bit
        // connection condition       4 Bits
        // Ref To STCID               1 byte
        // In time                    4 bytes
        // Out time                   4 bytes
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

}