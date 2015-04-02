package com.example.abhi.show;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.shamanland.fonticon.FontIconTypefaceHolder;


/**
 * Created by abhi on 21/01/15.
 */
public class showApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this,this.getString(R.string.parse_appID),this.getString(R.string.parse_clientID));
        ParseFacebookUtils.initialize(this.getString(R.string.app_id));

        FontIconTypefaceHolder.init(getAssets(), "icons.ttf");
    }
}