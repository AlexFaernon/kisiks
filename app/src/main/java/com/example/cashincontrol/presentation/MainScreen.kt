package com.example.cashincontrol.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.Transaction
import java.time.LocalDate


val cat = ExpensesCategory("Test1", R.drawable.icon_top_mian)
val trans = listOf(ExpensesTransaction(933f,"МИР", LocalDate.now(), cat, "АОО ХУЙ В ЖОПУ ЕНТЕРТЕЙМЕНТ"),
    ExpensesTransaction(5000f,"XXX", LocalDate.now(), cat, "АОО"),
    ExpensesTransaction(5000f,"XXX", LocalDate.now(), cat, "АОО"),
    ExpensesTransaction(5000f,"XXX", LocalDate.now(), cat, "АОО"),
    ExpensesTransaction(5000f,"XXX", LocalDate.now(), cat, "АОО"),
    ExpensesTransaction(5000f,"XXX", LocalDate.now(), cat, "АОО"),
    ExpensesTransaction(5000f,"XXX", LocalDate.now(), cat, "АОО"),
    ExpensesTransaction(5000f,"XXX", LocalDate.now(), cat, "АОО"))

@Preview
@Composable
fun MainScreen(){
    Column(
        verticalArrangement = Arrangement.spacedBy(23.dp) // Отступы между элементами
    ) {
        TopSection()
        TransactionListScreen(trans)
    }
}

@Composable
fun TopSection() {
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
fun TransactionCard(transaction: Transaction) {
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
                text = transaction.date.year.toString(),
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

                    Text(
                        text = transaction.card,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "Аптека Улыбка",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                // Сумма транзакции
                val sum =transaction.sum
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
fun TransactionListScreen(transactions: List<Transaction>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 23.dp, end = 23.dp, bottom = 148.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        itemsIndexed(transactions){
            index, item ->
            TransactionCard(item)
        }
    }
}