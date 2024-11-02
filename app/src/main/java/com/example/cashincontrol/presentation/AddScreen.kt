@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.cashincontrol.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.cashincontrol.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, top = 48.dp, end = 22.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopSection(navController)
        MainSection()
    }
}

@Composable
private fun TopSection(navController: NavController){
    // Заголовок с кнопкой и переключателем
    Row(modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {navController.navigate("main")}
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "Назад",
                tint = Color.Unspecified
            )
        }

        Text(
            text = "Добавление",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
                .padding(horizontal = 20.dp, vertical = 3.dp)
        )

        Spacer(modifier = Modifier.width(35.dp))
        DropdownMenu()
    }
}

@Composable
fun DropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("доход") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { expanded = true }
            .padding(vertical = 3.dp)
            .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp))
    ) {
        Text(
            text = selectedOption,
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.align(Alignment.Center)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("расход") },
                onClick = {
                    selectedOption = "расход"
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("доход") },
                onClick = {
                    selectedOption = "доход"
                    expanded = false
                }
            )
        }
    }
}

@Preview
@Composable
private fun MainSection(){
    Column(verticalArrangement =  Arrangement.spacedBy(20.dp)) {
        TransactionCategory()
        TransactionType()
        TransactionDate()
        TransactionComment()
        TransactionSwitch()
        FileUploadButton()
        MoneyInputField()
    }
}

@Composable
private fun TransactionCategory(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Категория платежа",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(start = 22.dp, end = 32.dp, top = 3.dp, bottom = 3.dp)
        )
        DropdownMenu()
    }
}

@Composable
private fun TransactionType()
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(52.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Вид платежа",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(start = 22.dp, end = 94.dp, top = 3.dp, bottom = 3.dp)
        )

        val message = remember{mutableStateOf("")}
        OutlinedTextField(
            message.value,
            {message.value = it},
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                focusedTextColor = Color.Black,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp))
        )
   }
}

@Composable
private fun TransactionDate(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(52.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Дата",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(start = 22.dp, end = 170.dp, top = 3.dp, bottom = 3.dp)
        )

        DatePickerDocked()
    }
}

@Composable
private fun TransactionComment()
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(52.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Комментарий",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(start = 22.dp, end = 94.dp, top = 3.dp, bottom = 3.dp)
        )

        val message = remember{mutableStateOf("")}
        OutlinedTextField(
            message.value,
            {message.value = it},
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                focusedTextColor = Color.Black,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp))
        )
    }
}

@Composable
private fun TransactionSwitch()
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(52.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Сделать платеж регулярным",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(start = 22.dp, end = 45.dp, top = 3.dp, bottom = 3.dp)
        )

        var checked by remember { mutableStateOf(true) }

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}

@Composable
fun FileUploadButton() {
    Button(
        onClick = { println("ONCLICK")/* TODO: логика загрузки */ },
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp))
            .padding(vertical = 3.dp, horizontal = 10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Text(
            text = "Загрузить выписку в формате PDF",
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )
    }
}

@Composable
fun MoneyInputField() {
    val message = remember{mutableStateOf("")}
    OutlinedTextField(
        message.value,
        {message.value = it},
        textStyle = TextStyle(fontSize = 48.sp),
        trailingIcon = {
            Text(
                text = "₽",
                color = Color.Black,
                fontSize = 48.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xFFBDBDBD),
            focusedBorderColor = Color(0xFFBDBDBD),
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}


@Composable
fun DatePickerDocked() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp))
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}