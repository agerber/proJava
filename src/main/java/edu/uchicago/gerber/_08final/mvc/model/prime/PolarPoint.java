package edu.uchicago.gerber._08final.mvc.model.prime;

import lombok.AllArgsConstructor;
import lombok.Data;

//this class used in conjunction with Point[] for rendering vector graphics
@Data
@AllArgsConstructor
public class PolarPoint  {

    private double r; // corresponds to the hypotenuse in cartesean, number between 0.0 and 1.0
    private double theta; //degrees in radians, number between 0.0 and 6.283 (2 * Pi)


}
