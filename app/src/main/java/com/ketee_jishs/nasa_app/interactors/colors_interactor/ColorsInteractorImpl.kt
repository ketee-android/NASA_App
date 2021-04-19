package com.ketee_jishs.nasa_app.interactors.colors_interactor

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.ketee_jishs.nasa_app.R

class ColorsInteractorImpl (context: Context) : ColorsInteractor {
    @RequiresApi(Build.VERSION_CODES.M)
    override val earthDateTextColor = context.getColor(R.color.second_text_color)
}