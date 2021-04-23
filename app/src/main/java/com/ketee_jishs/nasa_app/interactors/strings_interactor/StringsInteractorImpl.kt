package com.ketee_jishs.nasa_app.interactors.strings_interactor

import android.content.Context
import com.ketee_jishs.nasa_app.R

class StringsInteractorImpl (context: Context) : StringsInteractor {
    override val viewPagerTodayText = context.getString(R.string.view_pager_today_text)
    override val viewPagerYesterdayText = context.getString(R.string.view_pager_yesterday_text)
    override val viewPagerDayBeforeYDText = context.getString(R.string.view_pager_day_before_yd_text)
}