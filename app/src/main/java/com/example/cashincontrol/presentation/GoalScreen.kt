package com.example.cashincontrol.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
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
            RefillGoal(navController)
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
    val goalName = remember { mutableStateOf(UserClass.goal!!.goal) }
    val goalSum = remember { mutableStateOf(UserClass.goal!!.sum.toString()) }
    val goalDate = remember { mutableStateOf(UserClass.goal!!.targetDate.toString()) }
    LabeledRowWithText(label = "Название", textState = goalName)
    LabeledRowWithText(label = "Сумма", textState = goalSum)
    LabeledRowWithText(label = "Дата", textState = goalDate)
}

@Composable
private fun GoalChart() {
    val isPayment = remember { mutableStateOf(UserClass.goal!!.getPayments().isNotEmpty()) }

    val goal = UserClass.goal
    val pointsData = toChartPoints(goal)

    val inflationSum = goal!!.getInflationPayments().sum()
    val maxSecondLineY = goal.getPayments().maxOfOrNull { it.second } ?: 0f
    val maxY = maxOf(inflationSum, maxSecondLineY)

    val secondLineData = toSecondChartPoints(goal, maxY)

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
        .labelData { i -> (i * maxY / 5).toInt().toString() }
        .build()

    val linePlotData = LinePlotData(
        lines = if (isPayment.value) {
            listOf(
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
                ),
                Line(
                    dataPoints = secondLineData,
                    LineStyle(
                        color = Color(0xFFbb73ff),
                        lineType = LineType.Straight()
                    ),
                    IntersectionPoint(
                        color = Color(0xFFbb73ff),
                    ),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(
                        color = Color(0xFFbb73ff),
                    ),
                    SelectionHighlightPopUp()
                )
            )
        } else {
            listOf(
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
            )
        }
    )

    val lineChartData = LineChartData(
        linePlotData = linePlotData,
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

@Composable
fun RefillGoal(navController: NavController){
    var showDialog by remember { mutableStateOf(false) }
    Button(
        onClick = {
            showDialog = true
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFBEC399)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(3.dp)),
        shape = RoundedCornerShape(3.dp),
        contentPadding = PaddingValues(12.dp),
    ) {
        Text(
            text = "Пополнить цель",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }

    if (showDialog) {
        RefillGoalDialog(
            onDismiss = { showDialog = false
                        navController.navigate("goal")},
        )
    }
}

@Composable
fun RefillGoalDialog(
    onDismiss: () -> Unit,
) {
    val money = remember { mutableFloatStateOf(0f) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Пополнить цель", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = money.floatValue.toString(),
                    onValueChange = {
                        money.floatValue = it.toFloat()
                    },
                    label = { Text("Сумма") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Отмена")
                    }
                    TextButton(
                        onClick = {
                            UserClass.goal!!.addPayment(LocalDate.now(), money.floatValue)
                            onDismiss()
                        }

                    ) {
                        Text("Добавить")
                    }
                }
            }
        }
    }
}



fun toChartPoints(goal: Goal?): List<Point> {
    val monthlyPayments = goal?.getInflationPayments() ?: return emptyList()

    return monthlyPayments.mapIndexed { index, payment ->
        Point(
            x = index.toFloat(),
            y = payment
        )
    }
}

fun toSecondChartPoints(goal: Goal?, maxY: Float): List<Point> {
    val payments = goal?.getPayments() ?: return emptyList()

    return payments.map { (date, payment) ->
        val monthIndex = Period.between(goal.startDate, date).toTotalMonths()

        Point(
            x = monthIndex.toFloat(),
            y = (payment / maxY) * maxY
        )
    }
}


