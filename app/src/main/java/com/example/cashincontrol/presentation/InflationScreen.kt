package com.example.cashincontrol.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.goals.Inflation
import com.example.cashincontrol.domain.transaction.ExpensesCategory

@Preview
@Composable
fun InflationScreen() {
    if (UserClass.transactions.isEmpty()) {
        Text(
            modifier = Modifier.fillMaxSize().wrapContentHeight(),
            text = "Нет данных",
            fontSize = 32.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 30.dp, end = 10.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MainInflation()
            DynamicInflation()
            CategoryInflation()
        }
    }
}

@Composable
fun MainInflation(){
    Text(
        text = "Личная инфляция",
        fontSize = 20.sp,
        color = Color.Black,
        modifier = Modifier
            .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column() {
            Text(
                text = "16,4%",
                fontSize = 48.sp,
                color = Color.Black,
            )
            Text(
                text = "текущий уровень",
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.icon_up),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = Color.Unspecified
        )
    }
}

@Composable
fun DynamicInflation(){
    Text(
        text = "Динамика",
        fontSize = 20.sp,
        color = Color.Black,
        modifier = Modifier
            .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.screamer),
            contentDescription = null,
            modifier = Modifier.size(height = 40.dp, width = 10.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = "Ваш уровень личной инфляции выше среднего",
            fontSize = 13.sp,
            color = Color.Gray,
        )
    }
}

@Composable
fun CategoryInflation(){
    Inflation.updateCategoryInflation()
    val inflation = Inflation.CategoryInflation
    Log.d("ИНФЛЯЦИЯ", "DataForm: ${inflation.entries.size}")
//    val block = listOf(99.99f, 13f, 0.01f, 99.99f, 13f, 0.01f, 99.99f, 13f, 0.01f )
    Text(
        text = "Категории",
        fontSize = 20.sp,
        color = Color.Black,
        modifier = Modifier
            .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
    CategoriesInflationGrid(inflation)
}

@Composable
fun CategoriesInflationGrid(inflation:  Map<ExpensesCategory, Float>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(inflation.entries.toList().size) { index ->
            CategoryInflationCard(inflation.entries.toList()[index])
        }
    }
}

@Composable
fun CategoryInflationCard(inflation: Map. Entry<ExpensesCategory, Float>){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%.1f %%", inflation.value),
                fontSize = 24.sp,
                color = Color.Black,
            )
            Text(
                text = inflation.key.name.limit(15),
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        val iconRes = if (inflation.value < 0) R.drawable.icon_down else R.drawable.icon_up

        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
        )
    }
}

fun String.limit(maxLength: Int): String {
    return if (this.length > maxLength) this.take(maxLength) + "..." else this
}