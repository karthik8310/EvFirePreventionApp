package com.ev.fireprevention.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


import com.ev.fireprevention.EVFirePreventionApplication
import com.ev.fireprevention.data.local.entity.HistoryLog
import com.ev.fireprevention.ui.theme.BrandBlue
import com.ev.fireprevention.ui.theme.BrandGreen
import com.ev.fireprevention.ui.theme.BrandPrimary
import com.ev.fireprevention.ui.theme.BrandRed
import com.ev.fireprevention.ui.theme.BrandSurfaceVariant
import com.ev.fireprevention.ui.theme.BrandTextPrimary
import com.ev.fireprevention.ui.theme.BrandTextSecondary
import com.ev.fireprevention.ui.theme.BrandYellow
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as EVFirePreventionApplication
    val repository = application.historyRepository
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(repository)
    )
    val logs by viewModel.recentLogs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "History Analysis",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = BrandTextPrimary
            )
            Row {
                IconButton(onClick = { viewModel.addDummyData() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Simulate Data", tint = BrandPrimary)
                }
                IconButton(onClick = { viewModel.clearHistory() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Clear History", tint = BrandRed)
                }
            }
        }

        if (logs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No history data available. Tap refresh to simulate.", color = BrandTextSecondary)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    StyledLineChart(
                        title = "SOC Trend (%)",
                        logs = logs,
                        valueSelector = { it.soc },
                        lineColor = BrandGreen,
                        unit = "%"
                    )
                }
                item {
                    StyledLineChart(
                        title = "SOH Trend (%)",
                        logs = logs,
                        valueSelector = { it.soh },
                        lineColor = BrandBlue,
                        unit = "%"
                    )
                }
                item {
                    StyledLineChart(
                        title = "Pack Voltage (V)",
                        logs = logs,
                        valueSelector = { it.packVoltage.toFloat() },
                        lineColor = BrandYellow,
                        unit = "V"
                    )
                }
                
                item {
                    Text(
                        text = "Recent Logs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrandTextPrimary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(logs) { log ->
                    HistoryLogItem(log)
                }
            }
        }
    }
}

@Composable
fun StyledLineChart(
    title: String,
    logs: List<HistoryLog>,
    valueSelector: (HistoryLog) -> Float,
    lineColor: Color,
    unit: String
) {
    val chartEntryModel = remember(logs) {
        val entries = logs.mapIndexed { index, log ->
            FloatEntry(index.toFloat(), valueSelector(log))
        }
        entryModelOf(entries)
    }

    val markerFormatter = remember(logs, unit) {
        com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter { markedEntries, _ ->
            val entry = markedEntries.firstOrNull()?.entry as? FloatEntry
            val index = entry?.x?.toInt() ?: 0
            if (index in logs.indices) {
                val log = logs[index]
                val date = Date(log.timestamp)
                val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date)
                val value = String.format("%.1f", entry?.y)
                "$time\n$value $unit"
            } else {
                ""
            }
        }
    }

    val marker = rememberMarker(markerFormatter)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title, 
                style = MaterialTheme.typography.titleMedium, 
                fontWeight = FontWeight.Bold,
                color = BrandTextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            val lineSpec = com.patrykandpatrick.vico.compose.chart.line.lineSpec(
                lineColor = lineColor,
                lineBackgroundShader = DynamicShaders.fromBrush(
                    Brush.verticalGradient(
                        colors = listOf(
                            lineColor.copy(alpha = 0.5f),
                            lineColor.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
            )

            Chart(
                chart = lineChart(
                    lines = listOf(lineSpec)
                ),
                model = chartEntryModel,
                startAxis = rememberStartAxis(
                    label = com.patrykandpatrick.vico.compose.component.textComponent(
                        color = BrandTextSecondary,
                        textSize = 10.sp
                    ),
                    guideline = com.patrykandpatrick.vico.compose.component.lineComponent(
                        color = Color.Gray.copy(alpha = 0.2f)
                    ),
                    valueFormatter = { value, _ -> 
                        "${value.toInt()} $unit" 
                    }
                ),
                bottomAxis = rememberBottomAxis(
                    label = com.patrykandpatrick.vico.compose.component.textComponent(
                        color = BrandTextSecondary,
                        textSize = 10.sp
                    ),
                    guideline = null,
                    valueFormatter = { value, _ ->
                        val index = value.toInt()
                        if (index in logs.indices) {
                            val date = Date(logs[index].timestamp)
                            SimpleDateFormat("mm:ss", Locale.getDefault()).format(date)
                        } else {
                            ""
                        }
                    }
                ),
                marker = marker,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun HistoryLogItem(log: HistoryLog) {
    val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = dateFormat.format(Date(log.timestamp)),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = BrandTextPrimary
                )
                Text(
                    text = "SOC: ${log.soc.toInt()}% | SOH: ${log.soh.toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = BrandTextSecondary
                )
            }
            Text(
                text = "${String.format("%.1f", log.packVoltage)} V",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary
            )
        }
    }
}
