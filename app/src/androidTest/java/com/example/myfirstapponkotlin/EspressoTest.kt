package com.example.myfirstapponkotlin

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(AndroidJUnit4::class)
class EspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testCurrencyConversion() {
        // Enter amount
        Espresso.onView(ViewMatchers.withId(R.id.amountInput))
            .perform(ViewActions.typeText("100"))

        // Select currencies
        Espresso.onView(ViewMatchers.withId(R.id.fromCurrency))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("USD"))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.toCurrency))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("EUR"))
            .perform(ViewActions.click())

        // Click convert button
        Espresso.onView(ViewMatchers.withId(R.id.convertButton))
            .perform(ViewActions.click())

        // Verify result
        Espresso.onView(ViewMatchers.withId(R.id.resultText))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testTaskPlanner() {
        // Open task planner
        Espresso.onView(ViewMatchers.withText("Планировщик задач"))
            .perform(ViewActions.click())

        // Add new task
        Espresso.onView(ViewMatchers.withId(R.id.newTaskInput))
            .perform(ViewActions.typeText("Тестовая задача"))

        Espresso.onView(ViewMatchers.withId(R.id.addTaskButton))
            .perform(ViewActions.click())

        // Verify task is added
        Espresso.onView(ViewMatchers.withText("Тестовая задача"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Mark task as completed
        Espresso.onView(ViewMatchers.withId(R.id.taskCheckbox))
            .perform(ViewActions.click())

        // Delete task
        Espresso.onView(ViewMatchers.withId(R.id.deleteTaskButton))
            .perform(ViewActions.click())
    }
}

@RunWith(Parameterized::class)
class ParameterizedEspressoTest(private val amount: String, private val expectedResult: String) {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<String>> {
            return listOf(
                arrayOf("100", "85.50"),
                arrayOf("200", "171.00"),
                arrayOf("50", "42.75")
            )
        }
    }

    @Test
    fun testParameterizedConversion() {
        // Enter amount
        Espresso.onView(ViewMatchers.withId(R.id.amountInput))
            .perform(ViewActions.typeText(amount))

        // Select currencies
        Espresso.onView(ViewMatchers.withId(R.id.fromCurrency))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("USD"))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.toCurrency))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("EUR"))
            .perform(ViewActions.click())

        // Click convert button
        Espresso.onView(ViewMatchers.withId(R.id.convertButton))
            .perform(ViewActions.click())

        // Verify result
        Espresso.onView(ViewMatchers.withText(expectedResult))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
} 