package com.get_away;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.get_away.backend.getawayApi.model.Recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InternalRecomActivity extends ActionBarActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_recom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();

        int index = extras.getInt("index");
        System.out.println(index);
        final Map<String, List<Recommendation>> recommendation= RecommendationsDataHolder.getInstance().recommendationsData.get(index);
        final List<String> keys = new ArrayList<String>(recommendation.keySet());
        getSupportActionBar().setTitle(keys.get(0));

        System.out.println("  Internal Recom Activity  " + recommendation);
        listView = (ListView) findViewById(R.id.lv_country);
        listView.setAdapter(new InnerListAdapter(getBaseContext(),recommendation.get(keys.get(0))));

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                        // TODO Auto-generated method stub
                        try
                        {
                            //ObjectMapper mapper = new ObjectMapper();
                            //String jsonInString = mapper.writeValueAsString(recommendation.get(keys.get(0)));

                            //List<RecommendationPojo> obj = (List<RecommendationPojo>)mapper.readValue(jsonInString, RecommendationPojo.class);

                            //String friend = obj.get(position).getFriendCheckedIn();
                            Toast.makeText(getApplicationContext(), "Friend checkedin is " + position, Toast.LENGTH_LONG).show();
                        }catch(Exception exp){
                            System.err.println(exp.getMessage());
                        }
                    }
                }
        );
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), TabbedNavigator.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
