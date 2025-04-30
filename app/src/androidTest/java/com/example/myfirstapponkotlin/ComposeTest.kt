package com.example.myfirstapponkotlin

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(AndroidJUnit4::class)
class ComposeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testCurrencyConversion() {
        // Enter amount
        composeTestRule.onNodeWithTag("amountInput")
            .performTextInput("100")

        // Select currencies
        composeTestRule.onNodeWithTag("fromCurrency")
            .performClick()
        composeTestRule.onNodeWithText("USD")
            .performClick()

        composeTestRule.onNodeWithTag("toCurrency")
            .performClick()
        composeTestRule.onNodeWithText("EUR")
            .performClick()

        // Click convert button
        composeTestRule.onNodeWithTag("convertButton")
            .performClick()

        // Verify result
        composeTestRule.onNodeWithTag("resultText")
            .assertIsDisplayed()
    }

    @Test
    fun testTaskPlanner() {
        // Open task planner
        composeTestRule.onNodeWithText("Планировщик задач")
            .performClick()

        // Add new task
        composeTestRule.onNodeWithTag("newTaskInput")
            .performTextInput("Тестовая задача")

        composeTestRule.onNodeWithTag("addTaskButton")
            .performClick()

        // Verify task is added
        composeTestRule.onNodeWithText("Тестовая задача")
            .assertIsDisplayed()

        // Mark task as completed
        composeTestRule.onNodeWithTag("taskCheckbox")
            .performClick()

        // Delete task
        composeTestRule.onNodeWithTag("deleteTaskButton")
            .performClick()
    }
}

@RunWith(Parameterized::class)
class ParameterizedComposeTest(private val amount: String, private val expectedResult: String) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

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
        composeTestRule.onNodeWithTag("amountInput")
            .performTextInput(amount)

        // Select currencies
        composeTestRule.onNodeWithTag("fromCurrency")
            .performClick()
        composeTestRule.onNodeWithText("USD")
            .performClick()

        composeTestRule.onNodeWithTag("toCurrency")
            .performClick()
        composeTestRule.onNodeWithText("EUR")
            .performClick()

        // Click convert button
        composeTestRule.onNodeWithTag("convertButton")
            .performClick()

        // Verify result
        composeTestRule.onNodeWithText(expectedResult)
            .assertIsDisplayed()
    }
} 