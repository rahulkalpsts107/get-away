package com.get_away.backend.fsq;

import com.get_away.backend.TaskPushQueue;
import com.get_away.backend.pojo.Coordinates;
import com.get_away.backend.pojo.Recommendation;
import com.get_away.backend.pojo.User;
import com.get_away.backend.tasks.DBTask;
import com.get_away.backend.tasks.NotifPosterTask;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.repackaged.com.google.gson.JsonArray;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Deepesh on 5/10/2016.
 */
public class FSQInteraction {

    private static final Logger logger = Logger.getLogger(FSQInteraction.class.getName());
    private static final String FSQ_BASE_URL = "https://api.foursquare.com/v2/";
    private static final String VERSION_NO = "20160502";
    private static final String TAG = "FSQInteraction";
    private static FSQInteraction instance = null;

    TaskPushQueue queue = TaskPushQueue.getInstance();

    private FSQInteraction(){}
    public static FSQInteraction getInstance(){
        if(instance == null)
            instance = new FSQInteraction();
        return instance;
    }

    public User getUser(String accessToken, User user) {
        try {
            String userJson = performRequest("users/self", new HashMap<String, Object>(),
                    accessToken);
            logger.log(Level.WARNING, userJson);
            return parseUser(userJson, user);
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error while interacting with fsq " + e.getMessage());
        }
        return user;
    }

    public List<Recommendation> getRecommendations(String accessToken, List<Recommendation> rec) {
        try {
            String recJson = performRequest("checkins/recent", new HashMap<String, Object>(), accessToken);
            parseRecommendations(recJson, rec);
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error while interacting with fsq " + e.getMessage());
        }
        return rec;
    }

    public Map<String, List<Map<String, List<Recommendation>>>> getRecommendationsForApi(
            List<Recommendation> recoms, String accessToken) {
        getRecommendations(accessToken, recoms);

        List<Map<String, List<Recommendation>>> formatedRecoms = formatRecommendationsForGCM(recoms);
        Map<String, List<Map<String, List<Recommendation>>>> t = new
                HashMap<String, List<Map<String, List<Recommendation>>>>();
        t.put("getaway", formatedRecoms);
        System.out.println("FROM SERVER MAP " + t);
        return t;
    }

    public void getRecommendationsForQueue(List<Recommendation> recoms, String accessToken) {
        getRecommendations(accessToken, recoms);
        // Store Recommendations
        queue.push(new DBTask(new DBTask.StoreRecommendations(accessToken, recoms)));
        // Format for GCM and send it to GCM
        queue.push(new NotifPosterTask(formatRecommendationsForGCM(recoms).toString()));
    }

    private List<Map<String, List<Recommendation>>> formatRecommendationsForGCM(
            List<Recommendation> rec) {
        Map<String, Boolean> checkMap = new HashMap<String, Boolean>();
        List<Map<String, List<Recommendation>>> recoms = new ArrayList<Map<String, List<Recommendation>>>();

        for(Recommendation r : rec) {
            if(checkMap.containsKey(r.getCategoryName())) {
                int index = findRecomMapIndex(recoms, r.getCategoryName());
                if(index != -1) {
                    recoms.get(index).get(r.getCategoryName()).add(r);
                }
            } else {
                Map<String, List<Recommendation>> temp = new HashMap<String, List<Recommendation>>();
                List<Recommendation> tempR = new ArrayList<Recommendation>();
                tempR.add(r);
                temp.put(r.getCategoryName(), tempR);
                recoms.add(temp);
                checkMap.put(r.getCategoryName(), true);
            }
        }

        // Sort Recommendations individually
        for(Map<String, List<Recommendation>> m : recoms) {
            for(String s : m.keySet()) {
                List<Recommendation> recomToSort = m.get(s);
                if(recomToSort != null) {
                    sortRecommendations(1, recomToSort);
                    m.put(s, recomToSort);
                }
            }
        }

        // Fetch gcm-token from DB. Synchronous call
        /*UserDao userDao = new UserDao();
        User u = userDao.findUserFSQ(accessToken);
        if(u != null) {
            String message = Helper.constructGcmMessageForRecommendations(recoms, u.getGcmToken());
            queue.push(new NotifPosterTask(message));
            return recoms;
        }*/

        return recoms;
    }

    private int findRecomMapIndex(List<Map<String, List<Recommendation>>> recoms, String key) {
        for(int i=0; i < recoms.size(); i++) {
            if(recoms.get(i).containsKey(key)) {
                return i;
            }
        }
        return -1;
    }

    private List<Recommendation> sortRecommendations(int code, List<Recommendation> recs) {
        switch(code) {
            case 0:
                recs.sort(new Comparator<Recommendation>() {
                    @Override
                    public int compare(Recommendation o1, Recommendation o2) {
                        return o2.getCheckinsCount() - o1.getCheckinsCount();
                    }
                });
                break;
            case 1:
                final Double lati = 40.69433250497659;
                final Double longi = -73.9865353463385;
                recs.sort(new Comparator<Recommendation>() {

                    private Double distanceFromMe(Coordinates p) {
                        Double lat = Double.parseDouble(p.latitude);
                        Double lng = Double.parseDouble(p.longitude);

                        double theta = lng - longi;
                        double dist = Math.sin(deg2rad(lat)) * Math.sin(deg2rad(lati))
                                + Math.cos(deg2rad(lat)) * Math.cos(deg2rad(lati))
                                * Math.cos(deg2rad(theta));
                        dist = Math.acos(dist);
                        dist = rad2deg(dist);
                        return dist;
                    }

                    private double deg2rad(double deg) { return (deg * Math.PI / 180.0); }
                    private double rad2deg(double rad) { return (rad * 180.0 / Math.PI); }

                    @Override
                    public int compare(Recommendation o1, Recommendation o2) {
                        return distanceFromMe(o1.getLocation())
                                .compareTo(distanceFromMe(o2.getLocation()));
                    }
                });
                break;
            default:
                break;
        }
        return recs;
    }

