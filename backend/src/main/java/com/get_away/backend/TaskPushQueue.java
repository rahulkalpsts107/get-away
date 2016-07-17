package com.get_away.backend;

import com.get_away.backend.tasks.GetAwayTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import java.util.logging.Logger;

/**
 * Created by rrk on 4/30/16.
 */
public class TaskPushQueue {
    public static final int NOTIF_TASK = 1;
    public static final int DB_TASK = 2;

    private Queue processingQueue;
    private static TaskPushQueue instance = null;
    private static final Logger log = Logger.getLogger(TaskPushQueue.class.getName());

    private TaskPushQueue(){
        processingQueue = QueueFactory.getDefaultQueue();
        log.info("Queue created " + processingQueue.getQueueName());
    }

    public static TaskPushQueue getInstance(){
        if(instance == null)
            instance = new TaskPushQueue();
        return instance;
    }

    public void push(GetAwayTask task){
        processingQueue.add(TaskOptions.Builder.withPayload(task));
    }

    public void pushWithDelay(GetAwayTask task, int seconds){
        processingQueue.add(TaskOptions.Builder.withPayload(task).countdownMillis(1000*seconds));
    }
}
