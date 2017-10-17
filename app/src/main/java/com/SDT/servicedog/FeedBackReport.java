package com.SDT.servicedog;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


public class FeedBackReport extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView feedback_msg;
    ProgressDialog progress;
    JSONArray resultOutput;
    private TextView first_heading;
    private TextView second_heading, files_attached;
    private EditText first_txt;
    private EditText second_txt;
    private ImageView attach_img;
    private EditText optional_date;
   // private DatePicker datePicker;
    private EditText help_txt;
    private String selectedPath = null;
    String response_str = null;

    StringBuilder responseOutput, responseOutputRating;
    String retrieveFeedbackMsg, retrieveGridData;
    String _loginID, _clientid, _proogramme_id, _task_id, _task_name = null, _task_status = null, _button_id = null, _question_1, _question_2;
    String first_name, last_name;
    Button submitBtn, cancelBtn;
    ImageView attachment_btn;
    ImageButton imageButton;
    String responseStatus, responseMessage, responseStatusRating, responseMessageRating;

    ArrayList<String> task_name = new ArrayList<String>();
    ArrayList<String> task_description = new ArrayList<String>();
    ArrayList<String> task_status = new ArrayList<String>();
    public static String _utfValue = "";
    private static final int RESULT_SELECT_IMAGE = 1;

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private StringBuilder selectedDate, selectedDateAPIFORMAT;
    private Bitmap bitmap;
    Integer attachedFilesCount = 0, allFilesAttachedCount = 0;

    ArrayList<Uri> URI_data = new ArrayList<Uri>();
    ArrayList<String> bitmap_array_data = new ArrayList<String>();
    ArrayList<String> url_encoded_array_data = new ArrayList<String>();
    String encodedImage = null, urlencodedimagevalue = null;

    AttachmentContainer _attachments;

    String jsonImage = null, jsonVideo = null, jsonAudio = null, jsonDoc = null, jsonPdf = null, jsonStoryline = null;

    private ArrayList<String> imageArray = new ArrayList<String>();
    private ArrayList<byte[]> imageByteDataArray = new ArrayList<>();
    private ArrayList<String> videoArray = new ArrayList<String>();
    private ArrayList<String> audioArray = new ArrayList<String>();
    private ArrayList<String> docArray = new ArrayList<String>();
    private ArrayList<String> pdfArray = new ArrayList<String>();
    private ArrayList<String> storylineArray = new ArrayList<String>();

    // rating pop-up variables
    private Button btnVideo1, btnVideo2, btnVideo3, btnVideo4, btnVideo5;
    private ImageView img1, img2, img3, img4, img5;
    private Button rateSubmitBtn;
    private int clientRateValue = 0;
    private int rateFlag = 0;

    private ScrollView scrollviewFeedback;

    private View header;
    String TaskListDescclientID;


    // private ScrollView scrollviewFeedback
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back_report);

        feedback_msg = (TextView) findViewById(R.id.task_cmpletion_msg);

        first_heading = (TextView) findViewById(R.id.heading_one);
        second_heading = (TextView) findViewById(R.id.heading_two);
        first_txt = (EditText) findViewById(R.id.txt1);
        second_txt = (EditText) findViewById(R.id.txt2);
        attach_img = (ImageView) findViewById(R.id.attachment_img);
        optional_date = (EditText) findViewById(R.id.task2Text);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        help_txt = (EditText) findViewById(R.id.helpText);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        attachment_btn = (ImageView) findViewById(R.id.attachment_img);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        _attachments = new AttachmentContainer();
        files_attached = (TextView) findViewById(R.id.textView3);
        scrollviewFeedback = (ScrollView) findViewById(R.id.scrollviewFeedback);


        TaskListDescription.activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        System.out.println("/*******************************  FEEDBACK REPORT CLASS STARTED  *****************************************/");

        Intent intent = getIntent();
        retrieveFeedbackMsg = intent.getStringExtra("TASK_MSG");
        retrieveGridData = intent.getStringExtra("GRID_MSG");

        _loginID = intent.getStringExtra("EMAIL_ID");
        first_name = intent.getStringExtra("FIRSTNAME");
        last_name = intent.getStringExtra("LASTNAME");

        _clientid = intent.getStringExtra("CLIENT_ID");
        _button_id = intent.getStringExtra("BUTTON_ID");
//        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
//        _proogramme_id = haredpreferences.getString("LOGIN_PROGRAMME_ID", "null");
//        _task_id = haredpreferences.getString("LOGIN_TASK_ID", "null");
//        _task_name = haredpreferences.getString("LOGIN_TASK_NAME", "null");
//        _task_status = haredpreferences.getString("LOGIN_TASK_STATUS", "null");
        _proogramme_id = intent.getStringExtra("PROGRAMME_ID");
        _task_id = intent.getStringExtra("TASK_ID");
        _task_name = intent.getStringExtra("TASKNAME");
        _task_status = intent.getStringExtra("TASKSTATUS");

        _question_1 = intent.getStringExtra("QUESTION_1");
        _question_2 = intent.getStringExtra("QUESTION_2");

        final String NOTE_ACTION_VIEW = "jason.wei.custom.intent.action.NOTE_VIEW";

        System.out.println("retrieveFeedbackMsg and client_email_id and firstname & lastname and _clientID" + retrieveFeedbackMsg + _loginID + first_name + last_name + "\n" + _clientid);

        System.out.println("!!!...client_id, programme id, task id, task name & button id\n" + _clientid + "\n" + _proogramme_id + "\n" + _task_id + "\n" + _task_name + "\n" + _button_id);

        //   first_txt.setCursorVisible(true);
        //  second_txt.setCursorVisible(false);
        first_txt.requestFocus();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        if (_button_id.equals("6")) {
            first_txt.setHint("Enter reason for help");
            //  first_txt.setHintTextColor(R.color.hint_default_color);
            first_txt.setHintTextColor(Color.BLACK);

        }

