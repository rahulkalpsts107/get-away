package com.get_away;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.get_away.backend.getawayApi.model.Recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rrk on 5/10/16.
 */
public class RecommendationsFragment  extends ListFragment implements AdapterView.OnItemClickListener{
    public static RecommendationsAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recommendations_layout,null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         adapter = new RecommendationsAdapter(getContext(),RecommendationsDataHolder.getInstance().recommendationsData);

        // Attach the adapter to a ListView
        ListView listView = (ListView) getListView();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
        Map<String, List<Recommendation>> recommendation= RecommendationsDataHolder.getInstance().recommendationsData.get(position);
        List<String> keys = new ArrayList<String>(recommendation.keySet());

        Intent intent = new Intent(getActivity(),InternalRecomActivity.class);
        intent.putExtra("index",position);
        getActivity().startActivity(intent);
    }
}
