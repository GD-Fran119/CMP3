class SongList{

    val nombre: String
    val fechaCreacion: String
    val DEFAULT_DATE = "1970-01-01"
    private var canciones: MutableList<Song>
    var duracion: UInt = 0u
        private set

    constructor(nombre: String, canciones: MutableList<Song>, fechaCreacion: String) {
        this.nombre = nombre
        this.canciones = canciones
        this.fechaCreacion = fechaCreacion
        duracion = 0.toUInt()
        this.computeDuracion()
    }

    private fun computeDuracion(){
        duracion = 0.toUInt()
        canciones.forEach { s -> duracion += s.duracion }
    }

    fun addCancion(cancion: Song){
        canciones += cancion
    }

    fun getCancion(pos: Int): Song{
        return canciones[pos]
    }

    fun getListSize(): Int{
        return canciones.size
    }
    fun setCanciones(newCanciones: Array<Song>){
        this.canciones = newCanciones.toMutableList()
    }
}