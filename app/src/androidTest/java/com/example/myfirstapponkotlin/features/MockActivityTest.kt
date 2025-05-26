package com.example.myfirstapponkotlin.features

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myfirstapponkotlin.MainActivity3
import com.example.myfirstapponkotlin.presentation.MainActivity
import com.example.myfirstapponkotlin.presentation.history.HistoryActivity
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MockActivityTest {
    private lateinit var context: Context
    private lateinit var mockContext: Context

    @get:Rule
    val mActivityTestRule = ActivityScenarioRule(MainActivity3::class.java)

    @Before
    fun setup() {
    }

    @Test
    fun testMockActivityLaunch() {
        Thread.sleep(5000)
    }
} 