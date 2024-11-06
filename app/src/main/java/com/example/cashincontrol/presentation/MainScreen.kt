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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeCategory
import com.example.cashincontrol.domain.transaction.IncomeTransaction
import com.example.cashincontrol.domain.transaction.Transaction
import java.time.LocalDate


//val cat = ExpensesCategory("Продукты", R.drawable.icon_shop)
//val trans = listOf(ExpensesTransaction(933f,"МИР", LocalDate.now(), cat, "Магнит"),
//    ExpensesTransaction(4250f,"МИР", LocalDate.now(), cat, "Перекресток"),
//    ExpensesTransaction(10000f,"МИР", LocalDate.now(), cat, "Магнит"),
//    ExpensesTransaction(678.24f,"МИР", LocalDate.now(), cat, "Пятерочка"),
//    ExpensesTransaction(125f,"МИР", LocalDate.now(), cat, "Магнит"),
//    ExpensesTransaction(350f,"МИР", LocalDate.now(), cat, "Перекресток"),
//    ExpensesTransaction(200f,"МИР", LocalDate.now(), cat, "Перекресток"),
//    ExpensesTransaction(1299.99f,"МИР", LocalDate.now(), cat, "Перекресток"))


@Composable
fun MainScreen(navController: NavController){
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
            TransactionListScreen(UserClass.transactions)
        }

        IconButton(
            onClick = { navController.navigate("add") },
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
                text = "3 дня Вы ведёте бюджет!",
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
                text = "500 ₽",
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
        Spacer(modifier = Modifier.height(6.dp))
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

                    if (transaction.category is ExpensesCategory){
                        transaction as ExpensesTransaction
                        Text(
                            text = transaction.organization,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
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
            .padding(start = 23.dp, end = 23.dp, bottom = 170.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        itemsIndexed(transactions){
            index, item ->
            TransactionCard(item)
        }
    }
}
