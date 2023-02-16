package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.PolarPoint;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Utils {


    ////////////////////////////////////////////////////////////////////
    //Utility method for transforming cartesian2Polar
    ////////////////////////////////////////////////////////////////////
    public static List<PolarPoint> cartesianToPolar(Point[]  pntCartesians) {

        BiFunction<Point, Double, PolarPoint> cartToPolarTransform = (pnt, hyp) -> new PolarPoint(
                //this is r from PolarPoint(r,theta).
                hypotFunction(pnt.x, pnt.y) / hyp, //r is relative to the largestHypotenuse
                //this is theta from PolarPoint(r,theta)
                Math.toDegrees(Math.atan2(pnt.y, pnt.x)) * Math.PI / 180
        );

        //determine the largest hypotenuse
        //we must make hypotenuse final to pass into a stream on line 37 below.
        final double largestHypotenuse = Arrays.stream(pntCartesians)
                .map(p -> hypotFunction(p.x, p.y))
                .max(Double::compare)
                .orElse(0.0);


        return Arrays.stream(pntCartesians)
                .map(pnt -> cartToPolarTransform.apply(pnt, largestHypotenuse))
                .collect(Collectors.toList());

    }

    //private helper method
    private static double hypotFunction(double sideX, double sideY) {
        return Math.sqrt(Math.pow(sideX, 2) + Math.pow(sideY, 2));
    }


}