//        if (_button_id.equals("1") || _button_id.equals("6")) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//         }

        if (_button_id.equals("2") || _button_id.equals("5")) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        feedback_msg.setText(_task_name.toString());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        freeMemory();
        GetRAMSpaceMethod();


        if (retrieveGridData.equals("Task Completion")) {
            first_heading.setVisibility(View.VISIBLE);
            first_heading.setText(_question_1.toString());
            second_heading.setVisibility(View.VISIBLE);
            second_heading.setText(_question_2.toString());
            first_txt.setVisibility(View.VISIBLE);
            second_txt.setVisibility(View.VISIBLE);
            attach_img.setVisibility(View.VISIBLE);
            files_attached.setVisibility(View.VISIBLE);
            files_attached.setTypeface(null, Typeface.ITALIC);
            // scrollviewFeedback.setVisibility(View.VISIBLE);

        } else if (retrieveGridData.equals("Task not completed,\nI am unwell")) {
            first_heading.setVisibility(View.VISIBLE);
            second_heading.setVisibility(View.INVISIBLE);
            first_txt.setVisibility(View.INVISIBLE);
            second_txt.setVisibility(View.INVISIBLE);
            attach_img.setVisibility(View.INVISIBLE);
            optional_date.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.VISIBLE);
            optional_date.setText("");
            optional_date.setCursorVisible(false);
            // datePicker.setVisibility(View.VISIBLE);

        } else if (retrieveGridData.equals("Task not completed,\nI am not in a good space")) {

            first_heading.setVisibility(View.VISIBLE);
            first_heading.setText("Please click on Submit button to avail this feature.");
            first_heading.setTextSize(17);
            first_heading.setTypeface(null, Typeface.ITALIC);
            second_heading.setVisibility(View.INVISIBLE);
            first_txt.setVisibility(View.INVISIBLE);
            second_txt.setVisibility(View.INVISIBLE);
            attach_img.setVisibility(View.INVISIBLE);
            optional_date.setVisibility(View.INVISIBLE);

        } else if (retrieveGridData.equals("Task not completed,\nI have issues in the family")) {

            first_heading.setVisibility(View.VISIBLE);
            first_heading.setText("Please click on Submit button to avail this feature.");
            first_heading.setTextSize(17);
            first_heading.setTypeface(null, Typeface.ITALIC);
            second_heading.setVisibility(View.INVISIBLE);
            first_txt.setVisibility(View.INVISIBLE);
            second_txt.setVisibility(View.INVISIBLE);
            attach_img.setVisibility(View.INVISIBLE);
            optional_date.setVisibility(View.INVISIBLE);
        } else if (retrieveGridData.equals("Task not completed,\nI am on holiday")) {

            first_heading.setVisibility(View.VISIBLE);
            second_heading.setVisibility(View.INVISIBLE);
            first_txt.setVisibility(View.INVISIBLE);
            second_txt.setVisibility(View.INVISIBLE);
            attach_img.setVisibility(View.INVISIBLE);
            optional_date.setVisibility(View.VISIBLE);
            optional_date.setText("");
            optional_date.setCursorVisible(false);
            imageButton.setVisibility(View.VISIBLE);
            // datePicker.setVisibility(View.VISIBLE);

        } else if (retrieveGridData.equals("I am requesting help")) {

            first_heading.setVisibility(View.INVISIBLE);
            second_heading.setVisibility(View.INVISIBLE);
            first_txt.setVisibility(View.VISIBLE);
            second_txt.setVisibility(View.INVISIBLE);
            attach_img.setVisibility(View.VISIBLE);
            optional_date.setVisibility(View.INVISIBLE);
           // datePicker.setVisibility(View.INVISIBLE);
            help_txt.setVisibility(View.INVISIBLE);
            files_attached.setVisibility(View.VISIBLE);
            files_attached.setTypeface(null, Typeface.ITALIC);
        }

        first_txt.setHorizontallyScrolling(false);
        first_txt.setMaxLines(Integer.MAX_VALUE);


        second_txt.setHorizontallyScrolling(false);
        second_txt.setMaxLines(Integer.MAX_VALUE);

        first_txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });

