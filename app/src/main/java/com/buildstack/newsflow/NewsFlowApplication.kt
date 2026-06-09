package com.buildstack.newsflow

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsFlowApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
