package com.lilac.priyacoder.materialdesigninkotlin

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.lilac.priyacoder.materialdesigninkotlin.data.db.PhotoEntryDatabase
import com.lilac.priyacoder.materialdesigninkotlin.data.db.dao.PhotoEntryDao
import com.lilac.priyacoder.materialdesigninkotlin.data.db.model.PhotoEntriesModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PhotoEntryDaoTest {

    @JvmField @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var photoEntryDatabase: PhotoEntryDatabase
    private lateinit var photoEntryDao : PhotoEntryDao

    private val testDir = getCurrentWorkingDir()
    private val entryText = "Entry test"
    private val updatedText = "Updated value"

    private val photoEntryModel = getPhotoEntryModel(testDir, entryText)


    @Before
    fun initDb(){
        photoEntryDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), PhotoEntryDatabase::class.java).allowMainThreadQueries().build()
        photoEntryDao = photoEntryDatabase.photoEntryDao()
    }

    @After
    fun closeDb(){
        photoEntryDatabase.close()
    }

    @Test
    fun insertPhotoEntry() {

        val insertedId = photoEntryDao.insert(photoEntryModel)
        assertEquals(1,insertedId)

        //validate Db has no null values
        val photoEntriesInDb = photoEntryDao.getAllEntries()
        val expectedList = PhotoEntriesModel(1, "$testDir/resources/test_image.jpg", entryText)
        photoEntriesInDb.test().assertValue(listOf(expectedList))
    }

    @Test
    fun updatePhotoEntry(){
        photoEntryDao.insert(photoEntryModel)
        photoEntryModel.setEntry(updatedText)

        val updatedQty = photoEntryDao.update(photoEntryModel)
        assertEquals(1,updatedQty.toLong())

        val actualUpdatedList = photoEntryDao.getAllEntries()
        val expectedList = PhotoEntriesModel(1, "$testDir/resources/test_image.jpg", updatedText)
        actualUpdatedList.test().assertValue(listOf(expectedList))
    }

    @Test
    fun deletePhotoEntry(){
//
        photoEntryDao.insert(photoEntryModel)

        val expectedPhotoEntryModelInDb = PhotoEntriesModel(1, "$testDir/resources/test_image.jpg", entryText)

        var photoEntriesInDb = photoEntryDao.getAllEntries()
        photoEntriesInDb.test().assertValues(listOf(expectedPhotoEntryModelInDb))

        //Delete inserted Value
        val deletedQty = photoEntryDao.deletePhotoEntry(expectedPhotoEntryModelInDb)
        assertEquals(1,deletedQty)

        photoEntriesInDb = photoEntryDao.getAllEntries()
        photoEntriesInDb.test().assertValues(arrayListOf())
    }

    private fun getCurrentWorkingDir() : String{
        val context = InstrumentationRegistry.getContext()
        val packageMgr = context.packageManager
        val packageName = context.packageName
        val packageInfo = packageMgr.getPackageInfo(packageName, 0)
        return packageInfo.applicationInfo.dataDir
    }

    private fun getPhotoEntryModel(testDir: String, entryText: String) : PhotoEntriesModel{
        return PhotoEntriesModel(1, "$testDir/resources/test_image.jpg", entryText)
    }
}
