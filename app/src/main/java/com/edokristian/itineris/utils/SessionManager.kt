package com.edokristian.itineris.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val PREFS_NAME = "itineris"
    private val prefs : SharedPreferences
    val editor : SharedPreferences.Editor

    init {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    fun put(key: String, value: String){
        editor.putString(key, value)
            .apply()
    }

    fun getString(key: String) : String? {
        return prefs.getString(key, null)
    }

    fun put(key: String, value: Boolean){
        editor.putBoolean(key, value)
            .apply()
    }

    fun getBoolean(key: String) : Boolean {
        return prefs.getBoolean(key, false)
    }

    fun clear(){
        editor.clear()
            .apply()
    }

}