package edu.uchicago.gerber._08final.mvc.model;

/**
 * Created by ag on 6/17/2015.
 */


public class CollisionOp {

    //this could also be a boolean, but we want to be explicit about what we're doing
    public enum Operation {
        ADD, REMOVE
    }

    //members
    private Movable movable;
    private Operation operation;

    //constructor
    public CollisionOp(Movable movable, Operation op) {
        this.movable = movable;
        this.operation = op;
    }


    //getters
    public Movable getMovable() {
        return movable;
    }

    public Operation getOperation() {
        return operation;
    }

}
