package com.lilac.priyacoder.materialdesigninkotlin.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.lilac.priyacoder.materialdesigninkotlin.data.db.PhotoEntryDatabase
import com.lilac.priyacoder.materialdesigninkotlin.di.model.ImageLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context : Context) {

    private val databaseName : String = "photo-diary-database"

    @Singleton @Provides
    fun providesContext() : Context = context

    @Singleton @Provides
    fun provideRoomDatabase(context: Context) : PhotoEntryDatabase = Room.databaseBuilder(context, PhotoEntryDatabase::class.java,databaseName).build()

    @Singleton @Provides
    fun provideImageLoader(context: Context) : ImageLoader = ImageLoader(context)
}