package com.extel.smartdailyexpensetracker.ui.view.screen

import androidx.compose.runtime.Composable
import com.extel.smartdailyexpensetracker.model.Expense

@Composable
fun ExpenseListScreen(
    expenses: List<Expense> = emptyList(),
    onDateSelected: (Long) -> Unit = {}){

}