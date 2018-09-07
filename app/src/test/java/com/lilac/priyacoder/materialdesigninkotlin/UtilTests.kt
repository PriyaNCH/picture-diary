package com.lilac.priyacoder.materialdesigninkotlin

import com.lilac.priyacoder.materialdesigninkotlin.utils.MonthComparator
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ComparatorUtilTests {
    private val objectUnderTest = MonthComparator

    @Test
    fun testEqual() {
        val firstValue = "June"
        val secondValue = "June"
        val expectedResult = 0
        val actualResult = objectUnderTest.compare(firstValue,secondValue)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun testSorting(){
        val arrayTobeSorted = arrayOf("Dec","May","Aug")
        val expected = arrayOf("May","Aug","Dec")
        arrayTobeSorted.sortWith(objectUnderTest)

        //Compare array after sorted
        assertArrayEquals(expected, arrayTobeSorted)
    }
}
