package com.SDT.servicedog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.*;

import org.json.JSONArray;

import java.util.ArrayList;


public class Feedback extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private GridView gridView;
    private ListView listview;
    private TextView textviewHeadinig;

    ProgressDialog progress;
    JSONArray resultOutput;
    String progName;
    String fetchjsonData;
    String fetchjsonData1;
    String programReferenceID;
    StringBuilder responseOutput;
    String _programmeID;
    String _clientid;
    String _task_id, _task_name,_task_status;
    ArrayList<String> task_name = new ArrayList<String>();
    ArrayList<String> task_description = new ArrayList<String>();
    ArrayList<String> task_status = new ArrayList<String>();
    public static String _utfValue = "";
    private String headerName,_loginID,profile_image_url,_clientID;
    ImageView captureImageView;

    static final String[] GRID_DATA = new String[]{
            //  "Task Completion",
            "Task not completed,\nI am unwell",
            "Task not completed,\nI am not in a good space",
            "Task not completed,\nI have issues in the family",
            "Task not completed,\nI am on holiday",
            "I am requesting help"

    };
    static final String[] FeedbackMsgs = new String[]{
            // "Exercise Completed!",
            "Get Better Soon",
            "Hope to see you back on track soon",
            "We hope that all is well",
            "Please enter your return date from holiday",
            "Help will be requested. Please type in your question or leave blank and Provider will be in contact"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_main);
       // setContentView(R.layout.feedback_content_main);
        //first_heading = ( TextView )findViewById(R.id.heading_one);

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        System.out.println("/*******************************  FEEDBACK CLASS STARTED   *****************************************************/");

        initNavigationDrawer();

        TaskListDescription.activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        Intent intent = getIntent();
        _clientid = intent.getStringExtra("CLIENT_ID");
        _programmeID = intent.getStringExtra("PROGRAMME_ID");
        _task_id = intent.getStringExtra("TASK_ID");
        _task_name = intent.getStringExtra("TASKNAME");
        _task_status = intent.getStringExtra("TASKSTATUS");

        textviewHeadinig = (TextView) findViewById(R.id.textView);
        textviewHeadinig.setText("My Status Update");

        System.out.println("client_id, programme id, task id\n" + _clientid + "\n" + _programmeID + "\n" + _task_id);

        _utfValue = _programmeID;

        listview = (ListView) findViewById(R.id.listView);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new CustomFeedBack(this, GRID_DATA));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

