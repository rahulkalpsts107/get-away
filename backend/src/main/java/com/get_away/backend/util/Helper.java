package com.get_away.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.get_away.backend.pojo.Recommendation;
import com.get_away.backend.pojo.User;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by rrk on 5/11/16.
 */
public class Helper {
    private static final Logger log = Logger.getLogger(Helper.class.getName());
    public static String constructGcmMessageForUser(User user){
        String ret = "";
        try{
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();
            jData.put("message", mapper.writeValueAsString(user)); //we can add our payload here as a compressed JSON string
            jGcmData.put("to", user.getGcmToken());
            //What to send in GCM message.
            jGcmData.put("data", jData);
            ret = jGcmData.toString();

        }catch(IOException exp){
            log.info(exp.getMessage());
        }
        return ret;
    }

    public static String constructGcmMessageForRecommendation(User user){
        String ret = "";
        try{
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();
            jData.put("message", mapper.writeValueAsString(user.getRecommendations())); //we can add our payload here as a compressed JSON string
            jGcmData.put("to", user.getGcmToken());
            //What to send in GCM message.
            jGcmData.put("data", jData);
            ret = jGcmData.toString();

        }catch(IOException exp){
            log.info(exp.getMessage());
        }
        return ret;
    }

    public static String constructGcmMessageForRecommendations(List<Map<String,
            List<Recommendation>>> recs, String gcmToken) {
        String ret = "";
        try{
            ObjectMapper mapper = new ObjectMapper();
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();
            jData.put("message", mapper.writeValueAsString(recs.toString())); //we can add our payload here as a compressed JSON string
            jGcmData.put("to", gcmToken);
            //What to send in GCM message.
            jGcmData.put("data", jData);
            ret = jGcmData.toString();
        }catch(IOException exp){
            log.info(exp.getMessage());
        }
        return ret;
    }
}
