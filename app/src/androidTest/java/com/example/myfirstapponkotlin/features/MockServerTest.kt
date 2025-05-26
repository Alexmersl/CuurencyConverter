package com.example.myfirstapponkotlin.features

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myfirstapponkotlin.R
import com.example.myfirstapponkotlin.api.CurrencyApi
import com.example.myfirstapponkotlin.api.CurrencyRepository
import com.example.myfirstapponkotlin.pages.MainWindowPage
import com.example.myfirstapponkotlin.pages.MainWindowPage.getAmountFrom
import com.example.myfirstapponkotlin.pages.MainWindowPage.getAmountTo
import com.example.myfirstapponkotlin.presentation.MainActivity
import com.example.myfirstapponkotlin.presentation.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MockServerTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var context: Context
    private lateinit var currencyApi: CurrencyApi
    private lateinit var repository: CurrencyRepository

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        context = ApplicationProvider.getApplicationContext()
        
        mockWebServer.start(8080)
        
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        currencyApi = retrofit.create(CurrencyApi::class.java)
        repository = CurrencyRepository(currencyApi)
        
        val currenciesResponse = """
            {
                "result": "success",
                "documentation": "https://www.exchangerate-api.com/docs",
                "terms_of_use": "https://www.exchangerate-api.com/terms",
                "time_last_update_unix": 1704067201,
                "time_last_update_utc": "Wed, 03 Jan 2024 00:00:01 +0000",
                "time_next_update_unix": 1704153601,
                "time_next_update_utc": "Thu, 04 Jan 2024 00:00:01 +0000",
                "base_code": "USD",
                "conversion_rates": {
                    "USD": 1,
                    "EUR": 0.91,
                    "GBP": 0.78,
                    "JPY": 144.62
                }
            }
        """.trimIndent()
        
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(currenciesResponse)
            .addHeader("Content-Type", "application/json"))
    }

    @Test
    fun testCurrencyConversionWithMockServer() {
        val conversionResponse = """
            {
                "result": "success",
                "documentation": "https://www.exchangerate-api.com/docs",
                "terms_of_use": "https://www.exchangerate-api.com/terms",
                "time_last_update_unix": 1704067201,
                "time_last_update_utc": "Wed, 03 Jan 2024 00:00:01 +0000",
                "time_next_update_unix": 1704153601,
                "time_next_update_utc": "Thu, 04 Jan 2024 00:00:01 +0000",
                "base_code": "USD",
                "target_code": "EUR",
                "conversion_rate": 0.91,
                "conversion_result": 91.0
            }
        """.trimIndent()
        
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(conversionResponse)
            .addHeader("Content-Type", "application/json"))

        MainWindowPage.apply {
            enterAmount("100")
            selectCurrency(getAmountFrom(), "USD")
            selectCurrency(getAmountTo(), "EUR")
            convertBtnClick()
        }

        val request1 = mockWebServer.takeRequest(5, TimeUnit.SECONDS)
        assert(request1?.path?.contains("/v6/3e20f4c7e005a1301fc8ccca/latest/USD") == true) {
            "First request path should contain '/v6/3e20f4c7e005a1301fc8ccca/latest/USD', but was: ${request1?.path}"
        }

        val request2 = mockWebServer.takeRequest(5, TimeUnit.SECONDS)
        assert(request2?.path?.contains("/v6/3e20f4c7e005a1301fc8ccca/pair/USD/EUR/100") == true) {
            "Second request path should contain '/v6/3e20f4c7e005a1301fc8ccca/pair/USD/EUR/100', but was: ${request2?.path}"
        }
    }

    @Test
    fun testErrorHandling() {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(404)
            .setBody("Not Found"))

        MainWindowPage.apply {
            enterAmount("100")
            selectCurrency(getAmountFrom(), "USD")
            selectCurrency(getAmountTo(), "EUR")
            convertBtnClick()
        }

        val request = mockWebServer.takeRequest(5, TimeUnit.SECONDS)
        assert(request?.path?.contains("/v6/3e20f4c7e005a1301fc8ccca/pair/USD/EUR/100") == true) {
            "Request path should contain '/v6/3e20f4c7e005a1301fc8ccca/pair/USD/EUR/100', but was: ${request?.path}"
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
} 