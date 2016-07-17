package com.get_away.backend.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;


/**
 * Created by rrk on 4/30/16.
 */
public class NotifPosterTask implements GetAwayTask {

    private static final String GCM_LINK = "https://gcm-http.googleapis.com/gcm/send";
    private static final String API_KEY = System.getProperty("gcm.api.key");
    private static final Logger log = Logger.getLogger(NotifPosterTask.class.getName());

    private String data;
    // TODO: Remove the System.getProperty from here
    private String gcmToken;

    public NotifPosterTask(String data){
        this.data = data;
    }

    @Override
    public void execute() {
        try {
            log.info("Notifposter Executed by " + Thread.currentThread().getId());
            URL url = new URL(GCM_LINK);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(data.getBytes());
            log.info("Write done");

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();


            //System.out.println(resp);

        } catch (IOException exp) {
            log.info("Unable to send GCM message.");
            log.info(exp.getMessage());
        }

    }

    @Override
    public void run() {
        execute();
    }
}
