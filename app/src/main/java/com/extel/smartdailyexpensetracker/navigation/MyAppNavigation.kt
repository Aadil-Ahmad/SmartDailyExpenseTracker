package com.extel.smartdailyexpensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.extel.smartdailyexpensetracker.ui.view.screen.ExpenseEntryScreen
import com.extel.smartdailyexpensetracker.ui.view.viewmodel.MainViewModel
import com.extel.smartdailyexpensetracker.utill.Routes

@Composable
fun MyAppNavigation(viewModel: MainViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.ExpenseEntryScreen){
        composable(Routes.ExpenseEntryScreen){
            ExpenseEntryScreen(
                navController,
                totalToday = TODO(),
                onSubmit = TODO()
            )
        }
    }
}