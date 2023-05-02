import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.cmp3.R

class SongArrayAdapter{}

/*

class SongArrayAdapter: ArrayAdapter<String> {

    private var c: Activity
    companion object{
        fun create(context: Activity, songs: SongList): SongArrayAdapter{
            val canciones = songs.getCanciones()
            val names = mutableListOf<String>()
            canciones.forEach { e -> names += e.nombre }

            return SongArrayAdapter()
        }
    }


    private constructor(context: Activity, songs: SongList){
        this.c = context
    }
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = c.layoutInflater
        val rowView = inflater.inflate(R.layout.item_song_list_view, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val imageView = rowView.findViewById(R.id.icon) as ImageView
        val subtitleText = rowView.findViewById(R.id.description) as TextView

        titleText.text = title[position]
        imageView.setImageResource(R.mipmap.ic_launcher)
        subtitleText.text = description[position]

        return rowView
    }
}*/