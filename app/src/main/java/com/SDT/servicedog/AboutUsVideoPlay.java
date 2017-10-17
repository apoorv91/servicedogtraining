package com.SDT.servicedog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by laitkor3 on 25/07/16.
 */
public class AboutUsVideoPlay extends ActivityIntermediateClass {

    private VideoView video_play;
    ProgressDialog progress;
    JSONObject resultOutput;
    ProgressDialog pDialog;
    URL url = null;

    StringBuilder responseOutput;
    String _programmeID;
    String _videoUrlData, _newVideoUrlData;
    String _loginID, _clientid;
    String first_name, last_name;

    public static String _utfValue = "";

    private Toolbar toolbar;
    private View header;
    String TaskListDescclientID;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_video_play);

        video_play = (VideoView) findViewById(R.id.videoView);
        System.out.println("/*******************************  ABOUT US VIDEO PLAY CLASS STARTED   *****************************************************/");
        Intent intent = getIntent();
        _videoUrlData = intent.getStringExtra("VIDEO_URL");
        _programmeID = intent.getStringExtra("PROGRAMME_ID");
        _loginID = intent.getStringExtra("EMAIL_ID");
        _clientid = intent.getStringExtra("CLIENT_ID");

        // removed white spaces if any from the video url.

        _newVideoUrlData = _videoUrlData.replaceAll(" ", "%20");


        System.out.println("client_id and client_email_id and firstname & lastname and _clientID" + _programmeID + _loginID + first_name + last_name + "\n" + _clientid);
        System.out.println("ProgrammeID :" + _programmeID);
        System.out.println("about_us_video :" + _videoUrlData.toString());
        System.out.println("about_us_video url white spaces removed :" + _newVideoUrlData.toString());
        _utfValue = _programmeID;
        playVideoMethod();

        TaskListDescription.activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initNavigationDrawer();
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:
                        Intent aboutusintent = new Intent(AboutUsVideoPlay.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
//                        Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(AboutUsVideoPlay.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(AboutUsVideoPlay.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(AboutUsVideoPlay.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(AboutUsVideoPlay.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;
                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(AboutUsVideoPlay.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(AboutUsVideoPlay.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID",_loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(AboutUsVideoPlay.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish(); // Call once you redirect to another activity

                }
                return true;
            }
        });

        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
        tv_email.setText(haredpreferences.getString("first_name", "Null") + " " + haredpreferences.getString("last_name", "Null"));

        TaskListDescclientID = haredpreferences.getString("CLIENT_ID", "Null");

        System.out.println("training prog lcient id shared prefernce" + TaskListDescclientID);
        // System.out.println("shared preference values are: "+"first="+haredpreferences.getString("first_name", "Null")+"last="+haredpreferences.getString("last_name", "Null")+"client="+haredpreferences.getString("CLIENT_ID", "Null")+"phone="+haredpreferences.getString("phone_number", "Null")+"email="+haredpreferences.getString("email_id", "Null"));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (Imageclass.image_getdata_flag == 1) {

            ImageView profileImageView = (ImageView) header.findViewById(R.id.imageView3);
            profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());

        }
    }

    public void playVideoMethod() {

        // Create a progressbar
        pDialog = new ProgressDialog(AboutUsVideoPlay.this, R.style.dialog);
        // Set progressbar title
        pDialog.setTitle("Video Streaming...");
        // Set progressbar message
        pDialog.setMessage("Buffering Please wait");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        // Show progressbar
        pDialog.show();

        final MediaController mc = new MyMediaController(video_play.getContext());

        try {

            mc.setMediaPlayer(video_play);
            video_play.setMediaController(mc);

            // Start the MediaController
//            MediaController mediaController = new MediaController(this);
//            mediaController.setAnchorView(video_play);

            // Get the URL from String VideoURL
            // Uri video = Uri.parse("http://www.ebookfrenzy.com/android_book/movie.mp4");
//  Uri video = Uri.parse("http://servdog.dealopia.com//uploads//videos//Funny-Birds-Video.mp4");

            Uri video = Uri.parse(_newVideoUrlData.toString());
          //  video_play.setMediaController(mediaController);
            video_play.setVideoURI(video);

        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }

        video_play.requestFocus();
        video_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {

                View placeholder = (View) findViewById(R.id.placeholder);
                placeholder.setVisibility(View.GONE);
                pDialog.dismiss();
                video_play.start();
            }
        });
    }

    public class MyMediaController extends MediaController {

        public MyMediaController(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyMediaController(Context context, boolean useFastForward) {
            super(context, useFastForward);
        }

        public MyMediaController(Context context) {
            super(context);
        }

        @Override
        public void show(int timeout) {
            super.show(0);
        }

    }

}
