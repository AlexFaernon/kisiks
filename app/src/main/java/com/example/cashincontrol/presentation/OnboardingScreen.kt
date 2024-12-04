package com.example.cashincontrol.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cashincontrol.R
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.ui.theme.lexendMegaFamily


@Composable
fun OnboardingScreens(navController: NavController, onComplete: () -> Unit) {
    val currentScreen = remember { mutableIntStateOf(0) }

    if (!UserClass.isOnboardingCompleted) {
        if (currentScreen.intValue < OnboardingData.screens.size) {
            val screen = OnboardingData.screens[currentScreen.intValue]
            when (currentScreen.intValue) {
                0 -> OnboardingScreen1(screen = screen) { currentScreen.intValue++ }
                1 -> OnboardingScreen2(screen = screen) { currentScreen.intValue++ }
                2 -> OnboardingScreen3(screen = screen) { currentScreen.intValue++ }
                3 -> OnboardingScreen4(screen = screen) { currentScreen.intValue++ }
                4 -> OnboardingScreen5(screen = screen) { onComplete() }
            }
        }
    } else {
        navController.navigate("inflation")
    }
}


@Composable
private fun OnboardingScreen1(screen: OnboardingScreenData, onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = screen.description,
            fontSize = 16.sp,
            fontFamily = lexendMegaFamily,
            modifier = Modifier.padding(horizontal = 25.dp, vertical = 30.dp)
        )

        Image(
            painter = painterResource(screen.imageRes),
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .width(350.dp)
        )

        IconButton(
            onClick = onNext,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.End)
                .padding(bottom = 35.dp, end = 25.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.next),
                contentDescription = "Дальше",
                modifier = Modifier
                    .fillMaxSize(),
                tint = Color.Unspecified,)
        }
    }
}

@Composable
private fun OnboardingScreen2(screen: OnboardingScreenData, onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = screen.descriptionBackground,
            fontSize = 16.sp,
            fontFamily = lexendMegaFamily,
            modifier = Modifier
                .padding(start = 9.dp, end = 9.dp, top = 30.dp)
                .background(Color(0x70689E00), shape = RoundedCornerShape(27.dp))
                .padding(horizontal = 16.dp, vertical = 15.dp)

        )

        Text(
            text = screen.description,
            fontSize = 16.sp,
            fontFamily = lexendMegaFamily,
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 30.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
            Image(
                painter = painterResource(screen.imageRes),
                contentDescription = "Onboarding Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp)
                    .width(350.dp)
            )

            IconButton(
                onClick = onNext,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 35.dp, end = 25.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = "Дальше",
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

@Composable
private fun OnboardingScreen3(screen: OnboardingScreenData, onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = screen.description,
            fontSize = 16.sp,
            fontFamily = lexendMegaFamily,
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 30.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(screen.imageRes),
                contentDescription = "Onboarding Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp)
            )

            IconButton(
                onClick = onNext,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 35.dp, end = 25.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = "Дальше",
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

@Composable
private fun OnboardingScreen4(screen: OnboardingScreenData, onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = screen.description,
            fontSize = 16.sp,
            fontFamily = lexendMegaFamily,
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 30.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(screen.imageRes),
                contentDescription = "Onboarding Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(430.dp)
            )

            IconButton(
                onClick = onNext,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 35.dp, end = 25.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.next),
                    contentDescription = "Дальше",
                    modifier = Modifier
                        .fillMaxSize(),
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

@Composable
private fun OnboardingScreen5(screen: OnboardingScreenData, onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = screen.description,
            fontSize = 16.sp,
            fontFamily = lexendMegaFamily,
            modifier = Modifier
                .padding(horizontal = 25.dp, vertical = 30.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(screen.imageRes),
                contentDescription = "Onboarding Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(430.dp)
            )

            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBEC399)
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 35.dp, end = 25.dp)
                    .height(48.dp)
                    .border(1.dp, Color(0x78689E00), shape = RoundedCornerShape(3.dp)),
                shape = RoundedCornerShape(3.dp),
                contentPadding = PaddingValues(12.dp),
            ) {
                Text(
                    text = "Завершить",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

data class OnboardingScreenData(
    val imageRes: Int,
    val descriptionBackground: String,
    val description: String

)

object OnboardingData {
    val screens = listOf(
        OnboardingScreenData(
            imageRes = R.drawable.onboarding_1,
            descriptionBackground = "",
            description = """
                Привет! Это Влад, наш герой.

                Он обратил внимание, что денег до следующей зарплаты остаётся всё меньше. Раньше он не придавал этому значения, но теперь решил контролировать свои расходы и доходы.

                Влад был уверен, что инфляция его не касается, пока не узнал о личной инфляции.
            """.trimIndent()
        ),
        OnboardingScreenData(
            imageRes = R.drawable.onboarding_2,
            descriptionBackground = """
                Личная инфляция — это показатель, который отражает изменение стоимости потребительской корзины конкретного человека.

                Она учитывает цены на все товары и услуги, которые человек покупает регулярно.
            """.trimIndent(),
            description = """
                Влад решил посчитать свою личную инфляцию и был поражён результатами!

                Оказалось, стоимость товаров и услуг, которые он приобретает ежедневно, увеличилась гораздо значительнее, чем официальные данные Росстата об инфляции в стране.
            """.trimIndent()
        ),
        OnboardingScreenData(
            imageRes = R.drawable.onboarding_3,
            descriptionBackground = "",
            description = """
                Влад заметил, что траты на продукты питания значительно увеличились.

                Тогда он решил рационализировать свои расходы. Он начал более пристально следить за ценами, искать скидки и акции, сравнивать стоимость товаров в разных магазинах.
            """.trimIndent()
        ),
        OnboardingScreenData(
            imageRes = R.drawable.onboarding_4,
            descriptionBackground = "",
            description = """
                В результате Влад смог уменьшить траты на продукты и другие товары.

                Он осознал, что личная инфляция — это важный инструмент для грамотного распределения бюджета и достижения финансовых целей.

                Теперь Влад понял, что важно не только сокращать расходы, но и искать способы увеличить доходы.
            """.trimIndent()
        ),
        OnboardingScreenData(
            imageRes = R.drawable.onboarding_5,
            descriptionBackground = "",
            description = """
                  Влад решил начать инвестировать.
                  Хотя он еще новичок, но понимает, что для сохранения покупательной способности капитала его доходность должна превышать уровень личной инфляции. 

                  Например, 7% доходности при 2% инфляции гораздо выгоднее, чем 15% при инфляции в 20%. Именно поэтому он никогда не оценивает потенциальную прибыль и инфляцию отдельно друг от друга — так можно серьёзно ошибиться в оценке результатов своих инвестиций.
            """.trimIndent()
        )
    )
}
