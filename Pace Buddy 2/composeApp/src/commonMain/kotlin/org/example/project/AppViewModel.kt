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


    fun blurTimeField() {
        if (distance != null && pace != null) {
            val d = distance!!
            val p = pace!!
            time = calculateTime(distance = d, pace = p)
        }
    }
    fun blurDistanceField() {
        if (time != null || pace != null) {
            val t = time!!
            val p = pace!!
            distance = calculateDistance(time = t, pace = p)
        }
    }
    fun blurPaceField(){
        if (time != null && distance != null) {
            val t = time!!
            val d = distance!!
            pace = calculatePace(time = t, distance = d)
        }
    }


    // -----------------------------------


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