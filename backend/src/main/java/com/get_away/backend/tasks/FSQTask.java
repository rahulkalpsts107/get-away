package com.get_away.backend.tasks;

import com.get_away.backend.TaskPushQueue;
import com.get_away.backend.dao.LocationDao;
import com.get_away.backend.dao.RecommendationsDao;
import com.get_away.backend.fsq.FSQInteraction;
import com.get_away.backend.pojo.Recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by rrk on 5/7/16.
 */
public class FSQTask implements GetAwayTask {
    private static final Logger log = Logger.getLogger(GetAwayTask.class.getName());
    private String fsqId;
    public FSQTask(String fsqId){
        this.fsqId = fsqId;
    }
    @Override
    public void execute() {
        log.info("Foursquare task starts");
        RecommendationsDao dao = new RecommendationsDao();
        LocationDao lDao = new LocationDao();
//        /*Hardcoded recommendations*/
//        Recommendation r = new Recommendation();
//        r.setAddress("256 W 52nd St");
//        r.setCategoryId("3fd66200f964a52015eb1ee3");
//        r.setCategoryName("Restaurant");
//        r.setContact("");
//
//        Coordinates cor = new Coordinates();
//        cor.setLatitude("40.76320366960836");
//        cor.setLatitude("-73.98477345285308");
//        lDao.saveCoordinates(cor);
//
//        r.setLocation(cor);
//        r.friendsCheckedIn.add("47615321");


        List<Recommendation> recs = new ArrayList<>();
        //recs.add(r);
        FSQInteraction.getInstance().getRecommendations(fsqId, recs);
        for(Recommendation recommendation: recs){
            lDao.saveCoordinates(recommendation.getLocation());
            dao.saveRecommendation(recommendation);
        }

        /*Once we are done here lets start the DB persist operation*/
        TaskPushQueue queue = TaskPushQueue.getInstance();
        queue.push(new DBTask(new DBTask.StoreRecommendations(fsqId,recs)));
    }

    @Override
    public void run() {
        execute();
    }
}
