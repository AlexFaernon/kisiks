package com.example.cashincontrol.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.cashincontrol.domain.UserClass
import kotlin.random.Random

@Preview()
@Composable
fun AnalyticsScreen() {
    if (UserClass.transactions.isEmpty()) {
        Text(
            modifier = Modifier.fillMaxSize().wrapContentHeight(),
            text = "Нет данных",
            fontSize = 32.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 48.dp, end = 10.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Расходы по категориям",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            ExpensesDonutChart()
        }
    }
}

@Composable
private fun ExpensesDonutChart() {
    val donutChartData = createDonutChartData()
    val donutChartConfig = PieChartConfig(
        isSumVisible = true,
        labelColor = Color.Black,
        labelVisible = true,
        sliceLabelTextSize = 24.sp,
        strokeWidth = 40f,
        activeSliceAlpha = .9f,
        isAnimationEnable = true
    )

    Row(
    ) {
        DonutPieChart(
            modifier = Modifier
                .width(180.dp)
                .height(180.dp),
            donutChartData,
            donutChartConfig
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 170.dp)
                .padding(start = 16.dp)
                .align(alignment = Alignment.CenterVertically),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(donutChartData.slices) { slice ->
                LegendItem(slice.label, slice.color)
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, shape = CircleShape)
        )
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black
        )
    }
}

fun getRandomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}

@SuppressLint("DefaultLocale")
fun createDonutChartData(): PieChartData {
    val categoryColors = mutableMapOf<String, Color>()
    val expenseData = UserClass.getExpensesTransactions()
        .groupBy { it.category.name }
        .map { (categoryName, transactions) ->
//            val totalSum = (String.format("%.2f", transactions.map { it.sum }.sum())).toFloat()
            val totalSum = transactions.map { it.sum }.sum()
            val color = categoryColors.getOrPut(categoryName) { getRandomColor() }
            PieChartData.Slice(categoryName, totalSum, color)
        }

    return PieChartData(
        slices = expenseData,
        plotType = PlotType.Donut
    )
}

