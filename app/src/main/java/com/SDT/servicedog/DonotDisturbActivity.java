package com.SDT.servicedog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;


public class DonotDisturbActivity extends AppCompatIntermediateActivity {


    private Button submitBtn;
    private WebView textView_message;
    private String textview_message_content, resultOutput, resultOutputMsg, Client_ID, dnd_from, dnd_to, startTimePeriods = null, startSelectedTime = null, endTimePeriods = null, endSelectedTime = null;
    private TextView start_time = null, end_time = null;
    StringBuilder responseOutput;
    private Integer hour, minute, year, month, day;
    private int flagValue = 0;
    private String timeSet = "";
    private StringBuilder aTime = new StringBuilder();
    ProgressDialog progress;

    private Toolbar toolbar;
    private View header;
    String TaskListDescclientID,_loginID;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new ProgressDialog(DonotDisturbActivity.this, R.style.dialog);
        progress.setMessage("Loading Page");
        progress.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progress.dismiss();
            }
        }, 2000); // 3000 milliseconds delay

        setContentView(R.layout.content_donot_disturb);

        textView_message = (WebView) findViewById(R.id.textView_message);
        textview_message_content = "<html><body style=color:white;font-size:23px;>" + "<p align=\"justify\">" +
                "Mention time below during which you are busy and don't want to be get disturbed." +
                "We won't send any notifications during these hours to you !!" + "</p>" + "</body></html>";

        // final WebSettings webSettings = textView_message.getSettings();
        //Resources res = getResources();
        // int fontSize = (int) res.getDimension(R.dimen.textSize);
        //  webSettings.setDefaultFontSize(fontSize);

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();

        System.out.println("/******************************* DO NOT DISTURB CLASS STARTED ************************/");

        submitBtn = (Button) findViewById(R.id.submitBtn);
        start_time = (TextView) findViewById(R.id.editText_from);
        end_time = (TextView) findViewById(R.id.editText_to);

        textView_message.getSettings().setJavaScriptEnabled(true);
        textView_message.loadData(textview_message_content.toString(), "text/html", "utf-8");
        textView_message.setBackgroundColor(Color.TRANSPARENT);

        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        Client_ID = haredpreferences.getString("CLIENT_ID", "Null");
        dnd_from = haredpreferences.getString("dnd_from", "Null");
        dnd_to = haredpreferences.getString("dnd_to", "Null");
        _loginID = haredpreferences.getString("email_id","Null");
        Log.i("CLIENT ID VALUE: ", Client_ID + "\n" + dnd_from + " " + dnd_to);

        start_time.setText(dnd_from);
        end_time.setText(dnd_to);

