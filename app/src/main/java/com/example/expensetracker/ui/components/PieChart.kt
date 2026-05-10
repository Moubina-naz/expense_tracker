package com.example.expensetracker.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import com.example.expensetracker.data.models.ChartModel
import com.example.expensetracker.data.models.MonthItem
import com.example.expensetracker.data.models.TransactionEntity
import com.example.expensetracker.viewmodels.Transacviewmodel
import kotlin.math.atan2
import kotlin.math.PI
import kotlin.math.sqrt

@Composable
fun CategoryLegendList(
    charts: List<ChartModel>,
    selectedCategory: String?,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        charts.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowItems.forEach { chart ->
                    val isSelected = chart.name == selectedCategory
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onCategoryClick(chart.name) }
                            .background(if (isSelected) Color.Black.copy(alpha = 0.05f) else Color.Transparent)
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                    ) {
                        // Circular indicator like in the image
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(chart.color)
                        )
                        
                        Text(
                            text = chart.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected) Color.Black else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                // Add empty space if the row only has 1 item
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ChartCirclePie(
    modifier: Modifier,
    charts: List<ChartModel>,
    totalSpent: String,
    selectedCategory: String?,
    onCategoryClick: (String) -> Unit,
    size: Dp = 240.dp,
    strokeWidth: Dp = 24.dp, // Thinner, sleeker ring
) {
    val total = charts.sumOf { it.value.toDouble() }.toFloat()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Donut Chart with Center Text
        Box(
            modifier = Modifier
                .size(size)
                .pointerInput(charts) {
                    detectTapGestures { offset ->
                        val centerX = size.toPx() / 2
                        val centerY = size.toPx() / 2
                        val x = offset.x - centerX
                        val y = offset.y - centerY
                        
                        val radius = sqrt(x * x + y * y)
                        val innerRadius = (size.toPx() / 2) - strokeWidth.toPx()
                        val outerRadius = size.toPx() / 2
                        
                        // Check if tap is within the donut ring
                        if (radius in innerRadius..outerRadius) {
                            var angle = Math.toDegrees(atan2(y.toDouble(), x.toDouble())).toFloat()
                            // Adjust angle to match drawArc (-90 is top)
                            angle = (angle + 90 + 360) % 360
                            
                            var currentAngle = 0f
                            charts.forEach { chart ->
                                val sweepAngle = if (total > 0f) (chart.value / total) * 360f else 0f
                                if (angle >= currentAngle && angle <= currentAngle + sweepAngle) {
                                    onCategoryClick(chart.name)
                                    return@detectTapGestures
                                }
                                currentAngle += sweepAngle
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                onDraw = {
                    var startAngle = -90f // Start from top
                    charts.forEach {
                        val sweepAngle = if (total > 0f) (it.value / total) * 360f else 0f
                        val isSelected = it.name == selectedCategory

                        drawArc(
                            color = it.color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(
                                width = if (isSelected) (strokeWidth + 8.dp).toPx() else strokeWidth.toPx(),
                                cap = StrokeCap.Butt,
                                join = StrokeJoin.Miter
                            ),
                            alpha = if (selectedCategory == null || isSelected) 1f else 0.3f
                        )

                        startAngle += sweepAngle
                    }
                }
            )
            
            // Center Text exactly like the image
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "TOTAL SPENT",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )
                Text(
                    text = totalSpent,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Legend below the chart
        CategoryLegendList(
            charts = charts,
            selectedCategory = selectedCategory,
            onCategoryClick = onCategoryClick
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    viewModel: Transacviewmodel = viewModel(),
    navController: NavController
) {
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val categoryTotals by viewModel.categoryTotals.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val transactions by viewModel.transactionList.collectAsState()
    val months = remember { viewModel.generatePastMonths() }
    
    val context = androidx.compose.ui.platform.LocalContext.current
    val userPrefs = remember { com.example.expensetracker.data.models.UserPreferences(context) }
    val currencySymbol = remember(userPrefs.getUserCurrency()) { 
        com.example.expensetracker.utils.CurrencyManager.getCurrencySymbol(userPrefs.getUserCurrency())
    }
    
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent) // Use parent background
    ) {
        // Month Selector - Simplified text version from image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(48.dp)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(months) { month ->
                    val isSelected = month.value == selectedMonth
                    Text(
                        text = month.label,
                        color = if (isSelected) Color.Black else Color.Gray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable {
                            val parts = month.value.split("/")
                            if (parts.size == 2) {
                                viewModel.selectMonth(parts[0], parts[1])
                                selectedCategory = null
                            }
                        }
                    )
                }
            }
        }

        // Scrollable content area
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                // Chart Area
                when {
                    isLoading -> {
                        Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF81C784))
                        }
                    }
                    categoryTotals.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "No data for this period",
                                color = Color.White.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    else -> {
                        val tealPalette = listOf(
                            Color(0xFF649594), // Base teal
                            Color(0xFF81B0AF), // Light teal
                            Color(0xFF4D7A79), // Dark teal
                            Color(0xFFA2C9C8), // Very light teal
                            Color(0xFF3C6161), // Very dark teal
                            Color(0xFFB8DEDD)  // Pale teal
                        )

                        val pieData = categoryTotals.mapIndexed { index, it ->
                            ChartModel(
                                name = it.category,
                                value = it.total.toFloat(),
                                color = tealPalette[index % tealPalette.size]
                            )
                        }
                        
                        val totalSum = categoryTotals.sumOf { it.total.toDouble() }
                        val formattedTotal = "${currencySymbol}${"%,.2f".format(totalSum)}"

                        ChartCirclePie(
                            modifier = Modifier.fillMaxWidth(),
                            charts = pieData,
                            totalSpent = formattedTotal,
                            selectedCategory = selectedCategory,
                            onCategoryClick = { category ->
                                selectedCategory = if (selectedCategory == category) null else category
                            }
                        )
                    }
                }
            }

            // Transactions List appears only when a category is selected
            if (selectedCategory != null) {
                val filteredTransactions = transactions.filter { 
                    it.category.equals(selectedCategory, ignoreCase = true) && 
                    it.date.endsWith(selectedMonth)
                }

                item {
                    Text(
                        text = "$selectedCategory Details",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                items(filteredTransactions) { transaction ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        ExpenseItem(
                            transaction = transaction,
                            onClick = { /* Detail */ }
                        )
                    }
                }
                
                if (filteredTransactions.isEmpty()) {
                    item {
                        Text(
                            text = "No transactions found",
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (!isLoading && categoryTotals.isNotEmpty()) {
                // Hint when nothing is selected
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tap a category or chart segment to view list",
                            color = Color.Gray.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

fun getCategoryColor(category: String): Color {
    return when(category.trim().lowercase()){
        "food", "recurrent" -> Color(0xFF81C784) // Soft Green from image
        "transport", "luxury" -> Color(0xFF4DB6AC) // Teal from image
        "shopping", "misc" -> Color(0xFFE53935) // Red from image
        "entertainment" -> Color(0xFFBA68C8)
        "groceries" -> Color(0xFF64B5F6)
        "bills" -> Color(0xFFFFD54F)
        "travel" -> Color(0xFFFF8A65)
        else -> Color(0xFF90A4AE)
    }
}

@Composable
fun MonthChip(month: MonthItem, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF1F2937) else Color.Transparent,
        border = if (isSelected) null else BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = month.label,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
}
/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun StatisticsScreenPreview() {
    val sampleData = listOf(
        ChartModel(40f, Color(0xFF81C784), "Recurrent"),
        ChartModel(20f, Color(0xFF4DB6AC), "Luxury"),
        ChartModel(40f, Color(0xFFE53935), "Misc")
    )
    
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0B121F))) {
        ChartCirclePie(
            modifier = Modifier.align(Alignment.Center),
            charts = sampleData,
            size = 200.dp,
            selectedCategory = TODO(),
            onCategoryClick = TODO(),
            strokeWidth = TODO()
        )
    }
}
*/