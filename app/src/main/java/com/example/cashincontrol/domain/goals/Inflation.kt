package com.example.cashincontrol.domain.goals

import android.util.Log
import com.example.cashincontrol.domain.UserClass.Companion.transactions
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.ExpensesTransaction
import com.example.cashincontrol.domain.transaction.IncomeCategory
import java.time.LocalDate
import java.time.Month

class Inflation {
    companion object{
        lateinit var YearInflation: Map<Month, Float>
        lateinit var CategoryInflation: Map<ExpensesCategory, Float>

        fun updateInflation(){
            val lastYear = getYearlyMean(LocalDate.now())
            val previousYear = getYearlyMean(LocalDate.now().minusYears(1))

            YearInflation = getInflation(lastYear, previousYear)
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

        fun updateCategoryInflation(){
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
