package pl.heng.fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_intro_end.*
import pl.heng.R
import pl.heng.view.MainMenu

class IntroEnd : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intro_end, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        if (!userVisibleHint)
            return

        next.setOnClickListener {
            var intent = Intent(context,MainMenu::class.java)
            activity?.startActivity(intent)
        }

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000
        logo.animation = fadeIn
        startText.animation = fadeIn
        next.animation = fadeIn
        logo.visibility = View.VISIBLE
        startText.visibility = View.VISIBLE
        next.visibility = View.VISIBLE
        fadeIn.start()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            onResume()
        }
    }
}
