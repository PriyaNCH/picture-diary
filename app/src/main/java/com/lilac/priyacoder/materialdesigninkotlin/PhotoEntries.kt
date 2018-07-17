package com.lilac.priyacoder.materialdesigninkotlin

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "photoEntries")
data class PhotoEntries(@PrimaryKey(autoGenerate = true) var photoEntryId: Long = 0,
                        @ColumnInfo var imageFullPath : String?,
                        @ColumnInfo var photoEntry : String)