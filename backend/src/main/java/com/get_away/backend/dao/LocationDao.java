package com.get_away.backend.dao;

import com.get_away.backend.pojo.Coordinates;
import static com.googlecode.objectify.ObjectifyService.ofy;


/**
 * Created by rrk on 5/11/16.
 */
public class LocationDao {
    public void saveCoordinates(Coordinates object){
        ofy().save().entity(object).now();
    }
}
