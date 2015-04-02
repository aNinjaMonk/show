package com.example.abhi.show;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.shamanland.fonticon.FontIconDrawable;
import com.shamanland.fonticon.FontIconTypefaceHolder;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import jp.wasabeef.picasso.transformations.CropSquareTransformation;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private ProgressDialog progressDialog;

    MainActivity CameraActivity = null;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String mCurrentPhotoPath;
    public int curShow = 0;
    protected ArrayList<ShowData> sDataSet;
    RecyclerFragment fragment;
    CountDownTimer timer;
    LocationRequest mLocationRequest;
    EntryData myEntry;

    @InjectView(R.id.eventBrief) protected ImageView eventBrief;
    @InjectView(R.id.myEntry) protected ImageView mImageView;
    @InjectView(R.id.titleText) protected TextView titleText;
    @InjectView(R.id.briefText) protected TextView briefText;
    @InjectView(R.id.timerText) protected TextView timerText;
    @InjectView(R.id.showView) protected RelativeLayout showView;
    @InjectView(R.id.profileBtn) protected Button profileBtn;

    //ToDo: Camera orientation not restricting. opening in the landscape mode by default.
    //ToDo: Delete files from SD card.
    //ToDo: Progressbar to show the images/videos being uploaded.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Parse.enableLocalDatastore(this);

        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        FetchShowsInfo();

        /*if(savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new RecyclerFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }*/

        //CheckSwipeGestures();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            Log.e("Location","Lat:" + mLastLocation.getLatitude() + "Long:" + mLastLocation.getLongitude());
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try{

                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude(), 1);
                Log.e("Addresses",addresses.toString());

            }catch (IOException e1){
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Location","Connection Failed!");
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(final EntryData event){
        //myEntry = new EntryData();
        //myEntry = event;

        Picasso.with(getApplicationContext()).load(event.getImageUrl()).transform(new CropSquareTransformation()).into(mImageView);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EntryActivity.class);
                intent.putExtra("Selected",0);
                intent.putExtra("EntryId",event.getEntryId());
                intent.putExtra("EntryUrl",event.getImageUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void onEvent(NetworkStateChanged event){
        if(!event.isInternetConnected()){
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void CheckSwipeGestures(){
        //Multiple Shows
        showView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
            @Override
            public void onSwipeLeft() {
                if(curShow >= 0 && curShow < (sDataSet.size()-1)) {
                    curShow++;
                    RefreshFragment();
                    DisplayShowInfo(curShow);
                }
            }

            @Override
            public void onSwipeRight() {
                if (curShow > 0 && curShow <= (sDataSet.size() - 1)){
                    curShow--;
                    RefreshFragment();
                    DisplayShowInfo(curShow);
                }
            }
        });

        eventBrief.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
            @Override
            public void onSwipeLeft() {
                if(curShow >= 0 && curShow < (sDataSet.size()-1)) {
                    curShow++;
                    RefreshFragment();
                    DisplayShowInfo(curShow);
                }
            }

            @Override
            public void onSwipeRight() {
                if(curShow > 0 && curShow <= (sDataSet.size()-1)) {
                    curShow--;
                    RefreshFragment();
                    DisplayShowInfo(curShow);
                }
            }

            @Override
            public void onTapDouble() {
                Log.e("Tap","tap Double");
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage(R.string.leave_message).setTitle(R.string.leave_title);
            builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int id){

                    //ToDo: Remove the show from the user view.
                    //finish();
                    //onDestroy();
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

    private void RefreshFragment(){

        if(fragment != null){
            fragment.getData(sDataSet.get(curShow).getId());
        }
    }
    private void DisplayShowInfo(int index){
        titleText.setText(sDataSet.get(index).getTitle());
        briefText.setText(sDataSet.get(index).getBrief());
        showCountDown(sDataSet.get(index).getTo());
        Picasso.with(getApplicationContext()).load(sDataSet.get(index).getFileurl()).into(eventBrief);
    }
    private void FetchShowsInfo(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Show");
        query.whereLessThanOrEqualTo("from", new Date());
        query.whereGreaterThanOrEqualTo("to",new Date());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){

                    sDataSet = new ArrayList<ShowData>();
                    for(int i=0;i<parseObjects.size();i++){
                        ShowData show = new ShowData();
                        show.setId(parseObjects.get(i).getObjectId());
                        show.setBrief(parseObjects.get(i).getString("brief"));
                        show.setTitle(parseObjects.get(i).getString("title"));
                        show.setTo(parseObjects.get(i).get("to").toString());
                        ParseFile fileObject = (ParseFile) parseObjects.get(i).get("video");
                        show.setFileurl(fileObject.getUrl());
                        sDataSet.add(show);
                    }
                    //DisplayShowInfo(curShow);
                    //fragment.getData(sDataSet.get(curShow).getId());
                }
                else{
                    Log.e("Error",e.getMessage());
                }
            }
        });
    }
    private void showCountDown(String toDate){
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
    }

    public void submitEntryClick(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("File:","Error while creating file"+ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK){
            setPic();

        }
    }

    private void setPic() {
        // Get the dimensions of the View

        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        //Bitmap to Bytes[]
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();

        final ParseFile file = new ParseFile("JPEG_"+ new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".png",byteArray);

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                final ParseObject entry = new ParseObject("Entry");
                entry.put("file",file);
                entry.put("createdBy", ParseUser.getCurrentUser());
                entry.put("parent",ParseObject.createWithoutData("Show",sDataSet.get(curShow).getId()));

                entry.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        EventBus.getDefault().post(new EntryData(file.getUrl(), entry.getObjectId()));
                    }
                });
            }
        },new ProgressCallback() {

            @Override
            public void done(Integer integer) {
                //Delete the file from local device.
                File file = new File(mCurrentPhotoPath);
                boolean deleted = file.delete();

            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onPromoteClick(View v){
        Intent intent = new Intent(MainActivity.this,EntryActivity.class);
        startActivity(intent);

            /*final String [] items = new String[] {"Whatsapp", "Facebook"};
                final Integer[] icons = new Integer[] {R.drawable.promote, R.drawable.promote};
                //ListAdapter adapter = new ArrayAdapterWithIcon(MainActivity.this, items, icons);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(R.string.promote_title);

                /*builder.setItems(R.array.promote_options, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        Log.e("Parse",String.valueOf(which));
                    }
                });*/

                /*builder.setAdapter(adapter, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        Log.e("Parse",String.valueOf(which));
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();*/
    }

    public void onProfileViewClick(View v) {
        Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
        startActivity(intent);
    }
    public void onLeaveBtnClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(R.string.leave_message).setTitle(R.string.leave_title);
        builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id){

                //ToDo: Remove the show from the user view.

                Toast.makeText(getApplicationContext(),
                        "Left the show", Toast.LENGTH_SHORT)
                        .show();

            }
        });
        builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id){


                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

       /* Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/baskerville_semibold.ttf");
        TextView tv = (TextView) findViewById(R.id.logoText);
        tv.setTypeface(tf);*/


        //ToDo: Get Location

/*
        //Play video
        if(mediaController == null)
            mediaController = new MediaController(MainActivity.this);


        videoView = (VideoView)findViewById(R.id.videoView1);

        progressDialog = new ProgressDialog(MainActivity.this);
        //progressDialog.setTitle("video Loading Example..");
        progressDialog.setMessage("Loading.. ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try{
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1));
            //videoView.setVideoPath("http://www.ebookfrenzy.com/android_book/movie.mp4");

        }
        catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            public void onPrepared(MediaPlayer mediaPlayer){
                progressDialog.dismiss();
                videoView.seekTo(position);
                if(position == 0){
                    videoView.start();
                }
                else{
                    videoView.pause();
                }
            }
        });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       /* if(requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageBitmap(imageBitmap);
        }
        */
    /*}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("Position", videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        position = savedInstanceState.getInt("Position");
        videoView.seekTo(position);
    }*/

