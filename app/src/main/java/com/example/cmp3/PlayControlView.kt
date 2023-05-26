package com.example.cmp3

import ImageFadeInAnimation
import ImageFinderAndSetter
import Player
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.palette.graphics.Palette
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayControlView : AppCompatActivity() {
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_control_view)
        val currentSong = Player.instance.getCurrentSong()

        title = findViewById(R.id.play_control_title)
        desc = findViewById(R.id.play_control_desc)
        img = findViewById(R.id.play_control_img)

        title.text = currentSong?.title
        desc.text = if (currentSong?.artist == "<unknown>") "Unknown" else currentSong?.artist
        img.setImageResource(R.mipmap.ic_launcher)
        if(currentSong != null)
            ImageFinderAndSetter.setImage(img, currentSong.path)

        CoroutineScope(Dispatchers.Default).launch {
            if(currentSong != null){
                val mediaRetriever = MediaMetadataRetriever()
                mediaRetriever.setDataSource(currentSong.path)

                val data = mediaRetriever.embeddedPicture
                mediaRetriever.release()

                if(data != null) {
                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    val playButton = findViewById<MaterialButton>(R.id.play_control_play_button)
                    Palette.from(bitmap).generate{palette ->
                        if(palette != null)
                            playButton.setBackgroundColor(palette.getDarkVibrantColor(getColor(androidx.appcompat.R.color.button_material_dark)))
                    }
                    withContext(Dispatchers.Main) {
                        img.setImageBitmap(bitmap)
                        img.startAnimation(ImageFadeInAnimation(0f, 1f))
                    }
                }
            }
        }
    }
}