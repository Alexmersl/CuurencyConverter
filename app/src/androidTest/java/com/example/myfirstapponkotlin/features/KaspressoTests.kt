package com.example.myfirstapponkotlin.features


import com.example.myfirstapponkotlin.pages.KasMainPage
import org.junit.Test

class KaspressoTests : BaseTest() {


    @Test
    fun smokeTestGetResult() = run {
    step("Find amount From btn"){
        KasMainPage{
            amountFrom {
                isVisible()
                click()
                typeText("GEL")
                Thread.sleep(10000)
            }
        }
    }
    }
}