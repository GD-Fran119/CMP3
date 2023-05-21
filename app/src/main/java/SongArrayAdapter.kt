import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmp3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class SongArrayAdapter: RecyclerView.Adapter<SongArrayAdapter.SongListViewHolder> {

    private lateinit var context: Activity
    private lateinit var songs: SongList

    private constructor(c : Activity, s: SongList){
        this.songs = s
        this.context = c
    }

    companion object{
        fun create(c : Activity, s: SongList): SongArrayAdapter{
            return SongArrayAdapter(c, s)
        }
    }

    class SongListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val titleText: TextView = view.findViewById(R.id.title)
        private val subtitleText: TextView = view.findViewById(R.id.albumNArtist)
        private val imageView: ImageView = view.findViewById(R.id.icon)
        private var job : Job? = null

        fun bind(song: Song) {
            if(song.nombre.length > 27)
                titleText.text = "${song.nombre.substring(0, 24)}..."
            else
                titleText.text = song.nombre

            imageView.setImageResource(R.color.light_blue_400)

            if(job != null)
                job?.cancel()

            job = CoroutineScope(Dispatchers.Main).launch(Dispatchers.Default) {
                val mediaRetriever = MediaMetadataRetriever()
                mediaRetriever.setDataSource(song.rutaArchivo)

                val data = mediaRetriever.embeddedPicture
                mediaRetriever.release()

                if(data != null) {
                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    withContext(Dispatchers.Main) {
                        imageView.setImageBitmap(bitmap)
                    }
                }

            }

            subtitleText.text = "${song.artista} - ${song.album}"
        }

        private fun compressBitmap(bitmap: Bitmap, ratio: Int): Bitmap{

            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, ratio, outputStream)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                bitmap.compress(
                    Bitmap.CompressFormat.WEBP_LOSSY, // or WEBP_LOSSY
                    ratio,outputStream)
            } else {
                bitmap.compress(Bitmap.CompressFormat.WEBP, ratio, outputStream)
            }
            val inputStream = ByteArrayInputStream(outputStream.toByteArray())

            return BitmapFactory.decodeStream(inputStream)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_song_list_view, parent, false)
        return SongListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val song = songs.getCancion(position)
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return songs.getListSize()
    }

}