package com.extel.smartdailyexpensetracker.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.extel.smartdailyexpensetracker.model.Expense
import com.extel.smartdailyexpensetracker.ui.view.screen.*
import com.extel.smartdailyexpensetracker.ui.view.viewmodel.MainViewModel
import com.extel.smartdailyexpensetracker.utill.Routes

@Composable
fun MyAppNavigation(viewModel: MainViewModel){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ExpenseEntryScreen
    ) {
        composable(Routes.ExpenseEntryScreen) {

            val totalToday by viewModel.totalSpentToday.collectAsState()

            ExpenseEntryScreen(
                navController = navController,
                totalToday = totalToday,
                onSubmit = { title, amount, category, notes, receipt , timestamp, date->
                    val expense = Expense(
                        title = title,
                        amount = amount,
                        category = category,
                        notes = notes,
                        receiptUri = receipt,
                        timestamp = timestamp,
                        date = date
                    )
                    viewModel.addExpense(expense)
                }
            )
        }
        composable(Routes.ExpenseListScreen) {
            ExpenseListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Routes.ExpenseReportScreen) {
            ExpenseReportScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}