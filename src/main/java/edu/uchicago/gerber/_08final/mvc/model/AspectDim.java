package edu.uchicago.gerber._08final.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AspectDim {
    private double width;
    private double height;

    public AspectDim scale(double scale){

        setHeight(this.height * scale);
        setWidth(this.width * scale);

        return this;
    }
}
