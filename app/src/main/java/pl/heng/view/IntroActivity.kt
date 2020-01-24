package pl.heng.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_intro.*
import pl.heng.fragment.IntroEnd
import pl.heng.fragment.AboutAppFragment
import pl.heng.fragment.AboutNewHabitFragment
import pl.heng.view.animations.FadeOutPageTransformer


class IntroActivity : AppCompatActivity() {

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(pl.heng.R.layout.activity_intro)

        val isIntro = "intro"
        val sharedPreferences : SharedPreferences = this.getPreferences(Context.MODE_PRIVATE)
        var intro = sharedPreferences.getBoolean(isIntro,true)
        if (!intro){
            var intent = Intent(this,MainMenu::class.java)
            this.startActivity(intent)
            finish()
        }else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)

            actionBar?.hide()
            supportActionBar?.hide()
            tabLayout.setBackgroundColor(Color.TRANSPARENT)
            tabLayout.setupWithViewPager(pager)
            pager.adapter = pagerAdapter
            pager.setPageTransformer(true, FadeOutPageTransformer())
            val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putBoolean(isIntro, false)
                apply()
            }
        }
    }

    override fun onBackPressed() {
        if (pager.currentItem == 0)
            super.onBackPressed()
        else
            pager.setCurrentItem(pager.currentItem - 1, true)
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        val fragmentList = listOf(
            AboutAppFragment(),
            AboutNewHabitFragment(),
            IntroEnd()
        )

        override fun getCount(): Int = fragmentList.size

        override fun getItem(position: Int): Fragment = fragmentList[position]
    }
}
