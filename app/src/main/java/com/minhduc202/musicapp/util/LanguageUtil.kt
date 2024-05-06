package com.minhduc202.musicapp.util

import android.content.Context
import com.minhduc202.musicapp.preference.MyPreferences
import java.util.Locale

object LanguageUtil {
    fun setLanguage(context: Context) {
        var language: String? = MyPreferences.getInstance(context).getPrefLanguage()
        if (language == null) {
            language = Locale.getDefault().language
        }
        val newLocal: Locale = Locale(language!!.lowercase(Locale.getDefault()))
        val res = context.resources
        val config = res.configuration
        config.setLocale(newLocal)
        res.updateConfiguration(config, res.displayMetrics)
    }

    fun getLanguageName(context: Context) : String {
        return try {
            var language = MyPreferences.getInstance(context).getPrefLanguage()
            if (language == null) {
                language = Locale.getDefault().language
            }
            val newLocal = Locale(language.toString().lowercase())
            Locale.setDefault(newLocal)
            newLocal.displayName
        } catch (e: Exception) {
            ""
        }
    }
}