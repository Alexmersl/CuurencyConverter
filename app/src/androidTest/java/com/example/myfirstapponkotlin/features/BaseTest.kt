package com.example.myfirstapponkotlin.features


import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.myfirstapponkotlin.pages.UiDevicePreparation.Companion.setDevicePreferences
import com.example.myfirstapponkotlin.presentation.MainActivity
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.rules.Timeout
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.qameta.allure.android.rules.WindowHierarchyRule
import org.junit.Test

open class BaseTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.simple()
) {
    @get:Rule
    val globalTimeout: Timeout = Timeout.seconds(300)

    @get:Rule
    val windowHierarchyRule = WindowHierarchyRule()

    @get:Rule
    val mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeAllTests() {
            setDevicePreferences()
        }

    }

    @Test
    fun test(){
        Thread.sleep(4000)
    }
    /*
      @After
      open fun closeAllActivities() {
          hideQuickSettingsIfDisplayed()
          closeNotificationPage()
          hideNotificationIfDisplayed()
          if (mActivityTestRule.scenario.state != Lifecycle.State.DESTROYED) {
              mActivityTestRule.scenario.onActivity { activity: MainActivity -> activity.finishAndRemoveTask() }
          }
      }

     */

}