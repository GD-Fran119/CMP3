import android.view.animation.AlphaAnimation

class ImageFadeInAnimation(private val start: Float, private val end: Float): AlphaAnimation(start, end) {
    init{
        duration = 300
    }
}