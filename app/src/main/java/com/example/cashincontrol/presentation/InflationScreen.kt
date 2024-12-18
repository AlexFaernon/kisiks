package com.example.cashincontrol.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.goals.Inflation
import com.example.cashincontrol.domain.transaction.CheckCategory
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import java.time.Month

@Composable
fun InflationScreen(
    navController: NavController,
    isBottomBarVisible: (Boolean) -> Unit,
    paddingValues: PaddingValues
) {
    if (!UserClass.isOnboardingCompleted) {
        isBottomBarVisible(false)

        OnboardingScreens(
            navController = navController,
            onComplete = {
                UserClass.isOnboardingCompleted = true
                isBottomBarVisible(true)
                navController.navigate("inflation") {
                    popUpTo("inflation") { inclusive = true }
                }
            },
            isBottomBarVisible
        )
    } else {
        isBottomBarVisible(true)
        if (UserClass.transactions.isEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .padding(paddingValues),
                text = "Недостаточно данных",
                fontSize = 32.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        } else {
            Inflation.updateInflation()
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(start = 10.dp, top = 30.dp, end = 10.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MainInflation()
                DynamicInflation()
                CategoryInflation(Inflation.CategoryInflation)
                CheckInflation(Inflation.CheckInflation)
            }
        }
    }
}

@Composable
fun MainInflation(){
    Text(
        text = "Личная инфляция",
        fontSize = 20.sp,
        color = Color.Black,
        modifier = Modifier
            .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (Inflation.YearInflation.isNotEmpty()) {
            Column() {
                Text(
                    text = String.format("%.1f %%", Inflation.GlobalInflation),
                    fontSize = 48.sp,
                    color = Color.Black,
                )
                Text(
                    text = "текущий уровень",
                    fontSize = 14.sp,
                    color = Color.Gray,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            val iconRes =
                if (Inflation.GlobalInflation < 0) R.drawable.icon_down else R.drawable.icon_up
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
        }
        else {
            Text(
                text = "Недостаточно данных",
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun DynamicInflation(){
    Text(
        text = "Динамика",
        fontSize = 20.sp,
        color = Color.Black,
        modifier = Modifier
            .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconRes = if (Inflation.GlobalInflation < 0) R.drawable.icon_down_exclaim else R.drawable.screamer
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(height = 40.dp, width = 10.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(5.dp))

        val textRes = if (Inflation.GlobalInflation < 0) "ниже" else "выше"

        Text(
            text = "Ваш уровень личной инфляции $textRes среднего",
            fontSize = 13.sp,
            color = Color.Gray,
        )
    }
    InflationChart()
}

@Composable
fun CategoryInflation(inflation: Map<ExpensesCategory, Float>){
    val isExpanded = remember { mutableStateOf(true) }

    Column {
        Text(
            text = "Категории",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clickable { isExpanded.value = !isExpanded.value }
        )

        if (isExpanded.value) {
            CategoriesInflationGrid(inflation)
        }
    }
}

@Composable
fun CheckInflation(inflation: Map<CheckCategory, Float>){
    val isExpanded = remember { mutableStateOf(true) }

    Column {
        Text(
            text = "Товары и услуги",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clickable { isExpanded.value = !isExpanded.value }
        )

        if (isExpanded.value) {
            CheckCategoriesInflationGrid(inflation)
        }
    }
}


@Composable
fun CheckCategoriesInflationGrid(inflation:  Map<CheckCategory, Float>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 500.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(inflation.entries.toList().size) { index ->
            CheckCategoryInflationCard(inflation.entries.toList()[index])
        }
    }
}

@Composable
fun CategoriesInflationGrid(inflation:  Map<ExpensesCategory, Float>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 500.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(inflation.entries.toList().size) { index ->
            CategoryInflationCard(inflation.entries.toList()[index])
        }
    }
}

@Composable
fun CheckCategoryInflationCard(inflation: Map. Entry<CheckCategory, Float>){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%.1f %%", inflation.value),
                fontSize = 24.sp,
                color = Color.Black,
            )
            Text(
                text = inflation.key.name.limit(15),
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        val iconRes = if (inflation.value < 0) R.drawable.icon_down else R.drawable.icon_up

        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
        )
    }
}


@Composable
fun CategoryInflationCard(inflation: Map. Entry<ExpensesCategory, Float>){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%.1f %%", inflation.value),
                fontSize = 24.sp,
                color = Color.Black,
            )
            Text(
                text = inflation.key.name.limit(15),
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        val iconRes = if (inflation.value < 0) R.drawable.icon_down else R.drawable.icon_up

        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
        )
    }
}

