package com.lilac.priyacoder.materialdesigninkotlin.ui.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.lilac.priyacoder.materialdesigninkotlin.singleton.SingletonHolder

class Prefs private constructor(context: Context) {
    val PREFS_FILENAME = "MY_APP_PREFS"
    val PREFERRED_FOLDER_KEY = "user_preferred_folder"
    val settings: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME,MODE_PRIVATE)

    fun getValues(): SharedPreferences{
        return settings
    }

    fun setValue(key: String,value: String?){
        settings.edit().putString(key,value).apply()
    }
    companion object : SingletonHolder<Prefs, Context>(::Prefs)
}