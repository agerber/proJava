package edu.uchicago.gerber._08final.mvc.model

import lombok.AllArgsConstructor
import lombok.Data

data class PolarPoint(
    // corresponds to the hypotenuse in cartesean, number between 0 and 1
    val r: Double,
    //degrees in radians, number between 0 and 6.283
    val theta: Double
)
