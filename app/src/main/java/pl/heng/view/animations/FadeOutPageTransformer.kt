package pl.heng.view.animations

import android.view.View
import androidx.viewpager.widget.ViewPager

class FadeOutPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {

        view.apply {
            translationX = (-position * view.width) //Half the normal speed
            alpha = 1-Math.abs(position)
        }
    }
}