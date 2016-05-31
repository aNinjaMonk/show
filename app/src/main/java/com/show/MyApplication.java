package com.show;

import android.app.Application;
import android.content.Context;

import com.digits.sdk.android.Digits;
import com.helpshift.All;
import com.helpshift.Core;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.shamanland.fonticon.FontIconTypefaceHolder;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

/**
 * Created by abhijeet on 30/05/16.
 */
public class MyApplication extends Application {
    public static Context mContext;
    public static boolean islive = true;
    String parse_appId = "qmfrUeEE4DwJUaaQXielogYSvuc1l7ftwQokFxp5";
    String parse_client = "uUqOs1StHFhC8V051Pm680D0ceXOV4WGPgPIfzQR";

    String parse_appId_prod = "qmfrUeEE4DwJUaaQXielogYSvuc1l7ftwQokFxp5";
    String parse_client_prod = "uUqOs1StHFhC8V051Pm680D0ceXOV4WGPgPIfzQR";

    String helpshift_appKey = "5fe7b75cbd09f1353fcb5a89821e12a4";
    String helpshift_domain = "bahikhata.helpshift.com";
    String helpshift_appId = "bahikhata_platform_20150527081846916-a2683260529c87a";

    @Override
    public void onCreate() {
        super.onCreate();
        super.onCreate();

        mContext = getApplicationContext();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.digits_Key),
                getResources().getString(R.string.digits_secret));
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        //Parse.addParseNetworkInterceptor(new ParseLogInterceptor());
        Parse.enableLocalDatastore(this);

        if(islive)
            Parse.initialize(this, parse_appId_prod, parse_client_prod);
        else
            Parse.initialize(this, parse_appId,parse_client);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        FontIconTypefaceHolder.init(getAssets(), "icons.ttf");

        JodaTimeAndroid.init(this);

        HashMap config = new HashMap();
        Core.init(All.getInstance());
        Core.install(this,helpshift_appKey,helpshift_domain,helpshift_appId,config);
    }
}
