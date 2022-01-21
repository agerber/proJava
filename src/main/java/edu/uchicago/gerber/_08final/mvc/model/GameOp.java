package edu.uchicago.gerber._08final.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by ag on 6/17/2015.
 */

//the lombok @Data gives us automatic getters and setters
@Data
//the lombok @AllArgsConstructor gives us an All-Args-Constructor :)
@AllArgsConstructor
public class GameOp {
    //this could also be a boolean, but we want to be explicit about what we're doing
    public enum Operation {
        ADD, REMOVE
    }
    //members
    private Movable movable;
    private Operation operation;

}
