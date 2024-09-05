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

    //this is an example of the Fluent Interface pattern
    public AspectRatio scale(double scale){
        setHeight(this.height * scale);
        setWidth(this.width * scale);
        return this;
    }
}
