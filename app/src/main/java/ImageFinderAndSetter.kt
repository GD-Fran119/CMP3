import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Do not use for a long list of images
//Performance issues due to call stack
class ImageFinderAndSetter(){

    companion object{
        fun setImage(imageView: ImageView, path: String): Job {
            val job = CoroutineScope(Dispatchers.Main).launch{
                val mediaRetriever = MediaMetadataRetriever()
                mediaRetriever.setDataSource(path)

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
            return job
        }
    }

}