package com.lilac.priyacoder.materialdesigninkotlin

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import org.intellij.lang.annotations.Flow

@Dao
interface PhotoEntryDao{
    @Query("Select * from photoEntries where imageFullPath = :imagePath")
    fun getAllEntries(imagePath : String?) : Flowable<List<PhotoEntries>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photoEntries: PhotoEntries)

    @Query("DELETE from photoEntries where imageFullPath = :imagePath and photoEntry = :entry and photoEntryId = :id")
    fun deleteEntryById(imagePath: String, entry : String, id : Long)

    @Query("DELETE from photoEntries where imageFullPath = :imagePath")
    fun deleteAllByImage(imagePath: String)

    @Query("DELETE from photoEntries")
    fun deleteAll()

}