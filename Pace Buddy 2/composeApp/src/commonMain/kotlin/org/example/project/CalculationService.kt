package org.example.project

class CalculationService {

    // Distance = R * T
    fun calculateDistance(time: Double, pace: Double): Double {
        return time * pace
    }

    // Time = Distance / Rate
    fun calculateTime(distance: Double, pace: Double): Double {
        return distance / pace
    }

    // Rate = Distance / Time
    fun calculatePace(time: Double, distance: Double): Double {
        return distance / time
    }
}