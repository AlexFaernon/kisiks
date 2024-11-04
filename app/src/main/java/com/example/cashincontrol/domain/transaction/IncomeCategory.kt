package com.example.cashincontrol.domain.transaction

import com.example.cashincontrol.R

data class IncomeCategory(override val name: String, override val icon: Int = R.drawable.icon_shop) : Category()
