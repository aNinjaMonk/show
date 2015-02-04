package com.example.abhi.show;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permissions;
import java.security.Signature;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends Activity {

    @InjectView(R.id.loginMsg) protected TextView loginMsg;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        //GenerateKeyHash();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)){
            showHome();
        }
    }

    private void GenerateKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.aspiration.show",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void loginBtnListener(View v){
            progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

            List<String> permissions = Arrays.asList("email", ParseFacebookUtils.Permissions.Friends.ABOUT_ME);

            ParseFacebookUtils.logIn(permissions,LoginActivity.this,new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    progressDialog.dismiss();
                    if(parseUser == null){
                        loginMsg.setText("Allow facebook login to continue.");
                    }
                    else if(parseUser.isNew()){
                        showHome();

                        //Get Additional Permissions.
                        Session session = ParseFacebookUtils.getSession();
                        session.requestNewReadPermissions(new Session.NewPermissionsRequest(
                                LoginActivity.this,Arrays.asList(ParseFacebookUtils.Permissions.User.INTERESTS)));
                        ParseFacebookUtils.saveLatestSessionDataInBackground(parseUser);

                        //Store the username & nickname & email & gender into the data store.

                    }
                    else{
                        showHome();
                    }
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode,resultCode,data);
    }

    private void GroupInfo(){
        Intent intent = new Intent(getApplicationContext(),GroupActivity.class);
        startActivity(intent);
    }
    private void showHome(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

}
