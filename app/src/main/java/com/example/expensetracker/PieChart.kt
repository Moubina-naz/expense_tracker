import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.ChartModel
import com.example.expensetracker.Transacviewmodel
import kotlin.random.Random

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
fun StatisticsScreen() {
    val viewModel: Transacviewmodel = viewModel()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val categoryTotal by viewModel.categoryTotal.collectAsState(initial = emptyList())

  val pieData = categoryTotal.map{
      ChartModel(name = it.category, value = it.total,color = Color(Random.nextLong(0xFFFFFFF)))
  }
    val months = remember {
        viewModel.generatePastMonths()
    }
    Column(modifier = Modifier) {
        LazyRow (modifier = Modifier
            .fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ){items(months){months->
            val isSelected = months.value == selectedMonth


        }

        }
    }
    Box(modifier = Modifier){
        ChartCirclePie(modifier = Modifier.align(Alignment.Center), charts = pieData)

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    StatisticsScreen()
}

