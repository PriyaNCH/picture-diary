package com.lilac.priyacoder.materialdesigninkotlin

import com.lilac.priyacoder.materialdesigninkotlin.utils.ImageUtils
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.io.File

class ImageUtilTests{
    private val objectUnderTest = ImageUtils

    @Test
    fun testFileDimensions(){
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