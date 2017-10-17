package com.SDT.servicedog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;


public class Help extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView headerTextView;
    private TextView taskdescrip;
    private Button _feedbackBtn;
    private Button sendEmailBtn;

    ProgressDialog progress;
    JSONArray resultOutput;
    String header_task_name;
    String task_name_description;

    String TaskListDescclientID;
    StringBuilder responseOutput;
    String _programmeID, send_to_email;
    String _loginID, _clientid;
    String first_name, last_name;
    View header;
    ArrayList<String> task_name = new ArrayList<String>();
    ArrayList<String> task_description = new ArrayList<String>();
    ArrayList<String> task_status = new ArrayList<String>();
    public static String _utfValue = "";
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

//        TextView textview = (TextView) findViewById(R.id.textview1);
//        textview.setPaintFlags(textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //textview.setText("This text will be underlined");


        System.out.println("/*******************************  ABOUT US CLASS STARTED   *****************************************************/");
        Intent intent = getIntent();

        _programmeID = intent.getStringExtra("PROGRAMME_ID");
        _loginID = intent.getStringExtra("EMAIL_ID");
        sendEmailBtn = (Button) findViewById(R.id.button3);

        System.out.println("client_id and client_email_id and firstname & lastname and _clientID" + _programmeID + _loginID + first_name + last_name + "\n" + _clientid);
        System.out.println("ProgrammeID :" + _programmeID);
        System.out.println("header task name and description  :" + header_task_name + "\n" + task_name_description);
        _utfValue = _programmeID;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        initNavigationDrawer();

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String buttonText = (String) sendEmailBtn.getText();
                // mail sending method pass "to email" as parameter.
                sendEmail(buttonText);
                System.out.println("Value of button email id is = " + buttonText);
//                Toast.makeText(getApplicationContext(), "send mail button clicked", Toast.LENGTH_LONG).show();
            }
        });

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:
                        Intent aboutusintent = new Intent(Help.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
                        //Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(Help.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(Help.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(Help.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);


                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(Help.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;

                    case R.id.help_btn:

                        Toast.makeText(getApplicationContext(), "Help", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();

//                        Intent helpIintent = new Intent(Help.this, Help.class);
//                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(Help.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID", _loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(Help.this, MainActivity.class);
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


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //TODO your background code
                        CheckProfileImageFlagMethod(header);
                    }
                });
            }
        });
    }

    private void CheckProfileImageFlagMethod(View view) {

        if (Imageclass.image_getdata_flag == 1) {

            ImageView profileImageView = (ImageView) view.findViewById(R.id.imageView3);
            profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());

        }
//        else {
//            CheckProfileImageFlagMethod(view);
//        }
    }


    public void sendEmail(String send_to_email) {

        Log.i("Send email", "");
        String[] TO = {send_to_email};
        String[] CC = {""};

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }

//        String[] TO = {"email@server.com"};
//        Uri uri = Uri.parse("mailto:email@server.com")
//                .buildUpon()
//                .appendQueryParameter("subject", "subject")
//                .appendQueryParameter("body", "body")
//                .build();
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        // emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");

    }
}
