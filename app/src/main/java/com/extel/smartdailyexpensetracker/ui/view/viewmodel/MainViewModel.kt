package com.extel.smartdailyexpensetracker.ui.view.viewmodel

import androidx.lifecycle.*
import com.extel.smartdailyexpensetracker.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*
import java.time.LocalDate

open class MainViewModel : ViewModel() {
    private val _totalSpentToday = MutableStateFlow(0L)
    val totalSpentToday: StateFlow<Long> = _totalSpentToday

    internal val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses


    fun addExpense(expense: Expense){
        viewModelScope.launch {
            _expenses.value = _expenses.value + expense
            calculateTotal()
        }
    }

    internal fun calculateTotal() {
        val today = LocalDate.now()
        _totalSpentToday.value = _expenses.value
            .filter { it.date == today }
            .sumOf { it.amount }
    }
}