@Composable
private fun InflationChart() {
    val data = Inflation.YearInflation
    val reversedData = data.entries.reversed()

    val block = listOf(
        Point(x = 1f, 7.27f),
        Point(x = 2f, 7.5f),
        Point(x = 3f, 7.52f),
        Point(x = 4f, 7.82f),
        Point(x = 5f, 7.70f),
        Point(x = 6f, 7.86f),
        Point(x = 7f, 8.49f),
        Point(x = 8f, 9.09f),
        Point(x = 9f, 8.71f),
        Point(x = 10f, 8.41f),
        Point(x = 11f, 8.52f),
        Point(x = 12f, 8.62f)
    )

    val firstMonth = reversedData.firstOrNull()?.key ?: Month.JANUARY
    val pointsData = getPoints(reversedData).sortedBy { it.x }
    val monthLabels = getOrderedMonths(firstMonth.value)

    val combinedYValues = if (pointsData.isEmpty()) {
        block.map { it.y }
    } else {
        block.map { it.y } + pointsData.map { it.y }
    }

    val maxY = combinedYValues.maxOrNull()?.let { adaptiveRoundUp(it) } ?: 0f
    val minY = combinedYValues.minOrNull()?.let { adaptiveRoundDown(it) } ?: 0f
    val step = (maxY - minY) / 5

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .steps(block.size - 1)
        .labelData { index -> monthLabels[index % monthLabels.size] }
        .labelAndAxisLinePadding(15.dp)
        .shouldDrawAxisLineTillEnd(true)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(5)
        .axisOffset(10.dp)
        .labelData { i ->
            val value = minY + step * i
            String.format("%.2f", value)
        }
        .build()

    val lines = mutableListOf<Line>()

    if (pointsData.isNotEmpty()) {
        lines.add(
            Line(
                dataPoints = pointsData,
                LineStyle(color = Color(0xFFbb73ff)),
                IntersectionPoint(color = Color(0xFFbb73ff)),
                SelectionHighlightPoint(),
                ShadowUnderLine(color = Color(0xFFbb73ff)),
                SelectionHighlightPopUp()
            )
        )
    }

    lines.add(
        Line(
            dataPoints = block,
            LineStyle(color = Color(0xFFffbb73)),
            IntersectionPoint(color = Color(0xFFffbb73)),
            SelectionHighlightPoint(),
            ShadowUnderLine(color = Color(0xFFffbb73)),
            SelectionHighlightPopUp()
        )
    )

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(lines = lines),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )

    Column(horizontalAlignment = Alignment.Start) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = lineChartData
        )
        InflationLegendItem()
    }
}



@Composable
private fun InflationLegendItem() {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(start = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Color(0xFFbb73ff), shape = RoundedCornerShape(4.dp))
            )
            Text(
                text = "Личная инфляция",
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Color(0xFFffbb73), shape = RoundedCornerShape(4.dp))
            )
            Text(
                text = "Инфляция (по данным Росстата)",
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black
            )
        }
    }
}


private fun getOrderedMonths(startMonth: Int) : List<String>{
    val monthNames = listOf("янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек")
    return monthNames.drop(startMonth -1) + monthNames.take(startMonth-1)
}

private fun getPoints(data: List<Map.Entry<Month, Float>>): List<Point>{
    return data
        .map { (month, inflation) ->
        Point(x = month.value.toFloat(), inflation)
            //todo убрать деление на 30
    }
}

private fun mapYToScreen(y: Float, minY: Float, maxY: Float, height: Float): Float {
    return height * (1 - (y - minY) / (maxY - minY))
}

fun String.limit(maxLength: Int): String {
    return if (this.length > maxLength) this.take(maxLength) + "..." else this
}