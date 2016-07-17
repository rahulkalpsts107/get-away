package com.get_away;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.foursquare.android.nativeoauth.FoursquareCancelException;
import com.foursquare.android.nativeoauth.FoursquareDenyException;
import com.foursquare.android.nativeoauth.FoursquareInvalidRequestException;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.FoursquareOAuthException;
import com.foursquare.android.nativeoauth.FoursquareUnsupportedVersionException;
import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;
import com.get_away.backend.getawayApi.GetawayApi;
import com.get_away.backend.getawayApi.model.Coordinates;
import com.get_away.backend.getawayApi.model.MyBean;
import com.get_away.backend.getawayApi.model.Recommendation;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class MainActivity extends FragmentActivity {

    private static final int PERMISSIONS_LOCATION = 0;
    private static final int REQUEST_CODE_FSQ_CONNECT = 200;
    private static final int REQUEST_CODE_FSQ_TOKEN_EXCHANGE = 201;

    private static final String TAG = "GetawayIntentService";
    private static final String GCM_TOKEN = "GCM_TOKEN";
    //private String accessToken = null;

    /**
     * Obtain your client id and secret from:
     * https://foursquare.com/developers/apps
     */

    private static final String LOG_TAG = "MainActivity";

    // App made on my account
     private static final String CLIENT_ID = "";
     private static final String CLIENT_SECRET = "";


    public class MyThread extends Thread{
        private MainActivity thiz;
        private String accessToken;
        public MyThread(MainActivity thiz, String accessToken)
        {
            this.thiz = thiz;
            this.accessToken=accessToken;
        }
        @Override
        public void run() {
            Log.d(LOG_TAG, "Dispatching GCM Server");
            // Send to server
            GetawayApi.Builder builder = new GetawayApi.Builder(AndroidHttp.newCompatibleTransport(), new JacksonFactory(),null);
                    /*For local testing*/
            builder.setRootUrl("http://172.16.181.189:8080/_ah/api/");

                    /*Bewlo code to solve some wierd gzip crash required only in Test*/

                    builder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            try {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(thiz);

                String token = sharedPreferences.getString(GCM_TOKEN, null);

                Log.d(LOG_TAG, "Main activity recieved token");
                Log.d(TAG, "GCM Registration Token: " + token);

                GetawayApi api = builder.build();
                Coordinates coord = new Coordinates();
                coord.setLatitude("101.2");
                coord.setLongitude("300.1");
                MyBean bean = api.register(accessToken, token, coord).execute();
                Map<String, Object> generalRe = api.getGeneralRecommendations(accessToken).execute();
                System.out.println(generalRe);

                for(String s : generalRe.keySet()){
                    if(s.compareTo("getaway") == 0)
                    {
                        System.out.println("Got data for get away");
                        RecommendationsDataHolder.getInstance().recommendationsData = (List<Map<String, List<Recommendation>>>)(generalRe.get(s));
                        System.out.println("ACTUAL "+RecommendationsDataHolder.getInstance().recommendationsData);
                    }
                    else
                    {
                        System.out.println("Else calse no data");
                        //RecommendationsDataHolder.getInstance().recommendationsData = (List<Map<String, List<Recommendation>>>)(generalRe.get(s));
                    }
                }
                System.out.println("Data received is "+bean.getData());
            } catch(IOException e) {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		/*Start getaway background service*/
        Intent intent = new Intent(this, GetawayIntentService.class);
        startService(intent);
		
        setContentView(R.layout.activity_main);

        initializeLoginButt();
        getPermissions();


        //MyThread t = new MyThread(this,"123456");
        //t.start();
    }

    private void getPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        int fine_access = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(fine_access != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_LOCATION);
        }
    }

    private void initializeLoginButt() {
        Button btnLogin = (Button) findViewById(R.id.fq_login);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, TabbedNavigator.class));
            // Start the native auth flow.
            Intent intent = FoursquareOAuth.getConnectIntent(MainActivity.this, CLIENT_ID);

            // If the device does not have the Foursquare app installed, we'd
            // get an intent back that would open the Play Store for download.
            // Otherwise we start the auth flow.
            if (FoursquareOAuth.isPlayStoreIntent(intent)) {
                toastMessage(MainActivity.this, getString(R.string.app_not_installed_message));
                startActivity(intent);
            } else {
                // This returns the intent back and start the intent to foursquare
                startActivityForResult(intent, REQUEST_CODE_FSQ_CONNECT);
            }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult "+requestCode);
        switch (requestCode) {
            case REQUEST_CODE_FSQ_CONNECT:
                onCompleteConnect(resultCode, data);
                break;

            case REQUEST_CODE_FSQ_TOKEN_EXCHANGE:
                onCompleteTokenExchange(resultCode, data);
                startActivity(new Intent(this, TabbedNavigator.class));
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSIONS_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "Permission to access location granted");
                } else {
                    Toast.makeText(this, "We require you to provide the location information",
                            Toast.LENGTH_LONG).show();
                    finish();
                    System.exit(1);
                }
        }
    }

    private void onCompleteConnect(int resultCode, Intent data) {
        AuthCodeResponse codeResponse = FoursquareOAuth.getAuthCodeFromResult(resultCode, data);
        Exception exception = codeResponse.getException();

        if (exception == null) {
            // Success.
            String code = codeResponse.getCode();

            Log.d(LOG_TAG, "Access Code = " + code);
            performTokenExchange(code);

        } else {
            if (exception instanceof FoursquareCancelException) {
                // Cancel.
                toastMessage(this, "Canceled");

            } else if (exception instanceof FoursquareDenyException) {
                // Deny.
                toastMessage(this, "Denied");

            } else if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = exception.getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
                toastMessage(this, errorMessage + " [" + errorCode + "]");

            } else if (exception instanceof FoursquareUnsupportedVersionException) {
                // Unsupported Fourquare app version on the device.
                toastError(this, exception);

            } else if (exception instanceof FoursquareInvalidRequestException) {
                // Invalid request.
                toastError(this, exception);

            } else {
                // Error.
                toastError(this, exception);
            }
        }
    }

    private void onCompleteTokenExchange(int resultCode, Intent data) {
        AccessTokenResponse tokenResponse = FoursquareOAuth.getTokenFromResult(resultCode, data);
        Exception exception = tokenResponse.getException();

        //System.out.println(this.getClass().getName();
        if (exception == null) {
            String accessToken = tokenResponse.getAccessToken();
            // Success.

            Log.d(LOG_TAG, "Access Token = " + accessToken);

            // Persist the token for later use. In this example, we save
            // it to shared prefs.
            TokenStore.get().setToken(accessToken);

            MyThread t = new MyThread(this,accessToken);
            t.start();
        } else {
            if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = ((FoursquareOAuthException) exception).getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
                toastMessage(this, errorMessage + " [" + errorCode + "]");

            } else {
                // Other exception type.
                toastError(this, exception);
            }
        }
    }

    /**
     * Exchange a code for an OAuth Token. Note that we do not recommend you
     * do this in your app, rather do the exchange on your server. Added here
     * for demo purposes.
     *
     * @param code
     *          The auth code returned from the native auth flow.
     */
    private void performTokenExchange(String code) {
        Intent intent = FoursquareOAuth.getTokenExchangeIntent(this, CLIENT_ID, CLIENT_SECRET, code);
        startActivityForResult(intent, REQUEST_CODE_FSQ_TOKEN_EXCHANGE);
    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastError(Context context, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}