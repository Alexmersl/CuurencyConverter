# Настройка Allure для Android тестов

## Что было сделано

1. **Обновлен `allure.properties`** - добавлена строка `allure.results.useTestStorage=true`
2. **Добавлена зависимость Allure** - `io.qameta.allure:allure-kotlin-android:2.4.0`
3. **Добавлен orchestrator** - `androidx.test:orchestrator:1.4.0`
4. **Создан Gradle task** для получения отчета с устройства
5. **Обновлены все тесты** с Allure степами

## Структура изменений

### AllureExtensions.kt
```kotlin
package com.example.myfirstapponkotlin.features

import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.Step

fun step(name: String, block: () -> Unit) {
    Allure.step(name, block)
}

fun step(name: String, block: () -> String) {
    Allure.step(name, block)
}
```

### build.gradle.kts
```kotlin
// Добавлены зависимости
androidTestImplementation("io.qameta.allure:allure-kotlin-android:2.4.0")
androidTestUtil("androidx.test:orchestrator:1.4.0")

// Добавлен task для получения отчета
tasks.register("getReport", Exec::class) {
    commandLine("adb", "pull", "/sdcard/googletest/test_outputfiles/allure-results", "${project.rootDir}/allure-results")
}
```

### allure.properties
```
allure.results.useTestStorage=true
```

## Обновленные тесты

Все тесты теперь используют Allure степы:

- **EspressoTests.kt** - UI тесты с Espresso
- **KaspressoTests.kt** - тесты с Kaspresso
- **MockWebServerTest.kt** - тесты с MockWebServer
- **MockServerTest.kt** - интеграционные тесты с мок-сервером
- **RequestInterceptorTest.kt** - тесты перехватчиков запросов
- **MockitoTests.kt** - простые тесты с Mockito
- **MockActivityTest.kt** - тесты активности

## Запуск тестов и генерация отчета

### 1. Запуск тестов
```bash
./gradlew connectedAndroidTest
```

### 2. Получение отчета с устройства
```bash
./gradlew getReport
```

### 3. Просмотр отчета
```bash
allure serve allure-results
```

## Пример использования степов в тестах

```kotlin
@Test
fun smokeTestGetResult() {
    step("Запускаем главную активность") {
        ActivityScenario.launch(MainActivity::class.java)
    }
    
    step("Вводим сумму для конвертации: 500") {
        MainWindowPage.enterAmount("500")
    }
    
    step("Выбираем валюту 'из': USD") {
        MainWindowPage.selectCurrency(getAmountFrom(), "USD")
    }
    
    step("Выбираем валюту 'в': USD") {
        MainWindowPage.selectCurrency(getAmountTo(), "USD")
    }
    
    step("Нажимаем кнопку конвертации") {
        MainWindowPage.convertBtnClick()
    }
}
```

## Требования

- Android Studio
- Gradle
- ADB (Android Debug Bridge)
- Allure Command Line Tool

## Установка Allure CLI

### macOS
```bash
brew install allure
```

### Windows
```bash
scoop install allure
```

### Linux
```bash
sudo apt-add-repository ppa:qameta/allure
sudo apt-get update
sudo apt-get install allure
```

## Примечания

1. Отчеты сохраняются в `/sdcard/googletest/test_outputfiles/allure-results` на устройстве
2. Эмулятор должен быть запущен при выполнении `getReport` task
3. Все степы отображаются в отчете Allure с русскими названиями
4. При ошибках в тестах, степы помогают понять, на каком этапе произошла проблема 