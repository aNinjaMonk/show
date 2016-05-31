package com.show.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.show.MyApplication;
import com.show.R;

/**
 * Created by abhijeet on 30/05/16.
 */
public class LaunchActivity extends Activity {
    int SPLASH_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MyApplication.mContext, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_quick, 0);
            }
        }, SPLASH_TIME);

    }
}
