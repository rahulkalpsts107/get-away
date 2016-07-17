package com.get_away;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.get_away.backend.getawayApi.model.Coordinates;
import com.get_away.backend.getawayApi.model.Recommendation;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rrk on 4/30/16.
 */
public class GetawayMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    public GetawayMessageHandler(){
        System.out.println("GetawayMessageHandler crated");
    }
    @Override
    public void onMessageReceived(String from, Bundle data) {
        System.out.println("GCM App client received a notif from GCM Server");
        String message = data.getString("message");
        System.out.println("Message from server is "+message);

        Map<String,List<Recommendation>> dataMap;
        Map<String,List<Recommendation>> dataMap1;
        /******test dataMap*****/
        dataMap = new HashMap<>();
        dataMap1 = new HashMap<>();
        Recommendation r = new Recommendation();
        r.setAddress("256 W 52nd St");
        r.setCategoryId("3fd66200f964a52015eb1ee3");
        r.setCategoryName("Restaurant");
        r.setContact("");

        Coordinates cor = new Coordinates();
        cor.setLatitude("40.76320366960836");
        cor.setLatitude("-73.98477345285308");
        r.setLocation(cor);

        Recommendation r2 = new Recommendation();
        r2.setAddress("256 W 52nd St");
        r2.setCategoryId("3fd66200f964a52015eb1ee3");
        r2.setCategoryName("Restaurant");
        r2.setContact("");

        Coordinates cor2 = new Coordinates();
        cor2.setLatitude("40.76320366960836");
        cor2.setLatitude("-73.98477345285308");
        r2.setLocation(cor);

        //r.getFriendsCheckedIn().add("47615321");

        Recommendation r1 = new Recommendation();
        r1.setAddress("5 W 4th St");
        r1.setCategoryId("1234");
        r1.setCategoryName("Night pub");
        r1.setContact("");

        Coordinates cor1 = new Coordinates();
        cor.setLatitude("40.76320366960836");
        cor.setLatitude("-73.98477345285308");
        r1.setLocation(cor);

        Recommendation r3 = new Recommendation();
        r3.setAddress("5 W 4th St");
        r3.setCategoryId("1234");
        r3.setCategoryName("Night pub");
        r3.setContact("");

        Coordinates cor3 = new Coordinates();
        cor3.setLatitude("40.76320366960836");
        cor3.setLatitude("-73.98477345285308");
        r3.setLocation(cor);
        //r.getFriendsCheckedIn().add("47615321");

        List<Recommendation> recs = new ArrayList<>();
        List<Recommendation> recs1 = new ArrayList<>();
        recs.add(r);recs1.add(r1);recs.add(r2);recs1.add(r3);
        dataMap.put(r.getCategoryName(), recs);
        dataMap1.put(r1.getCategoryName(), recs1);

        List<Map<String,List<Recommendation>>> list = new ArrayList<>();
        list.add(dataMap);
        list.add(dataMap1);
        /******test dataMap*****/

        //Add extraction based on key
        {
            RecommendationsDataHolder holder = RecommendationsDataHolder.getInstance();
            //holder.setRecommendations(list);
        }

        createNotification(from, message);
    }

    // Creates notification based on title and body received
    private void createNotification(String title, String body) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)
                .setContentText(body);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }
}
