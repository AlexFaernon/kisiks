package com.example.cashincontrol.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.notifications.showNotification
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.Transaction

@Composable
fun MainScreen(navController: NavController){
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(23.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            TopSection()
            if(UserClass.transactions.isEmpty()){
                Text(
                    modifier = Modifier.fillMaxSize().wrapContentHeight().align(Alignment.CenterHorizontally),
                    text = "Нет данных, добавьте платежи",
                    fontSize = 26.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
            else {
                TransactionListScreen(UserClass.transactions)
            }
        }

        IconButton(
            onClick = {
                navController.navigate("add")
                showNotification(
                    context = context,
                    notificationId = 1,
                    title = "Пример уведомления",
                    message = "Это тестовое уведомление"
                )},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 88.dp)
                .size(60.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.action_button),
                contentDescription = "Добавить",
                tint = Color.Unspecified,)
        }
    }
}

@Composable
private fun TopSection() {
    val daysCount = remember { UserClass.getDaysSinceStart() }
    val dayWord = getDayWord(daysCount)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_top_mian),
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "$daysCount $dayWord Вы ведёте бюджет!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(43.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Баланс счёта",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Text(
                text = "${formatToTwoDecimals(UserClass.currentMoney.toString())} ₽",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // История операций

        Text(
            text = "История операций",
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier
                .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    val monthNames = listOf(
        "Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
        "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            // Дата транзакции
            Text(
                text = transaction.date.dayOfMonth.toString() + " " + monthNames[transaction.date.monthValue - 1],
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Row(
                verticalAlignment = Alignment.Top
            ) {
                // Иконка категории
                Icon(
                    painter = painterResource(id = transaction.category.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp),
                    tint = Color.Unspecified
                )

                Spacer(modifier = Modifier.width(9.dp))

                // Текстовая информация о транзакции
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = transaction.category.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

//                    Text(
//                        text = transaction.card,
//                        fontSize = 16.sp,
//                        color = Color.Gray
//                    )

                    Text(
                        text = transaction.commentary,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                // Сумма транзакции
                val sum = transaction.sum
                Text(
                    text = "$sum₽",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun TransactionListScreen(transactions: List<Transaction>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 23.dp, end = 23.dp, bottom = 150.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        itemsIndexed(transactions){
            index, item ->
            TransactionCard(item)
        }
    }
}

private fun getDayWord(days: Long): String {
    val lastTwoDigits = (days % 100).toInt()
    val lastDigit = (days % 10).toInt()

    return when {
        lastTwoDigits in 11..19 -> "дней"
        lastDigit == 1 -> "день"
        lastDigit in 2..4 -> "дня"
        else -> "дней"
    }
}
