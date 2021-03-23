package com.ketee_jishs.nasa_app.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ketee_jishs.nasa_app.R
import com.ketee_jishs.nasa_app.ui.picture.BottomNavigationDrawerFragment
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayData
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayFragment
import com.ketee_jishs.nasa_app.ui.picture.PictureOfTheDayViewModel
import com.ketee_jishs.nasa_app.ui.settings.SettingsFragment
import com.ketee_jishs.nasa_app.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import java.text.SimpleDateFormat

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val sharedPrefs by lazy {
        getSharedPreferences(CHIPS_SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        setBottomSheetBehavior(findViewById(R.id.bottomSheetContainer))
        onMenuItemClickListener()

        if (savedInstanceState == null) {
            goToMainScreen()
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(R.id.appBarFav)
        menu?.add(R.id.appBarSettings)
        return super.onCreateOptionsMenu(menu)
    }

    private fun onMenuItemClickListener() {
        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.appBarFav -> toast("Favourite")
                R.id.appBarSettings -> goToSettingsScreen()
            }
            true
        }
    }

    private fun Activity.toast(string: String?) {
        Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show()
    }

    private fun goToMainScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, PictureOfTheDayFragment.newInstance())
            .commitNow()

        bottomAppBar.navigationIcon =
            ContextCompat.getDrawable(applicationContext, R.drawable.ic_hamburger_menu_bottom_bar)
        bottomAppBar.setNavigationOnClickListener {
            let {
                BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
            }
        }
        bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)

        fab.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_plus_fab))
        fab.setOnClickListener { goToSettingsScreen() } // Потом придумаю что-нибудь другое

        initData()
        bottomSheetContainer.visibility = View.VISIBLE
    }

    private fun goToSettingsScreen() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, SettingsFragment.newInstance())
            .addToBackStack(null)
            .commit()

        bottomAppBar.navigationIcon = null
        bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)

        fab.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_back_fab))
        fab.setOnClickListener { goToMainScreen() }

        bottomSheetContainer.visibility = View.GONE
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        fab.visibility = View.VISIBLE
                        bottomAppBar.visibility = View.VISIBLE
                    }
                    else -> {
                        initData()
                        fab.visibility = View.GONE
                        bottomAppBar.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }


    @SuppressLint("SimpleDateFormat")
    private fun initData() {
        when (getChipsPrefs()) {
            CHECKED_TODAY -> getData(formatter.format(getCurrentDate()))
            CHECKED_YESTERDAY -> getData(formatter.format(getYesterdayDate()))
            CHECKED_DAY_BEFORE_YESTERDAY -> getData(formatter.format(getDayBeforeYesterdayDate()))
        }
    }

    private fun getChipsPrefs() = sharedPrefs?.getInt(CHIP_KEY, CHECKED_TODAY)

    private fun getData(date: String) {
        viewModel.getData(date)
            .observe(this@MainActivity, Observer<PictureOfTheDayData> { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    bottomSheetDescriptionHeader.text = serverResponseData.title
                    bottomSheetDescription.text = serverResponseData.explanation
                    bottomSheetProgressBar.visibility = View.GONE
                }
            }
            is PictureOfTheDayData.Loading -> {
                bottomSheetProgressBar.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                toast(data.error.message)
            }
        }
    }
}