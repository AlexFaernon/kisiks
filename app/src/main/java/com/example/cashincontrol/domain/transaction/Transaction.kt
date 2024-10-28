package com.example.cashincontrol.domain.transaction

import java.util.Date

abstract class Transaction(){
    abstract val sum: Float
    abstract val card: String
    abstract val date: Date
}
