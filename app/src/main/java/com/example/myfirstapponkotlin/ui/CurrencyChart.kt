package com.example.myfirstapponkotlin.ui

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CurrencyChart(
    historicalRates: List<Pair<Date, Double>>,
    modifier: Modifier = Modifier
) {
    if (historicalRates.isEmpty()) return

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
                setDrawGridBackground(false)
                setMaxVisibleValueCount(60)
                setDrawBorders(false)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    valueFormatter = object : ValueFormatter() {
                        private val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
                        override fun getFormattedValue(value: Float): String {
                            return dateFormat.format(Date(value.toLong()))
                        }
                    }
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    setDrawAxisLine(true)
                    setDrawLabels(true)
                }

                axisRight.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setDrawLabels(false)
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        update = { chart ->
            val entries = historicalRates.mapIndexed { index, (date, rate) ->
                Entry(date.time.toFloat(), rate.toFloat())
            }

            val dataSet = LineDataSet(entries, "Exchange Rate").apply {
                color = Color.BLUE
                setCircleColor(Color.BLUE)
                lineWidth = 2f
                circleRadius = 4f
                setDrawCircleHole(false)
                valueTextSize = 9f
                setDrawFilled(true)
                fillColor = Color.BLUE
                fillAlpha = 50
            }

            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
} 