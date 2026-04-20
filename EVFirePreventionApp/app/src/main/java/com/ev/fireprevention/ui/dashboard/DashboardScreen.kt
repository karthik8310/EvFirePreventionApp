package com.ev.fireprevention.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import com.ev.fireprevention.data.model.User
import androidx.compose.ui.graphics.Brush
import com.ev.fireprevention.ui.theme.BrandGradientStart
import com.ev.fireprevention.ui.theme.BrandGradientEnd
import com.ev.fireprevention.ui.theme.BrandSurfaceVariant
import com.ev.fireprevention.ui.theme.BrandPrimary
import com.ev.fireprevention.ui.theme.BrandAccent
import com.ev.fireprevention.ui.theme.BrandTextPrimary
import com.ev.fireprevention.ui.theme.BrandTextSecondary
import com.ev.fireprevention.ui.theme.BrandSuccess
import com.ev.fireprevention.ui.theme.BrandError
import com.ev.fireprevention.ui.theme.BrandSurface
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ev.fireprevention.ui.theme.BrandBlue
import com.ev.fireprevention.ui.theme.BrandGreen
import com.ev.fireprevention.ui.theme.BrandRed
import com.ev.fireprevention.ui.theme.BrandYellow
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Map
import com.ev.fireprevention.ui.theme.BrandDarkGray
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.SideEffect

// TODO: Replace with real ViewModel state
// To wire this up:
// 1. Create DashboardViewModel that exposes a StateFlow<DashboardUiState>
// 2. In MainActivity/NavGraph, collect the state and pass it to DashboardScreen
// 3. Replace dummy data initialization with data from Repository/UseCases

// --- Data Models ---

data class DashboardUiState(
    val deviceId: String = "Unknown",
    val battery: BatteryData = BatteryData(),
    val motor: MotorData = MotorData(),
    val charging: ChargingData = ChargingData(),
    val telematics: TelematicsData = TelematicsData(),
    val mlPredictions: MLPredictions = MLPredictions(),
    val alerts: List<AlertItem> = emptyList(),
    val fireRisk: String = "Low", // Added back for Summary Card
    val isLoading: Boolean = false
)

data class BatteryData(
    val packVoltage: Double = 0.0,
    val packCurrent: Double = 0.0,
    val packPower: Double = 0.0, // V * I
    val soc: Float = 0f,
    val soh: Float = 0f,
    val temperature: Double = 0.0,
    val cellVoltages: List<Double> = emptyList(),
    val cellBalanceStatus: String = "Balanced"
)

data class MotorData(
    val rpm: Int = 0,
    val temperature: Double = 0.0,
    val current: Double = 0.0,
    val torque: Double = 0.0
)

data class ChargingData(
    val isCharging: Boolean = false,
    val chargeCurrent: Double = 0.0,
    val chargeVoltage: Double = 0.0,
    val chargingTimeSeconds: Long = 0
)

data class TelematicsData(
    val speed: Double = 0.0, // km/h
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val ambientTemp: Double = 0.0
)

data class MLPredictions(
    val chargingClass: String = "N/A", // Fast, Slow
    val batteryHealthPred: String = "N/A", // SoH % or RUL
    val faultDetection: String = "Normal", // Normal, Faulty, Overheating, etc.
    val energyForecast: String = "N/A" // Range in km
)

data class AlertItem(
    val title: String,
    val message: String,
    val severity: AlertSeverity
)

enum class AlertSeverity {
    LOW, MEDIUM, HIGH
}

enum class BottomNavTab {
    DASHBOARD, HISTORY, ALERTS, PROFILE
}

// --- Main Screen ---



