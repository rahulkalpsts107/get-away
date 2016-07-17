package com.get_away;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by rrk on 4/30/16.
 */
public class GetawayIntentService extends IntentService {

    private static final String TAG = "GetawayIntentService";
    private static final String GCM_TOKEN = "GCM_TOKEN";
    public GetawayIntentService(){
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Received intent");
        // Make a call to Instance API
        InstanceID instanceID = InstanceID.getInstance(this);
        //Project number from google cloud console.
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.d(TAG, "Getaway intent service GCM Registration Token: " + token);

            // store this data
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            // save token
            sharedPreferences.edit().putString(GCM_TOKEN, token).apply();

        } catch (IOException e) {
            Log.e(TAG, "Error while sending token to app-server " + e.getMessage());
        }
    }
}