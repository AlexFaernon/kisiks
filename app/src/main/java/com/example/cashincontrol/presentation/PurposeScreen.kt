package com.example.cashincontrol.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.GroupBarChart
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.transaction.Category
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeTransaction
import com.example.cashincontrol.domain.transaction.Transaction
import java.time.LocalDate
import kotlin.random.Random

@Preview
@Composable
fun PurposeScreen() {
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
                .padding(start = 10.dp, top = 48.dp, end = 10.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MainPurpose()
            DynamicPurpose()
            CategoryPurpose()
        }
    }
}

@Composable
fun MainPurpose(){
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
fun DynamicPurpose(){
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
fun CategoryPurpose(){
    val block = listOf(99.99f, 13f, 0.01f, 99.99f, 13f, 0.01f, 99.99f, 13f, 0.01f )
    Text(
        text = "Категории",
        fontSize = 20.sp,
        color = Color.Black,
        modifier = Modifier
            .background(Color(0xFFDCFFBB), shape = RoundedCornerShape(3.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
    CategoriesPurposeGrid(block)
}

@Composable
fun CategoriesPurposeGrid(purposes: List<Float>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(purposes.size) { index ->
            CategoryPurposeCard(purposes[index])
        }
    }
}

@Composable
fun CategoryPurposeCard(purpose: Float, /*category: Category*/){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column() {
            Text(
                text = "$purpose %",
                fontSize = 24.sp,
                color = Color.Black,
            )
            Text(
                text = "Супермаркеты"/*category.name*/,
                fontSize = 14.sp,
                color = Color.Black,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.icon_up),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified
        )
    }
}