package com.example.myfirstapponkotlin

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.example.myfirstapponkotlin.pages.MainPage
import com.example.myfirstapponkotlin.pages.TaskPlannerPage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class UiAutomatorTest {
    private lateinit var device: UiDevice
    private lateinit var mainPage: MainPage
    private lateinit var taskPlannerPage: TaskPlannerPage

    @Before
    fun setUp() {
        device = UiDevice.getInstance(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation())
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(context.packageName).depth(0)), 5000)
        
        mainPage = MainPage(device)
        taskPlannerPage = TaskPlannerPage(device)
    }

    @Test
    fun testCurrencyConversion() {
        mainPage.apply {
            enterAmount("100")
            selectFromCurrency("USD")
            selectToCurrency("EUR")
            clickConvertButton()
            verifyResultDisplayed()
        }
    }

    @Test
    fun testTaskPlanner() {
        mainPage.openTaskPlanner()
        
        taskPlannerPage.apply {
            addNewTask("Тестовая задача")
            verifyTaskAdded("Тестовая задача")
            markTaskAsCompleted()
            deleteTask()
            verifyTaskDeleted("Тестовая задача")
        }
    }
}

@RunWith(Parameterized::class)
class ParameterizedUiAutomatorTest(private val amount: String, private val expectedResult: String) {
    private lateinit var device: UiDevice
    private lateinit var mainPage: MainPage

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

    @Before
    fun setUp() {
        device = UiDevice.getInstance(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation())
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(context.packageName).depth(0)), 5000)
        
        mainPage = MainPage(device)
    }

    @Test
    fun testParameterizedConversion() {
        mainPage.apply {
            enterAmount(amount)
            selectFromCurrency("USD")
            selectToCurrency("EUR")
            clickConvertButton()
            verifyResultText(expectedResult)
        }
    }
} 