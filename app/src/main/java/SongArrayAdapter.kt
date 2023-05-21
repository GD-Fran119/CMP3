import android.app.Activity
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cmp3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    class SongListViewHolder(view: View, activity: Activity) : RecyclerView.ViewHolder(view) {

        private val activity = activity
        private val titleText: TextView = view.findViewById(R.id.title)
        private val subtitleText: TextView = view.findViewById(R.id.albumNArtist)
        private val imageView: ImageView = view.findViewById(R.id.icon)
        private val button : Button = view.findViewById(R.id.mainViewSongButton)
        private var job : Job? = null
        private lateinit var song: Song

        fun bind(song: Song) {

            this.song = song
            titleText.text = song.nombre
            subtitleText.text = "${song.artista} - ${song.album}"
            button.setOnClickListener(View.OnClickListener {
                Toast.makeText(activity, song.getSizeMB().toString(), Toast.LENGTH_SHORT).show()
            })

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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_song_list_view, parent, false)
        return SongListViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val song = songs.getCancion(position)
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return songs.getListSize()
    }
}