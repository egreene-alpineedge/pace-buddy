package org.example.project.Services

class CalculationService {

    // Distance = R * T
    fun calculateDistance(time: Double, speed: Double): Double {
        return time * speed
    }

    // Time = Distance / Rate
    fun calculateTime(distance: Double, speed: Double): Double {
        return distance / speed
    }

    // Rate = Distance / Time
    fun calculateSpeed(time: Double, distance: Double): Double {
        return distance / time
    }

    // Speed = 1 / Pace
    fun calculateSpeed(pace: Double): Double {
        return 1/pace
    }

    // Pace = 1 / Speed
    fun calculatePace(speed: Double): Double {
        return 1/speed
    }
}