//        first_txt.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.v("log message:", "CHILD TOUCH");
//                // Disallow the touch request for parent scroll on touch of  child view
//                scrollviewFeedback.requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });

        second_txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                return true; // Focus will do whatever you put in the logic.
            }
        });

        // SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        // _clientID = haredpreferences.getString("CLIENT_ID", "Null");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previousScreen;

                if (_button_id.equals("1")) {
                    previousScreen = new Intent(getApplicationContext(), TaskList.class);
                } else {
                    previousScreen = new Intent(getApplicationContext(), UserMenuActivity.class);
                }
                previousScreen.putExtra("EMAIL_ID", _loginID);
                previousScreen.putExtra("PROGRAMME_ID", _proogramme_id);
                previousScreen.putExtra("CLIENT_ID", _clientid);
                previousScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(previousScreen);
                finish();

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {

                    if (_button_id.equals("1")) {
                        if (!first_txt.getText().toString().equals("") && !second_txt.getText().toString().equals("")) {

                            // receiveGetRequestFeedback(v);

                            RatingPopupMethod();
                            //  rateFlag = 1;

                        } else {

                            //  Toast.makeText(FeedBackReport.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            Toast toast = Toast.makeText(FeedBackReport.this, "Please fill all the fields", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else if (_button_id.equals("2") || _button_id.equals("5")) {

                        if (!optional_date.getText().toString().equals("")) {

                            receiveGetRequestFeedback(v);
                        } else {

                            AlertDialog.Builder alertReturnDateObject = new AlertDialog.Builder(FeedBackReport.this, R.style.MyAlertDialogStyle);
                            alertReturnDateObject.setMessage("Please Select a Date");
                            alertReturnDateObject.setCancelable(false);
                            alertReturnDateObject.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertLogin = alertReturnDateObject.create();
                            alertLogin.show();

                        }

                    } else {
// othr than butoon 1,2 and 5.

                        receiveGetRequestFeedback(v);
                    }
                } else {
                    //no connection
                    Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
                    toast.show();

                }

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        attachment_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean result = Utility.checkPermission(FeedBackReport.this);

                if (result) {
                    chooseFileAndUpload();
                }

                // Toast.makeText(getApplicationContext(), "attachment button clicked", Toast.LENGTH_LONG).show();
            }
        });

        if (retrieveGridData.equals("Task not completed,\nI am unwell") || retrieveGridData.equals("Task not completed,\nI am on holiday")) {

            first_heading.setText("Enter Return Date");

            //  calendar code starts here

            optional_date.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Use the current time as the default values for the picker
                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

                    calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    // showDate(year, month + 1, day);

                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(optional_date.getWindowToken(), 0);

                    showDialog(1111);

                }
            });

            imageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Use the current time as the default values for the picker
                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

                    calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    // showDate(year, month + 1, day);

                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(optional_date.getWindowToken(), 0);

                    showDialog(1111);

                }
            });
        }

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                        Intent aboutusintent = new Intent(FeedBackReport.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
//                        Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(FeedBackReport.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(FeedBackReport.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(FeedBackReport.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(FeedBackReport.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;
                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(FeedBackReport.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(FeedBackReport.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID",_loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(FeedBackReport.this, MainActivity.class);
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
    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }


    // method to get device RAM total and fre space in MB.
    public void GetRAMSpaceMethod() {

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        String AVAILABLE = formatSize(mi.availMem);
        String TOTAL = formatSize(mi.totalMem);
        //   long availableMegs = mi.availMem / 1048576L;

        System.out.println("TOTAL MEMORY usage is : " + TOTAL);
        System.out.println("AVAILABLE MEMORY usage is : " + AVAILABLE);

    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1111) {

            // return new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, myDateListener, year, month, day);

            DatePickerDialog datePickerstart = new DatePickerDialog(FeedBackReport.this, DatePickerDialog.THEME_HOLO_LIGHT, myDateListener, year, month, day);
            datePickerstart.setCancelable(false);
            datePickerstart.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerstart.setTitle("Select Return Date");
            datePickerstart.show();

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            System.out.println("selected date is: " + "year: " + arg1 + "month: " + arg2 + "day: " + arg3);

            if (arg1 < year) {

                showDate(year, month + 1, day);

            } else if (arg2 < month && arg1 == year) {

                showDate(year, month + 1, day);

            } else if (arg3 < day && arg1 == year && arg2 == month) {

                showDate(year, month + 1, day);

            } else {

                showDate(arg1, arg2 + 1, arg3);

            }
        }
    };

    private void showDate(int year, int month, int day) {

        selectedDate = new StringBuilder().append(day).append("/").append(month).append("/").append(year);

        // as per api requirement return date format should be in yyyy-mm-dd.
        selectedDateAPIFORMAT = new StringBuilder().append(year).append("-").append(month).append("-").append(day);

        optional_date.setText(selectedDate);
        System.out.println("calendar date selected is :" + selectedDate);
    }

    // calendar code ends here

    // image browse and upload code starts here

    public void chooseFileAndUpload() {

        if (_button_id.equals("1")) {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, Uri.parse("content://media/internal/images/media"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            try {
                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (_button_id.equals("6")) {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            try {
                startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    ArrayList<String> pathString = new ArrayList<String>();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                ClipData clipData = data.getClipData();
                int attach_fileType_flag = 0;  // 0 = default for images only , 1 = other than image type.

                if (_button_id.equals("6")) {
                    if (clipData != null) {
                        int count = clipData.getItemCount();

                        for (int i = 0; i < count; ++i) {

                            Uri uri_new = clipData.getItemAt(i).getUri();
                            String filename = GetFileName(uri_new);

                            String filenameArray[] = filename.split("\\.");
                            String extension = filenameArray[filenameArray.length - 1];

                            if (extension.equals("jpg") || extension.equals("png") || extension.equals("gif") || extension.equals("bmp") || extension.equals("jpeg") || extension.equals("JPG")) {


                            } else {
                                attach_fileType_flag = 1;
                                Toast.makeText(FeedBackReport.this, "Please Select Only Images", Toast.LENGTH_SHORT).show();
                            }
                            // }
                        }
                    } else {
                        String filename = GetFileName(uri);

                        String filenameArray[] = filename.split("\\.");
                        String extension = filenameArray[filenameArray.length - 1];

                        if (extension.equals("jpg") || extension.equals("png") || extension.equals("gif") || extension.equals("bmp") || extension.equals("jpeg") || extension.equals("JPG")) {


                        } else {
                            attach_fileType_flag = 1;
                            Toast toast = Toast.makeText(getApplicationContext(), " Please Select Only Images ", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 120);
                            toast.show();
                        }
                    }
                }

                if (attach_fileType_flag == 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && uri == null) {

                        attchmentDataConvertMethod(FeedBackReport.this, clipData, data);

                    } else if (uri != null) {
                        //startUpload(uri);
                        attchmentDataConvertMethod(FeedBackReport.this, null, data);
                    }
                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), allFilesAttachedCount + " files attached", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 20);
                    toast.show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

// image browse and upload code ends here


    public static String getContentName(ContentResolver resolver, Uri uri) {
        Cursor cursor = resolver.query(uri, null, null, null, null);
        cursor.moveToFirst();
        int nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
        if (nameIndex >= 0) {
            return cursor.getString(nameIndex);
        } else {
            return null;
        }
    }

    private void receiveGetRequestFeedback(View v) {
        new GetData(this).execute();
    }

    private class GetData extends AsyncTask<String, Void, Void> {

        private final Context context;
        StringBuilder responseOutput = null, stringBuilder = null;
        ;
        DataOutputStream dos = null;
        int b = 0, c = 0;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String responseFromServer = "";
        JSONObject json_new, json_1, json_2;

        public GetData(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context, R.style.dialog);
            progress = new ProgressDialog(FeedBackReport.this, R.style.dialog) {
                @Override
                public void onBackPressed() {
                    progress.cancel();
                    progress.dismiss();
                }
            };
            progress.setMessage("Loading");
            progress.setCancelable(false);
            // progress.setIndeterminate(false);
            progress.show();

        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            AlertDialog.Builder gosettings = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

            if (responseStatus.equals("1")) {

                first_txt.setText("");
                second_txt.setText("");


                //  Below the code starts to get updated next task data as getting at login like programme ,task id's and status & task name.

                try {

                    JSONObject fetchProgrammeDetails = null;
                    String loginProgrammeID, loginTaskID, loginTaskStatus, loginTaskName;

                    fetchProgrammeDetails = new JSONObject(json_new.getString("task").toString());
                    System.out.println("required data is=" + fetchProgrammeDetails);
                    loginProgrammeID = fetchProgrammeDetails.getString("program_id");
                    loginTaskID = fetchProgrammeDetails.getString("task_id");
                    loginTaskStatus = fetchProgrammeDetails.getString("status");

                    if (fetchProgrammeDetails.has("task_name")) {
                        loginTaskName = fetchProgrammeDetails.getString("task_name");
                    } else {
                        loginTaskName = "";
                    }
                    System.out.println("task completed....loginProgrammeID,loginTaskID,loginTaskStatus, loginTaskName : \n" + loginProgrammeID.toString() + "\n" + loginTaskID + "\n" + loginTaskStatus + "\n" + loginTaskName);

                    SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                    SharedPreferences.Editor sharedEditor = haredpreferences.edit();
                    sharedEditor.putString("LOGIN_PROGRAMME_ID", loginProgrammeID.toString());
                    sharedEditor.putString("LOGIN_TASK_ID", loginTaskID.toString());
                    sharedEditor.putString("LOGIN_TASK_STATUS", loginTaskStatus.toString());
                    sharedEditor.putString("LOGIN_TASK_NAME", loginTaskName.toString());
                    sharedEditor.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Code ends to get updated next task data as getting at login like programme ,task id's and status & task name.

                gosettings.setMessage(responseMessage);
                gosettings.setCancelable(false);
                gosettings.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        // Feedback saved successfully.Programme is completed.
                        // method for showing pop-up for rating the task.incase of button id 1 only i.e. exercise complete.
//                        if (_button_id.equals("1")) {
//
//                            RatingPopupMethod();
//                            rateFlag = 1;
//                        } else if (_button_id.equals("2") || _button_id.equals("3") || _button_id.equals("4") || _button_id.equals("5") || _button_id.equals("6")) {
//                    }

                        /* code for redirecting to task page or programme page */

                        if (_button_id.equals("1")) {

                            if (responseMessage.toString().equals("Feedback saved successfully.Programme is completed")) {

                                Intent previousScreen = new Intent(getApplicationContext(), TrainingProgrammes.class);
                                previousScreen.putExtra("EMAIL_ID", _loginID);
                                previousScreen.putExtra("CLIENT_ID", _clientid);
                                previousScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(previousScreen);

                            } else {

                                Intent previousScreen = new Intent(getApplicationContext(), TaskList.class);
                                previousScreen.putExtra("EMAIL_ID", _loginID);
                                previousScreen.putExtra("CLIENT_ID", _clientid);
                                previousScreen.putExtra("PROGRAMME_ID", _proogramme_id);
                                previousScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(previousScreen);
                            }

                        } else if (_button_id.equals("2") || _button_id.equals("3") || _button_id.equals("4") || _button_id.equals("5") || _button_id.equals("6")) {

                            Intent parentScreenObject = new Intent(FeedBackReport.this, UserMenuActivity.class);
                            parentScreenObject.putExtra("EMAIL_ID", _loginID);
                            parentScreenObject.putExtra("CLIENT_ID", _clientid);
                            parentScreenObject.putExtra("LOGIN_PROGRAMME_ID", _proogramme_id);
                            parentScreenObject.putExtra("LOGIN_TASK_ID", _task_id.toString());
                            parentScreenObject.putExtra("LOGIN_TASK_STATUS", _task_status.toString());
                            parentScreenObject.putExtra("LOGIN_TASK_NAME", _task_name.toString());
                            parentScreenObject.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(parentScreenObject);

                        }
                    }
                });

                AlertDialog alertBoxObject = gosettings.create();
                alertBoxObject.show();

            } else if (responseStatus.equals("0")) {
                if (_button_id.equals("6")) {
                    first_txt.setText("");

                }
                gosettings.setMessage(responseMessage);
                gosettings.setCancelable(false);
                gosettings.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        if (_button_id.equals("1")) {

                            Intent parentScreenObject = new Intent(FeedBackReport.this, TaskList.class);
                            parentScreenObject.putExtra("EMAIL_ID", _loginID);
                            parentScreenObject.putExtra("CLIENT_ID", _clientid);
                            parentScreenObject.putExtra("PROGRAMME_ID", _proogramme_id);
                            parentScreenObject.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(parentScreenObject);

                        } else if (_button_id.equals("2") || _button_id.equals("3") || _button_id.equals("4") || _button_id.equals("5") || _button_id.equals("6")) {

                            Intent parentScreenObject = new Intent(FeedBackReport.this, UserMenuActivity.class);
                            parentScreenObject.putExtra("EMAIL_ID", _loginID);
                            parentScreenObject.putExtra("CLIENT_ID", _clientid);
                            parentScreenObject.putExtra("LOGIN_PROGRAMME_ID", _proogramme_id);
                            parentScreenObject.putExtra("LOGIN_TASK_ID", _task_id.toString());
                            parentScreenObject.putExtra("LOGIN_TASK_STATUS", _task_status.toString());
                            parentScreenObject.putExtra("LOGIN_TASK_NAME", _task_name.toString());
                            parentScreenObject.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(parentScreenObject);

                        }

                    }
                });
                AlertDialog alertLogin = gosettings.create();
                alertLogin.show();
            }

            System.out.println("inside post of submit button method");

            GetRAMSpaceMethod();

            jsonImage = null;
            jsonVideo = null;
            allFilesAttachedCount = null;
            _attachments = null;

        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                URL url = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 2 * 1024 * 1024;

                url = new URL(ActivityIntermediateClass.baseApiUrl + "/feedbacks/submit/?api_key=uXO39sJA8JU7OOPrLZkxmR183larMagL&client_id=" + _clientid + "&program_id=" + _proogramme_id + "&task_id=" + _task_id + "&button_id=" + _button_id + "&rating=" + clientRateValue + "&device_type=android");

                Log.i("server url is^^^ : ", url.toString());
                System.out.println("inside do in background of submit button method");
                System.out.println("\ntotal attachment count: " + attachedFilesCount + "\n" + "images attached count : " + imageArray.size() + "\n" + "videos attached count : " + videoArray.size());

                if (_button_id.equals("1")) {

                    try {

                        conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(30 * 60 * 1000); // 30 minutes in milliseconds.
//                        conn.setChunkedStreamingMode(maxBufferSize);
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        // conn.setChunkedStreamingMode(0);
                        // conn.setChunkedStreamingMode(1024);
                        //  conn.setFixedLengthStreamingMode(maxBufferSize);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        dos = new DataOutputStream(conn.getOutputStream());
                        dos.writeBytes(twoHyphens + boundary + lineEnd);

//Adding Parameter answer_1

                        String answer_1_value = first_txt.getText().toString();
                        dos.writeBytes("Content-Disposition: form-data; name=\"answer_1\"" + lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(answer_1_value);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);

//Adding Parameter answer_2

                        String answer_2_value = second_txt.getText().toString();
                        dos.writeBytes("Content-Disposition: form-data; name=\"answer_2\"" + lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(answer_2_value);
                        dos.writeBytes(lineEnd);

// Adding Parameter media file(images only) code starts

                        if (imageArray.size() > 0) {

                            for (b = 0; b < imageArray.size(); b++) {

                                String fileName = imageArray.get(b);
                                File sourceFile = new File(fileName);
                                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                                dos.writeBytes(twoHyphens + boundary + lineEnd);
                                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file_image[]\";filename=\"" + fileName + "\"" + lineEnd);
                                dos.writeBytes(lineEnd);

                                InputStream stream = new ByteArrayInputStream(imageByteDataArray.get(b));
                                bytesAvailable = stream.available();   // get bytes of compressed image.
//                                conn.setChunkedStreamingMode(bytesAvailable);// create a buffer of maximum size
                                // bytesAvailable = fileInputStream.available();   // get bytes of original image.
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                buffer = new byte[bufferSize];
                                // read file and write it into form...
                                bytesRead = stream.read(buffer, 0, bufferSize);

                                while (bytesRead > 0) {
                                    dos.write(buffer, 0, bufferSize);
                                    bytesAvailable = stream.available();
                                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                    bytesRead = stream.read(buffer, 0, bufferSize);
                                }

                                // send multipart form data necesssary after file data...

                                System.out.println("### file successfully uploaded: " + sourceFile);
                                dos.writeBytes(lineEnd);
                                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                                // fileInputStream.close();
                            }
                            // GetRAMSpaceMethod();
                        }
                        //  video attachment code starts

                        if (videoArray.size() > 0) {

                            for (c = 0; c < videoArray.size(); c++) {

                                String fileName = videoArray.get(c);
                                File sourceFile = new File(fileName);

                                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                                dos.writeBytes(twoHyphens + boundary + lineEnd);
                                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file_video[]\";filename=\"" + fileName + "\"" + lineEnd);
                                dos.writeBytes(lineEnd);
                                // create a buffer of maximum size
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                buffer = new byte[bufferSize];
                                // read file and write it into form...
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                                while (bytesRead > 0) {
                                    dos.write(buffer, 0, bufferSize);
                                    bytesAvailable = fileInputStream.available();
                                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                                }

                                // send multipart form data necesssary after file data...

                                System.out.println("### file successfully uploaded: " + sourceFile);

                                dos.writeBytes(lineEnd);
                                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                                // fileInputStream.close();
                            }
                            //  GetRAMSpaceMethod();
                        }   // video attachment code ends here.

// Adding Parameter media file(images only) code ends
                        dos.flush();
                        dos.close();

                    } catch (MalformedURLException ex) {

                        ex.printStackTrace();
                        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

                    } catch (final Exception e) {

                        e.printStackTrace();
                        Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
                    }

                } else if (_button_id.equals("6")) {

                    try {

                        conn = (HttpURLConnection) url.openConnection();
                        //  conn.setChunkedStreamingMode(0);
                        conn.setConnectTimeout(30 * 60 * 1000); // 30 minutes in milliseconds.
//                        conn.setChunkedStreamingMode(maxBufferSize);
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        //  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        //  conn.setRequestProperty("uploaded_file", fileName);
                        dos = new DataOutputStream(conn.getOutputStream());
                        dos.writeBytes(twoHyphens + boundary + lineEnd);

// Adding Parameter help_comment

                        String answer_1_value = first_txt.getText().toString();
                        dos.writeBytes("Content-Disposition: form-data; name=\"help_comment\"" + lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(answer_1_value);
                        dos.writeBytes(lineEnd);

                        dos.writeBytes(twoHyphens + boundary + lineEnd);


// Adding Parameter media file(images only) code starts

                        if (imageArray.size() > 0) {

                            for (b = 0; b < imageArray.size(); b++) {

                                String fileName = imageArray.get(b);
                                File sourceFile = new File(fileName);
                                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                                dos.writeBytes(twoHyphens + boundary + lineEnd);
                                dos.writeBytes("Content-Disposition: form-data; name=\"help_attachment[]\";filename=\"" + fileName + "\"" + lineEnd);
                                dos.writeBytes(lineEnd);
                                // create a buffer of maximum size
                                // bytesAvailable = fileInputStream.available();

                                InputStream stream = new ByteArrayInputStream(imageByteDataArray.get(b));
                                bytesAvailable = stream.available();   // get bytes of compressed image.

                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                buffer = new byte[bufferSize];
                                // read file and write it into form...
                                bytesRead = stream.read(buffer, 0, bufferSize);

                                while (bytesRead > 0) {
                                    dos.write(buffer, 0, bufferSize);
                                    bytesAvailable = stream.available();
                                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                    bytesRead = stream.read(buffer, 0, bufferSize);
                                }

                                // send multipart form data necesssary after file data...

                                System.out.println("### file successfully uploaded: " + sourceFile);

                                dos.writeBytes(lineEnd);
                                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                                fileInputStream.close();
                            }
                        }
// Adding Parameter media file(images only) code ends

                    } catch (MalformedURLException ex) {

                        ex.printStackTrace();
                        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

                    } catch (final Exception e) {

                        e.printStackTrace();
                        Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
                    }
                    dos.flush();
                    dos.close();

                } else {

                    conn = (HttpURLConnection) url.openConnection();
                    //  conn.setChunkedStreamingMode(0);
                    conn.setConnectTimeout(30 * 60 * 1000); // 30 minutes in milliseconds.
//                        conn.setChunkedStreamingMode(maxBufferSize);
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    //  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                }

                if (selectedDate != null) {

                    try {

                        conn = (HttpURLConnection) url.openConnection();
                        //  conn.setChunkedStreamingMode(0);
                        conn.setConnectTimeout(30 * 60 * 1000); // 30 minutes in milliseconds.
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        //  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        //  conn.setRequestProperty("uploaded_file", fileName);
                        dos = new DataOutputStream(conn.getOutputStream());
                        dos.writeBytes(twoHyphens + boundary + lineEnd);

// Adding Parameter return date

                        String selectedDateValue = selectedDateAPIFORMAT.toString();
                        dos.writeBytes("Content-Disposition: form-data; name=\"return_date\"" + lineEnd);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(selectedDateValue);
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + lineEnd);

                    } catch (MalformedURLException ex) {

                        ex.printStackTrace();
                        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

                    } catch (final Exception e) {

                        e.printStackTrace();
                        Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
                    }
                    dos.flush();
                    dos.close();
                }
                //}

                // close the streams //


                final int responseCode = conn.getResponseCode();

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line = "";
                    responseOutput = new StringBuilder();
                    System.out.println("output===============" + br);
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();
                }
                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                FeedBackReport.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            System.out.println("JSON response is: " + responseOutput);

                            json_new = new JSONObject(responseOutput.toString());
                            System.out.println("json data is: " + json_new);
                            responseStatus = json_new.getString("status");
                            responseMessage = json_new.getString("message");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }
    } // end of async task method


    // rating popup window method
    public void RatingPopupMethod() {

        Dialog ratingDialog = new Dialog(FeedBackReport.this);
        ratingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ratingDialog.setContentView(R.layout.ratingpopupscreen);
        ratingDialog.setCancelable(true);
        ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = ratingDialog.getWindow();
        lp.copyFrom(window.getAttributes());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        lp.width = size.x;
        lp.height = size.y;
        // This makes the dialog take up the full width
        window.setAttributes(lp);

        // initialize buttons and imageViews in pop-up.

        btnVideo1 = (Button) ratingDialog.findViewById(R.id.btnVideo1);
        btnVideo2 = (Button) ratingDialog.findViewById(R.id.btnVideo2);
        btnVideo3 = (Button) ratingDialog.findViewById(R.id.btnVideo3);
        btnVideo4 = (Button) ratingDialog.findViewById(R.id.btnVideo4);
        btnVideo5 = (Button) ratingDialog.findViewById(R.id.btnVideo5);

        img1 = (ImageView) ratingDialog.findViewById(R.id.rate_img1);
        img2 = (ImageView) ratingDialog.findViewById(R.id.rate_img2);
        img3 = (ImageView) ratingDialog.findViewById(R.id.rate_img3);
        img4 = (ImageView) ratingDialog.findViewById(R.id.rate_img4);
        img5 = (ImageView) ratingDialog.findViewById(R.id.rate_img5);

        rateSubmitBtn = (Button) ratingDialog.findViewById(R.id.rateSubmitBtn);

        clientRateValue = 0;

        btnVideo1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Toast.makeText(getApplicationContext(), "button 1 clicked", Toast.LENGTH_LONG).show();
                SelectSmileyMethod(1);

            }
        });
        btnVideo2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //  Toast.makeText(getApplicationContext(), "button 2 clicked", Toast.LENGTH_LONG).show();
                SelectSmileyMethod(2);

            }
        });
        btnVideo3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //   Toast.makeText(getApplicationContext(), "button 3 clicked", Toast.LENGTH_LONG).show();
                SelectSmileyMethod(3);

            }
        });
        btnVideo4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //   Toast.makeText(getApplicationContext(), "button 4 clicked", Toast.LENGTH_LONG).show();
                SelectSmileyMethod(4);

            }
        });
        btnVideo5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //    Toast.makeText(getApplicationContext(), "button 5 clicked", Toast.LENGTH_LONG).show();
                SelectSmileyMethod(5);

            }
        });

        rateSubmitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //    Toast.makeText(getApplicationContext(), "rating submit button clicked", Toast.LENGTH_LONG).show();

                if (clientRateValue == 0) {

                    AlertDialog.Builder gosettings = new AlertDialog.Builder(FeedBackReport.this, R.style.MyAlertDialogStyle);
                    gosettings.setMessage("Please Rate The Task!");
                    gosettings.setCancelable(false);
                    gosettings.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertLogin = gosettings.create();
                    alertLogin.show();
                } else {
                    //  new sendRatevalueMethod(FeedBackReport.this, clientRateValue).execute();

                    receiveGetRequestFeedback(v);
                }

            }
        });
        ratingDialog.show();
    }

    //    rating module method
    public void SelectSmileyMethod(int buttonValue) {

        if (buttonValue == 1) {

            img1.setBackgroundResource(R.drawable.emoji_bg);
            img2.setBackgroundResource(R.drawable.sky_circle);
            img3.setBackgroundResource(R.drawable.sky_circle);
            img4.setBackgroundResource(R.drawable.sky_circle);
            img5.setBackgroundResource(R.drawable.sky_circle);

            clientRateValue = 1;

        } else if (buttonValue == 2) {

            img1.setBackgroundResource(R.drawable.sky_circle);
            img2.setBackgroundResource(R.drawable.emoji_bg);
            img3.setBackgroundResource(R.drawable.sky_circle);
            img4.setBackgroundResource(R.drawable.sky_circle);
            img5.setBackgroundResource(R.drawable.sky_circle);

            clientRateValue = 2;

        } else if (buttonValue == 3) {

            img1.setBackgroundResource(R.drawable.sky_circle);
            img2.setBackgroundResource(R.drawable.sky_circle);
            img3.setBackgroundResource(R.drawable.emoji_bg);
            img4.setBackgroundResource(R.drawable.sky_circle);
            img5.setBackgroundResource(R.drawable.sky_circle);

            clientRateValue = 3;

        } else if (buttonValue == 4) {

            img1.setBackgroundResource(R.drawable.sky_circle);
            img2.setBackgroundResource(R.drawable.sky_circle);
            img3.setBackgroundResource(R.drawable.sky_circle);
            img4.setBackgroundResource(R.drawable.emoji_bg);
            img5.setBackgroundResource(R.drawable.sky_circle);

            clientRateValue = 4;

        } else if (buttonValue == 5) {

            img1.setBackgroundResource(R.drawable.sky_circle);
            img2.setBackgroundResource(R.drawable.sky_circle);
            img3.setBackgroundResource(R.drawable.sky_circle);
            img4.setBackgroundResource(R.drawable.sky_circle);
            img5.setBackgroundResource(R.drawable.emoji_bg);

            clientRateValue = 5;

        }
        Log.i("Client  rating val:", String.valueOf(clientRateValue));
    }


    // rating module aynsc task.
    private class sendRatevalueMethod extends AsyncTask<String, Void, Void> {
        private final Context context;
        private final int ratingValue;

        public sendRatevalueMethod(Context c, int ratevalue) {
            this.context = c;
            this.ratingValue = ratevalue;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context, R.style.dialog);
            progress.setMessage("Loading");
            progress.show();
        }

        protected void onPostExecute(Void result) {

            AlertDialog.Builder alertRatingObject = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

            if (responseStatusRating.equals("1")) {

                rateFlag = 0;

                alertRatingObject.setMessage(responseMessageRating);
                alertRatingObject.setCancelable(false);
                alertRatingObject.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        // code for redirecting to task page or programme page

                        if (responseMessage.toString().equals("Feedback saved successfully.Programme is completed")) {

                            Intent previousScreen = new Intent(getApplicationContext(), TrainingProgrammes.class);
                            previousScreen.putExtra("EMAIL_ID", _loginID);
                            previousScreen.putExtra("CLIENT_ID", _clientid);
                            previousScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(previousScreen);

                        } else {

                            Intent previousScreen = new Intent(getApplicationContext(), TaskList.class);
                            previousScreen.putExtra("EMAIL_ID", _loginID);
                            previousScreen.putExtra("CLIENT_ID", _clientid);
                            previousScreen.putExtra("PROGRAMME_ID", _proogramme_id);
                            previousScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(previousScreen);
                        }
                    }
                });
                AlertDialog alertLogin = alertRatingObject.create();
                alertLogin.show();


            } else if (responseStatusRating.equals("0")) {
                alertRatingObject.setMessage(responseMessageRating);
                alertRatingObject.setCancelable(false);
                alertRatingObject.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = alertRatingObject.create();
                alertLogin.show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                URL urlRating = null;

                try {

                    urlRating = new URL(ActivityIntermediateClass.baseApiUrl + "/feedbacks/addRating/?api_key=" + ActivityIntermediateClass.apiKeyValue);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                HttpURLConnection connection = null;

                try {
                    connection = (HttpURLConnection) urlRating.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    connection.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write("&client_id=" + _clientid + "&task_id=" + _task_id + "&rate=" + String.valueOf(ratingValue));
                wr.flush();

                System.out.println("URL is: " + urlRating + "\n&client_id=" + _clientid + "&task_id=" + _task_id + "&rate=" + String.valueOf(ratingValue));

                final int responseCode = connection.getResponseCode();
                System.out.println("RESPONSE CODE is: " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + urlRating);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line = "";
                    responseOutputRating = new StringBuilder();
                    System.out.println("output===============" + br);
                    while ((line = br.readLine()) != null) {
                        responseOutputRating.append(line);
                    }
                    br.close();
                }

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutputRating.toString());

                FeedBackReport.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Log.i("JSON FEEDBCK : ", responseOutputRating.toString());

                        JSONObject json_new;
                        try {

                            json_new = new JSONObject(responseOutputRating.toString());
                            responseStatusRating = json_new.getString("status");
                            responseMessageRating = json_new.getString("message");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
                    }
                });
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    // attachemnt module(image & video) aynsc task.
    private void attchmentDataConvertMethod(Context c, ClipData clipData, Intent data) {

        new DataConvertMethod(c, clipData, data).execute();
    }

    private class DataConvertMethod extends AsyncTask<String, Void, Void> {

        private final Context context;
        private final ClipData clipData;
        private final Intent data;

        //         JSONArray jsonVideoArray = new JSONArray();
//         JSONObject jsonoparentvideobject = new JSONObject();
//        JSONArray jsonImageArray = new JSONArray();
//        JSONObject jsonoparentimagebject = new JSONObject();
        int i, j;


        public DataConvertMethod(Context c, ClipData clipData, Intent data) {
            this.context = c;
            this.clipData = clipData;
            this.data = data;
        }

        protected void onPreExecute() {

            imageArray = _attachments.getImageArray() == null ? new ArrayList<String>() : _attachments.getImageArray();
            videoArray = _attachments.getVideoArray() == null ? new ArrayList<String>() : _attachments.getVideoArray();
            audioArray = _attachments.getAudioArray() == null ? new ArrayList<String>() : _attachments.getAudioArray();
            docArray = _attachments.getDocArray() == null ? new ArrayList<String>() : _attachments.getDocArray();
            pdfArray = _attachments.getPdfArray() == null ? new ArrayList<String>() : _attachments.getPdfArray();
            storylineArray = _attachments.getStorylineArray() == null ? new ArrayList<String>() : _attachments.getStorylineArray();


            // System.out.println("imageArray : "+imageArray.size()+"\n"+"videoArray : "+videoArray.size()+"\n"+"\n"+"audioArray : "+audioArray.size()+"\n"+"\n"+"docArray : "+docArray.size()+"\n");

            progress = new ProgressDialog(this.context, R.style.dialog);
            progress = new ProgressDialog(FeedBackReport.this, R.style.dialog) {
                @Override
                public void onBackPressed() {
                    progress.cancel();
                    progress.dismiss();
                }
            };
            progress.setMessage("attaching files...");
            progress.setCancelable(false);
            // progress.setIndeterminate(false);
            progress.show();
        }

        protected void onPostExecute(Void result) {

            _attachments.setAudioArray(audioArray);
            _attachments.setVideoArray(videoArray);
            _attachments.setImageArray(imageArray);
            _attachments.setDocArray(docArray);
            _attachments.setPdfArray(pdfArray);
            _attachments.setStorylineArray(storylineArray);

            _attachments.setTask_id(_task_id);

            allFilesAttachedCount = ((audioArray.size()) + (videoArray.size()) + (imageArray.size()) + (docArray.size()) + (pdfArray.size()) + (storylineArray.size()));


            Log.i("VIDEO DATA: ", videoArray.toString() + "\nJSON PARENT IMAGE DATA: " + imageArray.toString());

            Toast toast = Toast.makeText(getApplicationContext(), allFilesAttachedCount + " files attached", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 20);
            toast.show();

            Log.i("FINAL VIDEO DATA: ", videoArray + "\nJOSN FINAL IMAGE DATA: " + imageArray.toString());
            progress.dismiss();

            System.out.println("inside post of attachemnt method");

            GetRAMSpaceMethod();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                selectedPath = null;
                if (clipData != null) {
                    int count = clipData.getItemCount();
                    attachedFilesCount = count;

                    for (int i = 0; i < count; ++i) {
                        URI_data.add(clipData.getItemAt(i).getUri());

                        Uri uri = clipData.getItemAt(i).getUri();


                        String filename = GetFileName(uri);
                        try {
                            addBaseString(uri, filename);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("URI DATA IS: " + clipData.getItemAt(i).getUri().toString());
                    }
                } else {
                    attachedFilesCount = 1;
                    Uri uri = data.getData();
                    String filename = GetFileName(uri);
                    try {
                        addBaseString(uri, filename);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//            System.out.println("image data and count is : " + URI_data + "\n" + attachedFilesCount);
//            System.out.println("image bitmap data is : " + bitmap_array_data);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }


    public String GetFileName(Uri uri) {
//
        String fileName;
        String path = "";
        String data_path = "";
        // String[] projection = {MediaStore.MediaColumns.DATA};
        String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
        ContentResolver cr = getApplicationContext().getContentResolver();
        try (Cursor metaCursor = cr.query(uri, projection, null, null, null)) {
            if (metaCursor != null) {
                try {
                    if (metaCursor.moveToFirst()) {
                        path = metaCursor.getString(0);
                    }
                } finally {
                    metaCursor.close();
                }
            }
        }
        fileName = path;
        System.out.println("Hello: " + uri);
        System.out.println("Hello: " + path);

        String[] projection_data = {MediaStore.MediaColumns.DATA};

        try (Cursor metaCursor = cr.query(uri, projection_data, null, null, null)) {
            if (metaCursor != null) {
                try {
                    if (metaCursor.moveToFirst()) {
                        data_path = metaCursor.getString(0);
                    }
                } finally {
                    metaCursor.close();
                }
            }
        }
        System.out.println("Hello: " + data_path);
        return fileName;

    }

    public void addBaseString(Uri uri, String filename) throws IOException, URISyntaxException {
        System.out.println("In GetBaseString()");

        String output = null;
        selectedPath = null;
        String attachedFile = null;
        String filenameArray[] = filename.split("\\.");
        String extension = filenameArray[filenameArray.length - 1];
//        InputStream inputStream = null;//You can get an inputStream using any IO API

        if (extension.equals("mp4") || extension.equals("3gp") || extension.equals("avi") || extension.equals("mpg") || extension.equals("flv") || extension.equals("mkv") || extension.equals("mov")) {

            // Toast.makeText(FeedBackReport.this, "Please Select Images", Toast.LENGTH_SHORT).show();

            selectedPath = getPathVideo(uri);
            attachedFile = selectedPath;

//            String uploadReturnString = uploadVideo(selectedPath);
//            System.out.println("\n Upload return string : " + uploadReturnString);

        } else if (extension.equals("jpg") || extension.equals("png") || extension.equals("gif") || extension.equals("bmp") || extension.equals("jpeg") || extension.equals("JPG")) {

            String filePathNew = getPath(getApplicationContext(), uri);
            //  output = Base64.encodeToString(ImageUtils.compressImage(filePathNew.toString()), Base64.DEFAULT);
            //  attachedFile = URLEncoder.encode(output.toString(), "utf-8");

            byte[] imageByteData = ImageUtils.compressImage(filePathNew.toString());
            long lengthbmp = imageByteData.length;

            String compressedImgSize = formatSize(lengthbmp);

            imageByteDataArray.add(imageByteData);

            Log.i("size of compressed image in bytes: ", String.valueOf(lengthbmp));
            Log.i("size of compressed image: ", String.valueOf(compressedImgSize));

            attachedFile = filePathNew.toString();
        }

        // String attachedFile = output64.toString();
        //  System.out.println("url encoding images: " + attachedFile);

        if (extension.equals("mp4") || extension.equals("3gp") || extension.equals("avi") || extension.equals("mpg") || extension.equals("flv") || extension.equals("mkv")) {

            videoArray.add(attachedFile);

        } else if (extension.equals("jpg") || extension.equals("png") || extension.equals("gif") || extension.equals("bmp")) {

            imageArray.add(attachedFile);

        }

// else if (extension.equals("mp3") || extension.equals("3ga") || extension.equals("avr") || extension.equals("amr") || extension.equals("m4a") || extension.equals("wav")) {
//
//            audioArray.add(attachedFile);
//
//        } else if (extension.equals("doc") || extension.equals("docx") || extension.equals("xls") || extension.equals("xlsx")) {
//
//            docArray.add(attachedFile);
//
//        } else if (extension.equals("pdf")) {
//
//            pdfArray.add(attachedFile);
//
//        } else if (extension.equals("html")) {
//            storylineArray.add(attachedFile);
//        }
    }

    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    // upload video incase of attachment method.
    public String getPathVideo(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


//    @Override
//    public void onBackPressed() {
//
//        if (rateFlag == 1) {  //Here no means dont allow user to go back
//
//        } else {
//            super.onBackPressed(); // Process Back key default behavior.
//        }
//
//    }
}