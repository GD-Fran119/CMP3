import java.math.RoundingMode
import java.text.DecimalFormat

class Song(
    val title: String, val artist: String,
    val album: String, val duration: UInt,
    val path: String, val fileSize: Int,
    val lyricsPath: String?) {

    fun getSizeMB(): Float{
        val TAMANO_MB = 1024.0f * 1024f

        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.HALF_EVEN

        return df.format(fileSize.toFloat() / TAMANO_MB).toFloat()
    }
}