//        start_time.setText(null);
//        end_time.setText(null);
        System.out.println("------start_time:" + dnd_from + "<br>end time: " + dnd_to);

        submitBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                ConnectivityManager cm_object = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi_connect = cm_object.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo data_connect = cm_object.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi_connect != null & data_connect != null) && (wifi_connect.isConnected() | data_connect.isConnected())) {
                    if (!start_time.getText().toString().equals("") && !end_time.getText().toString().equals("")) {

                        sendPostRequest(v);

                    } else if (start_time.getText().toString().equals("")) {
                        if (end_time.getText().toString().equals("")) {

                            Toast.makeText(DonotDisturbActivity.this, "Please Enter Both Fields", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DonotDisturbActivity.this, "Please Enter Start Time", Toast.LENGTH_SHORT).show();
                        }

                    } else if (end_time.getText().toString().equals("")) {
                        if (start_time.getText().toString().equals("")) {
                            Toast.makeText(DonotDisturbActivity.this, "Please Enter Both Fields", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DonotDisturbActivity.this, "Please Enter End Time", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(DonotDisturbActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        start_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(start_time.getWindowToken(), 0);

                aTime.setLength(0);
                flagValue = 0;
                hour = 0;
                minute = 0;

                year = 0;
                month = 0;
                day = 0;
                //flagValue = 3333;


                if (!start_time.getText().toString().equals("")) {

                    String[] start_date_value_sub_string, start_time_value_sub_string;

                    String[] startTime_string = start_time.getText().toString().split(" ");

                    startSelectedTime = startTime_string[1].toString().substring(0, 5);
                    start_time_value_sub_string = startSelectedTime.split(":");

                    hour = Integer.parseInt(start_time_value_sub_string[0]);
                    minute = Integer.parseInt(start_time_value_sub_string[1]);

                    start_date_value_sub_string = startTime_string[0].split("-");

                    year = Integer.parseInt(start_date_value_sub_string[0]);
                    month = Integer.parseInt(start_date_value_sub_string[1]);
                    day = Integer.parseInt(start_date_value_sub_string[2]);


                    //   Toast.makeText(DonotDisturbActivity.this, "start date time--- " + day + " " + month + " " + year + " " + hour + " " + minute, Toast.LENGTH_LONG).show();


                    Log.i("start time values : ", "\n" + start_time + "\n---" + startTime_string[0] + "-----" + startTime_string[1] + "---\n" + hour + "\n" + minute + "---\n" + year + "\n" + month + "---\n" + day);

                    DatePickerDialog datePickerstart = new DatePickerDialog(DonotDisturbActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, datePickerListener, year, month - 1, day);
                    datePickerstart.setCancelable(false);
                    datePickerstart.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerstart.setTitle("Select Start Date");
                    datePickerstart.show();

                    flagValue = 1111;


                } else {

                    final Calendar c = Calendar.getInstance();
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);

                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerstart = new DatePickerDialog(DonotDisturbActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, datePickerListener, year, month, day);
                    datePickerstart.setCancelable(false);
                    datePickerstart.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerstart.setTitle("Select Start Date");
                    datePickerstart.show();

                }

                flagValue = 1111;
                // showDialog(3333);
            }
        });

        end_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(end_time.getWindowToken(), 0);

                aTime.setLength(0);
                flagValue = 0;
                hour = 0;
                minute = 0;

                year = 0;
                month = 0;
                day = 0;
                //flagValue = 4444;

                if (!end_time.getText().toString().equals("")) {


                    String[] end_date_value_sub_string, end_time_value_sub_string;

                    String[] endTime_string = end_time.getText().toString().split(" ");

                    endSelectedTime = endTime_string[1].toString().substring(0, 5);
                    end_time_value_sub_string = endSelectedTime.split(":");

                    hour = Integer.parseInt(end_time_value_sub_string[0]);
                    minute = Integer.parseInt(end_time_value_sub_string[1]);

                    end_date_value_sub_string = endTime_string[0].split("-");

                    year = Integer.parseInt(end_date_value_sub_string[0]);
                    month = Integer.parseInt(end_date_value_sub_string[1]);
                    day = Integer.parseInt(end_date_value_sub_string[2]);


                    //  Toast.makeText(DonotDisturbActivity.this, "end date time " + day + " " + month + " " + year + " " + hour + " " + minute, Toast.LENGTH_LONG).show();
                    Log.i("end time values : ", "\n" + end_time + "\n---" + endTime_string[0] + "-----" + endTime_string[1] + "---\n" + hour + "\n" + minute + "---\n" + year + "\n" + month + "---\n" + day);

                } else {

                    final Calendar c = Calendar.getInstance();
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);

                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);

                }

                DatePickerDialog datePickerEnd = new DatePickerDialog(DonotDisturbActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, datePickerListener, year, month - 1, day);
                datePickerEnd.setCancelable(false);
                datePickerEnd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerEnd.setTitle("Select End Date");
                datePickerEnd.show();

                flagValue = 2222;
                //  showDialog(4444);
            }
        });

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
                        Intent aboutusintent = new Intent(DonotDisturbActivity.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
//                        Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(DonotDisturbActivity.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(DonotDisturbActivity.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(DonotDisturbActivity.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(DonotDisturbActivity.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;
                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(DonotDisturbActivity.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(DonotDisturbActivity.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID",_loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(DonotDisturbActivity.this, MainActivity.class);
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

    // code for time picker starts here

    @Override
    protected Dialog onCreateDialog(int id) {

        // set current time into output textview
        //  updateTime(hour, minute);

        switch (id) {
            case 1111:

                final TimePickerDialog timePicker = new TimePickerDialog(this,TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {

                        // TODO Auto-generated method stub
                        hour = hourOfDay;
                        minute = minutes;
                        updateTime(hour, minute);
                        hour = 0;
                        minute = 0;
                    }
                }, hour, minute, false); // 12 is the hour and 25 is minutes..please change this

                timePicker.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        // This is hiding the "Cancel" button:
                        timePicker.getButton(Dialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                    }
                });
                timePicker.setCancelable(false);
                timePicker.setTitle("Select Start Time");
                timePicker.show();
                break;

                // set time picker as current time
//                return new TimePickerDialog(DonotDisturbActivity.this, TimePickerDialog.THEME_HOLO_LIGHT, timePickerListener, hour, minute,
//                        false);
            case 2222:

                final TimePickerDialog timePickerEnd = new TimePickerDialog(this,TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {

                        // TODO Auto-generated method stub
                        hour = hourOfDay;
                        minute = minutes;
                        updateTime(hour, minute);
                        hour = 0;
                        minute = 0;
                    }
                }, hour, minute, false); // 12 is the hour and 25 is minutes..please change this

                timePickerEnd.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        // This is hiding the "Cancel" button:
                        timePickerEnd.getButton(Dialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
                    }
                });
                timePickerEnd.setCancelable(false);
                timePickerEnd.setTitle("Select End Time");
                timePickerEnd.show();
//                return new TimePickerDialog(DonotDisturbActivity.this, TimePickerDialog.THEME_HOLO_LIGHT, timePickerListener1, hour, minute,
//                        false);

        }
        return null;
    }

