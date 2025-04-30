package com.example.myfirstapponkotlin.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.myfirstapponkotlin.R
import com.example.myfirstapponkotlin.data.CurrencyDatabase
import com.example.myfirstapponkotlin.data.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val repository = CurrencyRepository(
            CurrencyDatabase.getDatabase(context).currencyDao()
        )

        CoroutineScope(Dispatchers.IO).launch {
            val rates = repository.allRates.collect { rates ->
                appWidgetIds.forEach { appWidgetId ->
                    updateAppWidget(context, appWidgetManager, appWidgetId, rates)
                }
            }
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        rates: List<com.example.myfirstapponkotlin.data.CurrencyRate>
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_currency)
        
        // Update widget with latest rates
        rates.take(3).forEachIndexed { index, rate ->
            views.setTextViewText(
                when (index) {
                    0 -> R.id.widget_currency1
                    1 -> R.id.widget_currency2
                    else -> R.id.widget_currency3
                },
                "${rate.currencyCode}: ${rate.rate}"
            )
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
} 