package com.get_away.backend.servlet;

import com.get_away.backend.pojo.Coordinates;
import com.get_away.backend.pojo.Recommendation;
import com.get_away.backend.pojo.User;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by rrk on 5/6/16.
 */
public class InitServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(InitServlet.class.getName());
    public InitServlet() throws InterruptedException {
        log.info("Initservlet");
        initObjectify();
//        Coordinates coord = new Coordinates();
//        coord.setLatitude("304.1");
//        coord.setLongitude("209.1");
//        User ser = new User(1L,"xxx", "rrk310@nyu.edu", "www.google.com" , "1234" , "Axsd123", coord);
//        UserDao dao = new UserDao();
//        dao.saveUser(ser);
//        Thread.sleep(5);
//        User user1 = dao.findUserFSQ("1234");
//        log.info(user1.toString());
    }

    public void initObjectify(){
        ObjectifyService.register(User.class);
        ObjectifyService.register(Recommendation.class);
        ObjectifyService.register(Coordinates.class);
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("Servlet destroyed");
        super.destroy();
    }
}