    private List<Recommendation> parseRecommendations(String recJson, List<Recommendation> recs) {
        System.out.println(recJson);
        JsonObject obj = parseGeneral(recJson);
        if(!obj.isJsonNull()) {
            JsonArray recObj = obj.getAsJsonArray("recent");
            if(!recObj.isJsonNull() && recObj.size() > 0) {
                for(int i = 0; i < recObj.size(); i++) {
                    JsonElement r = recObj.get(i);
                    Recommendation recom = new Recommendation();
                    JsonObject venueObj = r.getAsJsonObject().getAsJsonObject("venue");
                    if(!venueObj.isJsonNull()) {
                        JsonObject statObj = venueObj.getAsJsonObject("stats");
                        int checkinsCount = 1;
                        if(!statObj.isJsonNull()) {
                            try {
                                checkinsCount = statObj.get("checkinsCount").getAsInt();
                            } catch(Exception e) {}
                        }
                        recom.setCheckinsCount(checkinsCount);

                        String placeName = venueObj.get("name").getAsString();
                        recom.setPlaceName(placeName);

                        JsonObject locObj = venueObj.getAsJsonObject("location");
                        if(!locObj.isJsonNull()) {
                            String lat = locObj.get("lat").getAsString();
                            String lng = locObj.get("lng").getAsString();
                            JsonArray address = locObj.getAsJsonArray("formattedAddress");
                            if(!address.isJsonNull()) {
                                StringBuilder addr = new StringBuilder("");
                                for(JsonElement s : address) {
                                    addr.append(s.getAsString());
                                }
                                recom.setAddress(address.toString());
                            }

                            recom.setLocation(new Coordinates(lat, lng));

                            String contact = "";
                            recom.setContact(contact);

                            JsonArray catObj = venueObj.getAsJsonArray("categories");
                            if(!catObj.isJsonNull()) {
                                JsonObject catO = catObj.get(0).getAsJsonObject();
                                if(!catO.isJsonNull()) {
                                    String catName = catO.get("name").getAsString();
                                    String catId = catO.get("id").getAsString();
                                    recom.setCategoryId(catId);
                                    recom.setCategoryName(catName);
                                }
                            }
                        }
                    }

                    JsonObject userObj = r.getAsJsonObject().getAsJsonObject("user");
                    if(!userObj.isJsonNull()) {
                        String firstName = userObj.get("firstName").getAsString();
                        String lastName = userObj.get("lastName").getAsString();
                        String userId = userObj.get("id").getAsString();
                        Map<String, String> userMap = new HashMap<String, String>();
                        userMap.put(userId, firstName + " " + lastName);
                        recom.setFriendCheckedIn(firstName + " " + lastName);
                        //recom.setFriend(userId, firstName + " " + lastName);
                    }

                    recs.add(recom);
                }
            }
        }
        return recs;
    }

    private User parseUser(String json, User user) {
        JsonObject obj = parseGeneral(json);
        logger.log(Level.WARNING, "General= " + obj != null ? obj.toString() : null);
        if(!obj.isJsonNull()) {
            JsonObject userObj = obj.getAsJsonObject("user");
            if(!userObj.isJsonNull()) {
                String id = userObj.get("id").getAsString();
                String firstName = userObj.get("firstName").getAsString();
                String lastName = userObj.get("lastName").getAsString();
                user.setFsqID(id);
                user.setFsqName(firstName + " " + lastName);

                String contact = "";
                JsonObject contactObj = userObj.getAsJsonObject("contact");
                if(!contactObj.isJsonNull()) {
                    contact = contactObj.get("email").getAsString();
                    user.setEmail(contact);
                }

                String photoUrl = "";
                JsonObject photoObj = userObj.getAsJsonObject("photo");
                if(!photoObj.isJsonNull()) {
                   String pre = photoObj.get("prefix").getAsString();
                    String post = photoObj.get("suffix").getAsString();
                    photoUrl = pre + "original" + post;
                    user.setImageUrl(photoUrl);
                }
            }
        }
        return user;
    }

    private JsonObject parseGeneral(String json) {
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        if(!obj.isJsonNull()) {
            JsonObject resp = obj.getAsJsonObject("response");
            if(!resp.isJsonNull()) {
                return resp;
            }
        }
        return null;
    }

    private String performRequest(String endpoint, Map<String, Object> queryFields,
                                   String token) throws Exception {
        URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
        URL url = new URL(FSQ_BASE_URL + endpoint + "?oauth_token=" + token + "&v=" + VERSION_NO);
        HTTPResponse response = fetcher.fetch(url);

        if(response.getResponseCode() == 200) {
            return new String(response.getContent(), "UTF-8");
        }
        return null;
    }
}
