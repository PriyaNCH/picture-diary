package com.lilac.priyacoder.materialdesigninkotlin

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.lilac.priyacoder.materialdesigninkotlin.data.db.PhotoEntryDatabase
import com.lilac.priyacoder.materialdesigninkotlin.data.db.dao.PhotoEntryDao
import com.lilac.priyacoder.materialdesigninkotlin.data.db.model.PhotoEntriesModel
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.arch.core.executor.testing.InstantTaskExecutorRule

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
    assertNotNull(photoEntriesInDb.toList())
  }

  @Test
  fun updatePhotoEntry(){

    val updatedEntry = "New value"
    photoEntryDao.insert(photoEntryModel)
    photoEntryModel.setEntry(updatedEntry)

    val updatedQty = photoEntryDao.update(photoEntryModel)
    val photoEntriesInDb = photoEntryDao.getAllEntries()
    val expectedList = PhotoEntriesModel(1, "$testDir/resources/test_image.jpg", updatedEntry)
    photoEntriesInDb.test().assertValue(listOf(expectedList))
//    assertEquals(1,updatedQty.toLong())
  }

  @Test
  fun deletePhotoEntry(){

    photoEntryDao.insert(photoEntryModel)

    val deletedQty = photoEntryDao.deletePhotoEntry(photoEntryModel)
//    assertEquals(1,deletedQty)

    val photoEntriesInDb = photoEntryDao.getAllEntries()
    photoEntriesInDb.test().assertEmpty()
  }

  fun getCurrentWorkingDir() : String{
    val context = InstrumentationRegistry.getContext()
    val packageMgr = context.getPackageManager()
    var packageName = context.getPackageName()
    val packageInfo = packageMgr.getPackageInfo(packageName, 0)
      return packageInfo.applicationInfo.dataDir
  }

  fun getPhotoEntryModel(testDir: String, entryText: String) : PhotoEntriesModel{
    return PhotoEntriesModel(0, "$testDir/resources/test_image.jpg", entryText)
  }
}
