package com.get_away;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.get_away.backend.getawayApi.model.Recommendation;
import com.google.api.client.util.ArrayMap;

import java.util.List;

/**
 * Created by rrk on 5/12/16.
 */
public class InnerListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Recommendation> recommendations;
    public InnerListAdapter(Context context, List<Recommendation> recommendations) {
        mInflater = LayoutInflater.from(context);
        this.recommendations = recommendations;
    }

    public int getCount() {
        return recommendations.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.inner_list_row, null);
            holder = new ViewHolder();
            holder.text1 = (TextView) convertView
                    .findViewById(R.id.TextView01);
            holder.text2 = (TextView) convertView
                    .findViewById(R.id.TextView02);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        System.out.println("DUMMY GET ----->" + recommendations.get(position));


        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(recommendations.get(position));

            RecommendationPojo obj = mapper.readValue(jsonInString, RecommendationPojo.class);

            holder.text1.setText(obj.getPlaceName());
            holder.text2.setText(obj.getAddress());
        }catch(Exception exp){
            System.err.println(exp.getMessage());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView text1;
        TextView text2;
    }
}