@Composable
fun DashboardScreen(
    state: DashboardUiState,
    userProfile: User? = null,
    onNav: (String) -> Unit = {}, // Placeholder for navigation
    onLogout: () -> Unit = {}
) {
    var currentBottomTab by remember { mutableStateOf(BottomNavTab.DASHBOARD) }

    Scaffold(
        bottomBar = {
            DashboardBottomBar(
                currentTab = currentBottomTab,
                onTabSelected = { currentBottomTab = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(BrandGradientStart, BrandGradientEnd)
                    )
                )
                .padding(paddingValues)
        ) {
            when (currentBottomTab) {
                BottomNavTab.DASHBOARD -> DashboardContent(state)
                BottomNavTab.HISTORY -> com.ev.fireprevention.ui.history.HistoryScreen()
                BottomNavTab.ALERTS -> AlertsPage(state.alerts)
                BottomNavTab.PROFILE -> ProfilePage(userProfile, onLogout)
            }
        }
    }
}



@Composable
fun DashboardContent(
    state: DashboardUiState,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Battery", "Motor", "Charging", "Telematics")

    // Request Notification Permission on Android 13+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { /* Handle result if needed */ }
        )
        
        LaunchedEffect(Unit) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        Box(modifier = Modifier.padding(16.dp)) {
            DashboardHeader(deviceId = state.deviceId)
        }

        // Custom Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            contentColor = BrandPrimary,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BrandAccent,
                        height = 3.dp
                    )
                }
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                androidx.compose.material3.Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { 
                        Text(
                            text = title, 
                            maxLines = 1, 
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                        ) 
                    },
                    selectedContentColor = BrandAccent,
                    unselectedContentColor = BrandTextSecondary
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            when (selectedTab) {
                0 -> BatteryPage(state)
                1 -> MotorPage(state)
                2 -> ChargingPage(state)
                3 -> TelematicsPage(state)
            }

            if (state.alerts.isNotEmpty()) {
                AlertsSection(state.alerts)
            }
        }
    }
}

@Composable
fun BatteryPage(state: DashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MLPredictionCard(
            label = "Battery Health Prediction (SoH)",
            value = state.mlPredictions.batteryHealthPred,
            icon = Icons.Default.HealthAndSafety
        )
        
        MLPredictionCard(
            label = "Fault Detection",
            value = state.mlPredictions.faultDetection,
            icon = Icons.Default.Warning,
            isGood = state.mlPredictions.faultDetection == "Normal"
        )

        SummarySection(state)
        
        PackStatsRow(state)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BrandSurfaceVariant.copy(alpha = 0.7f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("BMS Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = BrandPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow("Pack Current", "${String.format("%.1f", state.battery.packCurrent)} A")
                DetailRow("Pack Power", "${String.format("%.1f", state.battery.packPower)} W")
                DetailRow("Cell Balance", state.battery.cellBalanceStatus)
            }
        }

        CellVoltagesSection(state.battery.cellVoltages)
    }
}

@Composable
fun MotorPage(state: DashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MLPredictionCard(
            label = "Motor Fault Detection",
            value = "Normal",
            icon = Icons.Default.Settings
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Speed,
                label = "RPM",
                value = "${state.motor.rpm}",
                color = BrandBlue
            )
            StatItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Thermostat,
                label = "Temp",
                value = "${String.format("%.1f", state.motor.temperature)} °C",
                color = if (state.motor.temperature > 80) BrandRed else BrandGreen
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Bolt,
                label = "Current",
                value = "${String.format("%.1f", state.motor.current)} A",
                color = BrandYellow
            )
            StatItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Build,
                label = "Torque",
                value = "${String.format("%.1f", state.motor.torque)} Nm",
                color = BrandDarkGray
            )
        }
    }
}

