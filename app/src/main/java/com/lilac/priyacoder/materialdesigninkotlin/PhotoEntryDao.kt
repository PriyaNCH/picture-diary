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
    fun update(photoEntryModel : PhotoEntriesModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photoEntryModel : PhotoEntriesModel)

    @Query("DELETE from photoEntries where imageFullPath = :imagePath")
    fun deleteAllByImage(imagePath: String)

    @Delete
    fun deletePhotoEntry(photoEntryModel : PhotoEntriesModel)

}