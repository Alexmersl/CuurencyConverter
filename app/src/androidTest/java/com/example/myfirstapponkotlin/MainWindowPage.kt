package com.example.myfirstapponkotlin

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId



object MainWindowPage {

    private val amountFrom : ViewInteraction = onView(withId(R.id.autoCompleteTextView2))

    private val amountTo: ViewInteraction = onView(withId(R.id.autoCompleteTextView1))
    private val convertBtn : ViewInteraction
        get() = onView(withId(R.id.button))

    private val amountInput : ViewInteraction
        get() = onView(withId(R.id.editTextNumber3))

    val resultFiled : ViewInteraction
        get() = onView(withId(R.id.textView2))
    private val toOfflineBtn : ViewInteraction
        get() = onView(withId(R.id.button3))
    fun getAmountFrom() : ViewInteraction = amountFrom
    fun getAmountTo() : ViewInteraction = amountTo

    fun convertBtnView() : ViewInteraction = convertBtn

    fun convertBtnClick(){
        convertBtn.perform(ViewActions.click())
    }
    fun enterAmount(string : String){
        amountInput.perform(ViewActions.typeText(string))
    }
    fun selectCurrency(currency : ViewInteraction){
        val hashSet = HttpConnectionClass.getCurrencySet()
        val strCurrency : String = hashSet.random()
        currency.perform(ViewActions.typeText(strCurrency))
        onView(ViewMatchers.withText(strCurrency))
            .perform(ViewActions.click())
    }

    fun getTextResult() : String? {
        return null
    }

}