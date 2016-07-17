package com.get_away;

import com.get_away.backend.getawayApi.model.Recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rrk on 5/12/16.
 */
public class RecommendationsDataHolder {
    private static RecommendationsDataHolder holder =null;
    public List<Map<String, List<Recommendation>>> recommendationsData = new ArrayList<>();//for getaway

    private RecommendationsDataHolder(){

    }

    public static RecommendationsDataHolder getInstance(){
        if(holder == null)
            holder = new RecommendationsDataHolder();
        return holder;
    }

    public void setRecommendations(List<Map<String,List<Recommendation>>> recommendations){
        /*May need to intelligently manipulate this list*/
        this.recommendationsData = recommendations;
    }
}
