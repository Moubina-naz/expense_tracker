package com.example.expensetracker

import android.graphics.Color.toArgb
import android.graphics.Paint
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.io.path.Path
import kotlin.io.path.moveTo


@Composable
fun LineChartScreen(modifier : Modifier,
                    xValues: List<Int>,
                    yValues: List<Int>,
                    points: List<Float>,
                    paddingSpace: Dp,
                    verticalStep: Int,
                    graphAppearance: GraphAppearance = GraphAppearance()) {
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = graphAppearance.graphAxisColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Box(
        modifier = modifier
            //.background(graphAppearance.backgroundColor)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val xAxisSpace = (size.width - paddingSpace.toPx()) / (xValues.size - 1)
            val yAxisSpace = size.height / yValues.size

            // Draw X axis labels
            xValues.forEachIndexed { index, value ->
                drawContext.canvas.nativeCanvas.drawText(
                    "$value",
                    xAxisSpace * index + paddingSpace.toPx(),
                    size.height - 10,
                    textPaint
                )
            }

            // Draw Y axis labels
            yValues.forEachIndexed { index, value ->
                drawContext.canvas.nativeCanvas.drawText(
                    "$value",
                    paddingSpace.toPx() / 2,
                    size.height - (yAxisSpace * index),
                    textPaint
                )
            }

            // Compute data points
            val coordinates = points.mapIndexed { index, point ->
                val x = xAxisSpace * index + paddingSpace.toPx()
                val y = size.height - (point / verticalStep) * yAxisSpace
                PointF(x, y).also {
                    if (graphAppearance.isCircleVisible) {
                        drawCircle(
                            color = graphAppearance.circleColor,
                            radius = 8f,
                            center = Offset(x, y)
                        )
                    }
                }
            }

            // Smooth path with quadratic curve
            val path = androidx.compose.ui.graphics.Path().apply {
                if (coordinates.size >= 2) {
                    moveTo(coordinates.first().x, coordinates.first().y)

                    for (i in 1 until coordinates.size - 1) {
                        val p0 = coordinates[maxOf(i - 1, 0)]
                        val p1 = coordinates[i]
                        val p2 = coordinates[minOf(i + 1, coordinates.size - 1)]

                        // Control points for Catmull-Rom
                        val control1X = p1.x + (p2.x - p0.x) / 6f
                        val control1Y = p1.y + (p2.y - p0.y) / 6f
                        val control2X = p2.x - (p2.x - p0.x) / 6f
                        val control2Y = p2.y - (p2.y - p0.y) / 6f

                        cubicTo(
                            control1X, control1Y,
                            control2X, control2Y,
                            p2.x, p2.y
                        )
                    }
                }
            }

            // Fill area under curve
            if (graphAppearance.isColorAreaUnderChart) {
                val fillPath = androidx.compose.ui.graphics.Path().apply {
                    addPath(path)
                    lineTo(coordinates.last().x, size.height)
                    lineTo(coordinates.first().x, size.height)
                    close()
                }
                drawPath(
                    fillPath,
                    brush = Brush.verticalGradient(
                        listOf(
                            graphAppearance.colorAreaUnderChart,
                            Color.Transparent
                        ),
                        endY = size.height
                    )
                )
            }

            drawPath(
                path,
                color = graphAppearance.graphColor,
                style = Stroke(
                    width = graphAppearance.graphThickness,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}

data class GraphAppearance(
    //val backgroundColor: Color = Color(0xFF101010),
    val graphColor: Color = Color.Cyan,
    val graphAxisColor: Color = Color.Gray,
    val circleColor: Color = Color.Red,
    val isCircleVisible: Boolean = true,
    val isColorAreaUnderChart: Boolean = true,
    val colorAreaUnderChart: Color = Color.Cyan.copy(alpha = 0.3f),
    val graphThickness: Float = 4f
)

@Preview(showBackground = true)
@Composable
fun LineChartScreenPreview() {
    val xValues = listOf(1, 2, 3, 4, 5, 6)
    val yValues = listOf(0, 20, 40, 60, 80, 100)
    val points = listOf(10f, 30f, 50f, 20f, 60f, 90f)

    LineChartScreen(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        xValues = xValues,
        yValues = yValues,
        points = points,
        paddingSpace = 32.dp,
        verticalStep = 20,
        graphAppearance = GraphAppearance()
    )
}
