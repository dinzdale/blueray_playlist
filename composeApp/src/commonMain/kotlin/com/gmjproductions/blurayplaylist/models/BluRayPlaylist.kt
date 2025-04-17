import com.gmjproductions.blurayplaylist.models.Playlist
import java.io.ByteArrayOutputStream

class BluRayPlaylist {
    fun fromVLCPlaylist(playlist: Playlist) : String {
        val byteOS = ByteArrayOutputStream()
        byteOS.writeBytes(getHeader().toByteArray())
        // (Playlist start) 4 bytes/Wprd boundary - offset from filie beginning
        // (Playlist Mark start) 4 Bytes/Word boundary - offset from filie beginning
        // (Extension Data start) 4 Bytes/Word boundary $00000000 - no extension
        // Reserved 20 Bytes == 0
        // (Playback type) 1 Byte = value 01 - standard, 02 - Random - 03 - shuffle
    }

    private fun getHeader() = "MPLS"
    private fun getVersionNumber() = "0200"
}