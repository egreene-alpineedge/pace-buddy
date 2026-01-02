package org.example.project

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.example.project.Services.CalculationService

class AppViewModel(
    private val calculationService: CalculationService
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

    fun convertTimeTextToValue(text: String): Double {
        val parts = text.split(":")
        if (parts.count() == 2) {
            val minutes = parts[0].toDouble()
            val seconds = parts[1].toDouble()
            return (minutes*60) + seconds
        }
        else if (parts.count() == 3) {
            val hours = parts[0].toDouble()
            val minutes = parts[1].toDouble()
            val seconds = parts[2].toDouble()
            return (hours*60*60) + (minutes*60) + seconds
        }
        else {
            val seconds = parts[0].toDouble()
            return seconds
        }
    }

    fun onBlurTimeField(text: String) {
        try {
            val convertedTime = convertTimeTextToValue(text)
            time = convertedTime
        } catch (e: NumberFormatException) {
            time = null
            return
        }

        val t = time!!
        if (distance != null) {
            val d = distance!!
            val s = calculateSpeed(time = t, distance = d)
            speed = s
            pace = calculatePace(speed = s)
        }
        else if (speed != null) {
            val s = speed!!
            distance = calculateDistance(time = t, speed = s)
        }
    }
    fun onBlurDistanceField(text: String) {
        println("Distance Blurred")
        try {
            val convertedDistance = text.toDouble()
            distance = convertedDistance
        } catch (e: NumberFormatException) {
            distance = null
            return
        }

        val d = distance!!
        if (time != null) {
            val t = time!!
            val s = calculateSpeed(time = t, distance = d)
            speed = s
            pace = calculatePace(speed = s)
        }
        else if (speed != null) {
            val s = speed!!
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
    fun onBlurSpeedField(text: String) {
        // Convert Speed text to Double
        try {
            val convertedSpeed = text.toDouble()
            speed = convertedSpeed
        } catch (e: NumberFormatException) {
            speed = null
            return
        }
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


    // -----------------------------------------------------------


    private fun calculateTime(distance: Double, speed: Double): Double {
        return calculationService.calculateTime(distance = distance, speed = speed)
    }
    private fun calculateDistance(time: Double, speed: Double): Double {
        return calculationService.calculateDistance(time = time, speed = speed)
    }
    private fun calculateSpeed(time: Double, distance: Double): Double {
        return calculationService.calculateSpeed(time = time, distance = distance) * 3600
    }
    private fun calculatePace(speed: Double): Double {
        return calculationService.calculatePace(speed = speed)
    }
    private fun calculateSpeed(pace: Double): Double {
        return calculationService.calculateSpeed(pace = pace)
    }

}