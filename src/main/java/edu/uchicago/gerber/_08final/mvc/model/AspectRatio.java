package edu.uchicago.gerber._08final.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AspectRatio {
    private double width;
    private double height;

    public AspectRatio scale(double scale){
        setHeight(this.height * scale);
        setWidth(this.width * scale);
        return this;
    }
}
