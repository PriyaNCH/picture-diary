package com.lilac.priyacoder.materialdesigninkotlin.data.db.dao

import android.arch.persistence.room.*
import com.lilac.priyacoder.materialdesigninkotlin.data.db.model.PhotoEntriesModel
import io.reactivex.Flowable

@Dao
interface PhotoEntryDao{

    @Query("Select * from photoEntries")
    fun getAllEntries() : Flowable<List<PhotoEntriesModel>>

    @Query("Select * from photoEntries where imageFullPath = :imagePath")
    fun getAllEntries(imagePath : String?) : Flowable<List<PhotoEntriesModel>>

    @Query("Select * from photoEntries")
    fun getAllEntriesByList() : List<PhotoEntriesModel>

    @Query("Update photoEntries set photoEntry = :entry where photoEntryId = :id")
    fun updateEntries(id : Long, entry : String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(photoEntryModel : PhotoEntriesModel) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photoEntryModel : PhotoEntriesModel) : Long

    @Query("Delete from photoEntries where imageFullPath = :imagePath")
    fun deleteAllByImage(imagePath: String)

    @Delete
    fun deletePhotoEntry(photoEntryModel : PhotoEntriesModel) : Int
}