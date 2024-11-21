package com.example.cashincontrol.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import java.time.LocalDate

@Composable
fun GoalScreen(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 48.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextWithBackground(
            text = "Финансовые цели",
            backgroundColor = Color(0xFFDCFFBB),
            paddingValues = PaddingValues(horizontal = 20.dp, vertical = 3.dp)
        )

        if (UserClass.goal != null) {
            GoalInfo()
        }
        else {
            IconButton(
                onClick = { navController.navigate("addGoal") },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(end = 20.dp, bottom = 88.dp)
                    .size(60.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.action_button),
                    contentDescription = "Добавить",
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

@Composable
private fun GoalInfo(){
    val goalName = remember { mutableStateOf("") }
    val goalSum = remember { mutableStateOf(UserClass.goal!!.sum.toString()) }
    val goalDate = remember { mutableStateOf(UserClass.goal!!.targetDate.toString()) }
    LabeledRowWithText(label = "Сумма", textState = goalSum)
    LabeledRowWithText(label = "Дата", textState = goalDate)
}