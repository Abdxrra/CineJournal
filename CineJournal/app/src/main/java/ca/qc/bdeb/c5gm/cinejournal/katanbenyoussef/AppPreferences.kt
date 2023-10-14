package ca.qc.bdeb.c5gm.cinejournal.katanbenyoussef

import android.content.Context

class AppPreferences(context: Context) {
    private val preferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    fun getSortOption(): String {
        return preferences.getString("sortOption", "") ?: ""
    }

    fun setSortOption(option: String) {
        preferences.edit().putString("sortOption", option).apply()
    }
}