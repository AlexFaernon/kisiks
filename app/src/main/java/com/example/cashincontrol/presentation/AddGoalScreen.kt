@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.example.cashincontrol.presentation

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.goals.Goal
import java.time.LocalDate

data class DataTargetForm(
    val goalName: String,
    val goalSum: Float,
    val goalDate: LocalDate,
    val monthlyPayment: Float,
    val monthlyPaymentPurpose: Float,
    val isNotifications:Boolean,
)

@Composable
fun AddGoalScreen(navController: NavController) {
    val goalName = remember { mutableStateOf("") }
    val goalSum = remember { mutableStateOf("") }
    val goalDate = remember { mutableStateOf(LocalDate.now()) }
    val monthlyPayment = remember { mutableStateOf("") }
    val monthlyPaymentPurpose = remember { mutableStateOf("") }
    val isNotifications = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 30.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopSection(navController)
        MainSection(
            goalName,
            goalSum,
            goalDate,
            monthlyPayment,
            monthlyPaymentPurpose,
            isNotifications,
        )

        Button(
            onClick = {
                val dataForm = DataTargetForm(
                    goalName = goalName.value,
                    goalSum = goalSum.value.toFloatOrNull() ?: 0f,
                    goalDate = goalDate.value,
                    monthlyPayment = monthlyPayment.value.toFloatOrNull() ?: 0f,
                    monthlyPaymentPurpose = monthlyPaymentPurpose.value.toFloatOrNull() ?: 0f,
                    isNotifications = isNotifications.value,
                )
                UserClass.goal = Goal(dataForm.goalName, dataForm.goalSum, dataForm.goalDate)
                monthlyPayment.value = UserClass.goal!!.monthlyPayment().toString()
                Log.d("monthly payment", UserClass.goal!!.monthlyPayment().toString())
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
            enabled = isButtonEnabled(goalDate.value, goalSum.value)
        ) {
            Text(
                text = "Рассчитать и сохранить",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }

    }
}

@Composable
private fun TopSection(navController: NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { navController.navigate("main") }) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "Назад",
                tint = Color.Unspecified
            )
        }

        TextWithBackground(
            text = "Добавление цели",
            backgroundColor = Color(0xFFDCFFBB),
            paddingValues = PaddingValues(horizontal = 20.dp, vertical = 3.dp)
        )

        Spacer(modifier = Modifier.weight(35f))
    }
}

@Composable
private fun MainSection(
    goalName: MutableState<String>,
    goalSum: MutableState<String>,
    goalDate: MutableState<LocalDate>,
    monthlyPayment: MutableState<String>,
    monthlyPaymentPurpose: MutableState<String>,
    isNotifications: MutableState<Boolean>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        TargetNameSection(goalName)
        TargetSumSection(goalSum)
        TargetDateSection(goalDate)
        MonthlyPaymentSection(monthlyPayment)
        MonthlyPaymentPurposeSection(monthlyPaymentPurpose)
        TargetSwitchSection(isNotifications)
    }
}

@Composable
private fun TargetNameSection(goalName: MutableState<String>) {
    LabeledRowWithTextField(label = "Название", textState = goalName)
}

@Composable
private fun TargetSumSection(goalSum: MutableState<String>) {
    LabeledRowWithNumTextField(label = "Сумма", textState = goalSum)
}

@Composable
private fun TargetDateSection(goalDate: MutableState<LocalDate>) {
    LabeledRow(label = "Дата") {
        DatePickerDocked(goalDate)
    }
}

@Composable
private fun MonthlyPaymentSection(monthlyPayment: MutableState<String>) {
    LabeledRowWithText(label = "Ежемесячный платеж", textState = monthlyPayment)
}

@Composable
private fun MonthlyPaymentPurposeSection(monthlyPaymentPurpose: MutableState<String>) {
    LabeledRowWithText(label = "Учитывая инфляцию", textState = monthlyPaymentPurpose)
}

@Composable
private fun TargetSwitchSection(isRegular: MutableState<Boolean>) {
    LabeledRow(label = "Подключить уведомления") {
        Switch(
            modifier = Modifier.size(width = 25.dp, height = 20.dp),
            checked = isRegular.value,
            onCheckedChange = { isRegular.value = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFBEC399),
                checkedTrackColor = Color(0xFFDCFFBB),
                checkedBorderColor = Color(0xFFBEC399)
            )
        )
    }
}

@Composable
fun LabeledRowWithText(label: String, textState: MutableState<String>) {
    LabeledRow(label = label) {
        OutlinedTextField(
            value = textState.value,
            onValueChange = { },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                focusedTextColor = Color.Black,
                disabledContainerColor = Color.Transparent,
                disabledTextColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp)),
            enabled = false
        )
    }
}

@Composable
fun LabeledRowWithNumTextField(label: String, textState: MutableState<String>) {
    LabeledRow(label = label) {
        OutlinedTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                focusedTextColor = Color.Black,
            ),

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp))
        )
    }
}

fun isButtonEnabled(goalDate: LocalDate, goalSum: String): Boolean {
    val today = LocalDate.now()
    val isDateValid = goalDate.isAfter(today.plusMonths(2))
    val isSumValid = goalSum.toFloatOrNull() != null && goalSum.toFloat() > 0

    return isDateValid && isSumValid
}