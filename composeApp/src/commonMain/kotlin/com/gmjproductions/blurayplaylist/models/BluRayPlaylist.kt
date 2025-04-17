import com.gmjproductions.blurayplaylist.models.Playlist
import java.io.ByteArrayOutputStream

class BluRayPlaylist {
    fun fromVLCPlaylist(playlist: Playlist) : String {
        val byteOS = ByteArrayOutputStream()
        byteOS.writeBytes(getHeader().toByteArray())
        // $00-$03 (Header) 4 bytes = "MPLS"
        // $04-$07 (Version Number) 4 bytes = "0200"
        // $08-$0B (Playlist Start Address) 4 Bytes/Word boundary - offset to Playlist section (from beginning of file)
        // $0C-$0F (Playlist Mark Start Address) 4 Bytes - offset to PMSA from beginning of file
        // $10-$13 (Extension Data Section)  4 Bytes - not included: $00000000
        // $14-$27 (Unused) 14 Bytes = 00 00 00 00 00 00 00
        // $28-$2B (Application Info Playlist length) 4 bytes - always: 0000000E (14 bytes???)
        // $2C (unused byte) - 1 Byte = 0
        // $2D (Playback type) - 1 Byte - $01 = Standard, $02 = Random, $03 = Shuffle Play
        // $2E-$2F
    }

    private fun getHeader() = "MPLS"
    private fun getVersionNumber() = "0200"
}