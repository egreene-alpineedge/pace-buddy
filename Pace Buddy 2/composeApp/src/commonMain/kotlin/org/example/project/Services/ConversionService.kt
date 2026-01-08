package org.example.project.Services

import org.example.project.DistanceUnit
import org.example.project.PaceUnit
import org.example.project.SpeedUnit

class ConversionService {

    fun toMeters(value: Double, fromUnit: DistanceUnit): Double {
        return value * fromUnit.metersFactor
    }

    fun fromMeters(value: Double, toUnit: DistanceUnit): Double {
        return value / toUnit.metersFactor
    }

    fun toMetersPerSecond(value: Double, fromUnit: SpeedUnit): Double {
        return value * fromUnit.metersPerSecondsFactor
    }

    fun fromMetersPerSecond(value: Double, toUnit: SpeedUnit): Double {
        return value / toUnit.metersPerSecondsFactor
    }

    fun toSecondsPerMeter(value: Double, fromUnit: PaceUnit): Double {
        return value * fromUnit.secondsPerMeterFactor
    }

    fun fromSecondsPerMeter(value: Double, toUnit: PaceUnit): Double {
        return value / toUnit.secondsPerMeterFactor
    }

}