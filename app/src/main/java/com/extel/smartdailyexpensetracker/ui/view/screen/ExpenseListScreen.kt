package com.extel.smartdailyexpensetracker.ui.view.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.extel.smartdailyexpensetracker.model.Expense
import com.extel.smartdailyexpensetracker.ui.view.viewmodel.MainViewModel
import com.extel.smartdailyexpensetracker.utill.Routes
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val ctx = LocalContext.current

    val expenses by viewModel.expenses.collectAsState()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var groupByCategory by remember { mutableStateOf(true) }

    val filteredExpenses = expenses.filter { it.date == selectedDate }
    val totalAmount = filteredExpenses.sumOf { it.amount }
    val totalCount = filteredExpenses.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense List") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Routes.ExpenseReportScreen)
                    }) {
                        Icon(Icons.Default.Create, contentDescription = "Reports")

                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Routes.ExpenseEntryScreen)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    Toast.makeText(ctx, "Calendar picker not implemented", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Date: ${selectedDate.format(DateTimeFormatter.ISO_DATE)}")
                }

                FilterChip(
                    selected = groupByCategory,
                    onClick = { groupByCategory = !groupByCategory },
                    label = {
                        Text(if (groupByCategory) "Grouped by Category" else "Grouped by Time")
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            Text("Total Count: $totalCount")
            Text("Total Amount: ₹${totalAmount / 100.0}", fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(16.dp))

            if (filteredExpenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No expenses for this date", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (groupByCategory) {
                        filteredExpenses.groupBy { it.category }.forEach { (category, items) ->
                            item {
                                Text(
                                    text = "Category: $category",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            items(items) { expense ->
                                ExpenseListItem(expense)
                            }
                        }
                    } else {
                        items(filteredExpenses.sortedByDescending { it.timestamp }) { expense ->
                            ExpenseListItem(expense)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseListItem(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(expense.title, fontWeight = FontWeight.Bold)
            Text("₹${expense.amount / 100.0}")
            Text("Category: ${expense.category}")
            Text("Date: ${expense.date}")
            expense.notes?.let {
                Text("Notes: $it")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpenseListScreen() {
    val mockNavController = rememberNavController()

    val sampleExpenses = listOf(
        Expense("Lunch", 2500, "Food", "Team lunch", LocalDateTime.now(), LocalDate.now()),
        Expense("Taxi", 5000, "Travel", "Airport ride", LocalDateTime.now().minusHours(2), LocalDate.now()),
        Expense("Electricity Bill", 120000, "Utility", "Monthly bill", LocalDateTime.now().minusDays(1), LocalDate.now().minusDays(1))
    )

    val fakeViewModel = object : MainViewModel() {
        init {
            _expenses.value = sampleExpenses
            calculateTotal()
        }
    }

    ExpenseListScreen(
        navController = mockNavController,
        viewModel = fakeViewModel
    )
}
