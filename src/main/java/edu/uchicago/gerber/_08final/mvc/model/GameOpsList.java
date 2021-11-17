package edu.uchicago.gerber._08final.mvc.model;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by ag on 6/17/2015.
 */
public class GameOpsList extends LinkedList {

    //this data structure is in contention by the "Event Dispatch" thread aka main-swing-thread, and the animation
    // thread. We must restrict access to it by one thread at a time by using a Lock.
    private ReentrantLock lock;

    public GameOpsList() {
        this.lock =   new ReentrantLock();
    }

    public void enqueue(Movable mov, CollisionOp.Operation operation) {


       try {
            lock.lock();
            addLast(new CollisionOp(mov, operation));
        } finally {
            lock.unlock();
        }
    }


    public CollisionOp dequeue() {
        try {
            lock.lock();
           return (CollisionOp) super.removeFirst();
        } finally {
            lock.unlock();
        }

    }
}
