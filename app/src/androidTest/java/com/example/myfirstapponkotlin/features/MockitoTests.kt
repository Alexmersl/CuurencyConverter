package com.example.myfirstapponkotlin.features

import org.junit.Test
import io.qameta.allure.kotlin.Allure

class MockitoTests : BaseTest() {

    @Test
    fun simpleMockitoTest() {
        Allure.step("Выполняем простую проверку") {
            assert(true) { "This test should always pass" }
        }
        
        Allure.step("Проверяем базовую логику") {
            val result = 2 + 2
            assert(result == 4) { "2 + 2 should equal 4" }
        }
    }
}