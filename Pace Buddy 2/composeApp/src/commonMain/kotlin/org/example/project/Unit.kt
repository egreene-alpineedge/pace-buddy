package org.example.project

enum class DistanceUnit(val metersFactor: Double) {
    KM(1000.0),                 // 1km = 1000m
    MI(1609.344)                // 1mi = 1609.344m
}

enum class SpeedUnit(val metersPerSecondsFactor: Double) {
    MI_HR(1609.344 / 3600.0),   // 1mi/hr = 1609.344m / 3600s
    KM_HR(1000.0 / 3600.0)      // 1km/hr = 1000m / 3600s
}

enum class PaceUnit(val secondsPerMeterFactor: Double) {
    SEC_MI(1 / 1609.344),    //   1sec/mi = 1 / 1609.344m
    SEC_KM(1 / 1000.0)       //   1sec/km = 1 / 1000m
}