package com.lilac.priyacoder.materialdesigninkotlin

import android.arch.persistence.room.*
import io.reactivex.Flowable
import org.intellij.lang.annotations.Flow

@Dao
interface PhotoEntryDao{

    @Query("Select * from photoEntries")
    fun getAllEntries() : Flowable<List<PhotoEntriesModel>>

    @Query("Select * from photoEntries where imageFullPath = :imagePath")
    fun getAllEntries(imagePath : String?) : Flowable<List<PhotoEntriesModel>>

    @Query("Update photoEntries set photoEntry = :entry where photoEntryId = :id")
    fun updateEntries(id : Long, entry : String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(photoEntries : PhotoEntriesModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photoEntries: PhotoEntriesModel)

    @Query("DELETE from photoEntries where imageFullPath = :imagePath and photoEntry = :entry and photoEntryId = :id")
    fun deleteEntryById(imagePath: String, entry : String, id : Long)

    @Query("DELETE from photoEntries where imageFullPath = :imagePath")
    fun deleteAllByImage(imagePath: String)

    @Query("DELETE from photoEntries")
    fun deleteAll()

}