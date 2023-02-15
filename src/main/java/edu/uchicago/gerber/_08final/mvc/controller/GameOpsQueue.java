package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.Movable;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Effectively a Queue that enqueues and dequeues Game Operations (add/remove)
 */
public class GameOpsQueue extends LinkedList<GameOp> {

    //this data structure is in contention by the "Event Dispatch" thread aka main-swing-thread, and the animation
    // thread. We must restrict access to it by one thread at a time by using a Lock.
    private final Lock lock;

    public GameOpsQueue() {
        lock =   new ReentrantLock();
    }

    //enqueues may happen either as a result of a keystroke such as a bullet fire (main-swing-thread) or
    // a collision, etc. (animation-thread). Use a lock to ensure this data structure is thread safe.
    public void enqueue(Movable mov, GameOp.Action action) {
       try {
            lock.lock();
            addLast(new GameOp(mov, action));
        } finally {
            lock.unlock();
        }
    }


    //dequeues are done in serial by the animation thread only. No lock required.
    public GameOp dequeue() {
            return removeFirst();
    }
}
