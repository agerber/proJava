package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.PolarPoint;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {


    ////////////////////////////////////////////////////////////////////
    //Utility method for transforming cartesian2Polar
    ////////////////////////////////////////////////////////////////////
    public static List<PolarPoint> cartesianToPolar(Point[]  pntCartesians) {

        //Function used in stream below, as well as in BiFunction below
        Function<Point, Double> hypotenuseOfPoint = (pnt) ->
                Math.sqrt(Math.abs(Math.pow(pnt.x, 2)) + Math.abs(Math.pow(pnt.y, 2)));

        //determine the largest hypotenuse
        //we must make hypotenuse final to pass into a stream below.
        final double LARGEST_HYP = Arrays.stream(pntCartesians)
                .map(hypotenuseOfPoint)
                .max(Double::compare)
                .orElse(0.0);


        //BiFunction used in stream below
        BiFunction<Point, Double, PolarPoint> cartToPolarTransform = (pnt, hyp) -> new PolarPoint(
                //this is r from PolarPoint(r,theta).
                hypotenuseOfPoint.apply(pnt) / hyp, //r is relative to the largestHypotenuse
                //this is theta from PolarPoint(r,theta)
                Math.toDegrees(Math.atan2(pnt.y, pnt.x)) * Math.PI / 180
        );

        return Arrays.stream(pntCartesians)
                .map(pnt -> cartToPolarTransform.apply(pnt, LARGEST_HYP))
                .collect(Collectors.toList());

    }


}
