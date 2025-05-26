package com.example.myfirstapponkotlin.features

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myfirstapponkotlin.api.CurrencyApi
import com.example.myfirstapponkotlin.api.CurrencyRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RequestInterceptorTest {
    private lateinit var context: Context
    private lateinit var currencyApi: CurrencyApi
    private lateinit var repository: CurrencyRepository
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var requestCaptureInterceptor: RequestCaptureInterceptor

    private class RequestCaptureInterceptor : Interceptor {
        var capturedRequest: okhttp3.Request? = null
        var capturedResponse: Response? = null

        override fun intercept(chain: Interceptor.Chain): Response {
            capturedRequest = chain.request()
            
            val response = chain.proceed(chain.request())
            
            capturedResponse = response
            
            return response
        }
    }

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        
        requestCaptureInterceptor = RequestCaptureInterceptor()
        
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestCaptureInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/v6/3e20f4c7e005a1301fc8ccca/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        currencyApi = retrofit.create(CurrencyApi::class.java)
        repository = CurrencyRepository(currencyApi)
    }

    @Test
    suspend fun testRequestInterception() {
        repository.getCurrencySet()
        
        val capturedRequest = requestCaptureInterceptor.capturedRequest
        assert(capturedRequest != null) { "Request was not captured" }
        
        val requestUrl = capturedRequest?.url?.toString()
        assert(requestUrl?.contains("/latest/USD") == true) {
            "Request URL should contain '/latest/USD', but was: $requestUrl"
        }
        
        assert(capturedRequest?.method == "GET") {
            "Request method should be GET, but was: ${capturedRequest?.method}"
        }
        
        val headers = capturedRequest?.headers
        assert(headers?.get("Content-Type")?.contains("application/json") == true) {
            "Content-Type header should contain 'application/json'"
        }
    }

    @Test
    suspend fun testResponseInterception() {
        repository.getCurrencySet()
        
        val capturedResponse = requestCaptureInterceptor.capturedResponse
        assert(capturedResponse != null) { "Response was not captured" }
        
        assert(capturedResponse?.code == 200) {
            "Response code should be 200, but was: ${capturedResponse?.code}"
        }
        
        val responseHeaders = capturedResponse?.headers
        assert(responseHeaders?.get("Content-Type")?.contains("application/json") == true) {
            "Response Content-Type header should contain 'application/json'"
        }
    }
} 