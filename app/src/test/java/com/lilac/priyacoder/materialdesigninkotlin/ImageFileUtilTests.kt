package com.lilac.priyacoder.materialdesigninkotlin

import android.graphics.BitmapFactory
import com.lilac.priyacoder.materialdesigninkotlin.utils.ImageUtils
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.spy
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class ImageUtilTests{
    private val objectUnderTest = ImageUtils

    @Test
    fun testFileDimensions(){
        val mockBitmapFactory : BitmapFactory = spy(BitmapFactory::class.java)

        val expectedWidth = 2610
        val expectedHeight = 4640
        val resource = getFileFromPath(this,"test_image.jpg")
        val actualOptions = objectUnderTest.getFileDimensions(resource)
        assertEquals(expectedHeight, actualOptions.outHeight)
        assertEquals(expectedWidth, actualOptions.outWidth)
    }

    private fun getFileFromPath(utilTests: ImageUtilTests, fileName : String) : File {
        val classLoader = utilTests.javaClass.classLoader
        val resource = classLoader.getResource(fileName)
        return File(resource.path)
    }
}