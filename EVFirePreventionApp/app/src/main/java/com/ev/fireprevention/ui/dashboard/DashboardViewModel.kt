package com.ev.fireprevention.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ev.fireprevention.utils.NotificationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val notificationHelper = NotificationHelper(application)
    private var isSimulationRunning = true

    init {
        startDataSimulation()
    }

    private fun startDataSimulation() {
        viewModelScope.launch {
            while (isSimulationRunning) {
                updateSimulationData()
                delay(3000) // Update every 3 seconds
            }
        }
    }

    private fun updateSimulationData() {
        // 1. Battery Data
        val soc = Random.nextFloat() * 100
        val soh = 90f + Random.nextFloat() * 10
        val packVoltage = 40.0 + Random.nextDouble() * 10.0
        // Simulate current: positive = discharging (driving), negative = charging
        val isDriving = Random.nextBoolean()
        val packCurrent = if (isDriving) {
            Random.nextDouble(10.0, 100.0) // Discharging
        } else {
            -Random.nextDouble(0.0, 50.0) // Charging or Idle
        }
        val packPower = packVoltage * packCurrent
        val temp = 20.0 + Random.nextDouble() * 30.0
        val cellVoltages = List(12) { 3.0 + Random.nextDouble() * 1.2 }
        val minCell = cellVoltages.minOrNull() ?: 0.0
        val maxCell = cellVoltages.maxOrNull() ?: 0.0
        val cellBalance = if ((maxCell - minCell) > 0.1) "Imbalanced" else "Balanced"
        
        val batteryData = BatteryData(
            packVoltage = packVoltage,
            packCurrent = packCurrent,
            packPower = packPower,
            soc = soc,
            soh = soh,
            temperature = temp,
            cellVoltages = cellVoltages,
            cellBalanceStatus = cellBalance
        )

        // 2. Motor Data
        val rpm = if (packCurrent > 0) (packCurrent * 50).toInt() + Random.nextInt(-100, 100) else 0
        val motorTemp = 30.0 + Random.nextDouble() * 50.0
        val motorCurrent = if (rpm > 0) packCurrent else 0.0
        val torque = if (rpm > 0) packCurrent * 2.5 else 0.0
        
        val motorData = MotorData(
            rpm = rpm,
            temperature = motorTemp,
            current = motorCurrent,
            torque = torque
        )

        // 3. Charging Data
        val isCharging = packCurrent < -1.0
        val chargeCurrent = if (isCharging) -packCurrent else 0.0
        val chargeVoltage = if (isCharging) packVoltage else 0.0
        val chargeTime = if (isCharging) Random.nextLong(0, 3600) else 0
        
        val chargingData = ChargingData(
            isCharging = isCharging,
            chargeCurrent = chargeCurrent,
            chargeVoltage = chargeVoltage,
            chargingTimeSeconds = chargeTime
        )

        // 4. Telematics Data
        val speed = if (rpm > 0) (rpm / 100.0) * 1.2 else 0.0
        val lat = 12.9716 + Random.nextDouble(-0.001, 0.001)
        val lon = 77.5946 + Random.nextDouble(-0.001, 0.001)
        val ambientTemp = 25.0 + Random.nextDouble(-2.0, 2.0)
        
        val telematicsData = TelematicsData(
            speed = speed,
            latitude = lat,
            longitude = lon,
            ambientTemp = ambientTemp
        )

        // 5. ML Predictions
        val chargingClass = if (chargeCurrent > 20) "Fast" else if (chargeCurrent > 1) "Slow" else "N/A"
        val healthPred = "${soh.toInt()}%"
        val faultStatus = if (temp > 45 || cellBalance == "Imbalanced") "Faulty" else "Normal"
        val range = (soc * 3.5).toInt().toString() + " km"

        val mlPredictions = MLPredictions(
            chargingClass = chargingClass,
            batteryHealthPred = healthPred,
            faultDetection = faultStatus,
            energyForecast = range
        )

        // Alerts Logic
        val alerts = mutableListOf<AlertItem>()
        var fireRisk = "Low"
        
        // High Temp Alert
        if (temp > 45.0) {
            alerts.add(AlertItem("High Temperature", "Pack temp is ${String.format("%.1f", temp)}°C", AlertSeverity.HIGH))
            fireRisk = "High"
            notificationHelper.showNotification("High Temperature Alert", "Battery pack temperature is critical: ${String.format("%.1f", temp)}°C", 1001)
        } else if (temp > 40.0) {
            fireRisk = "Medium"
        }

        // Low SOC Alert
        if (soc < 20.0) {
            alerts.add(AlertItem("Low Battery", "SOC is below 20%", AlertSeverity.MEDIUM))
            if (fireRisk == "Low") fireRisk = "Medium"
        }

        // Cell Imbalance Alert
        if (cellBalance == "Imbalanced") {
            alerts.add(AlertItem("Cell Imbalance", "Voltage deviation detected", AlertSeverity.LOW))
        }

        // ... (previous code)

        // Update State
        _uiState.update { currentState ->
            currentState.copy(
                deviceId = "EV-SIM-2025",
                battery = batteryData,
                motor = motorData,
                charging = chargingData,
                telematics = telematicsData,
                mlPredictions = mlPredictions,
                alerts = alerts,
                fireRisk = fireRisk,
                isLoading = false
            )
        }

        // Log to History
        viewModelScope.launch {
            try {
                val app = getApplication<com.ev.fireprevention.EVFirePreventionApplication>()
                // historyRepository is initialized in Application.onCreate, so it should be safe to access.
                
                val historyLog = com.ev.fireprevention.data.local.entity.HistoryLog(
                    timestamp = System.currentTimeMillis(),
                    soc = soc,
                    soh = soh,
                    packVoltage = packVoltage,
                    packCurrent = packCurrent,
                    temperature = temp,
                    type = "CYCLE"
                )
                app.historyRepository.insertLog(historyLog)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
