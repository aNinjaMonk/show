package com.example.abhi.show;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class RecyclerFragment extends Fragment {

    /** Required Overrides for Sample Fragments */

    /*protected abstract RecyclerView.LayoutManager getLayoutManager();
    protected abstract RecyclerView.ItemDecoration getItemDecoration();
    protected abstract int getDefaultItemCount();*/

    protected RecyclerView mRecyclerView;
    protected ArrayList<EntryData> mDataSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //      curShowId = getArguments().getString("curShowId");
        //    Log.e("showId",curShowId);

        View rootView = inflater.inflate(R.layout.recycler_view_frag,container,false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),0,false));
        mRecyclerView.addItemDecoration(new SpacerDecorator(getActivity().getApplicationContext()));
        mRecyclerView.scrollToPosition(0);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putSerializable();
        super.onSaveInstanceState(outState);
    }

    public void getData(String showId){
        //Fetch all entries except mine... If my entry present it should be placed at my entry..
        //

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Entry");
        ParseObject show = new ParseObject("Show");
        show.setObjectId(showId);
        query.whereEqualTo("parent",show);
        query.include("createdBy");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    mDataSet = new ArrayList<EntryData>();

                    for (int i = 0; i < parseObjects.size(); i++) {
                        ParseFile fileObject = (ParseFile) parseObjects.get(i).get("file");
                        //If matches then set the entry on myEntry instead of putting inside entries data.

                        ParseObject user = parseObjects.get(i).getParseObject("createdBy");
                        if(user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId()))
                            EventBus.getDefault().post(new EntryData(fileObject.getUrl(), parseObjects.get(i).getObjectId()));
                        else
                            mDataSet.add(new EntryData(fileObject.getUrl(), parseObjects.get(i).getObjectId()));
                    }
                    mRecyclerView.setAdapter(new CustomAdapter(getActivity().getApplicationContext(), mDataSet));
                }
            }
        });
    }

}