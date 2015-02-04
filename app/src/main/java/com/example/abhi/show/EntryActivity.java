package com.example.abhi.show;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by abhi on 21/01/15.
 */
public class EntryActivity extends ActionBarActivity {

    @InjectView(R.id.entryView) protected ImageView entryView;
    @InjectView(R.id.commentBox) protected EditText commentBox;
    private String EntryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_entry);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        int id = extras.getInt("Selected");
        EntryId = extras.getString("EntryId");

        Picasso.with(getApplicationContext()).load(extras.getString("EntryUrl")).into(entryView);
        showComments();
    }

    private void showComments(){
        //Fetch the name of the submitter.
        //Fetch the comments on the entry & show along with the name,picture of the commentor.

        ParseQuery query = ParseQuery.getQuery("Comment");
        ParseObject entry = new ParseObject("Entry");
        entry.setObjectId(EntryId);
        query.whereEqualTo("parent",entry);
        query.include("createdBy");

        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {

                //Log.e
                //Get user who created the comment..
                Log.e("List","size"+list.size());
            }
        });
    }

    public void closeBtnListener(View v){
        EntryActivity.this.finish();
    }
    public void commentBtnListener(View v){
        //Attach textedit to keyboard height.
        //Toggle the textbox when in write mode.

        commentBox.requestFocus();
        commentBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        (new Handler()).postDelayed(new Runnable() {

            public void run() {
                commentBox.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                commentBox.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
    }
    public void loveBtnListener(View v){

    }

    public void postBtnListener(View v){

        ParseObject comment = new ParseObject("Comment");
        comment.put("createdBy",ParseUser.getCurrentUser());
        comment.put("parent",ParseObject.createWithoutData("Entry",EntryId));
        comment.put("text",commentBox.getText().toString());
        comment.saveInBackground();

        //Refresh the data in the comment view.
        //showComments();
    }
}
