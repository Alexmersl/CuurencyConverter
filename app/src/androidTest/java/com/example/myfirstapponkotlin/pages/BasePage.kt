package com.example.myfirstapponkotlin.pages

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

open class BasePage(protected val device: UiDevice) {
    protected fun waitForElement(resId: String, timeout: Long = 5000) {
        device.wait(Until.hasObject(By.res(resId)), timeout)
    }

    protected fun waitForText(text: String, timeout: Long = 5000) {
        device.wait(Until.hasObject(By.text(text)), timeout)
    }

    protected fun clickElement(resId: String) {
        device.findObject(By.res(resId)).click()
    }

    protected fun setText(resId: String, text: String) {
        device.findObject(By.res(resId)).text = text
    }

    protected fun isElementDisplayed(resId: String): Boolean {
        return device.findObject(By.res(resId)).exists()
    }

    protected fun isTextDisplayed(text: String): Boolean {
        return device.findObject(By.text(text)).exists()
    }
} 