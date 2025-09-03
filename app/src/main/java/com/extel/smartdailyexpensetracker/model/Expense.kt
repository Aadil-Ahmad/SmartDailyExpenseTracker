package com.extel.smartdailyexpensetracker.model

data class Expense(
    val id: Int,
    val title: String,
    val amount: Long,
    val category: String,
    val timestamp: Long
)
