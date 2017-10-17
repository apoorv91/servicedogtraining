package com.SDT.servicedog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.SDT.servicedog.TaskListsCollectionData.DatumTask;
import com.SDT.servicedog.TaskListsCollectionData.MediaTasks;
import com.SDT.servicedog.TaskListsCollectionData.TaskRootObject;
import com.SDT.servicedog.TaskListsCollectionData.Tasks;


public class TaskListDescription extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView headerTextView;
    private TextView taskdescrip;
    private TextView number_of_DaysTextView;
    private TextView time_RemTextView;
    private ListView listView;
    View header;

    private Button _feedbackBtn, _videosloadBtn, _imagesLoadBtn, _audioLoadBtn, _docsLoadBtn, _storylineLoadBtn, buttonReadMore;
    ProgressDialog progress;
    JSONObject js = new JSONObject();
    String header_task_name, resultOutput;
    String task_name_description;
    JSONObject json = null;
    JSONObject json2 = null;

    String TaskListDescclientID, _taskStatus, _question_1, _question_2;
    String _taskDays, _taskHours, _taskMinutes, _taskSeconds, _taskTime;
    StringBuilder responseOutput;
    String _programmeID, _taskID = null;
    String _loginID, _clientid;
    String first_name, last_name;
    String fetchedDays, fetchedhrs, fetchedmin, fetchedsec;
    ArrayList<String> taskVideoaArray = new ArrayList<String>();
    ArrayList<String> task_ImagesArray = new ArrayList<String>();
    ArrayList<String> mediaNames = new ArrayList<String>();
    ArrayList<String> mediaType = new ArrayList<String>();

    ArrayList<String> fetchedImagesArray = new ArrayList<String>();
    ArrayList<String> fetchedImagesICONArray = new ArrayList<String>();
    ArrayList<String> fetchedmediaNames = new ArrayList<String>();
    public static String _utfValue = "";
    private static final String FORMAT = "%02d:%02d:%02d";

    ArrayList<String> media_thumb_urls = new ArrayList<String>();
    ArrayList<String> media_names = new ArrayList<String>();
    ArrayList<String> medias = new ArrayList<String>();

    ArrayList<String> photos_thumb_urls = new ArrayList<String>();
    ArrayList<String> photos_names = new ArrayList<String>();
    ArrayList<String> photos = new ArrayList<String>();

    ArrayList<String> media_storyline_url = new ArrayList<String>();
    ArrayList<String> media_storyline_name = new ArrayList<String>();

    ArrayList<String> media_audio_url = new ArrayList<String>();
    ArrayList<String> media_audio_name = new ArrayList<String>();

    ArrayList<String> media_document_url = new ArrayList<String>();
    ArrayList<String> media_document_name = new ArrayList<String>();

    ArrayList<String> media_pdf_url = new ArrayList<String>();
    ArrayList<String> media_pdf_name = new ArrayList<String>();

    int popupFlag = 0;
    int mediaLoadFlag = 0 ;

    public static Activity activityTaskTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_description);

        TextView textview = (TextView) findViewById(R.id.textview1);
        textview.setPaintFlags(textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //textview.setText("This text will be underlined");
        headerTextView = (TextView) findViewById(R.id.textView);
        taskdescrip = (TextView) findViewById(R.id.textview2);
        number_of_DaysTextView = (TextView) findViewById(R.id.txtDayRem);
        time_RemTextView = (TextView) findViewById(R.id.txtTimeRem);
        _feedbackBtn = (Button) findViewById(R.id.feedbackBtn);


        _videosloadBtn = (Button) findViewById(R.id.btnVideo);
        _imagesLoadBtn = (Button) findViewById(R.id.imgBtn);

        _audioLoadBtn = (Button) findViewById(R.id.audioBtn);
        _docsLoadBtn = (Button) findViewById(R.id.docsBtn);
        //  _pdfLoadBtn = (Button) findViewById(R.id.imgBtn);
        _storylineLoadBtn = (Button) findViewById(R.id.storylineBtn);
        buttonReadMore = (Button) findViewById(R.id.buttonReadMore);


        activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        System.out.println("/*******************************  TASKLIST DESCRIPTION CLASS STARTED   *****************************************************/");

        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        _clientid = haredpreferences.getString("CLIENT_ID", "Null");
        _loginID = haredpreferences.getString("email_id", "Null");

        Intent intent = getIntent();
        _programmeID = intent.getStringExtra("PROGRAMME_ID");
        _taskID = intent.getStringExtra("TASK_ID");
        _taskStatus = intent.getStringExtra("TASKSTATUS");

        _question_1 = intent.getStringExtra("QUESTION_1");
        _question_2 = intent.getStringExtra("QUESTION_2");


        System.out.println("client id, programme id & task id are: \n" + _clientid + "\n" + _programmeID + "\n" + _taskID);
        System.out.println("TASK_DAYS, TASK_HOURS, TASK_MINUTES & TASK_SECONDS are: \n" + _taskDays + "\n" + _taskHours + "\n" + _taskMinutes + "\n" + _taskSeconds);

        _utfValue = _programmeID;

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        initNavigationDrawer();

        _feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(TaskListDescription.this, FeedBackReport.class);
                //  Intent newIntent = new Intent(TaskListDescription.this, Feedback.class);
                newIntent.putExtra("CLIENT_ID", _clientid);
                newIntent.putExtra("PROGRAMME_ID", _programmeID);
                newIntent.putExtra("TASK_ID", _taskID);
                newIntent.putExtra("TASKNAME", header_task_name.toString());
                newIntent.putExtra("TASK_MSG", "Exercise Completed!");
                newIntent.putExtra("GRID_MSG", "Task Completion");
                newIntent.putExtra("BUTTON_ID", "1");

                newIntent.putExtra("QUESTION_1", _question_1);
                newIntent.putExtra("QUESTION_2", _question_2);

                startActivity(newIntent);
                // Toast.makeText(getApplicationContext(), "FeedbackButton_Clicked", Toast.LENGTH_LONG).show();
            }
        });
        buttonReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChildSectionView();

            }
        });

      //  SegregateImagesAndVideosForTask(_taskID);


        System.out.println("image value in task description page: " + Imageclass.bitmapImageValue);


        // code for popup dilog box task description pre load

        final Dialog dialog = new Dialog(TaskListDescription.this, android.R.style.Theme_Holo_Dialog_NoActionBar);

        dialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.custompopuplayout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationDialog_1;


        // Grab the window of the dialog, and change the width
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        lp.width = size.x;
        lp.height = size.y;
        // This makes the dialog take up the full width
        window.setAttributes(lp);

        ImageView icon_close = (ImageView) dialog.findViewById(R.id.imageCloseBtn);
        icon_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        WebView textDescription = (WebView) dialog.findViewById(R.id.textDescription);

        textDescription.setBackgroundColor(Color.TRANSPARENT);
        textDescription.getSettings().setJavaScriptEnabled(true);
        String textview_message_content = "<p align=\"justify\">" + task_name_description + "</p>";
        textDescription.loadDataWithBaseURL(null, textview_message_content, "text/html", "utf-8", null);
        TextView TextHead = (TextView) dialog.findViewById(R.id.text_head);
        TextHead.setText("Task Description");

        SharedPreferences haredpreferences_save = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = haredpreferences_save.edit();
        sharedEditor.putString("_taskId", _taskID);
        sharedEditor.putString("programId", _programmeID);
        sharedEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        _taskID = null;

        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        _taskID = haredpreferences.getString("_taskId", "Null");
        _programmeID = haredpreferences.getString("programId", "Null");
        _clientid = haredpreferences.getString("CLIENT_ID", "Null");

        new GetDataNew(this).execute();

      if(mediaLoadFlag==0) {
          SegregateImagesAndVideosForTask(_taskID);
          mediaLoadFlag++;
      }
      //System.out.println("@@@ media load counter value: "+ mediaLoadFlag);

        if (medias.size() == 0) {
            _videosloadBtn.setAlpha((float) 0.5);
        }

        if (photos.size() == 0) {
            _imagesLoadBtn.setAlpha((float) 0.5);
        }

        if (media_audio_url.size() == 0) {
            _audioLoadBtn.setAlpha((float) 0.5);
        }

        if (media_document_url.size() == 0) {
            _docsLoadBtn.setAlpha((float) 0.5);
        }

        if (media_storyline_url.size() == 0) {
            _storylineLoadBtn.setAlpha((float) 0.5);
        }

        _videosloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (medias.size() == 0) {

                } else {

                    Intent nextScreen2 = new Intent(getApplicationContext(), TaskVideoList.class);
                    nextScreen2.putExtra("TASK_VIDEO_ARRAY", medias);
                    nextScreen2.putExtra("TASK_IMAGE_ARRAY", media_thumb_urls);
                    nextScreen2.putExtra("MEDIA_NAMES", media_names);
                    startActivity(nextScreen2);
                }
            }
        });

        _imagesLoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (photos.size() == 0) {

                } else {

                    Intent nextScreen2 = new Intent(getApplicationContext(), TaskImageList.class);
                    nextScreen2.putExtra("TASK_IMAGE_ARRAY", photos);
                    nextScreen2.putExtra("MEDIA_NAMES", photos_names);
                    startActivity(nextScreen2);
                }

                //Toast.makeText(getApplicationContext(), "images_listing"+task_ImagesArray, Toast.LENGTH_LONG).show();
            }
        });

        _audioLoadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "audio button clicked", Toast.LENGTH_LONG).show();


                if (media_audio_url.size() == 0) {

                } else {

                    Intent nextscreen3 = new Intent(getApplicationContext(), TaskAudioList.class);
                    nextscreen3.putExtra("CLIENT_ID", _clientid);
                    nextscreen3.putExtra("PROGRAMME_ID", _programmeID);
                    nextscreen3.putExtra("TASK_ID", _taskID);
                    nextscreen3.putExtra("TASK_AUDIO_URLS", media_audio_url);
                    nextscreen3.putExtra("TASK_AUDIO_NAMES", media_audio_name);
                    startActivity(nextscreen3);

                }

                System.out.println("media urls and media names are: " + media_audio_url + "\n" + media_audio_name);
            }
        });

        _docsLoadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (media_document_url.size() == 0) {

                } else {

                    Intent nextscreen4 = new Intent(getApplicationContext(), TaskDocumentList.class);
                    nextscreen4.putExtra("CLIENT_ID", _clientid);
                    nextscreen4.putExtra("PROGRAMME_ID", _programmeID);
                    nextscreen4.putExtra("TASK_ID", _taskID);
                    nextscreen4.putExtra("TASK_DOCUMENT_URLS", media_document_url);
                    nextscreen4.putExtra("TASK_DOCUMENT_NAMES", media_document_name);
                    startActivity(nextscreen4);
                }
            }
        });

        _storylineLoadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (media_storyline_url.size() == 0) {

                } else {

                    Intent nextscreen5 = new Intent(getApplicationContext(), TaskStoryLine.class);
                    nextscreen5.putExtra("TASK_STORY_URLS", media_storyline_url);
                    nextscreen5.putExtra("TASK_STORY_NAMES", media_storyline_name);
                    startActivity(nextscreen5);
                }
            }
        });

    }

    private void ChildSectionView() {

        final Dialog dialog = new Dialog(TaskListDescription.this, android.R.style.Theme_Holo_Dialog_NoActionBar);
        dialog.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custompopuplayout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationDialog_1;


        // Grab the window of the dialog, and change the width
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        lp.width = size.x;
        lp.height = size.y;
        // This makes the dialog take up the full width
        window.setAttributes(lp);

        ImageView icon_close = (ImageView) dialog.findViewById(R.id.imageCloseBtn);
        icon_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        WebView textDescription = (WebView) dialog.findViewById(R.id.textDescription);

        textDescription.setBackgroundColor(Color.TRANSPARENT);
        textDescription.getSettings().setJavaScriptEnabled(true);
        String textview_message_content = "<p align=\"justify\">" + task_name_description + "</p>";
        textDescription.loadDataWithBaseURL(null, textview_message_content, "text/html", "utf-8", null);
        TextView TextHead = (TextView) dialog.findViewById(R.id.text_head);
        // TextHead.setText("Task Description");

        StringBuilder sb = new StringBuilder(header_task_name.toString());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        // TextHead.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        TextHead.setText(sb.toString());
       // TextHead.setText("Opening & Closing doors turning lights on and off Opening & Closing doors turning lights on and off Opening & Closing doors turning lights on and off Opening & Closing doors turning lights on and off Opening & Closing doors turning lights on and off Opening & Closing doors turning lights on and off Opening & Closing doors turning lights on and off Opening & Closing doors turning lights on and off");

        runOnUiThread(
                new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog.show();
                    }
                }
        );


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //  progress.dismiss();
                dialog.show();
            }
        }, 200);


    }

    public void SegregateImagesAndVideosForTask(String taskID) {

        TaskRootObject taskroot = MediaGetClass.taskRoot;

        List<DatumTask> data = taskroot.getData();
        for (int i = 0; i < data.size(); i++) {
            Tasks task = data.get(i).getTasks();
            List<MediaTasks> media = task.getMedia();
            if (data.get(i).getTasks().getTask().id.equals(taskID)) {
                for (int ii = 0; ii < media.size(); ii++) {
                    if (media.get(ii).media_type.equals("photos")) {

                        photos_thumb_urls.add(media.get(ii).thumb_url);
                        photos_names.add(media.get(ii).name);
                        photos.add(media.get(ii).media);

                    }
                    if (media.get(ii).media_type.equals("videos")) {

                        media_thumb_urls.add(media.get(ii).thumb_url);
                        media_names.add(media.get(ii).name);
                        medias.add(media.get(ii).media);

                    }
                    if (media.get(ii).media_type.equals("audio")) {

                        media_audio_url.add(media.get(ii).media);
                        media_audio_name.add(media.get(ii).name);

                    }
                    if (media.get(ii).media_type.equals("docs")) {

                        media_document_url.add(media.get(ii).media);
                        media_document_name.add(media.get(ii).name);

                    }
                    if (media.get(ii).media_type.equals("storyline")) {

                        media_storyline_url.add(media.get(ii).media);
                        media_storyline_name.add(media.get(ii).name);

                    }
                }
            }
        }

    }


    public void countDownTimerCode() {

        // countdown timer code starts here.

        Common.timerFinishFlag = 0; // 0 for timer started and in background.

        Integer DateTimeConvertToMilliSeconds = ((Integer.parseInt(fetchedhrs) * 60 * 60 * 1000) + (Integer.parseInt(fetchedmin) * 60 * 1000) + (Integer.parseInt(fetchedsec) * 1000));
        System.out.println("Total time in milli seconds is : " + DateTimeConvertToMilliSeconds);

        // if (Common.timerCreateObjFlag == 0) {

        new CountDownTimer(DateTimeConvertToMilliSeconds, 1000) { // adjust the milli seconds here

            @Override
            public void onTick(long millisUntilFinished) {

                Double countdownValue = Math.ceil((millisUntilFinished) / 1000);
                System.out.println("*****timer value in mili seconds : ******" + String.valueOf(countdownValue));
                //number_of_DaysTextView.setText(Integer.parseInt(fetchedDays)-1 + " " + "Days" + " " + "Remaining");
                time_RemTextView.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + "\n" + "Hours");
            }

            @Override
            public void onFinish() {
                //  time_RemTextView.setText("done!");
                countDownTimerCheck();
            }
        }.start();
        //   Common.timerCreateObjFlag = 1;
        // }


        // countdown timer code ends here.
    }

    // timer check code here that timer value is finished or not.

    public void countDownTimerCheck() {

        if (!fetchedDays.toString().equals("")) {

            if (!fetchedDays.toString().equals("0")) {

//            if (fetchedDays == "0") {
//
//            } else {
                number_of_DaysTextView.setText(String.valueOf(Integer.parseInt(fetchedDays) - 1) + " " + "Days" + "\n" + "Remaining");

                fetchedDays = String.valueOf(Integer.parseInt(fetchedDays) - 1);
                fetchedhrs = "23";
                fetchedmin = "59";
                fetchedsec = "59";
                countDownTimerCode();
//            }

            } else {

                time_RemTextView.setText("timer stopped");

                Common.timerFinishFlag = 1; // 1 for timer is finished task time elapsed.
                //   Toast.makeText(this, "Timer is stopped", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("MyBroadcast");
                intent.putExtra("Timer", "Task_Timer_Timeout");
                sendBroadcast(intent);

                if (AppCompatIntermediateActivity.taskTimerFlag == 1) {

                    if (Common.timerCreateObjFlag == 0) {

                        AlertDialog.Builder alertReturnDateObject = new AlertDialog.Builder(activityTaskTimer, R.style.MyAlertDialogStyle);

                        alertReturnDateObject.setTitle("");
                        alertReturnDateObject.setMessage("Time For This Task Is Finished. Please Contact Administrator.");
                        alertReturnDateObject.setCancelable(false);
                        alertReturnDateObject.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                Intent intentgobackobj = new Intent(activityTaskTimer, TrainingProgrammes.class);
                                intentgobackobj.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intentgobackobj);

                            }
                        });
                        AlertDialog alertLogin = alertReturnDateObject.create();
                        // alertLogin.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        alertLogin.show();

                        Common.timerCreateObjFlag = 1;


                        Button bq = alertLogin.getButton(DialogInterface.BUTTON_POSITIVE);
                        bq.setTextColor(Color.BLACK);

                    }
                } else {

                }
            }
        }
    }

    public void initNavigationDrawer() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {

            new GetData(this).execute();

        } else {
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();

        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:

                        Intent aboutusintent = new Intent(TaskListDescription.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
                        //Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(TaskListDescription.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(TaskListDescription.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(TaskListDescription.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);


                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(TaskListDescription.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;

                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(TaskListDescription.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(TaskListDescription.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID", _loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(TaskListDescription.this, MainActivity.class);
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
        System.out.println("training prog lcient id shared prefernce and programme_id and task_id" + TaskListDescclientID + "\n" + _programmeID + "\n"
                + _taskID);
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

    }

    private void CheckProfileImageFlagMethod(View view) {

        if (Imageclass.image_getdata_flag == 1) {

            ImageView profileImageView = (ImageView) view.findViewById(R.id.imageView3);
            profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());

        }
//        else {
//            CheckProfileImageFlagMethod(view);
//        }
        AppCompatIntermediateActivity.taskTimerFlag = 1;
    }


    private class GetData extends AsyncTask<String, Void, Void> {

        private final Context context;

        public GetData(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {


            progress = new ProgressDialog(this.context, R.style.dialog);
            progress.setMessage("Loading");
            progress.show();
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            number_of_DaysTextView.setText(fetchedDays + " " + "Days" + "\n" + "Remaining");


            //taskdescrip.setText(Html.fromHtml(task_name_description.toString()),TextView.BufferType.NORMAL)
            // String sampleText = "<p>This is for the testing and it has got nothing to be done with anything&nbsp;<a href=\"http://google.com\">google</a> .This is for the testing and it has got nothing to be done with anything .This is for the testing and it has got nothing to be done with anything .This is for the testing and it has got nothing to be done with anything .This is for the testing and it has got nothing to be done with anything .This is for the testing and it has got nothing to be done with anything <a href=\"http://wikipedia.com\">weki</a>.This is for the testing and it has got nothing to be done with anything .This is for the testing and it has got nothing to be done with anything.This is for the testing and it has got nothing to be done with anything " +
//                   ".This is for the testing and it has got nothing to be done with anything.</p>";


            Spanned htmlAsSpanned = Html.fromHtml(String.valueOf(task_name_description));
            taskdescrip.setText(htmlAsSpanned);
            taskdescrip.setMaxLines(2);
            taskdescrip.setTextColor(Color.parseColor("#000000"));

            headerTextView.setText(header_task_name);

            System.out.println("task name is: " + header_task_name);


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

            /******************************* END OF TASK LIST CLASS ********************************/

        }

        @Override
        protected Void doInBackground(String... params) {

            Intent intent = getIntent();

            _programmeID = intent.getStringExtra("PROGRAMME_ID");
            _loginID = intent.getStringExtra("EMAIL_ID");
            _taskID = intent.getStringExtra("TASK_ID");
            header_task_name = intent.getStringExtra("TASKNAME");
            task_name_description = intent.getStringExtra("TASKDESCRIPTION");
            mediaType = intent.getStringArrayListExtra("MEDIA_TYPE");

            System.out.println("programme_id and firstname & lastname and _clientID" + _programmeID + _loginID + first_name + last_name + "\n" + _clientid);
            System.out.println("header task name and description and task_id  :" + header_task_name + "\n" + task_name_description + "\n" + "task id is : " + _taskID);
            // System.out.println("task_videosArray and task_imagesArray and media names and media_type :" + taskVideoaArray + "\n"
//                    + task_ImagesArray+"\n"+mediaNames+"\n"+mediaType );
            try {

                System.out.println("Task time value is : " + _taskTime);

                URL url = null;
                url = new URL(ActivityIntermediateClass.baseApiUrl+"/programs/taskTime/?api_key="+ActivityIntermediateClass.apiKeyValue+"&client_id=" + _clientid.toString() + "&program_id=" + _programmeID + "&task_id=" + _taskID);

                System.out.println("Url for task time is : " + url.toString());

                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    connection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);

                final int responseCode = connection.getResponseCode();

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                TaskListDescription.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        System.out.println("response output===============" + responseOutput.toString());

                        try {
                            json = new JSONObject(responseOutput.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            resultOutput = json.get("data").toString();
                            System.out.println("response data" + resultOutput.toString());

                            json2 = new JSONObject(resultOutput.toString());

                            // countdown timer code starts here.

                            fetchedDays = json2.getString("days");
                            fetchedhrs = json2.getString("hours");
                            fetchedmin = json2.getString("minutes");
                            fetchedsec = json2.getString("seconds");

                            countDownTimerCode();
                            AppCompatIntermediateActivity.taskTimerFlag = 1;


                            // countdown timer code ends here.

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        System.out.println("*******the fetched days and fetched hours , minutes, seconds are**********" + fetchedDays + "\n" + fetchedhrs
                                + "\n" + fetchedmin + "\n" + fetchedsec);
                        progress.dismiss();
                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetDataNew extends AsyncTask<String, Void, Void> {

        private final Context context;
        JSONArray resultOutput, resultOutputMedia;
        String resultOutputMsg;
        int resultStatus;
        List<MediaTasks> fetchjsonData1;
        String programReferenceID;
        StringBuilder responseOutput;
        String _programmeID;
        View header;
        String _loginID, _clientid , _preview_role;
        String first_name, last_name;
        ImageView _imageView3;

        ArrayList<String> task_name = new ArrayList<String>();
        ArrayList<String> task_description = new ArrayList<String>();
        ArrayList<String> task_status = new ArrayList<String>();
        ArrayList<String> task_id = new ArrayList<String>();

        ArrayList<String> Questions_1 = new ArrayList<String>();
        ArrayList<String> Questions_2 = new ArrayList<String>();

        ArrayList<String> task_videos = new ArrayList<>();
        ArrayList<String> task_thumb_url_images = new ArrayList<>();
        ArrayList<String> media_name = new ArrayList<>();

        ArrayList<String> task_images = new ArrayList<>();
        ArrayList<String> task_images_icon = new ArrayList<>();
        ArrayList<String> media_images_name = new ArrayList<>();

        Integer task_days = 0, task_hours = 0, task_minutes = 0, task_seconds = 0;
        String media_type, taskTime;

        public GetDataNew(Context c) {
            this.context = c;

        }

        protected void onPreExecute() {
//            progress = new ProgressDialog(this.context, R.style.dialog);
//            progress.setMessage("Loading");
//            progress.show();
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                URL url = null;

                SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                _taskID = haredpreferences.getString("_taskId", "Null");
                _programmeID = haredpreferences.getString("programId", "Null");
                _clientid = haredpreferences.getString("CLIENT_ID", "Null");

                try {
                    // url = new URL("http://servdog.dealopia.com/users/clientAuth/?email=brijesh@gmail.com&password=m2n1shlko");
                    url = new URL(ActivityIntermediateClass.baseApiUrl + "/programs/tasks/?api_key=" + ActivityIntermediateClass.apiKeyValue + "&client_id=" + _clientid + "&id=" + _programmeID);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                System.out.println("url is: " + url);
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    connection.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);

                final int responseCode = connection.getResponseCode();

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "GET");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                responseOutput = new StringBuilder();
                // System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                TaskListDescription.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //  System.out.println("response output===============" + responseOutput.toString());

                        ArrayList<String> programlist = new ArrayList<String>();
                        JSONObject json = null;
                        JSONObject json2 = null;
                        JSONObject json3 = null;

                        Gson gson = new Gson();

                        TaskRootObject taskRoot = gson.fromJson(responseOutput.toString(), TaskRootObject.class);
                        MediaGetClass.taskRoot = taskRoot;

                        int resultStatus = taskRoot.status;
                        String resultOutputMsg = taskRoot.message;
                        AlertDialog.Builder goLogin = new AlertDialog.Builder(TaskListDescription.this, R.style.MyAlertDialogStyle);
                        // System.out.println("result output" + resultOutput);

                        // if (resultOutput.contains("User already registered.")) {

                        if (resultStatus == 0) {

                            goLogin.setMessage(resultOutputMsg);
                            goLogin.setCancelable(false);
                            goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    Intent intentgobackobj = new Intent(TaskListDescription.this, TrainingProgrammes.class);
                                    intentgobackobj.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intentgobackobj);
                                }
                            });
                            AlertDialog alertLogin = goLogin.create();
                            alertLogin.show();
                            progress.dismiss();
                        }
                        //else if (resultOutput.contains("Registration Successful")) {

                        else if (resultStatus == 1) {


                            try {
                                json = new JSONObject(responseOutput.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                resultOutput = json.getJSONArray("data");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("result output is : " + resultOutput);


                            task_name.clear();
                            task_description.clear();
                            task_status.clear();

                            for (int i = 0; i < resultOutput.length(); i++) {

                                task_name.add(taskRoot.data.get(i).Task.Task.title);
                                task_description.add(taskRoot.data.get(i).Task.Task.description.toString().replace("\r\n\r\n", "\n\n"));
                                task_status.add(taskRoot.data.get(i).status);
                                Questions_1.add(taskRoot.data.get(i).Task.Task.question_1);
                                Questions_2.add(taskRoot.data.get(i).Task.Task.question_2);

                                if (taskRoot.data.get(i).status.equals("active")) {
                                    String taskTimeData = taskRoot.data.get(i).time.toString();

                                    System.out.println("JSON TIMER DATA ON TASK LIST PAGE IS: " + taskTimeData);

                                    try {
                                        json3 = new JSONObject(taskTimeData.toString());

                                        taskTime = json3.toString();

                                        System.out.println("json value is=" + json3 + "====");

                                        if (json3.toString().equals("{}")) {


                                        } else {

                                            task_days = json3.getInt("days");
                                            task_hours = json3.getInt("hours");
                                            task_minutes = json3.getInt("minutes");
                                            task_seconds = json3.getInt("seconds");

                                            // System.out.println("days, hrs, min and sec are: " + task_days + "  " + task_hours + "  " + task_minutes + "  " + task_seconds);

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("JSON TASK TIME DATA IS: " + json3);
                                }

                                task_id.add(taskRoot.data.get(i).Task.Task.id);

                                fetchjsonData1 = taskRoot.data.get(i).Task.Media;
                                // System.out.println("media data" + fetchjsonData1);
////
                                if (fetchjsonData1.size() > 0) {
                                    for (int j = 0; j < fetchjsonData1.size(); j++) {
                                        media_type = (fetchjsonData1.get(j).media_type);

                                        if (media_type.equals("videos")) {
                                            MediaGetClass.task_videos.add(fetchjsonData1.get(j).media);
                                            MediaGetClass.media_name.add(fetchjsonData1.get(j).name);

                                        }
                                        if (media_type.equals("photos")) {

                                            MediaGetClass.task_images.add(fetchjsonData1.get(j).media);
                                            MediaGetClass.task_images_icon.add(fetchjsonData1.get(j).thumb_url);
                                            MediaGetClass.media_images_name.add(fetchjsonData1.get(j).name);

                                        }
                                        if (media_type.equals("audio")) {

                                            MediaGetClass.media_audio_url.add(fetchjsonData1.get(j).media);
                                            MediaGetClass.media_audio_name.add(fetchjsonData1.get(j).name);

                                        }
                                        if (media_type.equals("docs")) {

                                            MediaGetClass.media_document_url.add(fetchjsonData1.get(j).media);
                                            MediaGetClass.media_document_name.add(fetchjsonData1.get(j).name);

                                        }
                                        if (media_type.equals("storyline")) {

                                            MediaGetClass.media_storyline_url.add(fetchjsonData1.get(j).media);
                                            MediaGetClass.media_storyline_name.add(fetchjsonData1.get(j).name);

                                        }
                                    }
                                }
                                System.out.println("media counter value is : " + fetchjsonData1.size());
                            }
                            System.out.println("DESCRIPTION array data is:" + task_description.get(0) + "\n");

//                            progress.dismiss();
                        }
                    }
                });

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }
}
