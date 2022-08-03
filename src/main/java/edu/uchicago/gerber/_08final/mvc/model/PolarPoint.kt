package edu.uchicago.gerber._08final.mvc.model

import lombok.AllArgsConstructor
import lombok.Data

data class PolarPoint(
    //we use the wrapper-class Double as members to get the Comparable interface
    //because Asteroid needs to sort by theta when generating random-shapes.
    val r // corresponds to the hypotenuse in cartesean, number between 0 and 1
            : Double,
    val theta //degrees in radians, number between 0 and 6.283
            : Double
)
