package com.example.stagenography.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SteganographyApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}