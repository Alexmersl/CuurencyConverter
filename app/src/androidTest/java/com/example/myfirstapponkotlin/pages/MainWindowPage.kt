package com.example.myfirstapponkotlin.pages

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.myfirstapponkotlin.HttpConnectionClass
import com.example.myfirstapponkotlin.R
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not


object MainWindowPage {

    private val amountFrom: ViewInteraction = onView(withId(R.id.autoCompleteTextView2))

    private val amountTo: ViewInteraction = onView(withId(R.id.autoCompleteTextView1))
    private val convertBtn: ViewInteraction
        get() = onView(withId(R.id.button))

    private val amountInput: ViewInteraction
        get() = onView(withId(R.id.editTextNumber3))

    val offlineBtnMatcher: Matcher<View>
        get() = withId(R.id.button3)

    val resultFiled: ViewInteraction
        get() = onView(withId(R.id.textView2))
    private val toOfflineBtn: ViewInteraction
        get() = onView(withId(R.id.button3))
    private val toHistoryBtn : ViewInteraction
        get() = onView(withId(R.id.button4))

    fun ViewInteraction.click(): ViewInteraction = this.perform(ViewActions.click())

    fun inputAmount() : ViewInteraction = amountInput

    fun getAmountFrom(): ViewInteraction = amountFrom
    fun getAmountTo(): ViewInteraction = amountTo

    fun convertBtnView(): ViewInteraction = convertBtn

    fun convertBtnClick() {
        convertBtn.perform(ViewActions.click())
    }
    fun clearInputText(viewInteraction: ViewInteraction) {
        viewInteraction.perform(clearText())
    }

    fun enterAmount(string: String) {
        amountInput.perform(ViewActions.typeText(string))
    }

    fun selectCurrency(currency: ViewInteraction, strCurrency : String) {
        currency.perform(ViewActions.typeText(strCurrency))
        onView(ViewMatchers.withText(strCurrency))
            .perform(ViewActions.click())
    }

    fun amountInputCheckIsBlank() {
        amountInput.check(matches(not(withHint("Text"))))
    }

    fun navigateToHistory(){
        toHistoryBtn.click()
    }

    fun getTextResult(): String? {
        return null
    }

}