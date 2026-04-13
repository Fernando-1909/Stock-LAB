package com.example.sistema_de_laboratorio

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class LaboratorioApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", true)
        
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
