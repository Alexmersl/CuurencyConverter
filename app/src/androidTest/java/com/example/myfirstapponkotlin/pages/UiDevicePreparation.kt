package com.example.myfirstapponkotlin.pages

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.BeforeClass

class UiDevicePreparation {
    companion object {
        fun setDevicePreferences() {
            val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

            uiDevice.executeShellCommand("settings put global animator_duration_scale 0")
            uiDevice.executeShellCommand("settings put global transition_animation_scale 0")
            uiDevice.executeShellCommand("settings put global window_animation_scale 0")


            uiDevice.executeShellCommand("settings put secure long_press_timeout 2500")


            uiDevice.executeShellCommand("settings put secure show_ime_with_hard_keyboard 0")


        }

    }
}