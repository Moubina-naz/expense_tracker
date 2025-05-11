import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ChartModel
import com.example.expensetracker.R
import com.example.expensetracker.Room.MonthlyData
import com.example.expensetracker.Room.TransactionRepository
import com.example.expensetracker.Transacviewmodel
import com.example.expensetracker.charts

@Composable
fun ChartCirclePie(
    modifier: Modifier,
    charts: List<ChartModel>,
    size: Dp = 200.dp,
    strokeWidth: Dp = 16.dp,
    //viewModel : Transacviewmodel
) {
 /*   val monthlyData by viewModel.monthlyData.collectAsState(initial = emptyList())
    val categoryData by viewModel.categoryData.collectAsState(initial = emptyList())
    val selectedMonth by viewModel.selectedMonth.collectAsState()

    LazyColumn(modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){items(monthlyData) { m ->
        MonthlyStatisticsItem(m)
    }

    }

  */
    Canvas(
        modifier = modifier
            .size(size)
            .background(Color.LightGray)
            .padding(12.dp),
        onDraw = {
            var startAngle = 0f

            charts.forEach {
                val sweepAngle = (it.value / 100f) * 360f

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
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsItem(viewModel: Transacviewmodel) {
    // Collect the UI-ready monthly data from the viewModel
    val monthlyData by viewModel.uiMonthlyData.collectAsState(initial = emptyList())
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val categoryData by viewModel.categoryData.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(monthlyData) { monthData ->
                val isSelected = selectedMonth == monthData.monthYear

                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) colorResource(id = R.color.base) else Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            viewModel.selectMonth(monthData.monthYear) // ✅ correct call
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = monthData.monthName, // ✅ already in MonthlyData
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }

        // Additional stats content could go here...
    }
}

