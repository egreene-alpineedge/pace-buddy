package org.example.project

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.example.project.Services.CalculationService
import org.example.project.Services.ConversionService
import org.example.project.models.Split
import kotlin.math.roundToInt

class AppViewModel(
    private val calculationService: CalculationService,
    private val conversionService: ConversionService
) {
    var time by mutableStateOf<Double?>(null)
        private set

    var distance by mutableStateOf<Double?>(null)
        private set
    var distanceUnit by mutableStateOf<DistanceUnit>(DistanceUnit.MI)
        private set

    var pace by mutableStateOf<Double?>(null) // value is seconds per mile
        private set
    var paceUnit by mutableStateOf<PaceUnit>(PaceUnit.SEC_MI)
        private set

    var speed by mutableStateOf<Double?>(null) // value is seconds per mile
        private set
    var speedUnit by mutableStateOf<SpeedUnit>(SpeedUnit.MI_HR)
        private set

    var split by mutableStateOf<Double?>(null)
        private set
    var splits by mutableStateOf<List<Split>>(mutableListOf())
        private  set
    var splitUnit by mutableStateOf<DistanceUnit>(DistanceUnit.MI)
        private set


    fun onReset() {
        time = null
        distance = null
        pace = null
        speed = null
        split = null
        splits = mutableListOf()
    }

    fun onBlurTimeField(value: Double) {
        if (value == 0.0) return

        time = value

        updateFromTime()

    }
    fun onBlurDistanceField(value: Double) {
        if (value == 0.0) return

        println("Distance Blurred")
        distance = value

        val d = distance!!
        updateFromDistance(d)
    }
    fun onBlurSpeedField(value: Double) {
        if (value == 0.0) return

        println("Speed Blurred")
        speed = value

        val s = speed!!
        updateFromSpeed(s)
    }
    fun onBlurPaceField(value: Double) {
        if (value == 0.0) return

        print("Pace Blurred")
        pace = value
        val p = pace!!
        updateFromPace(p)
    }
    fun onBlurSplitField(value: Double) {
        if (value == 0.0) return

        print("Split Blurred")
        split = value

        updateSplits()
    }

    fun onToggleDistanceUnit(unitString: String) {
        val oldUnit = distanceUnit
        val newUnit = when (unitString) {
            "mi" -> DistanceUnit.MI
            "km" -> DistanceUnit.KM
            else -> DistanceUnit.MI
        }

        distanceUnit = newUnit

        val d = distance

        if (d != null) {
            val convertedDistance = convertDistance(value = d, fromUnit = oldUnit, toUnit = newUnit)
            distance = convertedDistance
            updateFromDistance(convertedDistance)
        }

    }
    fun onTogglePaceUnit(unitString: String) {
        val oldUnit = paceUnit
        val newUnit = when (unitString) {
            "/mi" -> PaceUnit.SEC_MI
            "/km" -> PaceUnit.SEC_KM
            else -> PaceUnit.SEC_MI
        }

        paceUnit = newUnit

        val p = pace

        if (p != null) {
            val convertedPace = convertPace(value = p, fromUnit = oldUnit, toUnit = newUnit)
            pace = convertedPace
            updateFromPace(convertedPace)
        }
    }
    fun onToggleSpeedUnit(unitString: String) {
        val oldUnit = speedUnit
        val newUnit = when (unitString) {
            "mph" -> SpeedUnit.MI_HR
            "kph" -> SpeedUnit.KM_HR
            else -> SpeedUnit.MI_HR
        }

        speedUnit = newUnit

        val s = speed

        if (s != null) {
            val convertedSpeed = convertSpeed(value = s, fromUnit = oldUnit, toUnit = newUnit)
            speed = convertedSpeed
            updateFromSpeed(convertedSpeed)
        }
    }
    fun onToggleSplitUnit(unitString: String) {
        val oldUnit = splitUnit
        val newUnit = when (unitString) {
            "mi" -> DistanceUnit.MI
            "km" -> DistanceUnit.KM
            else -> DistanceUnit.MI
        }

        splitUnit = newUnit

        val sp = split

        if (sp != null) {
            val convertedSplit = convertDistance(value = sp, fromUnit = oldUnit, toUnit = newUnit)
            split = convertedSplit
            updateSplits()
        }
    }

    fun updateFromDistance(d: Double) {
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

        // Update Splits
        updateSplits()
    }
    fun updateFromSpeed(s: Double) {
        // Calculate Pace
        pace = calculatePace(speed = s)

        // Calculate Time
        if (distance != null) {
            val d = distance!!
            time = calculateTime(distance = d, speed = s)
        }

        // Calculate Distance
        else if (time != null) {
            val t = time!!
            distance = calculateDistance(time = t, speed = s)
        }

        // Update Splits
        updateSplits()
    }
    fun updateFromPace(p: Double) {
        // Calculate Speed
        speed = calculateSpeed(pace = p)

        val s = speed!!

        // Calculate Time
        if (distance != null) {
            val d = distance!!
            time = calculateTime(distance = d, speed = s)
        }

        // Calculate Distance
        else if (time != null) {
            val t = time!!
            distance = calculateDistance(time = t, speed = s)
        }

        // Update Splits
        updateSplits()
    }
    fun updateFromTime() {
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

        // Update Splits
        updateSplits()
    }

    fun updateSplits() {
        val t = time
        val d = distance
        val s = speed
        val sp = split
        if (t == null || d == null || s == null || sp == null) {
            return
        }

        val setSplitUnit = splitUnit

        val splitInterval = sp * setSplitUnit.metersFactor

        // Convert Distance to meters
        val distanceInMeters = conversionService.toMeters(value = d, fromUnit = distanceUnit)

        // Calculate Splits
        val firstSplitTimeUnrounded = t / (distanceInMeters/splitInterval)
        val firstSplitTime = (firstSplitTimeUnrounded*1000).roundToInt().toDouble() / 1000

        var currentDistance = splitInterval
        var currentTime = firstSplitTime
        var iteration = 0
        val splitsToAdd = mutableListOf<Split>()
        while (currentDistance <= distanceInMeters && iteration < 500) {
            splitsToAdd.add(
                Split(
                    length = currentDistance,
                    unit = setSplitUnit,
                    time = currentTime
                )
            )

            currentDistance += splitInterval
            currentTime += firstSplitTime
            iteration++
        }

        // Convert to Preferred Split Unit
        val splitResult = splitsToAdd.map { split ->
            Split(
                length = conversionService.fromMeters(value = split.length, toUnit = split.unit),
                unit = split.unit,
                time = split.time
            )
        }


        splits = splitResult

    }
    // -----------------------------------------------------------


    private fun calculateTime(distance: Double, speed: Double): Double {

        // Convert Distance to meters
        val setDistanceUnit = distanceUnit
        val distanceInMeters = conversionService.toMeters(value = distance, fromUnit = setDistanceUnit)

        // Convert Speed to m/s
        val setSpeedUnit = speedUnit
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
        val setSpeedUnit = speedUnit
        val speedInMetersPerSecond = conversionService.toMetersPerSecond(value = speed, fromUnit = setSpeedUnit)

        // Calculate
        val distanceInMeters = calculationService.calculateDistance(time = timeInSeconds, speed = speedInMetersPerSecond)

        // Convert to preferred Distance Unit
        val setDistanceUnit = distanceUnit
        val distanceResult = conversionService.fromMeters(value = distanceInMeters, toUnit = setDistanceUnit)

        // Return Result
        return distanceResult
    }
    private fun calculateSpeed(time: Double, distance: Double): Double {

        // Convert Distance to meters
        val setDistanceUnit = distanceUnit
        val distanceInMeters = conversionService.toMeters(value = distance, fromUnit = setDistanceUnit)

        // Time is already in seconds
        val timeInSeconds = time

        // Calculate
        val speedInMetersPerSecond = calculationService.calculateSpeed(time = timeInSeconds, distance = distanceInMeters)

        // Convert to Preferred Speed Unit
        val setSpeedUnit = speedUnit
        val speedResult = conversionService.fromMetersPerSecond(value = speedInMetersPerSecond, toUnit = setSpeedUnit)

        // Return Result
        return speedResult
    }


    private fun calculatePace(speed: Double): Double {
        // Convert Speed to m/s
        val setSpeedUnit = speedUnit
        val speedInMetersPerSecond = conversionService.toMetersPerSecond(value = speed, fromUnit = setSpeedUnit)

        // Calculate
        val paceInSecondsPerMeter = calculationService.calculatePace(speed = speedInMetersPerSecond)

        // Convert to Preferred Pace Unit
        val setPaceUnit = paceUnit
        val paceResult = conversionService.fromSecondsPerMeter(value = paceInSecondsPerMeter, toUnit = setPaceUnit)

        // Return Result
        return paceResult
    }
    private fun calculateSpeed(pace: Double): Double {

        // Convert Pace to s/m
        val setPaceUnit = paceUnit
        val paceInSecondsPerMeter = conversionService.toSecondsPerMeter(value = pace, fromUnit = setPaceUnit)

        // Calculate
        val speedInMetersPerSecond = calculationService.calculateSpeed(pace = paceInSecondsPerMeter)

        // Convert to Preferred Speed Unit
        val setSpeedUnit = speedUnit
        val speedResult = conversionService.fromMetersPerSecond(value = speedInMetersPerSecond, toUnit = setSpeedUnit)

        // Return Result
        return speedResult
    }

    private fun convertDistance(value: Double, fromUnit: DistanceUnit, toUnit: DistanceUnit): Double {
        // Convert to Base
        val distanceInMeters = conversionService.toMeters(value = value, fromUnit = fromUnit)

        // Convert to preferred Distance Unit
        val distanceResult = conversionService.fromMeters(value = distanceInMeters, toUnit = toUnit)

        // Return Result
        return distanceResult
    }
    private fun convertPace(value: Double, fromUnit: PaceUnit, toUnit: PaceUnit): Double {
        // Convert to Base
        val paceInSecondsPerMeter = conversionService.toSecondsPerMeter(value = value, fromUnit = fromUnit)

        // Convert to Preferred Pace Unit
        val paceResult = conversionService.fromSecondsPerMeter(value = paceInSecondsPerMeter, toUnit = toUnit)

        // Return Result
        return paceResult
    }
    private fun convertSpeed(value: Double, fromUnit: SpeedUnit, toUnit: SpeedUnit): Double {
        // Convert to Base
        val speedInMetersPerSecond = conversionService.toMetersPerSecond(value = value, fromUnit = fromUnit)

        // Convert to Preferred Speed Unit
        val speedResult = conversionService.fromMetersPerSecond(value = speedInMetersPerSecond, toUnit = toUnit)

        // Return Result
        return speedResult
    }

}