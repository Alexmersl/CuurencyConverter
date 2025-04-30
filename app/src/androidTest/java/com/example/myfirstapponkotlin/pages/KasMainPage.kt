package com.example.myfirstapponkotlin.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.myfirstapponkotlin.MainActivity
import com.example.myfirstapponkotlin.R
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText

object KasMainPage : KScreen<KasMainPage>() {
    override val layoutId: Int = R.layout.activity_main
    override val viewClass: Class<*> = MainActivity::class.java

    val amountFrom = KEditText{withId(R.id.autoCompleteTextView2)}

    private val amountTo: ViewInteraction = onView(withId(R.id.autoCompleteTextView1))
    private val convertBtn: ViewInteraction
        get() = onView(withId(R.id.button))

    private val amountInput: ViewInteraction
        get() = onView(withId(R.id.editTextNumber3))

}