@Composable
fun ChargingPage(state: DashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MLPredictionCard(
            label = "Charging Classification",
            value = state.mlPredictions.chargingClass,
            icon = Icons.Default.Bolt
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = if (state.charging.isCharging) BrandGreen.copy(alpha = 0.1f) else BrandSurfaceVariant.copy(alpha = 0.7f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (state.charging.isCharging) Icons.Default.Bolt else Icons.Default.PowerOff,
                    contentDescription = null,
                    tint = if (state.charging.isCharging) BrandGreen else Color.Gray,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (state.charging.isCharging) "Charging Active" else "Not Charging",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (state.charging.isCharging) {
                    Text(
                        text = "Time: ${state.charging.chargingTimeSeconds / 60} min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (state.charging.isCharging) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Bolt,
                    label = "Voltage",
                    value = "${String.format("%.1f", state.charging.chargeVoltage)} V",
                    color = BrandYellow
                )
                StatItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.ElectricalServices,
                    label = "Current",
                    value = "${String.format("%.1f", state.charging.chargeCurrent)} A",
                    color = BrandBlue
                )
            }
        }
    }
}

@Composable
fun TelematicsPage(state: DashboardUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MLPredictionCard(
            label = "Energy Forecast (Range)",
            value = state.mlPredictions.energyForecast,
            icon = Icons.Default.Map
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Speed,
                label = "Speed",
                value = "${String.format("%.1f", state.telematics.speed)} km/h",
                color = BrandBlue
            )
            StatItem(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Thermostat,
                label = "Ambient",
                value = "${String.format("%.1f", state.telematics.ambientTemp)} °C",
                color = BrandDarkGray
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BrandSurfaceVariant.copy(alpha = 0.7f))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = BrandRed, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Location", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        "${String.format("%.4f", state.telematics.latitude)}, ${String.format("%.4f", state.telematics.longitude)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun MLPredictionCard(
    label: String,
    value: String,
    icon: ImageVector,
    isGood: Boolean = true
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BrandSurfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandPrimary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandTextSecondary
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BrandTextPrimary
                )
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = BrandTextSecondary)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = BrandTextPrimary)
    }
}

@Composable
fun DashboardHeader(deviceId: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Battery Health Dashboard",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = BrandTextPrimary
            )
            Text(
                text = "Device: $deviceId",
                style = MaterialTheme.typography.bodyMedium,
                color = BrandTextSecondary
            )
        }
        LiveStatusIndicator()
    }
}

@Composable
fun LiveStatusIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Circle,
            contentDescription = "Live",
            tint = BrandSuccess.copy(alpha = alpha),
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Live",
            style = MaterialTheme.typography.labelSmall,
            color = BrandSuccess,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SummarySection(state: DashboardUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            modifier = Modifier.weight(1f),
            title = "SOC",
            content = {
                CircularProgressWithText(
                    progress = state.battery.soc / 100f,
                    text = "${state.battery.soc.toInt()}%",
                    color = getColorForSoc(state.battery.soc)
                )
            }
        )

        SummaryCard(
            modifier = Modifier.weight(1f),
            title = "SOH",
            content = {
                CircularProgressWithText(
                    progress = state.battery.soh / 100f,
                    text = "${state.battery.soh.toInt()}%",
                    color = BrandBlue
                )
            }
        )

        SummaryCard(
            modifier = Modifier.weight(1f),
            title = "Fire Risk",
            content = {
                FireRiskBadge(risk = state.fireRisk)
            }
        )
    }
}

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurfaceVariant.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = BrandTextPrimary
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

@Composable
fun CircularProgressWithText(progress: Float, text: String, color: Color) {
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(70.dp),
            color = BrandSurface,
            strokeWidth = 8.dp,
            trackColor = BrandSurface,
        )
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(70.dp),
            color = color,
            strokeWidth = 8.dp,
            strokeCap = StrokeCap.Round,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = BrandTextPrimary
        )
    }
}

@Composable
fun FireRiskBadge(risk: String) {
    val (color, label) = when (risk.lowercase()) {
        "low" -> BrandSuccess to "Low"
        "medium" -> BrandAccent to "Medium"
        "high" -> BrandError to "High"
        else -> Color.Gray to "Unknown"
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun PackStatsRow(state: DashboardUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Bolt,
            label = "Voltage",
            value = "${String.format("%.1f", state.battery.packVoltage)} V",
            color = BrandYellow
        )
        StatItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Thermostat,
            label = "Temp",
            value = "${String.format("%.1f", state.battery.temperature)} °C",
            color = if (state.battery.temperature > 40) BrandError else BrandBlue
        )
        StatItem(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.History,
            label = "Power",
            value = "${String.format("%.0f", state.battery.packPower)} W",
            color = BrandTextSecondary
        )
    }
}

