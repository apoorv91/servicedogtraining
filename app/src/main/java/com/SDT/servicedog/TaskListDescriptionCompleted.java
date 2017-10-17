package com.SDT.servicedog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.SDT.servicedog.TaskListsCollectionData.DatumTask;
import com.SDT.servicedog.TaskListsCollectionData.MediaTasks;
import com.SDT.servicedog.TaskListsCollectionData.TaskRootObject;
import com.SDT.servicedog.TaskListsCollectionData.Tasks;

public class TaskListDescriptionCompleted extends AppCompatIntermediateActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView headerTextView;
    private TextView taskdescrip;

    private Button _videosloadBtn, _imagesLoadBtn, _audioLoadBtn, _docsLoadBtn, _storylineLoadBtn, buttonReadMore;
    ProgressDialog progress;
    JSONObject js = new JSONObject();
    String header_task_name, resultOutput;
    String task_name_description;
    JSONObject json = null;
    View header;

    String TaskListDescclientID, _taskStatus;

    StringBuilder responseOutput;
    String _programmeID, _taskID;
    String _loginID, _clientid;
    String first_name, last_name;
    ArrayList<String> mediaType = new ArrayList<String>();

    public static String _utfValue = "";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_description_completed);

        TextView textview = (TextView) findViewById(R.id.textview1);
        textview.setPaintFlags(textview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        headerTextView = (TextView) findViewById(R.id.textView);
        taskdescrip = (TextView) findViewById(R.id.textview2);

        _videosloadBtn = (Button) findViewById(R.id.btnVideo);
        _imagesLoadBtn = (Button) findViewById(R.id.imgBtn);

        _audioLoadBtn = (Button) findViewById(R.id.audioBtn);
        _docsLoadBtn = (Button) findViewById(R.id.docsBtn);
        _storylineLoadBtn = (Button) findViewById(R.id.storylineBtn);
        buttonReadMore = (Button) findViewById(R.id.buttonReadMore);

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();

        System.out.println("/*******************************  TASKLIST DESCRIPTION COMPLETED CLASS STARTED   *****************************************************/");

        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        _clientid = haredpreferences.getString("CLIENT_ID", "Null");
        _loginID = haredpreferences.getString("email_id", "Null");

        Intent intent = getIntent();
        _programmeID = intent.getStringExtra("PROGRAMME_ID");
        _taskID = intent.getStringExtra("TASK_ID");
        _taskStatus = intent.getStringExtra("TASKSTATUS");

        System.out.println("client id, programme id & task id are: \n" + _clientid + "\n" + _programmeID + "\n" + _taskID);
        _utfValue = _programmeID;
        toolbar = (Toolbar) findViewById(R.id.toolbar);

//        taskdescrip.setText(task_name_description);
//        taskdescrip.setTextColor(Color.parseColor("#000000"));
//        taskdescrip.setMaxLines(2);
        initNavigationDrawer();

        buttonReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // custom dialog
                ChildSectionView();
            }
        });

        SegregateImagesAndVideosForTask(_taskID);

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

//                    AlertDialog alertdialog_video = new AlertDialog.Builder(TaskListDescriptionCompleted.this, R.style.MyAlertDialogStyle).create();
//
//                    alertdialog_video.setMessage("Sorry!!! There are no video files for this task.");
//                    alertdialog_video.setCancelable(false);
//                    alertdialog_video.setButton("ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    alertdialog_video.show();

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

//                    AlertDialog alertdialog_image = new AlertDialog.Builder(TaskListDescriptionCompleted.this, R.style.MyAlertDialogStyle).create();
//                    alertdialog_image.setMessage("Sorry!!! There are no image files for this task.");
//                    alertdialog_image.setCancelable(false);
//                    alertdialog_image.setButton("ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    alertdialog_image.show();

                } else {

                    Intent nextScreen2 = new Intent(getApplicationContext(), TaskImageList.class);
                    nextScreen2.putExtra("TASK_IMAGE_ARRAY", photos);
                    nextScreen2.putExtra("MEDIA_NAMES", photos_names);
                    startActivity(nextScreen2);
                }
            }
        });

        _audioLoadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (media_audio_url.size() == 0) {

//                    AlertDialog alertdialog_audio = new AlertDialog.Builder(TaskListDescriptionCompleted.this, R.style.MyAlertDialogStyle).create();
//
//                    alertdialog_audio.setMessage("Sorry!!! There are no audio files for this task.");
//                    alertdialog_audio.setCancelable(false);
//                    alertdialog_audio.setButton("ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    alertdialog_audio.show();

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

//                    AlertDialog alertdialog_docs = new AlertDialog.Builder(TaskListDescriptionCompleted.this, R.style.MyAlertDialogStyle).create();
//                    alertdialog_docs.setMessage("Sorry!!! There are no document files for this task.");
//                    alertdialog_docs.setCancelable(false);
//                    alertdialog_docs.setButton("ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    alertdialog_docs.show();

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

//                    AlertDialog alertdialog_stroyline = new AlertDialog.Builder(TaskListDescriptionCompleted.this, R.style.MyAlertDialogStyle).create();
//                    alertdialog_stroyline.setMessage("Sorry!!! There are no storyline files for this task.");
//                    alertdialog_stroyline.setCancelable(false);
//                    alertdialog_stroyline.setButton("ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//                    alertdialog_stroyline.show();

                } else {

                    Intent nextscreen5 = new Intent(getApplicationContext(), TaskStoryLine.class);
                    nextscreen5.putExtra("TASK_STORY_URLS", media_storyline_url);
                    nextscreen5.putExtra("TASK_STORY_NAMES", media_storyline_name);
                    startActivity(nextscreen5);
                }
            }
        });

        System.out.println("image value in task description page: " + Imageclass.bitmapImageValue);


        // code for popup dilog box task description pre load

        final Dialog dialog = new Dialog(TaskListDescriptionCompleted.this, android.R.style.Theme_Holo_Dialog_NoActionBar);
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

    }

    private void ChildSectionView() {

        final Dialog dialog = new Dialog(TaskListDescriptionCompleted.this, android.R.style.Theme_Holo_Dialog_NoActionBar);
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
//        TextHead.setText("Task Description");

        StringBuilder sb = new StringBuilder(header_task_name.toString());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
       // TextHead.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        TextHead.setText(sb.toString());

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


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                //  progress.dismiss();
//                dialog.show();
//            }
//        }, 200);

        // dialog.show();
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

    public void initNavigationDrawer() {

        ConnectivityManager cm_object = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi_connect = cm_object.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo data_connect = cm_object.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi_connect != null & data_connect != null) && (wifi_connect.isConnected() | data_connect.isConnected())) {
            new GetData(this).execute();
        } else {
            Toast.makeText(TaskListDescriptionCompleted.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:

                        Intent aboutusintent = new Intent(TaskListDescriptionCompleted.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);

                        break;
                    case R.id._trainingprog:
                        Intent trainingintent = new Intent(TaskListDescriptionCompleted.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:

                        Intent historyintent = new Intent(TaskListDescriptionCompleted.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:

                        Intent newintent = new Intent(TaskListDescriptionCompleted.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);


                        break;
                    case R.id.settingschange:

                        Intent settingIintent = new Intent(TaskListDescriptionCompleted.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;

                    case R.id.help_btn:

                        Intent helpIintent = new Intent(TaskListDescriptionCompleted.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(TaskListDescriptionCompleted.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID",_loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(TaskListDescriptionCompleted.this, MainActivity.class);
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

            progress.dismiss();
            return null;
        }
    }

}
