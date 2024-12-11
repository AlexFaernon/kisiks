package com.example.cashincontrol.domain.transaction

abstract class Category {
    abstract var name: String
    abstract val icon: Int

    override fun equals(other: Any?): Boolean {
        if (other !is Category){
            return false
        }

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}