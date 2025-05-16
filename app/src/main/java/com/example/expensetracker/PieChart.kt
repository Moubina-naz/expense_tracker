import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.expensetracker.ChartModel
import com.example.expensetracker.Room.MonthItem
import com.example.expensetracker.Transacviewmodel
import kotlin.random.Random


@Composable
fun CategoryLegendGrid(
    charts: List<ChartModel>,
    modifier: Modifier = Modifier
) {
    val total = charts.sumOf { it.value.toDouble() }.toFloat()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 columns like your reference
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 400.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(charts) { chart ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(0.9f) // Match aspect ratio from reference
                    //.border(1.dp,) // Subtle border instead of debug red
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Color indicator (replacing icon)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(20))
                            .background(chart.color)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Category name
                    Text(
                        text = chart.name,
                        color = Color.Black,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        textAlign = TextAlign.Center
                    )

                    // Amount and percentage
                    Text(
                        text = "₹${chart.value.toInt()} (${(chart.value/total*100).toInt()}%)",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChartCirclePie(
    modifier: Modifier,
    charts: List<ChartModel>,
    size: Dp = 200.dp,
    strokeWidth: Dp = 16.dp,
    //viewModel : Transacviewmodel
) {
    val total = charts.sumOf { it.value.toDouble() }.toFloat()


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Canvas(
            modifier = modifier
                .size(size)

                .padding(12.dp),
            onDraw = {
                var startAngle = 0f
                charts.forEach {
                    val sweepAngle = if (total > 0f) (it.value / total) * 360f else 0f


                    drawArc(
                        color = it.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(
                            width = strokeWidth.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )

                    startAngle += sweepAngle
                }
            }
        )

        Column(
            modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
                CategoryLegendGrid(charts=charts)


        }
    }
}




    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun StatisticsScreen( viewModel: Transacviewmodel = viewModel()) {

        val selectedMonth by viewModel.selectedMonth.collectAsState()
        val categoryTotals by viewModel.categoryTotals.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val months = remember { viewModel.generatePastMonths() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp), // Add horizontal padding here
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Month Selector - Removed unnecessary Box
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 4.dp) // Only vertical padding
            ) {
                items(months) { month ->
                    val isSelected = month.value == selectedMonth
                    MonthChip(
                        month = month,
                        isSelected = isSelected,
                        modifier = Modifier.padding(vertical = 4.dp),
                        onClick = {
                            val (m, y) = month.value.split("/")
                            viewModel.selectMonth(m, y)
                        }
                    )
                }
            }

            // Content - Removed Spacer and unnecessary Box
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 16.dp)
                    )
                }
                categoryTotals.isEmpty() -> {
                    Text(
                        text = "No expenses for ${months.find { it.value == selectedMonth }?.label ?: "this month"}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    val pieData = categoryTotals.map {
                        ChartModel(
                            name = it.category,
                            value = it.total.toFloat(),
                            color = getCategoryColor(it.category)
                        )
                    }
                    ChartCirclePie(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 4.dp), // Reduced top padding
                        charts = pieData,
                        size=180.dp
                        //strokeWidth =12.dp
                    )
                }
            }
        }


    }

fun getCategoryColor(category: String): Color {
return when(category.trim().lowercase()){
        "food" -> Color(0xFFF44336)
        "transport" -> Color(0xFFE91E63)
        "shopping" -> Color(0xFF9C27B0)
    "entertainment" -> Color(0xFF673AB7)
    "groceries" -> Color(0xFF3F51B5)
    "bills" -> Color(0xFF2196F3)
    "travel" -> Color(0xFF03A9F4)
    "takeout" -> Color(0xFF00BCD4)
    else -> Color.Gray
        /*{ val hash =category.hashCode()
        val r = (hash shr 16 and 0xFF)
        val g = (hash shr 8 and 0xFF)
        val b = (hash and 0xFF)
        return Color(r, g, b, alpha = 200)
    }

         */
        //Color(Random.nextLong(0xFFFFFFF)).copy(alpha = 0.8f)

    }
}


@Composable
    fun MonthChip(month: MonthItem, isSelected: Boolean, modifier: Modifier = Modifier,onClick: () -> Unit) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
            modifier = Modifier.clickable { onClick() }
        ) {
            Text(
                text = month.label,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    @Composable
    fun StatisticsScreenPreviewContent() {
        val sampleData = listOf(
            ChartModel(40f, Color.Red, "Food"),
            ChartModel(30f, Color.Green, "Transport"),
            ChartModel(30f, Color.Blue, "Shopping",)
        )
        Column {
            LazyRow(modifier = Modifier.padding(8.dp)) {
                items(
                    listOf(
                        MonthItem("Jan 2024", "01/2024"),
                        MonthItem("Feb 2024", "02/2024")
                    )
                ) { month ->
                    MonthChip(month = month, isSelected = month.value == "01/2024") {
                        /* Do nothing in preview */
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                ChartCirclePie(modifier = Modifier.align(Alignment.Center), charts = sampleData)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun StatisticsScreenPreview() {
        StatisticsScreenPreviewContent(

        )
    }


