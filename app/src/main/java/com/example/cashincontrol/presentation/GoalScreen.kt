package com.example.cashincontrol.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import java.time.LocalDate

@Composable
fun GoalScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 48.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextWithBackground(
            text = "Финансовые цели",
            backgroundColor = Color(0xFFDCFFBB),
            paddingValues = PaddingValues(horizontal = 20.dp, vertical = 3.dp)
        )

        if (UserClass.goal != null) {
            GoalInfo()
            GoalChart()
        }
        else {
            IconButton(
                onClick = { navController.navigate("addGoal") },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(end = 20.dp, bottom = 88.dp)
                    .size(60.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.action_button),
                    contentDescription = "Добавить",
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

@Composable
private fun GoalInfo(){
    val goalName = remember { mutableStateOf("") }
    val goalSum = remember { mutableStateOf(UserClass.goal!!.sum.toString()) }
    val goalDate = remember { mutableStateOf(UserClass.goal!!.targetDate.toString()) }
    LabeledRowWithText(label = "Сумма", textState = goalSum)
    LabeledRowWithText(label = "Дата", textState = goalDate)
}

@Preview
@Composable
private fun GoalChart() {
    val pointsData: List<Point> =
        listOf(
            Point(1.0f, 1.0f),
            Point(2.0f, 1.4f),
            Point(3.0f, 1.8f),
            Point(4.0f, 2.2f),
            Point(5.0f, 2.6f),
            Point(6.0f, 3.0f),
            Point(7.0f, 3.4f),
            Point(8.0f, 3.8f),
            Point(9.0f, 4.2f),
            Point(10.0f, 4.6f),
            Point(11.0f, 5.0f),
            Point(12.0f, 5.0f)
        )

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .steps(pointsData.size - 1)
        .labelData { index -> monthName(index + 1) }
        .labelAndAxisLinePadding(15.dp)
        .shouldDrawAxisLineTillEnd(true)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelAndAxisLinePadding(10.dp)
        .axisOffset(50.dp)
        .labelData { i -> i.toString() }
        .build()


    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = Color(0xFFffbb73),
                        ),
                    IntersectionPoint(
                        color = Color(0xFFffbb73),
                    ),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(
                        color = Color(0xFFffbb73),
                    ),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )
}