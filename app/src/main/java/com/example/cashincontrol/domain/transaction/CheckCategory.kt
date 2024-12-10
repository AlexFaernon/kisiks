package com.example.cashincontrol.domain.transaction

import com.example.cashincontrol.R

data class CheckCategory (
    override var name: String,
    val alias: HashSet<String>,
    override val icon: Int = R.drawable.icon_shop
): Category()