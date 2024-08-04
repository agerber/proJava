package edu.uchicago.gerber._08final.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AspectDim {
    private double w;
    private double h;

    public AspectDim scale(double scale){
        return new AspectDim(this.w * scale, this.h * scale);
    }
}