// code for date picker listener method starts here

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is called, below method will be called.
        // The arguments will be working to get the Day of Week to show it in a special TextView for it.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            //Toast.makeText(DonotDisturbActivity.this, "on date set func" + selectedYear + " " + selectedMonth + " " + selectedDay, Toast.LENGTH_LONG).show();
            String year1 = null;
            String month1 = null;
            String day1 = null;

            year1 = String.valueOf(selectedYear);
            month1 = String.valueOf(selectedMonth + 1);
            day1 = String.valueOf(selectedDay);
            // start_time.setText(month1 + "/" + day1 + "/" + year1);
            //  start_time.setText(DateFormat.format("EEEE", new Date(selectedYear, selectedMonth, selectedDay - 1)).toString());

            if (Integer.parseInt(day1) < 10) {
                day1 = "0" + day1;
            } else {
                day1 = String.valueOf(day1);
            }

            if (Integer.parseInt(month1) < 10) {
                month1 = "0" + month1;
            } else {
                month1 = String.valueOf(month1);
            }

            System.out.println("Date picker selected date: " + month1 + "-" + day1 + "-" + year1);

            aTime.append(year1).append('-').append(month1).append("-").append(day1).append(' ').toString();

            // Toast.makeText(DonotDisturbActivity.this, "on date set end toast "+ year1 + " " + month1 + " " + day1, Toast.LENGTH_LONG).show();

//            if (flagValue == 1111) {
//                start_time.setText(aTime);
//            } else if (flagValue == 2222) {
//                end_time.setText(aTime);
//            }

            showDialog(flagValue);
        }
    };

    // code for date picker listener method ends here

//    public TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
//
//        @Override
//        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
//            // TODO Auto-generated method stub
//            hour = hourOfDay;
//            minute = minutes;
//            updateTime(hour, minute);
//            hour = 0;
//            minute = 0;
//        }
//    };

    private void updateTime(int hours, int mins) {

        String minutes = "";

        if (hours == 0) {
            hours += 12;
        }

        if (mins < 10) {
            minutes = "0" + mins;
        } else {
            minutes = String.valueOf(mins);
        }

        // Append in a StringBuilder
        aTime.append(hours).append(':').append(minutes).append(':').append("00").toString();

        System.out.println("----- atime value is:" + aTime.toString());

        if (flagValue == 1111) {
            start_time.setText(aTime);
        } else if (flagValue == 2222) {
            end_time.setText(aTime);
        }
//        StringBuilder toParse = aTime;
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); // I assume d-M, you may refer to M-d for month-day instead.
//        Date date = null; // You will need try/catch around this
//        try {
//            date = formatter.parse(String.valueOf(toParse));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        long millis = date.getTime();
//        System.out.println("########converted date time is######### : "+millis);
    }

    // code for time picker ends here


    public void sendPostRequest(View View) {
        new PostClass(this).execute();
    }

    private class PostClass extends AsyncTask<String, Void, Void> {

        private final Context context;
        private ProgressDialog progress;

        public PostClass(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context, R.style.dialog);
            progress.setMessage("Loading");
            progress.show();
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            AlertDialog.Builder godndpage = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            System.out.println("result output" + resultOutput);


            if (resultOutput.equals("0")) {

                godndpage.setMessage(resultOutputMsg);
                godndpage.setCancelable(false);
                godndpage.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = godndpage.create();
                alertLogin.show();
            }

            if (resultOutput.equals("1")) {

                dnd_from = start_time.getText().toString();
                dnd_to = end_time.getText().toString();

                SharedPreferences sharedpreference = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
                SharedPreferences.Editor sharededitor = sharedpreference.edit();
                sharededitor.putString("dnd_from", start_time.getText().toString());
                sharededitor.putString("dnd_to", end_time.getText().toString());
                sharededitor.commit();

                //end_time.setText("");
                // start_time.setText("");

                godndpage.setMessage(resultOutputMsg);
                godndpage.setCancelable(false);
                godndpage.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = godndpage.create();
                alertLogin.show();

            }
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                URL url = null;
                try {
                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/users/dndhours/?api_key="+ActivityIntermediateClass.apiKeyValue+"&client_id=" + Client_ID);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Log.i("-----URL IS-----: ", url.toString());

                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
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

                OutputStreamWriter wr_object = new OutputStreamWriter(connection.getOutputStream());
                wr_object.write("&from=" + start_time.getText().toString() + "&to=" + end_time.getText().toString());
                wr_object.flush();

                Log.i("Post Data Values: ", "&from=" + start_time.getText().toString() + "&to=" + end_time.getText().toString());

                int responseCode = connection.getResponseCode();

                final StringBuilder output = new StringBuilder("Request URL " + url);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                DonotDisturbActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.i("JSON REPOSNSE IS: ", responseOutput.toString());

                        JSONObject json = null;
                        try {
                            json = new JSONObject(responseOutput.toString());
                            resultOutput = json.getString("status");
                            resultOutputMsg = json.getString("message");
                            System.out.println("the output" + resultOutput);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
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
