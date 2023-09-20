package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.Movable;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Effectively a Queue that enqueues and dequeues Game Operations (add/remove)
 */
public class GameOpsQueue extends LinkedList<GameOp> {

    public void enqueue(Movable mov, GameOp.Action action) {
        addLast(new GameOp(mov, action));
    }

    public GameOp dequeue() {
        return removeFirst();
    }
}
