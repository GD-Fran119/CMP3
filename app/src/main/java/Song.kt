import java.math.RoundingMode
import java.text.DecimalFormat

class Song(
    val nombre: String, val artista: String,
    val album: String, val duracion: UInt,
    val rutaArchivo: String, val tamanoArchivo: Int,
    val letraCancion: String?) {

    fun getSizeMB(): Float{
        val TAMANO_MB = 1024.0f

        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.HALF_EVEN

        return df.format(tamanoArchivo.toFloat() / TAMANO_MB).toFloat()
    }
}