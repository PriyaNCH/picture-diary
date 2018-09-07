package com.lilac.priyacoder.materialdesigninkotlin.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.lilac.priyacoder.materialdesigninkotlin.data.db.model.PhotoEntriesModel
import com.lilac.priyacoder.materialdesigninkotlin.data.db.dao.PhotoEntryDao

@Database(entities = arrayOf(PhotoEntriesModel::class), version = 1)
abstract class PhotoEntryDatabase : RoomDatabase(){

    abstract fun photoEntryDao() : PhotoEntryDao
}