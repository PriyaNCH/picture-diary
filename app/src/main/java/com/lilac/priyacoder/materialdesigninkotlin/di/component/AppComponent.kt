package com.lilac.priyacoder.materialdesigninkotlin.di.component

import com.lilac.priyacoder.materialdesigninkotlin.di.module.AppModule
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.DetailActivity
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.ImagesViewActivity
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent{
    fun inject(detailActivity: DetailActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(imagesViewActivity: ImagesViewActivity)
}