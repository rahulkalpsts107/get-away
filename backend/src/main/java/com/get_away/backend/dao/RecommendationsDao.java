package com.get_away.backend.dao;

import com.get_away.backend.pojo.Recommendation;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by rrk on 5/11/16.
 */
public class RecommendationsDao {
    public void saveRecommendation(Recommendation object){
        ofy().save().entity(object).now();
    }
}
