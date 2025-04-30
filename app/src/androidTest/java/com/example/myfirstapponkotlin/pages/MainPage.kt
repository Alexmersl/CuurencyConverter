package com.example.myfirstapponkotlin.pages

import com.example.myfirstapponkotlin.R

class MainPage(device: UiDevice) : BasePage(device) {
    fun enterAmount(amount: String) {
        setText("com.example.myfirstapponkotlin:id/amount_input", amount)
    }

    fun selectFromCurrency(currency: String) {
        clickElement("com.example.myfirstapponkotlin:id/from_currency")
        device.findObject(By.text(currency)).click()
    }

    fun selectToCurrency(currency: String) {
        clickElement("com.example.myfirstapponkotlin:id/to_currency")
        device.findObject(By.text(currency)).click()
    }

    fun clickConvertButton() {
        clickElement("com.example.myfirstapponkotlin:id/convert_button")
    }

    fun verifyResultDisplayed() {
        waitForElement("com.example.myfirstapponkotlin:id/result_text")
    }

    fun verifyResultText(expectedText: String) {
        waitForText(expectedText)
    }

    fun openTaskPlanner() {
        device.findObject(By.text("Планировщик задач")).click()
    }
} 