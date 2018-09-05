package com.lilac.priyacoder.materialdesigninkotlin.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.lilac.priyacoder.materialdesigninkotlin.data.local.db.PhotoEntryDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context : Context) {

    @Singleton @Provides
    fun providesContext() : Context = context

    @Singleton @Provides
    fun provideRoomDatabase(context: Context) : PhotoEntryDatabase = Room.databaseBuilder(context, PhotoEntryDatabase::class.java,"photo-diary-database").build()

}