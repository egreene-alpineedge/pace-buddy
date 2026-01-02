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

    // Convert seconds per mile to miles per hour
    fun calculateSpeed(pace: Double): Double {
        return 3600.0/pace
    }

    // Convert miles per hour to minutes per mile
    fun calculatePace(speed: Double): Double {
        return 3600/speed
    }
}
