package com.example.cashincontrol.domain.transaction

import com.example.cashincontrol.R

data class IncomeCategory(override var name: String, override val icon: Int = R.drawable.icon_income) : Category()
