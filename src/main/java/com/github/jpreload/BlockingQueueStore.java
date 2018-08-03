package com.github.jpreload;

import com.github.jpreload.model.Params;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by chenjunlong on 2018/8/3.
 */
public class BlockingQueueStore {

    private static BlockingQueue<Params> blockingQueue = new LinkedBlockingQueue();

    public static BlockingQueue<Params> getBlockingQueue() {
        return blockingQueue;
    }
}
