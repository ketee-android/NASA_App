package com.ketee_jishs.nasa_app.ui


import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.ketee_jishs.nasa_app.R
import com.ketee_jishs.nasa_app.util.KEY_THEME
import com.ketee_jishs.nasa_app.util.THEME_DAY
import com.ketee_jishs.nasa_app.util.THEME_NIGHT
import com.ketee_jishs.nasa_app.util.THEME_PREFS_NAME
import kotlinx.android.synthetic.main.activity_splash.*

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity(R.layout.activity_splash) {
    private val themePrefs by lazy {
        getSharedPreferences(THEME_PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        initTheme()

        splashImageView.alpha = 0f
        starOneImage.alpha = 0f
        starTwoImage.alpha = 0f
        starThreeImage.alpha = 0f
        starFourImage.alpha = 0f
        starFiveImage.alpha = 0f
        starSixImage.alpha = 0f
        starSevenImage.alpha = 0f

        splashImageView.animate().alpha(1f).duration = 1200
        starOneImage.animate().alpha(0.8f).duration = 800
        starThreeImage.animate().alpha(0.9f).duration = 900

        starFourImage.animate().alpha(0.6f).duration = 600
        starFiveImage.animate().alpha(0.5f).duration = 700
        starSixImage.animate().alpha(0.4f).duration = 1000
        starSevenImage.animate().alpha(0.7f).duration = 1200

        starTwoImage.animate().alpha(0.9f)
            .setDuration(1200)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            })
    }

    private fun initTheme() {
        when (getThemePrefs()) {
            THEME_DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun getThemePrefs() = themePrefs?.getInt(KEY_THEME, THEME_DAY)
}