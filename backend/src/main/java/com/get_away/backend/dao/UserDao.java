package com.get_away.backend.dao;

import com.get_away.backend.pojo.User;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;


/**
 * Created by rrk on 5/6/16.
 */
public class UserDao {

    public void saveUser(User object){
        ofy().save().entity(object).now();
    }

    public User findUser(Long id){
        return ofy().load().type(User.class).id(id).now();
    }

    public User findUserFSQ(String fsqId){
        return ofy().load().type(User.class).filter("fsqId", fsqId).first().now();
    }

    public List<User> getAllUsers(){
        return null;
    }
}
