package com.get_away.backend.tasks;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.get_away.backend.TaskPushQueue;
import com.get_away.backend.dao.LocationDao;
import com.get_away.backend.dao.RecommendationsDao;
import com.get_away.backend.dao.UserDao;
import com.get_away.backend.pojo.Coordinates;
import com.get_away.backend.pojo.Recommendation;
import com.get_away.backend.pojo.User;
import com.get_away.backend.util.Helper;
import com.googlecode.objectify.Key;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by rrk on 4/30/16.
 * Task takes care of all DB related operations
 */
public class DBTask implements GetAwayTask {

    private static final Logger log = Logger.getLogger(DBTask.class.getName());
    private DBSimpleOperation toPerform;

    public DBTask(DBSimpleOperation toPerform){
        this.toPerform = toPerform;
    }

    /*Implement this class for a new DB Operation*/
    public interface DBSimpleOperation{
        public static final long serialVersionUID = 7526472295622776447L;
        public void perform();
    }

    /*Will store recommendations*/
    public static class StoreRecommendations implements DBSimpleOperation,Serializable{
        String fsqId;
        List<Recommendation> recommendations;
        public StoreRecommendations(String fsqId, List<Recommendation> recommendations){
            this.fsqId = fsqId;
            this.recommendations= recommendations;
        }

        @Override
        public void perform(){
            log.info("DBTask: Store Recommendations is executing");
            UserDao dao = new UserDao();
            RecommendationsDao rdao = new RecommendationsDao();
            User user = dao.findUserFSQ(fsqId);
            if(user != null) {
                log.info("Saving recommendaitons in user");
                for(Recommendation r: recommendations)
                {
                    rdao.saveRecommendation(r);
                }
                user.recommendations = recommendations;
                dao.saveUser(user);
            }
            User user1 = dao.findUserFSQ(fsqId);//loading again to double check
            log.info("Going to store these recommendations");
            if(user1 != null) {
                for (Recommendation r : user1.recommendations) {
                    log.info(r.toString());
                }
            }
            TaskPushQueue queue = TaskPushQueue.getInstance();
            //queue.pushWithDelay(new NotifPosterTask(Helper.constructGcmMessageForUser(user1)),2);
            queue.push(new NotifPosterTask(Helper.constructGcmMessageForRecommendation(user1)));

        }
    }

    /*DB operation Only to store user*/
    public static class StoreUser implements DBSimpleOperation,Serializable{
        User user;
        public StoreUser(User user){
            this.user = user;
        }

        @Override
        public void perform(){
            log.info("DBTask: Store User is executing");
            UserDao dao = new UserDao();
            LocationDao ldao = new LocationDao();
            User obj =dao.findUserFSQ(user.getFsqId());
            if(obj == null) {
                log.info("Obj not found hence saving");
                Coordinates cor = new Coordinates();
                cor.setLatitude(user.getCurrentLoc().getLatitude());
                cor.setLongitude(user.getCurrentLoc().getLongitude());
                ldao.saveCoordinates(cor);
                user.setCurrentLoc(cor);
                dao.saveUser(user);
            }
            else
                log.info("User already exists so no need of storing "+obj.toString());
        }
    }

    /*DB operation Only to store user*/
    public static class UpdateCoordinates implements DBSimpleOperation,Serializable{
        String fsqId;
        public UpdateCoordinates(String fsqId){
            this.fsqId = fsqId;
        }

        @Override
        public void perform(){
            log.info("DBTask: Store User is executing");
            UserDao dao = new UserDao();
            User obj =dao.findUserFSQ(fsqId);
            if(obj == null) {
                log.info("user is not there");
            }
            else{

            }
        }
    }

    @Override
    public void execute() {
        toPerform.perform();
    }

    @Override
    public void run() {
        execute();
    }
}
