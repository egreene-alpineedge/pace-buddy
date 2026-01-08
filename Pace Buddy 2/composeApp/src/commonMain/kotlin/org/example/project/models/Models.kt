package org.example.project.models

import org.example.project.DistanceUnit

data class Split(
    val length: Double,
    val unit: DistanceUnit,
    val time: Double
)