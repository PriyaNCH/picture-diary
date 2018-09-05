package com.lilac.priyacoder.materialdesigninkotlin

import android.app.Application
import com.lilac.priyacoder.materialdesigninkotlin.di.component.AppComponent
import com.lilac.priyacoder.materialdesigninkotlin.di.component.DaggerAppComponent
import com.lilac.priyacoder.materialdesigninkotlin.di.module.AppModule

class PhotoDiaryApp : Application() {
    private var appComponent: AppComponent? = null

    companion object {
        private var app: PhotoDiaryApp? = null

        fun app(): PhotoDiaryApp? {
            return app
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext)).build()
    }

    fun appComponent(): AppComponent? {
        return appComponent
    }
}