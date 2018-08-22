package com.example.lee.dcnyc18.prefs

import android.app.Application
import android.content.Context
import javax.inject.Inject

interface PrefsManager {
    fun persistNextApiPage(page: Int)
    fun retrieveNextApiPage(): Int
}

class ApplicationPreferenceManager @Inject constructor(
        application: Application
): PrefsManager {
    private val prefsManager = application.getSharedPreferences(APP_PREF_FILE_NAME, Context.MODE_PRIVATE)

    override fun persistNextApiPage(page: Int) {
        prefsManager.edit().putInt(KEY_API_PAGE, page).apply()
    }

    override fun retrieveNextApiPage(): Int {
        return prefsManager.getInt(KEY_API_PAGE, 1)
    }

    companion object {
        private const val APP_PREF_FILE_NAME = "AppPrefFile"

        private const val KEY_API_PAGE = "ApiPageKey"
    }
}

