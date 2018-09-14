package com.lilac.priyacoder.materialdesigninkotlin

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.ImagesViewActivity
import com.lilac.priyacoder.materialdesigninkotlin.ui.activity.main.MainActivity
import com.lilac.priyacoder.materialdesigninkotlin.ui.adapter.ImageBucketsAdapter
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentationTest {
    @Rule
    @JvmField
    val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp(){
        Intents.init()
    }

    @After
    fun close(){
        Intents.release()
    }
    @Test
    fun testOnClickImage(){
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition<ImageBucketsAdapter.ViewHolder>(0,click()))
        Intents.intended(IntentMatchers.hasComponent(ImagesViewActivity::class.java.name))
    }
}