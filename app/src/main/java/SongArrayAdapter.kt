import android.content.Context
import android.content.Intent
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
import com.example.cmp3.PlayControlView
import com.example.cmp3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SongArrayAdapter private constructor(private var context: Context, private var songs: SongList) :
    RecyclerView.Adapter<SongArrayAdapter.SongListViewHolder>() {

    companion object{
        fun create(c : Context, s: SongList): SongArrayAdapter{
            return SongArrayAdapter(c, s)
        }
    }

    class SongListViewHolder(private val view: View, private val activity: Context) : RecyclerView.ViewHolder(view) {

        private val titleText: TextView = view.findViewById(R.id.title)
        private val subtitleText: TextView = view.findViewById(R.id.albumNArtist)
        private val imageView: ImageView = view.findViewById(R.id.icon)
        private val button : Button = view.findViewById(R.id.mainViewSongButton)
        private var job : Job? = null
        private lateinit var song: Song

        fun bind(song: Song, pos: UInt) {

            this.song = song
            titleText.text = song.title
            val artist = if (song.artist == "<unknown>") "Unknown" else song.artist
            subtitleText.text = "${artist} - ${song.album}"

            imageView.setImageResource(R.drawable.ic_music_note)

            view.setOnClickListener {
                //New intent to play control view with song playing
                try {
                    Player.instance.setList(MainListHolder.getMainList()!!)
                    Player.instance.setCurrentSongAndPLay(pos)
                }catch (e: Exception){
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(activity, PlayControlView::class.java)
                activity.startActivity(intent)

            }

            button.setOnClickListener {
                Toast.makeText(activity, song.getSizeMB().toString(), Toast.LENGTH_SHORT).show()
            }

            if(job != null)
                job?.cancel()

            job = CoroutineScope(Dispatchers.Main).launch(Dispatchers.Default) {

                val mediaRetriever = MediaMetadataRetriever()
                mediaRetriever.setDataSource(song.path)

                val data = mediaRetriever.embeddedPicture
                mediaRetriever.release()

                if(data != null) {
                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)

                    withContext(Dispatchers.Main) {
                        imageView.setImageBitmap(bitmap)
                        imageView.startAnimation(ImageFadeInAnimation(0f, 1f))
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
        val song = songs.getSong(position.toUInt())
        holder.bind(song, position.toUInt())
    }

    override fun getItemCount(): Int {
        return songs.getListSize().toInt()
    }
}