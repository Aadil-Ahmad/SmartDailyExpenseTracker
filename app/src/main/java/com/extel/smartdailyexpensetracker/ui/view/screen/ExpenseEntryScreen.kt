package com.extel.smartdailyexpensetracker.ui.view.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.extel.smartdailyexpensetracker.R
import com.extel.smartdailyexpensetracker.utill.Routes
import java.time.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(
    navController: NavController,
    totalToday: Long = 0L,
    onSubmit: (
        title: String,
        amount: Long,
        category: String,
        notes: String?,
        receipt: String?,
        timestamp: LocalDateTime,
        date: LocalDate
    ) -> Unit
) {
    val ctx = LocalContext.current
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Staff") }
    var notes by remember { mutableStateOf("") }
    var receipt by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Staff", "Travel", "Food", "Utility")
    var expanded by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    val buttonScale by animateFloatAsState(
        targetValue = if (isSubmitting) 1.1f else 1f,
        label = "submitScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Total Spent Today: ₹${totalToday }",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { if (it.all { c -> c.isDigit() }) amount = it },
            label = { Text("Amount (₹)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(12.dp))

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                label = { Text("Category") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = {
                            category = cat
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { if (it.length <= 100) notes = it },
            label = { Text("Notes (optional)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2
        )

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable {
                    receipt = "mock_receipt.jpg"
                    Toast.makeText(ctx, "Receipt attached", Toast.LENGTH_SHORT).show()
                },
            contentAlignment = Alignment.Center
        ) {
            if (receipt == null) {
                Text("Tap to add Receipt Image", color = Color.Gray)
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Receipt",
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (title.isBlank() || amount.isBlank() || amount.toLongOrNull() == null || amount.toLong() <= 0) {
                    Toast.makeText(ctx, "Please enter valid Title and Amount", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val nowDate = LocalDate.now()
                val nowTimestamp = LocalDateTime.now()

                isSubmitting = true
                onSubmit(title, amount.toLong(), category, notes.ifBlank { null }, receipt, nowTimestamp, nowDate)

                Toast.makeText(ctx, "Expense Added!", Toast.LENGTH_SHORT).show()

                title = ""
                amount = ""
                category = "Staff"
                notes = ""
                receipt = null
                navController.navigate(Routes.ExpenseListScreen)

            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .scale(buttonScale),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Submit", fontSize = 18.sp)
        }

        AnimatedVisibility(visible = isSubmitting) {
            Text("Adding expense...", modifier = Modifier.padding(top = 8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewExpenseEntryScreen() {
    val mockNavController = rememberNavController()

    ExpenseEntryScreen(
        navController = mockNavController,
        totalToday = 500L,
        onSubmit = { title, amount, category, notes, receipt, timestamp, date ->
            println("Expense Submitted -> $title | ₹$amount | $category")
        }
    )
}