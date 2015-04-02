package com.example.abhi.show;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by abhi on 24/02/15.
 */
public class ShowActivity extends Activity{
    protected ArrayList<Show> shows;
    //private static RecyclerView recyclerView;
    //private static ShowAdapter adapter;

    /*@InjectView(R.id.eventBrief) protected ImageView eventBrief;
    @InjectView(R.id.myEntry) protected ImageView mImageView;
    @InjectView(R.id.titleText) protected TextView titleText;
    @InjectView(R.id.briefText) protected TextView briefText;*/
    //@InjectView(R.id.showView) protected RelativeLayout showView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shows);

        ButterKnife.inject(this);
        //fetchShows();
        Log.e("Shows","Shows listing!");

    }
    public void showShows(){
        /*recyclerView = (RecyclerView)findViewById(R.id.showsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,1,false));
        adapter = new ShowAdapter(this.getApplicationContext(), shows);
        recyclerView.setAdapter(adapter);*/

        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        //recyclerView.setHasFixedSize(true);
        //recyclerView.scrollToPosition(0);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);

            builder.setMessage(R.string.leave_message).setTitle(R.string.leave_title);
            builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int id){

                    System.exit(0);
                }
            });
            builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int id){
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void fetchShows(){

        shows = new ArrayList<Show>();
        //shows.add(new Show(1,"Hello buddy!",3,(R.drawable.image).getAbsolutePath(),null));
        showShows();

        //ParseQuery<ParseObject> query = ParseQuery.getQuery("Show");
        /*query.whereLessThanOrEqualTo("from", new Date());
        query.whereGreaterThanOrEqualTo("to",new Date());*/

        /*shows = new ArrayList<Show>();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    Log.e("Size",String.valueOf(parseObjects.size()));

                    for(int i=0;i<parseObjects.size();i++){
                        //ArrayList<Entry> entries = new ArrayList<Entry>();
                        //Log.e("Show",parseObjects.get(i).toString());
                        //entries.add(new Entry());
                        Log.e("Show","cming");
                        shows.add(new Show(parseObjects.get(i).getObjectId(),
                                parseObjects.get(i).getString("title"),
                                parseObjects.get(i).get("to").toString(),
                                ((ParseFile)parseObjects.get(i).get("video")).getUrl(),
                                null));
                    }
                    //showShows();
                }
                else{
                    Log.e("Error", e.getMessage());
                }
            }
        });*/
    }
    /*private void showCountDown(String toDate){
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

        try {
            Date d1 = dateformat.parse(toDate);
            Date d2 = dateformat.parse(dateformat.format(new Date()));

            long diff = d1.getTime() - d2.getTime();

            //Check if countdown exists.. if yes.... kill it.
            if(timer != null){
                timer.cancel();
            }
            timer = new CountDownTimer(diff,60000){
                public void onTick(long milliUntilFinished){

                    long days = milliUntilFinished / (1000*60*60 *24) % 365; //32832000000 ~ 15 days.
                    long hours = milliUntilFinished / (1000*60*60) %24; //1500/60 = 1 hr
                    long minutes = milliUntilFinished / (1000*60) %60; //225/60 = 3 % 60 = 3
                    long seconds = milliUntilFinished/1000 % 60; // 225%60 = 45sec

                    if(days > 0)
                        timerText.setText(days + " d\n"+ hours + " hr");
                    else if(days == 0 && hours > 0)
                        timerText.setText(hours + " hr\n" + minutes + " min");
                    else if(days == 0 && hours == 0 ){
                        timerText.setText(minutes + " min\n" + seconds + " sec");
                    }
                }
                public void onFinish(){
                    //timerText.setText("time Up!");
                    //Once time is up show the results.
                }
            };
            timer.start();


        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
        };
    }*/
}

