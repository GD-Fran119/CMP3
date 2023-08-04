package com.example.animations

import android.view.animation.AlphaAnimation


/**
 * Class for generating an [AlphaAnimation] that lasts 300ms.
 * @param fromAlpha alpha value from which to start [0.0 - 1.0].
 * @param toAlpha ending alpha value [0.0 - 1.0].
 */
class ImageFadeInAnimation(fromAlpha: Float, toAlpha: Float) : AlphaAnimation(fromAlpha, toAlpha) {
    init{
        duration = 300
    }
}