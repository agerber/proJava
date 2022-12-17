package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.PolarPoint;

import java.awt.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Utils {


    ////////////////////////////////////////////////////////////////////
    //Utility methods for transforming cartesian2Polar, pointsListToArray, etc.
    ////////////////////////////////////////////////////////////////////
    public static List<PolarPoint> cartesianToPolar(List<Point> pntCartesians) {

        BiFunction<Point, Double, PolarPoint> cartToPolarTransform = (pnt, hyp) -> new PolarPoint(
                //this is r from PolarPoint(r,theta).
                hypotFunction(pnt.x, pnt.y) / hyp, //r is relative to the largestHypotenuse
                //this is theta from PolarPoint(r,theta)
                Math.toDegrees(Math.atan2(pnt.y, pnt.x)) * Math.PI / 180
        );


        //determine the largest hypotenuse
        double largestHypotenuse = 0;
        for (Point pnt : pntCartesians)
            if (hypotFunction(pnt.x, pnt.y) > largestHypotenuse)
                largestHypotenuse = hypotFunction(pnt.x, pnt.y);


        //we must make hypotenuse final to pass into a stream.
        final double hyp = largestHypotenuse;


        return pntCartesians.stream()
                .map(pnt -> cartToPolarTransform.apply(pnt, hyp))
                .collect(Collectors.toList());

    }

    public static Point[] pointsListToArray(List<Point> listPoints) {
        return listPoints.stream()
                .toArray(Point[]::new);

    }

    //private helper method
    private static double hypotFunction(double sideX, double sideY) {
        return Math.sqrt(Math.pow(sideX, 2) + Math.pow(sideY, 2));
    }


}
