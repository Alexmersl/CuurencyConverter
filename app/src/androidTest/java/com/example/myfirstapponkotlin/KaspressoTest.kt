package com.example.myfirstapponkotlin

import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.kaspresso.testcases.core.testcontext.BaseTestContext
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class KaspressoTest : TestCase() {

    @Test
    fun testCurrencyConversion() = run {
        step("Open app and perform currency conversion") {
            MainScreen {
                amountInput {
                    typeText("100")
                }
                fromCurrency {
                    click()
                    selectCurrency("USD")
                }
                toCurrency {
                    click()
                    selectCurrency("EUR")
                }
                convertButton {
                    click()
                }
                resultText {
                    isDisplayed()
                }
            }
        }
    }

    @Test
    fun testTaskPlanner() = run {
        step("Test task planner functionality") {
            MainScreen {
                taskPlannerButton {
                    click()
                }
            }
            TaskPlannerScreen {
                newTaskInput {
                    typeText("Тестовая задача")
                }
                addTaskButton {
                    click()
                }
                taskList {
                    childAt<TaskPlannerScreen.TaskItem>(0) {
                        taskText {
                            hasText("Тестовая задача")
                        }
                        taskCheckbox {
                            click()
                        }
                        deleteButton {
                            click()
                        }
                    }
                }
            }
        }
    }
}

@RunWith(Parameterized::class)
class ParameterizedKaspressoTest(private val amount: String, private val expectedResult: String) : TestCase() {

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
    fun testParameterizedConversion() = run {
        step("Test parameterized currency conversion") {
            MainScreen {
                amountInput {
                    typeText(amount)
                }
                fromCurrency {
                    click()
                    selectCurrency("USD")
                }
                toCurrency {
                    click()
                    selectCurrency("EUR")
                }
                convertButton {
                    click()
                }
                resultText {
                    hasText(expectedResult)
                }
            }
        }
    }
}

class MainScreen : BaseTestContext() {
    val amountInput = view { withId(R.id.amount_input) }
    val fromCurrency = view { withId(R.id.from_currency) }
    val toCurrency = view { withId(R.id.to_currency) }
    val convertButton = view { withId(R.id.convert_button) }
    val resultText = view { withId(R.id.result_text) }
    val taskPlannerButton = view { withText("Планировщик задач") }

    fun selectCurrency(currency: String) {
        view { withText(currency) }.click()
    }
}

class TaskPlannerScreen : BaseTestContext() {
    val newTaskInput = view { withId(R.id.new_task_input) }
    val addTaskButton = view { withId(R.id.add_task_button) }
    val taskList = view { withId(R.id.task_list) }

    class TaskItem : BaseTestContext() {
        val taskText = view { withId(R.id.task_text) }
        val taskCheckbox = view { withId(R.id.task_checkbox) }
        val deleteButton = view { withId(R.id.delete_task_button) }
    }
} 