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

import java.net.URL;

public class AudioPlay extends AppCompatIntermediateActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private VideoView video_play;
    private ImageView img_audio;
    ProgressDialog pDialog;
    URL url = null;
    String _audioUrlData,_newaudioUrlData, _clientId, _programmeId, _taskId;

    private View header;
    String TaskListDescclientID,_loginID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();

        setContentView(R.layout.activity_audio_play);

        video_play = (VideoView) findViewById(R.id.videoView);
        //  img_audio = (ImageView) findViewById(R.id.imageView_audio);

        TaskListDescription.activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        System.out.println("/*******************************  AUDIO PLAY CLASS STARTED  *****************************************************/");

        Intent intent = getIntent();
        _audioUrlData = intent.getStringExtra("AUDIO_URL");
        _clientId = intent.getStringExtra("CLIENT_ID");
        _programmeId = intent.getStringExtra("PROGRAMME_ID");
        _taskId = intent.getStringExtra("TASK_ID");

//        _client_id = intent.getStringExtra("CLIENT_ID");
//        _audioUrlData = intent.getStringExtra("PROGRAMME_ID");
//        _audioUrlData = intent.getStringExtra("TASK_ID");

        System.out.println("audio file is :" + _audioUrlData);

         _newaudioUrlData = _audioUrlData.replaceAll(" ", "%20");

        System.out.println("New audio file url is :" + _newaudioUrlData);

        System.out.println("Audio play page------ client id, programme id & task id are: \n" + _clientId + "\n" + _programmeId + "\n" + _taskId);
        playAudioMethod();

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
                        Intent aboutusintent = new Intent(AudioPlay.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
//                        Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(AudioPlay.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(AudioPlay.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(AudioPlay.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(AudioPlay.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;
                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(AudioPlay.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(AudioPlay.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID",_loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(AudioPlay.this, MainActivity.class);
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

    public void playAudioMethod() {

        // Create a progressbar
        pDialog = new ProgressDialog(AudioPlay.this, R.style.dialog);
        // Set progressbar title
        pDialog.setTitle("Audio Streaming...");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            // MediaController mediaController = new MediaController(this);
            final MediaController mc = new MyMediaControllerAudio(video_play.getContext());

            mc.setAnchorView(video_play);
            Uri video = Uri.parse(_newaudioUrlData);
            video_play.setMediaController(mc);
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
                //  placeholder.setVisibility(View.GONE);

                pDialog.dismiss();
                //  img_audio.setBackgroundResource(R.drawable.audio_icon);
                //  img_audio.setVisibility(View.VISIBLE);
                video_play.start();
            }
        });
    }

    public class MyMediaControllerAudio extends MediaController {

        public MyMediaControllerAudio(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyMediaControllerAudio(Context context, boolean useFastForward) {
            super(context, useFastForward);
        }

        public MyMediaControllerAudio(Context context) {
            super(context);
        }

        @Override
        public void show(int timeout) {
            super.show(0);
        }

    }
}
