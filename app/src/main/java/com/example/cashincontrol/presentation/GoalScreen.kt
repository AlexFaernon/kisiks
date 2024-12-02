package com.example.cashincontrol.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.goals.Goal
import java.time.LocalDate
import java.time.Period

@Composable
fun GoalScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 30.dp, end = 10.dp)
            .background(Color.White),
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
    val goal = UserClass.goal

    val pointsData = toChartPoints(goal)

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .steps(pointsData.size - 1)
        .labelData { index ->
            val currentMonth = goal!!.startDate.plusMonths(index.toLong())
            "${monthName(currentMonth.monthValue)}-${currentMonth.year.toString().takeLast(2)}"
        }
        .labelAndAxisLinePadding(15.dp)
        .shouldDrawAxisLineTillEnd(true)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelAndAxisLinePadding(10.dp)
        .axisOffset(50.dp)
        .labelData { i -> (i * goal!!.sum / 5).toInt().toString() }
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = Color(0xFFffbb73),
                        lineType = LineType.Straight()
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
        backgroundColor = Color.White
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = lineChartData
        )

        GoalLegendItem()
    }
}

@Composable
private fun GoalLegendItem() {
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
            text = "Цель",
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
            text = "Пополнение",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black
        )
    }
}

fun toChartPoints(goal: Goal?): List<Point> {
    val months = Period.between(goal!!.startDate, goal.targetDate).toTotalMonths()
    val monthlyPayment = goal.monthlyPayment()

    return (0..months).map { month ->
        Point(
            x = month.toFloat(),
            y = (monthlyPayment * month)
        )
    }
}