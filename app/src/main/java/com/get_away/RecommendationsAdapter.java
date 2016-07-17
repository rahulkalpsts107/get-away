package com.get_away;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.get_away.backend.getawayApi.model.Recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rrk on 5/12/16.
 */
public class RecommendationsAdapter extends ArrayAdapter <Map<String, List<Recommendation>>> {
    public RecommendationsAdapter(Context context, List<Map<String, List<Recommendation>>>recommendations) {
        //List<Recommendation> list =
        super(context, 0, recommendations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Map<String, List<Recommendation>> recommendation = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recommendations_view_layout, parent, false);
        }
        // Lookup view for data population
        TextView categoryName = (TextView) convertView.findViewById(R.id.txtCategory);

        // Populate the data into the template view using the data object

        List<String> keys = new ArrayList<String>(recommendation.keySet());

        categoryName.setText(keys.get(0));

        // Return the completed view to render on screen
        return convertView;
    }
}