package com.get_away.backend.tasks;

import com.google.appengine.api.taskqueue.DeferredTask;

/**
 * Created by rrk on 4/30/16.
 */
public interface GetAwayTask extends DeferredTask {
        public static final long serialVersionUID = 7526472295622776147L;
        public void execute();
}