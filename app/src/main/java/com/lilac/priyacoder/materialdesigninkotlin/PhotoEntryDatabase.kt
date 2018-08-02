package com.lilac.priyacoder.materialdesigninkotlin

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.provider.ContactsContract

@Database(entities = arrayOf(PhotoEntriesModel::class), version = 1)
abstract class PhotoEntryDatabase : RoomDatabase(){

    abstract fun photoEntryDao() : PhotoEntryDao
}