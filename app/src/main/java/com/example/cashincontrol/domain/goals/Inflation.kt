package com.example.cashincontrol.domain.goals

import android.util.Log
import com.example.cashincontrol.domain.UserClass
import com.example.cashincontrol.domain.transaction.ExpensesCategory
import com.example.cashincontrol.domain.transaction.IncomeCategory
import com.example.cashincontrol.domain.transaction.IncomeTransaction
import java.time.LocalDate
import java.time.Month

class Inflation {
    companion object{
        val YearInflation: List<Pair<Int, Float>> = mutableListOf()
        lateinit var CategoryInflation: Map<ExpensesCategory, Float>

        fun updateInflation(){
            val transactions = UserClass.transactions
            var startIndex = -1
            val currentMonth = LocalDate.now().month
            for (i in 0..transactions.size){
                if (transactions[i].date.month != currentMonth){
                    startIndex = i
                    break
                }
            }

            if (startIndex == -1){
                return
            }

            val meansByMonth: MutableList<Pair<Month, Float>> = mutableListOf()
            var month = transactions[startIndex].date.month
            var transactionCount = 0f
            var transactionSum = 0f

            for (transaction in transactions.subList(startIndex, transactions.size)){
                if (transaction.date.month == currentMonth) break

                if (transaction is IncomeTransaction) continue

                if (transaction.date.month != month){
                    val mean = transactionSum / transactionCount
                    meansByMonth.add(Pair(month, mean))

                    month = transaction.date.month
                    transactionCount = 0f
                    transactionSum = 0f
                }

                transactionCount++
                transactionSum += transaction.sum
            }

            val mean = transactionSum / transactionCount
            meansByMonth.add(Pair(month, mean))
            for (i in meansByMonth){
                Log.d("inflation", i.toString())
            }
        }

        fun updateCategoryInflation(){
            val today = LocalDate.now()
            val monthAgo = today.minusMonths(1)
            val currentPeriod =  getCategoriesMeanForMonth(today)
            val previousPeriod = getCategoriesMeanForMonth(monthAgo)

            val result: MutableMap<ExpensesCategory, Float> = mutableMapOf()
            for (category in currentPeriod.keys){
                if (!previousPeriod.containsKey(category)) continue

                result[category] = currentPeriod[category]!! / previousPeriod[category]!!
            }

            CategoryInflation = result
        }

        private fun getCategoriesMeanForMonth(startDate: LocalDate): Map<ExpensesCategory, Float>{
            val means: MutableMap<ExpensesCategory, Pair<Int, Float>> = mutableMapOf()
            val monthAgo = startDate.minusMonths(1)

            var startIndex = -1
            for (i in 0..UserClass.transactions.size){
                if (UserClass.transactions[i].date.toLocalDate() <= startDate){
                    startIndex = i
                }
            }

            if (startIndex == -1){
                return mapOf()
            }

            for (i in startIndex..UserClass.transactions.size){
                val transaction = UserClass.transactions[i]
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
                result[keyValue.key] = mean;
            }

            return result
        }
    }
}
