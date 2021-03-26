package com.ketee_jishs.nasa_app.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.ketee_jishs.nasa_app.R
import com.ketee_jishs.nasa_app.util.KEY_THEME
import com.ketee_jishs.nasa_app.util.THEME_DAY
import com.ketee_jishs.nasa_app.util.THEME_NIGHT
import com.ketee_jishs.nasa_app.util.THEME_PREFS_NAME
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    private val sharedPrefs by lazy {activity?.getSharedPreferences(THEME_PREFS_NAME, Context.MODE_PRIVATE)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onStart() {
        super.onStart()
        changeTheme()
        initTheme()
    }

    private fun changeTheme() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonLightTheme -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_DAY)
                R.id.radioButtonDarkTheme -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_NIGHT)
            }
        }
    }

    private fun setTheme (themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }

    private fun saveTheme(theme: Int) = sharedPrefs?.edit()?.putInt(KEY_THEME, theme)?.apply()

    private fun initTheme() {
        when (getSavedTheme()) {
            THEME_DAY -> {
                radioButtonLightTheme.isChecked = true
            }
            THEME_NIGHT -> {
                radioButtonDarkTheme.isChecked = true
            }
        }
    }

    private fun getSavedTheme() = sharedPrefs?.getInt(KEY_THEME, THEME_DAY)

    companion object {
        fun newInstance() = SettingsFragment()
    }
}