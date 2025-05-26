package com.example.myfirstapponkotlin.features

import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isTouchable
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.myfirstapponkotlin.presentation.MainActivity
import com.example.myfirstapponkotlin.pages.MainWindowPage
import com.example.myfirstapponkotlin.pages.MainWindowPage.amountInputCheckIsBlank
import com.example.myfirstapponkotlin.pages.MainWindowPage.convertBtnView
import com.example.myfirstapponkotlin.R
import com.example.myfirstapponkotlin.pages.MainWindowPage.clearInputText
import com.example.myfirstapponkotlin.pages.MainWindowPage.click
import com.example.myfirstapponkotlin.pages.MainWindowPage.convertBtnClick
import com.example.myfirstapponkotlin.pages.MainWindowPage.enterAmount
import com.example.myfirstapponkotlin.pages.MainWindowPage.getAmountFrom
import com.example.myfirstapponkotlin.pages.MainWindowPage.getAmountTo
import com.example.myfirstapponkotlin.pages.MainWindowPage.inputAmount
import com.example.myfirstapponkotlin.pages.MainWindowPage.navigateToHistory
import com.example.myfirstapponkotlin.pages.MainWindowPage.offlineBtnMatcher
import com.example.myfirstapponkotlin.pages.MainWindowPage.selectCurrency
import com.google.android.material.textview.MaterialTextView
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.equalToIgnoringCase
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.isEmptyOrNullString
import org.hamcrest.Matchers.isEmptyString
import org.junit.Test
import org.hamcrest.Matchers.*


class EspressoTests : BaseTest() {

    /*
    @Before
    fun launchActivity(){
        ActivityScenario.launch(MainActivity::class.java)
    }

     */


    @Test
    fun isNullResult() {
        MainWindowPage.resultFiled.check(matches(withText("")))
    }

    @Test
    fun smokeTestGetResult() {
        ActivityScenario.launch(MainActivity::class.java)
        MainWindowPage.enterAmount("500")
        MainWindowPage.selectCurrency(getAmountFrom(), "USD")
        MainWindowPage.selectCurrency(getAmountTo(), "USD")
        MainWindowPage.convertBtnClick()
    }

    @Test
    fun anyElementEnabled() {
        convertBtnView().check(matches(isClickable()))
    }

    @Test
    fun testWithText() {
        onView(withText("Расчет")).check(matches(isDisplayed()))
    }

    @Test
    fun testHasLinks() {
        onView(withText("Расчет")).check(matches(isEnabled()))
    }


    @Test
    fun isAssaignFromClass() {
        onView(isAssignableFrom(AppCompatEditText::class.java)).perform(click())
        onView(withClassName(equalTo(MaterialTextView::class.java.name))).check(matches(isEnabled()))
    }

    @Test
    fun isDisplayedTest() {
        onView(isDisplayed())
    }

    @Test
    fun rootMatcherTest() {
        onView(withText("Расчет")).inRoot(RootMatchers.isFocusable())
        onView(withText("Расчет")).inRoot(isTouchable())

    }

    @Test
    fun hamCrestMatchers() {
        onView(withId(`is`(R.id.editTextNumber3))).check(matches(isDisplayed()))

        onView(withClassName(`is`(MaterialTextView::class.java.name))).check(matches(isDisplayed()))

        onView(withText(`is`("Расчет"))).check(matches(isDisplayed())) // is() может принимать любой другой matcher или значение и проверять,
        // соответствует ли оно этому matcher'у или значению.

        assertThat(
            "String", equalTo("String")
        ) // equalTo() спользуется для проверки того, что объект равен другому объекту

        amountInputCheckIsBlank() //not() amountInput.check(matches(not(withHint("Text")))) значение не соответствует определенному условию

        onView(allOf(withId(R.id.editTextNumber3), instanceOf(TextView::class.java))).check(
            matches(isDisplayed())
        ) // instanceOf()  является ли объект экземпляром указанного класса или его подкласса

    }

    @Test
    fun combiningHamcrestMatchersTest() {
        onView(
            allOf(
                withId(R.id.button4), withText(equalTo("Button"))
            )
        ).check(matches(isClickable())) // allOf

        onView(anyOf(withId(R.id.button4), withText("Text"))).check(matches(isDisplayed())) // anyOF
    }

    @Test
    fun stringMatchers() {
        onView(allOf(withHint(`is`("Результат")), withText(isEmptyString()))).check(
            matches(
                isDisplayed()
            )
        ) // isEmptyString() проверяет что строка является пустой

        onView(allOf(withId(R.id.editTextNumber3), withText(`is`(isEmptyOrNullString())))).check(
            matches(isDisplayed())
        ) // строка является либо пустой строкой (""), либо null

        onView(
            withHint(`is`(startsWith("Введите")))
        ).check(matches(isDisplayed())) // startsWith() строка начинается с определенной String, аналогично endWith()

        onView(withHint(equalToIgnoringCase("РЕЗУЛЬТАТ"))).check(matches(isDisplayed())) // equalToIgnoringCase() проверка
        // строки с игнорированием регистра

        onView(withHint(equalToIgnoringWhiteSpace("Резу льтат"))).check(matches(isDisplayed())) // equalToCompressingWhiteSpace проверка
        // игнорируя пробелы в строке
    }

    @Test
    fun viewActionsTest() {
        onView(offlineBtnMatcher).click() // click()
        onView(withId(R.id.button2)).perform(click())

        onView(offlineBtnMatcher).perform(longClick()) // longСlick()

        onView(withId(R.id.editTextNumber3)).perform(typeText("22"))
            .perform(pressImeActionButton()) // pressImeActionButton() нажимает кнопку "Enter" на клваиатуре

        onView(withId(R.id.editTextNumber3)).perform(pressKey(39)) // perform(pressKey()) нажатие на
        // соовтествующий номеру элемент на клавиаутре

        onView(ViewMatchers.isRoot()).perform(closeSoftKeyboard())
            .perform(pressBack()) // closeSoftKeyboard() закрытие клавиатуры и действие назад pressBack()

    }

    @Test
    fun viewAssertions() {
        onView(offlineBtnMatcher).check(matches(isDisplayed())) // matches() позволяет проверять состояние
        // и характеристики представлений (views)

        onView(withId(R.id.button2)).check(doesNotExist()) // doesNotExist() используется для проверки того,
        // что определённое представление отсутствует на экране
    }

    @Test
    fun dataInteractionTest() {
        repeat(5) {
            clearInputText(inputAmount())
            enterAmount("34")
            clearInputText(getAmountFrom())
            clearInputText(getAmountTo())
            selectCurrency(getAmountFrom(), "USD")
            selectCurrency(getAmountTo(), "GEL")
            convertBtnClick()
        }
        navigateToHistory()

        Thread.sleep(10000)
        onData(withText("34 --> 91.9564")).inAdapterView(withId(R.id.currencyHistory))
            .atPosition(0)
            .check(matches(isDisplayed()))

        "34 --> 91.9564"


    }


}