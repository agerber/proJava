package edu.uchicago.gerber._08final.mvc.model.prime;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
    This class is used to adjust the aspect ratio of the mini-map (or radar screen) for non-square universes.
 */
@Data
@AllArgsConstructor
public class AspectRatio {
    private double width;
    private double height;

    /* This is an example of the Fluent Interface design pattern, which relies on method chaining to make the code
    more readable and intuitive. In this pattern, methods return the instance of the object, allowing multiple method
    calls to be linked together in a single, fluid expression.
   */
    public AspectRatio scale(double scale){
        setHeight(this.height * scale);
        setWidth(this.width * scale);
        return this;
    }
}
