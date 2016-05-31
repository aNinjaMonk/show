package com.show.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.show.MyApplication;
import com.show.R;

import java.util.List;

/**
 * Created by abhijeet on 30/05/16.
 */
public class LoginActivity extends Activity {
    String pass = "Xp7&h@/R=G4eqw&[";

    ProgressBar spinner;
    TextView loginText;
    AuthCallback authCallback;
    View background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        loginText = (TextView)findViewById(R.id.loginText);
        //background = (View)findViewById(R.id.back);
        spinner.setVisibility(View.GONE);
        loginText.setVisibility(View.GONE);
        //background.setVisibility(View.GONE);

        ParseUser currentuser = ParseUser.getCurrentUser();
        if(currentuser != null && currentuser.getString("name") == null){
            showHome();
        }
        else{
            authCallback = new AuthCallback() {
                @Override
                public void failure(DigitsException e) {
                    spinner.setVisibility(View.GONE);
                    loginText.setText(e.getLocalizedMessage());
                }

                @Override
                public void success(final DigitsSession session, String phoneNumber) {
                    spinner.setVisibility(View.VISIBLE);
                    loginText.setVisibility(View.VISIBLE);
                    //background.setVisibility(View.VISIBLE);

                    ParseUser.logInInBackground(session.getPhoneNumber(), pass, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUsr, ParseException e) {
                            if (e == null) {
                                parseUsr.put("digitsSession", session.hashCode());
                                parseUsr.saveEventually();

                            } else {
                                ParseUser parseUser = new ParseUser();
                                parseUser.setUsername(session.getPhoneNumber());
                                parseUser.setPassword(pass);
                                parseUser.put("digitsSession", session.hashCode());
                                parseUser.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            //Adding the user to
                                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                            //finish();
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            };
        }
    }

    public void loginClick(View view){
        Digits.authenticate(authCallback, R.style.DigitsTheme);
    }

    private void showHome(){
        Intent intent = new Intent(MyApplication.mContext, MainActivity.class);
        startActivity(intent);
    }
}
