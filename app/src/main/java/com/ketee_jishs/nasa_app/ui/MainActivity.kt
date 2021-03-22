package com.ketee_jishs.nasa_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ketee_jishs.nasa_app.R
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }
}