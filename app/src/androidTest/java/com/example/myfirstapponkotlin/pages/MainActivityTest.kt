package com.example.myfirstapponkotlin.pages

import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isTouchable
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasImeAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.myfirstapponkotlin.MainActivity
import com.example.myfirstapponkotlin.MainWindowPage
import com.example.myfirstapponkotlin.MainWindowPage.convertBtnView
import com.example.myfirstapponkotlin.R
import com.example.myfirstapponkotlin.features.BaseTest
import com.google.android.material.textview.MaterialTextView
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test


class MainActivityTest : BaseTest() {

    /*
    @Before
    fun launchActivity(){
        ActivityScenario.launch(MainActivity::class.java)
    }

     */


    @Test
    fun isNullResult(){
        MainWindowPage.resultFiled.check(ViewAssertions.matches(ViewMatchers.withText("")))
    }
    @Test
    fun smokeTestGetResult(){
        ActivityScenario.launch(MainActivity::class.java)
        MainWindowPage.enterAmount("500")
        MainWindowPage.selectCurrency(MainWindowPage.getAmountFrom())
        MainWindowPage.selectCurrency(MainWindowPage.getAmountTo())
        MainWindowPage.convertBtnClick()
    }

    @Test
    fun anyElementEnabled(){
        convertBtnView().check(matches(isClickable()))
    }

    @Test
    fun testWithText(){
        onView(withText("Расчет")).check(matches(isDisplayed()))
    }

    @Test
    fun testHasLinks(){
        onView(withText("Расчет")).check(matches(isEnabled()))
    }

    fun supportsInputMethod(){
        onView(withId(R.id.editTextNumber3)).perform(click())
        onView(withId(R.id.editTextNumber3)).check(matches(hasImeAction(EditorInfo.IME_ACTION_NEXT)))
    }

    @Test
    fun isAssaignFromClass(){
        onView(isAssignableFrom(AppCompatEditText::class.java)).perform(click())
        onView(withClassName(equalTo(MaterialTextView::class.java.name))).check(matches(isEnabled()))
    }

    @Test
    fun isDisplayedTest(){
        onView(isDisplayed())
    }

    @Test
    fun rootMatcherTest(){
        var view = onView(withText("Расчет")).inRoot(RootMatchers.isFocusable()) //
        onView(withText("Расчет")).inRoot(isTouchable())

    }

    @Test
    fun hamCrestMatchers(){
        onView(withId(`is`(R.id.editTextNumber3))).check(matches(isDisplayed()))
        onView(withClassName(`is`(MaterialTextView::class.java.name))).check(matches(isDisplayed()))
        //onView(withClassName(`is`("string"))).check(matches(isDisplayed()))
    }



}