package com.ketee_jishs.nasa_app.ui.mars

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ketee_jishs.nasa_app.interactors.strings_interactor.StringsInteractor
import com.ketee_jishs.nasa_app.util.*

@Suppress("DEPRECATION")
class MainMarsViewPagerAdapter(
    fragmentManager: FragmentManager,
    private val stringsInteractor: StringsInteractor
) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(
        MarsFragment(getDayBeforeYesterdayDate()),
        MarsFragment(getThirdDate()),
        MarsFragment(getForthDate())
    )

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> fragments[TODAY_MARS_FRAGMENT]
            1 -> fragments[YESTERDAY_MARS_FRAGMENT]
            2 -> fragments[DAY_BEFORE_YESTERDAY_MARS_FRAGMENT]
            else -> fragments[TODAY_MARS_FRAGMENT]
        }
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> stringsInteractor.viewPagerTodayText
            1 -> stringsInteractor.viewPagerYesterdayText
            2 -> stringsInteractor.viewPagerDayBeforeYDText
            else -> stringsInteractor.viewPagerTodayText
        }
    }
}