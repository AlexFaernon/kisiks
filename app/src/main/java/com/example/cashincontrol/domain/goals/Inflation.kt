package com.example.cashincontrol.domain.goals

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.UserClass.Companion.checkTransactions
import com.example.cashincontrol.domain.UserClass.Companion.transactions
import com.example.cashincontrol.domain.transaction.CheckCategory
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeCategory
import java.time.LocalDate
import java.time.Month

class Inflation {
    companion object{
        var GlobalInflation = 0f
        lateinit var YearInflation: Map<Month, Float>
        lateinit var CategoryInflation: Map<ExpensesCategory, Float>
        lateinit var CheckInflation: Map<CheckCategory, Float>

        val officialInflation = listOf(
            7.27f, 7.5f, 7.52f, 7.82f, 7.70f, 7.86f, 8.49f, 9.09f, 8.71f, 8.41f, 8.52f, 8.62f
        )

        fun getOfficialForMonth(month: Month = LocalDate.now().month): Float{
            return officialInflation[month.value - 1]
        }

        @Composable
        fun UpdateInflation(){
            UpdateYearlyInflation()
            updateCategoryInflation()
            updateCheckInflation()
        }

        @Composable
        private fun UpdateYearlyInflation(){
            val lastYear = getYearlyMean(LocalDate.now())
            val previousYear = getYearlyMean(LocalDate.now().minusYears(1))

            YearInflation = getInflation(lastYear, previousYear)

            val globalInflation = YearInflation.values.average().toFloat()
            GlobalInflation = if (globalInflation.isNaN()) 0f else globalInflation
            UserClass.achievementSystem.CheckInflation()

            Log.d("inflation", "Yearly inflation")
            for (i in YearInflation){
                Log.d("inflation", "${i.key} ${i.value}")
            }
        }

        private fun getYearlyMean(startDate: LocalDate): Map<Month, Float>{
            val startIndex = transactions.indexOfFirst { it.date.month != startDate.month && it.date.toLocalDate() <= startDate }
            if (startIndex == -1) return mapOf()

            val endDate = transactions[startIndex].date.minusYears(1)
            val pastEndIndex = transactions.indexOfFirst { it.date.year == endDate.year && it.date.month == endDate.month}
            val endIndex = if (pastEndIndex == -1) transactions.size - 1 else pastEndIndex - 1

            val groupByMonth = transactions.slice(startIndex..endIndex).groupBy { it.date.month }
            val meanByMonth: MutableMap<Month, Float> = mutableMapOf()
            for (group in groupByMonth){
                val expensesTransactions = group.value.filterIsInstance<ExpensesTransaction>()
                if (expensesTransactions.isEmpty()) continue
                meanByMonth[group.key] = (expensesTransactions.sumOf { it.sum.toDouble() } / expensesTransactions.size).toFloat()
            }

            Log.d("inflation", "start from $startDate")
            for (i in meanByMonth){
                Log.d("inflation", "${i.key} ${i.value}")
            }

            return meanByMonth
        }

        private fun updateCategoryInflation(){
            val lastPeriod =  getCategoriesMeanForMonth(LocalDate.now())
            val previousPeriod = getCategoriesMeanForMonth(LocalDate.now().minusMonths(1))

            CategoryInflation = getInflation(lastPeriod, previousPeriod)

        }

        private fun getCategoriesMeanForMonth(startDate: LocalDate): Map<ExpensesCategory, Float>{
            val means: MutableMap<ExpensesCategory, Pair<Int, Float>> = mutableMapOf()
            val monthAgo = startDate.minusMonths(1)

            var startIndex = -1
            for (i in 0..<transactions.size){
                if (transactions[i].date.toLocalDate() <= startDate){
                    startIndex = i
                    break
                }
            }

            if (startIndex == -1){
                return mapOf()
            }

            for (i in startIndex..<transactions.size){
                val transaction = transactions[i]
                if (transaction.date.toLocalDate() < monthAgo) break

                if (transaction.category is IncomeCategory) continue

                val category = transaction.category as ExpensesCategory
                if (means.containsKey(category)){
                    val oldValue = means[category]
                    means[category] = Pair(oldValue!!.first + 1, oldValue.second + transaction.sum)
                }
                else{
                    means[category] = Pair(1, transaction.sum)
                }
            }

            val result: MutableMap<ExpensesCategory, Float> = mutableMapOf()
            for (keyValue in means){
                val mean = keyValue.value.second / keyValue.value.first
                result[keyValue.key] = mean
            }

            return result
        }

        private fun updateCheckInflation(){
            val lastPeriod =  getCheckCategoriesMeanForMonth(LocalDate.now())
            val previousPeriod = getCheckCategoriesMeanForMonth(LocalDate.now().minusMonths(1))

            CheckInflation = getInflation(lastPeriod, previousPeriod)
        }

        private fun getCheckCategoriesMeanForMonth(startDate: LocalDate): Map<CheckCategory, Float>{
            val monthAgo = startDate.minusMonths(1)

            var startIndex = -1
            for (i in 0..<checkTransactions.size){
                if (checkTransactions[i].date.toLocalDate() <= startDate){
                    startIndex = i
                    break
                }
            }

            if (startIndex == -1){
                return mapOf()
            }

            val means: MutableMap<CheckCategory, Pair<Int, Float>> = mutableMapOf()
            for (i in startIndex..<checkTransactions.size){
                val transaction = checkTransactions[i]
                if (transaction.date.toLocalDate() < monthAgo) break

                for (categoryPair in transaction.category) {
                    val category = categoryPair.key
                    val sum = categoryPair.value

                    if (means.containsKey(category)) {
                        val oldValue = means[category]
                        means[category] =
                            Pair(oldValue!!.first + 1, oldValue.second + sum)
                    } else {
                        means[category] = Pair(1, sum)
                    }
                }
            }

            val result: MutableMap<CheckCategory, Float> = mutableMapOf()
            for (keyValue in means){
                val mean = keyValue.value.second / keyValue.value.first
                result[keyValue.key] = mean
            }

            return result
        }

        private fun <T> getInflation(lastPeriod: Map<T, Float>, previousPeriod: Map<T, Float>): Map<T, Float>{
            val result: MutableMap<T, Float> = mutableMapOf()

            for (meanGroup in lastPeriod.keys){
                if (!previousPeriod.containsKey(meanGroup)) continue

                result[meanGroup] = (lastPeriod[meanGroup]!! / previousPeriod[meanGroup]!! - 1) * 100
            }

            return result
        }
    }
}
