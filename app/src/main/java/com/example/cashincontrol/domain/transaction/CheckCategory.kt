package com.example.cashincontrol.domain.transaction

data class CheckCategory(
    val name: String,
    val alias: MutableList<String>
)