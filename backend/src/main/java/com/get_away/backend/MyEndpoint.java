/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.get_away.backend;

import com.get_away.backend.fsq.FSQInteraction;
import com.get_away.backend.pojo.Coordinates;
import com.get_away.backend.pojo.Recommendation;
import com.get_away.backend.pojo.User;
import com.get_away.backend.tasks.DBTask;
import com.get_away.backend.tasks.FSQTask;
import com.get_away.backend.tasks.NotifPosterTask;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
  name = "getawayApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.get_away.com",
    ownerName = "backend.get_away.com",
    packagePath=""
  )
)
public class MyEndpoint {

    private Map<String,String> userTokens = new HashMap<>();
    private static final Logger log = Logger.getLogger(MyEndpoint.class.getName());

    @ApiMethod(name = "register" ,path = "register")
    public MyBean register(@Named("fsqid") String fsqid, @Named("token") String gcmtoken, Coordinates coord) {
        MyBean response = new MyBean();
        response.setData("Received  a registration request for " + fsqid + " " + gcmtoken);

        log.info("recived fsqid " + fsqid + " token " + gcmtoken + coord);
        userTokens.put(fsqid, gcmtoken);

        JSONObject jGcmData = new JSONObject();
        JSONObject jData = new JSONObject();
        jData.put("message", "fsqid " + fsqid + " token " + gcmtoken + " coord " +coord); //we can add our payload here as a compressed JSON string
        jGcmData.put("to", gcmtoken);

        // What to send in GCM message.
        jGcmData.put("data", jData);


        User u = new User();
        FSQInteraction.getInstance().getUser(fsqid, u);

        if(u == null) {
            u = new User("raulh", "", "www.google.com" , gcmtoken , fsqid, coord.getLatitude(), coord.getLongitude());
        }


        log.info(u.toString());

        //This is just a reponse
        TaskPushQueue queue = TaskPushQueue.getInstance();


        /*Lets send response*/
        queue.push(new NotifPosterTask(jGcmData.toString()));

        /*lets store the user*/
        queue.push(new DBTask(new DBTask.StoreUser(u)));

        /*Trigger a FSQ task that will start parsing recommendations for this user*/
        //queue.pushWithDelay(new FSQTask(fsqid),5);//added 2 seconds delay

        return response;
    }

    @ApiMethod(name = "sample", path = "sample")
    public List<Recommendation> sample(@Named("token") String token) {
        FSQInteraction inte = FSQInteraction.getInstance();
        List<Recommendation> rs = new ArrayList<Recommendation>();

        inte.getRecommendations(token, rs);
        return rs;

        //MyBean b = new MyBean();
        //return rs;
        /*b.setData(rs.toString());
        return b;*/
    }

    @ApiMethod(name = "getSpeRecommendations", path = "getSpeRecommendations")
    public Map<String, List<Map<String, List<Recommendation>>>> getSpeRecommendations(@Named("token") String token) {
        System.out.println("ENDPOINT :  getSpeRecommendations");
        FSQInteraction inte = FSQInteraction.getInstance();
        return inte.getRecommendationsForApi(new ArrayList<Recommendation>(), token);
    }

    @ApiMethod(name = "getGeneralRecommendations", path = "getGeneralRecommendations")
    public Map<String, List<Map<String, List<Recommendation>>>> getGeneralRecommendations(@Named("token") String token) {
        System.out.println("ENDPOINT :  getGeneralRecommendations");
        FSQInteraction inte = FSQInteraction.getInstance();
        return inte.getRecommendationsForApi(new ArrayList<Recommendation>(), token);
    }
}
