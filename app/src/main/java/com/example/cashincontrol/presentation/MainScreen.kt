package com.example.cashincontrol.presentation

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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

@Preview
@Composable
fun MainScreen(){
    Column(
        verticalArrangement = Arrangement.spacedBy(23.dp) // Отступы между элементами
    ) {
        TopSection()
        TransactionCard()
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
fun TransactionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            // Дата транзакции
            Text(
                text = "16 сентября",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Row(
                verticalAlignment = Alignment.Top
            ) {
                // Иконка категории
                Icon(
                    painter = painterResource(id = R.drawable.icon_main),
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
                        text = "Здоровье и красота",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Text(
                        text = "Карта МИР",
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
                Text(
                    text = "-920₽",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}

//@Composable
//fun TransactionListScreen(transactions: List<Transaction>) {
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(13.dp)
//    ) {
//        items(transactions) { transaction ->
//            TransactionCard(transaction = transaction)
//        }
//    }
//}