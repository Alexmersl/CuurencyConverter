package com.example.myfirstapponkotlin.pages

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector

object SystemSettingsPage {

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    fun getToast(string : String) : UiObject {
       return device.findObject(selector(string))
    }

    fun selector (string : String) : UiSelector {
       return UiSelector().textContains(string)
    }

}