package com.ketee_jishs.nasa_app.ui.mars

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ketee_jishs.nasa_app.util.DAY_BEFORE_YESTERDAY_MARS_FRAGMENT
import com.ketee_jishs.nasa_app.util.TODAY_MARS_FRAGMENT
import com.ketee_jishs.nasa_app.util.YESTERDAY_MARS_FRAGMENT

@Suppress("DEPRECATION")
class MainMarsViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = arrayOf(
        MarsTodayFragment(),
        MarsYesterdayFragment(),
        MarsDayBeforeYesterdayFragment()
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
            0 -> "            Today            "
            1 -> "    Yesterday    "
            2 -> "Day before YD"
            else -> "      Today      "
        }
    }
}