@Composable
fun StatItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BrandSurfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandTextSecondary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BrandTextPrimary
            )
        }
    }
}

@Composable
fun CellVoltagesSection(voltages: List<Double>) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Cell Voltages",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(voltages.withIndex().toList()) { (index, voltage) ->
                    CellVoltageBar(index + 1, voltage)
                }
            }
        }
    }
}

@Composable
fun CellVoltageBar(cellIndex: Int, voltage: Double) {
    // Dummy logic for deviation: assume nominal is ~3.7V. Highlight if far off.
    // In real app, calculate deviation from average.
    val isDeviant = voltage < 3.0 || voltage > 4.2 // Example thresholds
    val barColor = if (isDeviant) BrandRed else BrandGreen
    val heightFraction = ((voltage - 2.5) / (4.5 - 2.5)).coerceIn(0.1, 1.0).toFloat()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.height(120.dp)
    ) {
        Text(
            text = String.format("%.2f", voltage),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (isDeviant) BrandRed else MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(16.dp)
                .weight(1f, fill = false) // Fill available space up to max
                .height(100.dp * heightFraction) // Dynamic height
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(barColor)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "C$cellIndex",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AlertsSection(alerts: List<AlertItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Active Alerts",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp)
        )
        alerts.forEach { alert ->
            AlertCard(alert)
        }
    }
}

@Composable
fun AlertCard(alert: AlertItem) {
    val (bgColor, iconColor, icon) = when (alert.severity) {
        AlertSeverity.HIGH -> Triple(BrandRed.copy(alpha = 0.1f), BrandRed, Icons.Default.Warning)
        AlertSeverity.MEDIUM -> Triple(BrandYellow.copy(alpha = 0.1f), BrandYellow, Icons.Default.BatteryAlert)
        AlertSeverity.LOW -> Triple(BrandBlue.copy(alpha = 0.1f), BrandBlue, Icons.Default.Notifications)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = alert.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DashboardBottomBar(
    currentTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit
) {
    NavigationBar(
        containerColor = BrandSurfaceVariant, // Darker bottom bar
        contentColor = BrandPrimary,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = currentTab == BottomNavTab.DASHBOARD,
            onClick = { onTabSelected(BottomNavTab.DASHBOARD) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPrimary,
                selectedTextColor = BrandPrimary,
                indicatorColor = BrandPrimary.copy(alpha = 0.2f),
                unselectedIconColor = BrandTextSecondary,
                unselectedTextColor = BrandTextSecondary
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.History, contentDescription = "History") },
            label = { Text("History") },
            selected = currentTab == BottomNavTab.HISTORY,
            onClick = { onTabSelected(BottomNavTab.HISTORY) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPrimary,
                selectedTextColor = BrandPrimary,
                indicatorColor = BrandPrimary.copy(alpha = 0.2f),
                unselectedIconColor = BrandTextSecondary,
                unselectedTextColor = BrandTextSecondary
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
            label = { Text("Alerts") },
            selected = currentTab == BottomNavTab.ALERTS,
            onClick = { onTabSelected(BottomNavTab.ALERTS) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPrimary,
                selectedTextColor = BrandPrimary,
                indicatorColor = BrandPrimary.copy(alpha = 0.2f),
                unselectedIconColor = BrandTextSecondary,
                unselectedTextColor = BrandTextSecondary
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentTab == BottomNavTab.PROFILE,
            onClick = { onTabSelected(BottomNavTab.PROFILE) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BrandPrimary,
                selectedTextColor = BrandPrimary,
                indicatorColor = BrandPrimary.copy(alpha = 0.2f),
                unselectedIconColor = BrandTextSecondary,
                unselectedTextColor = BrandTextSecondary
            )
        )
    }
}



@Composable
fun AlertsPage(alerts: List<AlertItem>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Active Alerts", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        
        if (alerts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No active alerts", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            }
        } else {
            AlertsSection(alerts)
        }
    }
}

@Composable
fun ProfilePage(user: User?, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        
        Text("User Profile", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailRow("Username", user?.name ?: "Guest")
                DetailRow("Email", user?.email ?: "N/A")
                DetailRow("Phone", user?.phone ?: "N/A")
                DetailRow("App Version", "1.0.0")
            }
        }
        
        androidx.compose.material3.Button(
            onClick = onLogout,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = BrandRed)
        ) {
            Text("Logout")
        }
    }
}

