@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.example.cashincontrol.presentation

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
private fun TopSection(navController: NavController) {
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
            text = "Добавление",
            backgroundColor = Color(0xFFDCFFBB),
            paddingValues = PaddingValues(horizontal = 20.dp, vertical = 3.dp)
        )

        Spacer(modifier = Modifier.weight(35f))
        TransactionDropdownMenu()
    }
}

@Preview
@Composable
private fun MainSection() {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        TransactionCategorySection()
        TransactionTypeSection()
        TransactionDateSection()
        TransactionCommentSection()
        TransactionSwitchSection()
        FileUploadButton()
        MoneyInputField()
    }
}

@Composable
private fun TransactionCategorySection() {
    LabeledRow(label = "Категория платежа") {
        CategoryDropdownMenu()
    }
}

@Composable
private fun TransactionTypeSection() {
    LabeledRowWithTextField(label = "Вид платежа")
}

@Composable
private fun TransactionDateSection() {
    LabeledRow(label = "Дата") {
        DatePickerDocked()
    }
}

@Composable
private fun TransactionCommentSection() {
    LabeledRowWithTextField(label = "Комментарий")
}

@Composable
private fun TransactionSwitchSection() {
    LabeledRow(label = "Сделать платеж регулярным") {
        var checked by remember { mutableStateOf(true) }
        Switch(
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}

@Composable
fun LabeledRow(label: String, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextWithPadding(label)
        content()
    }
}

@Composable
fun LabeledRowWithTextField(label: String) {
    LabeledRow(label = label) {
        val message = remember { mutableStateOf("") }
        OutlinedTextField(
            value = message.value,
            onValueChange = { message.value = it },
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
fun TextWithPadding(text: String) {
    Text(
        text = text,
        color = Color.Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier
            .padding(start = 22.dp, end = 32.dp, top = 3.dp, bottom = 3.dp)
    )
}

@Composable
fun TextWithBackground(text: String, backgroundColor: Color, paddingValues: PaddingValues) {
    Text(
        text = text,
        color = Color.Black,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(3.dp))
            .padding(paddingValues)
    )
}

@Composable
fun TransactionDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("доход") }

    Box(
        modifier = Modifier
            .clickable { expanded = true }
            .fillMaxSize()
            .padding(start = 35.dp)
            .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp)),
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

@Composable
fun CategoryDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("продукты") }
    val categories = remember { mutableStateListOf("продукты", "лекарства", "другое") }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clickable { expanded = true }
            .fillMaxSize()
            .padding(vertical = 3.dp)
            .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
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
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        selectedOption = category
                        expanded = false
                    }
                )
            }
            DropdownMenuItem(
                text = { Text("Добавить категорию") },
                onClick = {
                    expanded = false
                    showDialog = true
                }
            )
        }
    }
    if (showDialog) {
        AddCategoryDialog(categories = categories, onDismiss = { showDialog = false })
    }
}

@Composable
fun AddCategoryDialog(categories: MutableList<String>, onDismiss: () -> Unit) {
    var newCategory by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Добавить новую категорию", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newCategory,
                    onValueChange = { newCategory = it },
                    label = { Text("Название категории") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Отмена")
                    }
                    TextButton(
                        onClick = {
                            if (newCategory.isNotBlank()) {
                                categories.add(newCategory)
                                onDismiss()
                            }
                        }
                    ) {
                        Text("Добавить")
                    }
                }
            }
        }
    }
}

@Composable
fun FileUploadButton() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Log.d("FileUpload", "Selected file: $it")
        }
    }

    Button(
        onClick = { launcher.launch("application/pdf") },
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
    val message = remember { mutableStateOf("") }
    OutlinedTextField(
        value = message.value,
        onValueChange = { message.value = it },
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

    Box(modifier = Modifier.fillMaxWidth()) {
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