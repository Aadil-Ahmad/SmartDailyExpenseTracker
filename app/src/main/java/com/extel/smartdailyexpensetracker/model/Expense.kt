package com.extel.smartdailyexpensetracker.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Expense(
//    val id: Int,
    val title: String,
    val amount: Long,
    val category: String,
    val notes: String?,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val date: LocalDate = LocalDate.now(),
    val receiptUri: String? = null
)
