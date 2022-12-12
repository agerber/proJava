package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.Movable;
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
    public enum Action {
        ADD, REMOVE
    }
    //members
    private Movable movable;
    private Action action;

}
