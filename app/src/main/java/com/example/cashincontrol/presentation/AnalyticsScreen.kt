package com.example.cashincontrol.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.GroupBarChart
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeTransaction
import com.example.cashincontrol.domain.transaction.Transaction
import java.time.LocalDate
import kotlin.random.Random

@Composable
fun AnalyticsScreen() {
    if (UserClass.getExpensesTransactions().isEmpty() || UserClass.getIncomeTransactions().isEmpty()) {
        Text(
            modifier = Modifier.fillMaxSize().wrapContentHeight(),
            text = "Недостаточно данных",
            fontSize = 32.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 30.dp, end = 10.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(10.dp)
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
            Text(
                text = "Сравнение доходов и расходов",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            MainBarChart(UserClass.transactions)
            BottomCard("Достижение \"Финансовый гений\"", "На протяжении 10 месяцев у Вас получается сохранять более 10% зарплаты!", R.drawable.icon_achiv)
            BottomCard("Подсказка", "Отличная идея - сохранять небольшую часть дохода (5%-10%) в пользу финансовой подушки безопасности.", R.drawable.icon_hint)
        }
    }
}

@Composable
fun BottomCard(
    title: String,
    description: String,
    icon: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun MainBarChart(transactions: List<Transaction>) {
    val months = 12
    val barChartData = prepareGroupBarChartData(transactions, months)

    val maxYValue = barChartData
        .flatMap { it.barList }
        .maxOfOrNull { it.point.y }
        ?.let { adaptiveRoundUp(it) } ?: 0f

    val scaledBarChartData = barChartData.map { groupBar ->
        groupBar.copy(
            barList = groupBar.barList.map { barData ->
                barData.copy(
                    point = barData.point.copy(
                        y = (barData.point.y / maxYValue) * maxYValue
                    )
                )
            }
        )
    }

    val firstMonthWithData = barChartData.firstOrNull()?.label?.let { monthNameToNumber(it) } ?: 1
    val xAxisData = AxisData.Builder()
        .axisStepSize(20.dp)
        .steps(barChartData.size - 1)
        .bottomPadding(20.dp)
        .labelData { index ->
            monthName(firstMonthWithData + index)
        }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelAndAxisLinePadding(10.dp)
        .axisOffset(50.dp)
        .labelData { index ->
            (index * (maxYValue / 5)).toInt().toString()
        }
        .build()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        GroupBarChart(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(),
            groupBarChartData = GroupBarChartData(
                barPlotData = BarPlotData(
                    groupBarList = scaledBarChartData,
                    barColorPaletteList = listOf(
                        Color(0xFFffbb73),
                        Color(0xFFbb73ff)
                    ),
                ),
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                horizontalExtraSpace = 15.dp,
                paddingEnd = 0.dp
            )
        )

        MainLegendItem()
    }
}

fun adaptiveRoundUp(value: Float): Float {
    return when {
        value <= 100 -> roundUpToNearest(value, 10)
        value <= 1000 -> roundUpToNearest(value, 100)
        else -> roundUpToNearest(value, 1000)
    }
}

fun adaptiveRoundDown(value: Float): Float {
    return when {
        value >= -100 -> roundDownToNearest(value, 10)
        value >= -1000 -> roundDownToNearest(value, 100)
        else -> roundDownToNearest(value, 1000)
    }
}

fun roundUpToNearest(value: Float, nearest: Int): Float {
    return ((value + nearest - 1) / nearest).toInt() * nearest.toFloat()
}

fun roundDownToNearest(value: Float, nearest: Int): Float {
    return ((value - nearest + 1) / nearest).toInt() * nearest.toFloat()
}


@Composable
private fun MainLegendItem() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(Color(0xFFffbb73), shape = RoundedCornerShape(4.dp))
        )
        Text(
            text = "Доходы",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .size(16.dp)
                .background(Color(0xFFbb73ff), shape = RoundedCornerShape(4.dp))
        )
        Text(
            text = "Расходы",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black
        )
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
        strokeWidth = 30f,
        activeSliceAlpha = .9f,
        isAnimationEnable = true
    )
    Row(
    ) {
        DonutPieChart(
            modifier = Modifier
                .width(170.dp)
                .height(170.dp),
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
                PieLegendItem(slice.label, slice.color)
            }
        }
    }
}

@Composable
fun PieLegendItem(label: String, color: Color) {
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

fun prepareGroupBarChartData(transactions: List<Transaction>, months: Int = 12): List<GroupBar> {
    val currentYear = LocalDate.now().year
    val filteredTransactions = transactions.filter { it.date.year == currentYear }
    val groupedTransactions = filteredTransactions.groupBy { it.date.monthValue }

    val firstMonthWithData = groupedTransactions.keys.minOrNull() ?: 1

    return (firstMonthWithData..months).map { month ->
        val income = groupedTransactions[month]?.filterIsInstance<IncomeTransaction>()?.map { it.sum }?.sum() ?: 0.0
        val expense = groupedTransactions[month]?.filterIsInstance<ExpensesTransaction>()?.map { it.sum }?.sum() ?: 0.0

        GroupBar(
            label = monthName(month),
            barList = listOf(
                BarData(
                    point = Point(month.toFloat(), income.toFloat()),
                    color = Color(0xFFFF9800),
                    label = "Доходы"
                ),
                BarData(
                    point = Point(month.toFloat(), expense.toFloat()),
                    color = Color(0xFF9C27B0),
                    label = "Расходы"
                )
            )
        )
    }
}

fun monthName(month: Int): String {
    val monthNames = listOf("янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек")
    return monthNames[month - 1]
}

fun monthNameToNumber(monthName: String): Int {
    val months = listOf(
        "янв", "фев", "мар", "апр", "май", "июн",
        "июл", "авг", "сен", "окт", "ноя", "дек"
    )
    return months.indexOf(monthName) + 1
}

