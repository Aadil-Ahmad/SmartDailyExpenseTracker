package com.extel.smartdailyexpensetracker.ui.view.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.extel.smartdailyexpensetracker.model.Expense
import com.extel.smartdailyexpensetracker.ui.view.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ExpenseReportScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val ctx = LocalContext.current
    val expenses by viewModel.expenses.collectAsState()

    // filter last 7 days
    val today = LocalDate.now()
    val last7Days = (0..6).map { today.minusDays(it.toLong()) }.reversed()

    val dailyTotals = last7Days.associateWith { date ->
        expenses.filter { it.date == date }.sumOf { it.amount }
    }

    val categoryTotals = expenses
        .filter { it.date >= today.minusDays(6) }
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Expense Report (Last 7 Days)",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Text("Daily Totals", style = MaterialTheme.typography.titleMedium)
        dailyTotals.forEach { (date, total) ->
            Text("${date}: ₹${total}")
        }

        Spacer(Modifier.height(16.dp))

        Text("Category-wise Totals", style = MaterialTheme.typography.titleMedium)
        categoryTotals.forEach { (category, total) ->
            Text("$category: ₹${total }")
        }

        Spacer(Modifier.height(24.dp))

        Text("Mock Bar Chart", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)) {
            val barWidth = size.width / (dailyTotals.size * 2)
            val maxAmount = (dailyTotals.values.maxOrNull() ?: 1).toFloat()
            dailyTotals.values.forEachIndexed { index, amount ->
                val barHeight = (amount / maxAmount) * size.height
                drawRect(
                    color = Color(0xFF3F51B5),
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = index * barWidth * 2,
                        y = size.height - barHeight
                    ),
                    size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Export Options", style = MaterialTheme.typography.titleMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Button(onClick = {
                Toast.makeText(ctx, "Simulating PDF export...", Toast.LENGTH_SHORT).show()
            }) {
                Text("Export PDF")
            }
            Button(onClick = {
                Toast.makeText(ctx, "Simulating CSV export...", Toast.LENGTH_SHORT).show()
            }) {
                Text("Export CSV")
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Expense report (last 7 days)")
                }
                ctx.startActivity(Intent.createChooser(shareIntent, "Share Report"))
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Share Report")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpenseReportScreen() {
    val mockNavController = rememberNavController()

    val fakeExpenses = listOf(
        Expense(
            title = "Coffee",
            amount = 1500,
            category = "Food",
            notes = "Morning coffee",
            timestamp = LocalDateTime.now().withHour(9).withMinute(0),
            date = LocalDate.now(),
            receiptUri = "mock_receipt.jpg"
        ),
        Expense(
            title = "Taxi",
            amount = 5000,
            category = "Travel",
            notes = "Airport ride",
            timestamp = LocalDateTime.now().minusDays(1).withHour(13).withMinute(30),
            date = LocalDate.now().minusDays(1),
            receiptUri = null
        ),
        Expense(
            title = "Snacks",
            amount = 700,
            category = "Food",
            notes = null,
            timestamp = LocalDateTime.now().minusDays(2).withHour(16).withMinute(0),
            date = LocalDate.now().minusDays(2),
            receiptUri = null
        ),
        Expense(
            title = "Internet Bill",
            amount = 12000,
            category = "Utility",
            notes = "Monthly plan",
            timestamp = LocalDateTime.now().minusDays(3).withHour(10).withMinute(15),
            date = LocalDate.now().minusDays(3),
            receiptUri = null
        ),
        Expense(
            title = "Salary Advance",
            amount = 500000,
            category = "Staff",
            notes = "Advance payment",
            timestamp = LocalDateTime.now().minusDays(4).withHour(11).withMinute(45),
            date = LocalDate.now().minusDays(4),
            receiptUri = null
        )
    )

    val fakeViewModel = object : MainViewModel() {
    }

    ExpenseReportScreen(
        navController = mockNavController,
        viewModel = fakeViewModel
    )
}


