package com.lilac.priyacoder.materialdesigninkotlin.data.local.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "photoEntries")
data class PhotoEntriesModel(@PrimaryKey(autoGenerate = true) var photoEntryId: Long = 0,
                        @ColumnInfo var imageFullPath : String?,
                        @ColumnInfo var photoEntry : String){
    fun getId() : Long{
        return photoEntryId
    }

    fun setEntry(entry : String){
        this.photoEntry = entry
    }
}