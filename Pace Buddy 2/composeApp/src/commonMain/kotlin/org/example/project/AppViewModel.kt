package org.example.project

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.example.project.Services.CalculationService
import org.example.project.Services.ConversionService

class AppViewModel(
    private val calculationService: CalculationService,
    private val conversionService: ConversionService
) {
    var time by mutableStateOf<Double?>(null)
        private set
    var distance by mutableStateOf<Double?>(null)
        private set
    var pace by mutableStateOf<Double?>(null) // value is seconds per mile
        private set
    var speed by mutableStateOf<Double?>(null) // value is seconds per mile
        private set

    fun onReset() {
        time = null
        distance = null
        pace = null
        speed = null
    }

    fun onBlurTimeField(value: Double) {
        time = value

        val t = time!!

        // Calculate Speed
        if (distance != null) {
            val d = distance!!
            val s = calculateSpeed(time = t, distance = d)
            speed = s
            pace = calculatePace(speed = s)
        }

        // Calculate Distance
        else if (speed != null) {
            val s = speed!!
            distance = calculateDistance(time = t, speed = s)
        }
    }
    fun onBlurDistanceField(value: Double) {
        println("Distance Blurred")
        distance = value

        val d = distance!!

        // Calculate Speed
        if (time != null) {
            val t = time!!
            val s = calculateSpeed(time = t, distance = d)
            speed = s
            pace = calculatePace(speed = s)
        }

        // Calculate Time
        else if (speed != null) {
            val s = speed!!
            time = calculateTime(distance = d, speed = s)
        }
    }

    fun onBlurSpeedField(value: Double) {
        println("Speed Blurred")
        speed = value

        val s = speed!!

        // Calculate Pace
        val p = calculatePace(speed = s)
        pace = p

        // Calculate Distance
        if (time != null) {
            val t = time!!
            distance = calculateDistance(time = t, speed = s)
        }

        // Calculate Time
        else if (distance != null) {
            val d = distance!!
            time = calculateTime(distance = d, speed = s)
        }
    }

    fun onBlurPaceField(text: String) {

//        // Convert Pace text to Double
//        try {
//            val convertedPace = text.toDouble()
//            pace = convertedPace
//        } catch (e: NumberFormatException) {
//            pace = null
//            return
//        }
//        val p = pace!!
//
//        // Calculate Speed
//        speed = calculateSpeed(pace = p)
//
//        // Calculate Distance
//        if (time != null) {
//            val t = time!!
//            distance = calculateDistance(time = t, speed = p)
//        }
//
//        // Calculate Time
//        else if (distance != null) {
//            val d = distance!!
//            time = calculateTime(distance = d, pace = p)
//        }


    }

    // -----------------------------------------------------------


    private fun calculateTime(distance: Double, speed: Double): Double {

        // Convert Distance to meters
        val setDistanceUnit = DistanceUnit.MI
        val distanceInMeters = conversionService.toMeters(value = distance, fromUnit = setDistanceUnit)

        // Convert Speed to m/s
        val setSpeedUnit = SpeedUnit.MI_HR
        val speedInMetersPerSecond = conversionService.toMetersPerSecond(value = speed, fromUnit = setSpeedUnit)

        // Calculate
        val timeInSeconds = calculationService.calculateTime(distance = distanceInMeters, speed = speedInMetersPerSecond)

        // No Preferred Time Unit

        // Return Result
        return timeInSeconds
    }
    private fun calculateDistance(time: Double, speed: Double): Double {

        // Time is already in seconds
        val timeInSeconds = time

        // Convert Speed to m/s
        val setSpeedUnit = SpeedUnit.MI_HR
        val speedInMetersPerSecond = conversionService.toMetersPerSecond(value = speed, fromUnit = setSpeedUnit)

        // Calculate
        val distanceInMeters = calculationService.calculateDistance(time = timeInSeconds, speed = speedInMetersPerSecond)

        // Convert to preferred Distance Unit
        val setDistanceUnit = DistanceUnit.MI
        val distanceResult = conversionService.fromMeters(value = distanceInMeters, toUnit = setDistanceUnit)

        // Return Result
        return distanceResult
    }
    private fun calculateSpeed(time: Double, distance: Double): Double {

        // Convert Distance to meters
        val setDistanceUnit = DistanceUnit.MI
        val distanceInMeters = conversionService.toMeters(value = distance, fromUnit = setDistanceUnit)

        // Time is already in seconds
        val timeInSeconds = time

        // Calculate
        val speedInMetersPerSecond = calculationService.calculateSpeed(time = timeInSeconds, distance = distanceInMeters)

        // Convert to Preferred Speed Unit
        val setSpeedUnit = SpeedUnit.MI_HR
        val speedResult = conversionService.fromMetersPerSecond(value = speedInMetersPerSecond, toUnit = setSpeedUnit)

        // Return Result
        return speedResult
    }


    private fun calculatePace(speed: Double): Double {
        return calculationService.calculatePace(speed = speed)
    }
    private fun calculateSpeed(pace: Double): Double {
        return calculationService.calculateSpeed(pace = pace)
    }

}