// --- Helpers ---

fun getColorForSoc(soc: Float): Color {
    return when {
        soc <= 20 -> BrandRed
        soc <= 50 -> BrandYellow
        else -> BrandGreen
    }
}

// --- Previews ---

@Preview(showBackground = true, heightDp = 1000)
@Composable
fun DashboardScreenPreview() {
    val dummyState = DashboardUiState(
        deviceId = "EV-BAT-2025-X",
        battery = BatteryData(
            packVoltage = 48.2,
            packCurrent = 15.5,
            packPower = 747.1,
            soc = 78f,
            soh = 94f,
            temperature = 32.5,
            cellVoltages = listOf(3.9, 3.92, 3.88, 3.91, 3.9, 3.89, 3.93, 3.9, 3.85, 3.91, 3.92, 3.89),
            cellBalanceStatus = "Balanced"
        ),
        motor = MotorData(
            rpm = 3500,
            temperature = 65.0,
            current = 15.5,
            torque = 120.0
        ),
        charging = ChargingData(
            isCharging = false
        ),
        telematics = TelematicsData(
            speed = 45.0,
            latitude = 12.9716,
            longitude = 77.5946,
            ambientTemp = 28.0
        ),
        mlPredictions = MLPredictions(
            batteryHealthPred = "94%",
            faultDetection = "Normal",
            energyForecast = "270 km"
        ),
        fireRisk = "Low",
        alerts = listOf(
            AlertItem("Cell Imbalance", "Minor deviation in Cell 9", AlertSeverity.LOW)
        )
    )
    DashboardScreen(state = dummyState)
}

@Preview(showBackground = true, name = "Critical State")
@Composable
fun DashboardScreenCriticalPreview() {
    val criticalState = DashboardUiState(
        deviceId = "EV-BAT-CRIT-01",
        battery = BatteryData(
            packVoltage = 42.0,
            packCurrent = 80.0,
            packPower = 3360.0,
            soc = 15f,
            soh = 60f,
            temperature = 65.0,
            cellVoltages = listOf(3.2, 3.1, 2.8, 3.2, 3.1, 3.0, 2.9, 3.2, 2.5, 3.1, 3.0, 3.1),
            cellBalanceStatus = "Imbalanced"
        ),
        motor = MotorData(
            rpm = 0,
            temperature = 85.0,
            current = 0.0,
            torque = 0.0
        ),
        charging = ChargingData(
            isCharging = false
        ),
        telematics = TelematicsData(
            speed = 0.0,
            latitude = 12.9716,
            longitude = 77.5946,
            ambientTemp = 35.0
        ),
        mlPredictions = MLPredictions(
            batteryHealthPred = "60%",
            faultDetection = "Faulty",
            energyForecast = "50 km"
        ),
        fireRisk = "High",
        alerts = listOf(
            AlertItem("High Temperature", "Pack temp critical (65°C)", AlertSeverity.HIGH),
            AlertItem("Low Voltage", "Cell 9 critical voltage", AlertSeverity.HIGH),
            AlertItem("Low SOC", "Battery needs charging", AlertSeverity.MEDIUM)
        )
    )
    DashboardScreen(state = criticalState)
}