//                if (GRID_DATA[position].equals("Task Completion")) {
//
//                    Intent newIntent = new Intent(Feedback.this, FeedBackReport.class);
//                    newIntent.putExtra("TASK_MSG", FeedbackMsgs[position]);
//                    newIntent.putExtra("GRID_MSG", GRID_DATA[position]);
//                    newIntent.putExtra("CLIENT_ID", _clientid);
//                    newIntent.putExtra("PROGRAMME_ID", _programmeID);
//                    newIntent.putExtra("TASK_ID", _task_id);
//                    newIntent.putExtra("BUTTON_ID", "1");
//                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(newIntent);
//
//                } else
                if (GRID_DATA[position].equals("Task not completed,\nI am unwell")) {

                    //optional_date.setVisibility(View.VISIBLE);

                    Intent newIntent = new Intent(Feedback.this, FeedBackReport.class);
                    newIntent.putExtra("TASK_MSG", FeedbackMsgs[position]);
                    newIntent.putExtra("GRID_MSG", GRID_DATA[position]);
                    newIntent.putExtra("CLIENT_ID", _clientid);
                    newIntent.putExtra("PROGRAMME_ID", _programmeID);
                    newIntent.putExtra("TASK_ID", _task_id);
                    newIntent.putExtra("TASKNAME", _task_name);
                    newIntent.putExtra("TASKSTATUS", _task_status);
                    newIntent.putExtra("BUTTON_ID", "2");
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(newIntent);

                } else if (GRID_DATA[position].equals("Task not completed,\nI am not in a good space")) {

                    Intent newIntent = new Intent(Feedback.this, FeedBackReport.class);
                    newIntent.putExtra("TASK_MSG", FeedbackMsgs[position]);
                    newIntent.putExtra("GRID_MSG", GRID_DATA[position]);
                    newIntent.putExtra("CLIENT_ID", _clientid);
                    newIntent.putExtra("PROGRAMME_ID", _programmeID);
                    newIntent.putExtra("TASK_ID", _task_id);
                    newIntent.putExtra("TASKNAME", _task_name);
                    newIntent.putExtra("TASKSTATUS", _task_status);
                    newIntent.putExtra("BUTTON_ID", "3");

                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(newIntent);
                } else if (GRID_DATA[position].equals("Task not completed,\nI have issues in the family")) {

                    Intent newIntent = new Intent(Feedback.this, FeedBackReport.class);
                    newIntent.putExtra("TASK_MSG", FeedbackMsgs[position]);
                    newIntent.putExtra("GRID_MSG", GRID_DATA[position]);

                    newIntent.putExtra("CLIENT_ID", _clientid);
                    newIntent.putExtra("PROGRAMME_ID", _programmeID);
                    newIntent.putExtra("TASK_ID", _task_id);
                    newIntent.putExtra("TASKNAME", _task_name);
                    newIntent.putExtra("TASKSTATUS", _task_status);
                    newIntent.putExtra("BUTTON_ID", "4");

                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(newIntent);
                } else if (GRID_DATA[position].equals("Task not completed,\nI am on holiday")) {

                    // datePicker.setVisibility(View.VISIBLE);

                    Intent newIntent = new Intent(Feedback.this, FeedBackReport.class);
                    newIntent.putExtra("TASK_MSG", FeedbackMsgs[position]);
                    newIntent.putExtra("GRID_MSG", GRID_DATA[position]);

                    newIntent.putExtra("CLIENT_ID", _clientid);
                    newIntent.putExtra("PROGRAMME_ID", _programmeID);
                    newIntent.putExtra("TASK_ID", _task_id);
                    newIntent.putExtra("TASKNAME", _task_name);
                    newIntent.putExtra("TASKSTATUS", _task_status);
                    newIntent.putExtra("BUTTON_ID", "5");

                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(newIntent);
                } else if (GRID_DATA[position].equals("I am requesting help")) {

                    // help_txt.setVisibility((View.VISIBLE));

                    Intent newIntent = new Intent(Feedback.this, FeedBackReport.class);
                    newIntent.putExtra("TASK_MSG", FeedbackMsgs[position]);
                    newIntent.putExtra("GRID_MSG", GRID_DATA[position]);

                    newIntent.putExtra("CLIENT_ID", _clientid);
                    newIntent.putExtra("PROGRAMME_ID", _programmeID);
                    newIntent.putExtra("TASK_ID", _task_id);
                    newIntent.putExtra("TASKNAME", _task_name);
                    newIntent.putExtra("TASKSTATUS", _task_status);
                    newIntent.putExtra("BUTTON_ID", "6");

                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(newIntent);
                }
                //Toast.makeText(getApplicationContext(), "You Clicked "+GRID_DATA[position], Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void initNavigationDrawer() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:

                        Intent aboutusintent = new Intent(Feedback.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
                        break;
                    case R.id._trainingprog:

                        Intent trainingintent = new Intent(Feedback.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
//                        Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
//                        break;
                    case R.id._taskhis:
                        Intent historyintent = new Intent(Feedback.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        Intent newintent = new Intent(Feedback.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        Intent settingIintent = new Intent(Feedback.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;

                    case R.id.help_btn:
                        Intent helpIintent = new Intent(Feedback.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;
                    case R.id.menu_btn:

                        Intent menuIintent = new Intent(Feedback.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID", _loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:
                        Intent intent = new Intent(Feedback.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

//                        make this at all places where Drawer is called.

                        SharedPreferences preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                        if (preferences.contains("password")) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("password");
                            editor.commit();
                        }
                        finish(); // Call once you redirect to another activity
                }
                return true;
            }
        });

        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        profile_image_url = haredpreferences.getString("profile_pic", "null");

        System.out.println("value of image url from shared preference " + profile_image_url);

        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) header.findViewById(R.id.tv_email);

        if (profile_image_url.equals("")) {

            Bitmap bitmap_default_image = BitmapFactory.decodeResource(this.getResources(), R.drawable.user_icon);
            Imageclass.bitmapImageValue = bitmap_default_image;
            ImageView profileImageView = (ImageView) header.findViewById(R.id.imageView3);
            profileImageView.setImageBitmap(bitmap_default_image);
            Imageclass.image_getdata_flag = 1;
        }

        tv_email.setText(haredpreferences.getString("first_name", "Null") + " " + haredpreferences.getString("last_name", "Null"));
        _clientID = haredpreferences.getString("CLIENT_ID", "Null");
        _utfValue = _clientID;

        captureImageView = (ImageView) header.findViewById(R.id.imageView3);
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
}