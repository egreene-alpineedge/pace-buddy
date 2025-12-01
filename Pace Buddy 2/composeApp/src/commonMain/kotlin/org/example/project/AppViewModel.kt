package org.example.project

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class AppViewModel(
    private val calculationService: CalculationService
) {
    var time by mutableStateOf<Double?>(null)
        private set
    var distance by mutableStateOf<Double?>(null)
        private set
    var pace by mutableStateOf<Double?>(null) // value is seconds per mile
        private set


    fun onBlurTimeField(text: String) {
        try {
            val convertedTime = text.toDouble()
            time = convertedTime
        } catch (e: NumberFormatException) {
            time = null
            return
        }

        val t = time!!
        if (distance != null) {
            val d = distance!!
            pace = calculatePace(time = t, distance = d)
        }
        else if (pace != null) {
            val p = pace!!
            distance = calculateDistance(time = t, pace = p)
        }
    }
    fun onBlurDistanceField(text: String) {
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
            pace = calculatePace(time = t, distance = d)
        }
        else if (pace != null) {
            val p = pace!!
            time = calculateTime(distance = d, pace = p)
        }
    }
    fun onBlurPaceField(text: String) {
        try {
            val convertedPace = text.toDouble()
            pace = convertedPace
        } catch (e: NumberFormatException) {
            pace = null
            return
        }

        val p = pace!!
        if (time != null) {
            val t = time!!
            distance = calculateDistance(time = t, pace = p)
        }
        else if (distance != null) {
            val d = distance!!
            time = calculateTime(distance = d, pace = p)
        }

    }


    // -----------------------------------------------------------


    private fun calculateTime(distance: Double, pace: Double): Double {
        return calculationService.calculateTime(distance = distance, pace = pace)
    }
    private fun calculateDistance(time: Double, pace: Double): Double {
        return calculationService.calculateDistance(time = time, pace = pace)
    }
    private fun calculatePace(time: Double, distance: Double): Double {
        return calculationService.calculatePace(time = time, distance = distance)
    }
}