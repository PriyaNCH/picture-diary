package com.lilac.priyacoder.materialdesigninkotlin

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.main.MainActivity
import com.lilac.priyacoder.materialdesigninkotlin.ui.preferences.Prefs
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.temporal.ValueRange

@RunWith(AndroidJUnit4::class)
class SharedPrefsInstrumentationTest {

    private lateinit var preferredFolderValue : String
    private lateinit var prefs : Prefs
    private val KEY = "testKey"
    private val VALUE = "testValue"
    @Rule
    @JvmField
    val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        val instrumentationContext = InstrumentationRegistry.getTargetContext()
        prefs = Prefs.getInstance(instrumentationContext)
    }

    @Test
    fun validateSharedPrefs(){
        prefs.setValue(KEY, VALUE)
        preferredFolderValue = prefs.getValues().getString(KEY,null)
        Assert.assertEquals(VALUE, preferredFolderValue)
    }
}