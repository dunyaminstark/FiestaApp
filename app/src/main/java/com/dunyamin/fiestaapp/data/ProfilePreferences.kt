package com.dunyamin.fiestaapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class ProfilePreferences(context: Context) {

    companion object {
        private const val PREFS_NAME = "profile_prefs"
        private const val KEY_NAME = "profile_name"
        private const val KEY_BIO = "profile_bio"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveProfile(name: String, bio: String) {
        sharedPreferences.edit {
            putString(KEY_NAME, name)
            putString(KEY_BIO, bio)
        }
    }

    fun getName(): String? {
        return sharedPreferences.getString(KEY_NAME, null)
    }

    fun getBio(): String? {
        return sharedPreferences.getString(KEY_BIO, null)
    }
}
