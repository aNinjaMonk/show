package com.show.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.helpshift.support.Support;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shamanland.fonticon.FontIconDrawable;
import com.shamanland.fonticon.FontIconTextView;
import com.show.MyApplication;
import com.show.R;
import com.show.fragments.ShowFragment;

/**
 * Created by abhijeet on 30/05/16.
 */
public class MainActivity extends AppCompatActivity{
    Toolbar toolbar;
    DrawerLayout mDrawer;
    NavigationView nvDrawer;
    ActionBarDrawerToggle drawerToggle;
    TabLayout tabLayout;
    ViewPager viewPager;
    //ViewPagerAdapter adapter;
    private ProgressBar spinner;
    TextView logoutText;
    View background;
    boolean isStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        //tabLayout = (TabLayout) findViewById(R.id.tabs);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        logoutText = (TextView) findViewById(R.id.logoutText);
        background = findViewById(R.id.back);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        isStart = sharedPreferences.getBoolean("start", true);

        //setupPagerAdapter();

        spinner.setVisibility(View.GONE);
        logoutText.setVisibility(View.GONE);
        background.setVisibility(View.GONE);

        // Find our drawer_menu view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setDrawerListener(drawerToggle);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        // Setup drawer_menu view
        setupDrawer();

        setupFragment();
    }

    public void setupFragment(){
        ShowFragment showFragment = new ShowFragment();
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction().add(R.id.frameLayout,showFragment);
        transaction.commit();
    }

    public void setupDrawer() {
        FontIconTextView logout = (FontIconTextView) nvDrawer.findViewById(R.id.logout);
        FontIconTextView share = (FontIconTextView) nvDrawer.findViewById(R.id.share);
        FontIconTextView support = (FontIconTextView) nvDrawer.findViewById(R.id.support);
        //FontIconTextView buy = (FontIconTextView) nvDrawer.findViewById(R.id.buy);
        TextView name = (TextView) nvDrawer.findViewById(R.id.name);

        Drawable icon_setting = FontIconDrawable.inflate(MyApplication.mContext, R.xml.icon_setting);
        Drawable icon_logout = FontIconDrawable.inflate(MyApplication.mContext, R.xml.icon_logout);
        Drawable icon_share = FontIconDrawable.inflate(MyApplication.mContext, R.xml.icon_share);
        Drawable icon_support = FontIconDrawable.inflate(MyApplication.mContext, R.xml.icon_support);
        Drawable icon_buy = FontIconDrawable.inflate(MyApplication.mContext, R.xml.icon_buy);

        support.setCompoundDrawables(null, icon_support, null, null);
        share.setCompoundDrawables(null, icon_share, null, null);
        logout.setCompoundDrawables(null, icon_logout, null, null);
        //buy.setCompoundDrawables(null, icon_buy, null, null);

        name.setText((ParseUser.getCurrentUser().getString("name") == null) ? "" : ParseUser.getCurrentUser().getString("name"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer_menu toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void menuItemClicked(View view) {
        /*if (view.getId() == R.id.buy) {
            Log.e("Clicked", "buy");



        } else */
        if (view.getId() == R.id.share) {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_message));
            try {
                startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MyApplication.mContext, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.logout) {
            //show logout text and spinner.
            logoutText.setText(getResources().getString(R.string.logging_out));
            background.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            logoutText.setVisibility(View.VISIBLE);

            //Logout of twitter session.
            Digits.getSessionManager().clearActiveSession();

            //Clear shared Preference.
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    //Redirect to login page.
                    Intent intent = new Intent(getApplication(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        } else if (view.getId() == R.id.support) {
            //SupportDialog dialog = new SupportDialog();
            //dialog.show(getSupportFragmentManager(), "support");

            Support.showFAQs(this);

        }
        mDrawer.closeDrawers();
    }

    public void showMenu(View view) {
        mDrawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //drawerToggle.syncState();
    }
}
