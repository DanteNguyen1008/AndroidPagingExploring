package com.cat.reddithexagonal

import android.app.Application
import com.cat.framework.local.database.RedditDatabase
import com.cat.reddithexagonal.modules.frameworkModules
import com.cat.reddithexagonal.modules.interactorModules
import com.cat.reddithexagonal.modules.viewModelModules
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HexApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@HexApplication)
            modules(arrayListOf(viewModelModules, interactorModules, frameworkModules))
        }

        Fresco.initialize(this)
        Stetho.initializeWithDefaults(this);
    }
}