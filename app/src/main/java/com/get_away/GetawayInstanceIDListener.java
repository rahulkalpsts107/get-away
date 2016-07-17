package com.get_away;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by rrk on 4/30/16.
 */
public class GetawayInstanceIDListener extends InstanceIDListenerService {

    public void onTokenRefresh() {
        Intent intent = new Intent(this, GetawayIntentService.class);
        startService(intent);
    }

}
