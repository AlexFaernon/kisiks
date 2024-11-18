@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.example.cashincontrol.presentation

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.bankParcing.BankParser
import com.example.cashincontrol.domain.transaction.Category
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.IncomeCategory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

data class DataForm(
    val transactionType: String,
    val transactionCategory: Category,
//    val transactionKind: String,
    val transactionDate: LocalDate,
    val transactionComment: String,
    val isRegular:Boolean,
    val moneyAmount: Float,
)



@Composable
fun AddScreen(navController: NavController) {
    val transactionType = remember { mutableStateOf("Доход") }
    val transactionCategory = remember { mutableStateOf(UserClass.GetIncomeCategory().first() as Category) }
//    val transactionKind = remember { mutableStateOf("") }
    val transactionDate = remember { mutableStateOf(LocalDate.now()) }
    val transactionComment = remember { mutableStateOf("") }
    val isRegular = remember { mutableStateOf(true) }
    val moneyAmount = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 48.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopSection(navController, transactionType, transactionCategory, transactionComment, moneyAmount)
        MainSection(
            navController,
            transactionCategory,
            transactionDate,
            transactionComment,
            isRegular,
            moneyAmount,
            transactionType.value
        )

        Button(
            onClick = {
                val dataForm = DataForm(
                    transactionType = transactionType.value,
                    transactionCategory = transactionCategory.value,
//                    transactionKind = transactionKind.value,
                    transactionDate = transactionDate.value,
                    transactionComment = transactionComment.value,
                    isRegular = isRegular.value,
                    moneyAmount = moneyAmount.value.toFloatOrNull() ?: 0f
                )
                Log.d("СОХРАНИТЬ", "DataForm: $dataForm")
                val isExpenses = dataForm.transactionType != "Доход"
                UserClass.addTransaction(isExpenses, dataForm.moneyAmount, dataForm.transactionDate.atStartOfDay(), dataForm.transactionCategory, dataForm.transactionComment)
                navController.navigate("main")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFBEC399)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(3.dp)),
            shape = RoundedCornerShape(3.dp),
            contentPadding = PaddingValues(12.dp),
        ) {
            Text(
                text = "Сохранить",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun TopSection(navController: NavController,
                       transactionType: MutableState<String>,
                       transactionCategory: MutableState<Category>,
                       transactionComment: MutableState<String>,
                       moneyAmount: MutableState<String>) {
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
        TransactionDropdownMenu(transactionType,transactionCategory,transactionComment,moneyAmount)
    }
}

@Composable
private fun MainSection(
    navController: NavController,
    transactionCategory: MutableState<Category>,
//    transactionKind: MutableState<String>,
    transactionDate: MutableState<LocalDate>,
    transactionComment: MutableState<String>,
    isRegular: MutableState<Boolean>,
    moneyAmount: MutableState<String>,
    transactionType: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        TransactionCategorySection(transactionCategory, transactionType)
//        TransactionKindSection(transactionKind)
        TransactionDateSection(transactionDate)
        TransactionCommentSection(transactionComment)
        TransactionSwitchSection(isRegular)
        FileUploadButton(navController)
        MoneyInputField(moneyAmount)
    }
}

@Composable
private fun TransactionCategorySection(transactionCategory: MutableState<Category>, transactionType: String) {
    LabeledRow(label = "Категория платежа") {
        CategoryDropdownMenu(transactionCategory, transactionType)
    }
}

@Composable
private fun TransactionKindSection(transactionKind: MutableState<String>) {
    LabeledRowWithTextField(label = "Вид платежа", textState = transactionKind)
}

@Composable
private fun TransactionDateSection(transactionDate: MutableState<LocalDate>) {
    LabeledRow(label = "Дата") {
        DatePickerDocked(transactionDate)
    }
}

@Composable
private fun TransactionCommentSection(transactionComment: MutableState<String>) {
    LabeledRowWithTextField(label = "Комментарий", textState = transactionComment)
}

@Composable
private fun TransactionSwitchSection(isRegular: MutableState<Boolean>) {
    LabeledRow(label = "Сделать платеж регулярным") {
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
fun LabeledRowWithTextField(label: String, textState: MutableState<String>) {
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
            .padding(start = 22.dp, end = 28.dp, top = 3.dp, bottom = 3.dp)
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
fun TransactionDropdownMenu(transactionType: MutableState<String>,
                            transactionCategory: MutableState<Category>,
                            transactionComment: MutableState<String>,
                            moneyAmount: MutableState<String>) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clickable { expanded = true }
            .fillMaxSize()
            .padding(start = 35.dp)
            .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp)),
    ) {
        Text(
            text = transactionType.value,
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.Center)

        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Расход") },
                onClick = {
                    transactionType.value = "Расход"
                    expanded = false
                    resetFormFields(transactionCategory, transactionComment, moneyAmount, true)
                }
            )
            DropdownMenuItem(
                text = { Text("Доход") },
                onClick = {
                    transactionType.value = "Доход"
                    expanded = false
                    resetFormFields(transactionCategory, transactionComment, moneyAmount, false)
                }
            )
        }
    }
}

fun resetFormFields(
    transactionCategory: MutableState<Category>,
    transactionComment: MutableState<String>,
    moneyAmount: MutableState<String>,
    isExpenses: Boolean
) {
    transactionCategory.value = if (isExpenses) UserClass.getExpensesCategory().first() else UserClass.GetIncomeCategory().first()
    transactionComment.value = ""
    moneyAmount.value = ""
}

@Composable
fun CategoryDropdownMenu(transactionCategory: MutableState<Category>, transactionType: String) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var categoryList = if (transactionType == "Доход") UserClass.GetIncomeCategory() else UserClass.getExpensesCategory()

    Box(
        modifier = Modifier
            .clickable { expanded = true }
            .fillMaxSize()
            .padding(vertical = 3.dp)
            .border(1.dp, Color(0xFFBDBDBD), shape = RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = transactionCategory.value.name,
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 5.dp).align(Alignment.Center)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoryList.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        transactionCategory.value = category
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
        AddCategoryDialog(onDismiss = { showDialog = false }, transactionType)
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun AddCategoryDialog(onDismiss: () -> Unit, transactionType: String) {
    var newCategory = remember {
        mutableStateOf(if (transactionType == "Доход") IncomeCategory("") else ExpensesCategory(""))
    }

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
                    value = newCategory.value.name,
                    onValueChange = {newCategory.value = if (transactionType == "Доход") IncomeCategory(it) else ExpensesCategory(it)},
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
                            if (newCategory.value.name.isNotBlank()) {
                                UserClass.categories.add(newCategory.value)
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
fun FileUploadButton(navController: NavController) {
    val pdfUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        pdfUri.value = uri
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            inputStream?.use { _ ->
                BankParser.parse(inputStream)
                navController.navigate("main")
            }
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
fun MoneyInputField(moneyAmount: MutableState<String>) {
    OutlinedTextField(
        value = moneyAmount.value,
        onValueChange = { moneyAmount.value = it },
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
fun DatePickerDocked(transactionDate: MutableState<LocalDate>) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            transactionDate.value = Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            showDatePicker = false
        }
    }

    val selectedDate = transactionDate.value.toString()

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