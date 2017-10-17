package com.SDT.servicedog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.*;

import com.google.gson.Gson;
import com.SDT.servicedog.TaskListsCollectionData.MediaTasks;
import com.SDT.servicedog.TaskListsCollectionData.TaskRootObject;

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


public class TaskList extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ListView listView;
    ProgressDialog progress;
    JSONArray resultOutput, resultOutputMedia;
    String resultOutputMsg;
    int resultStatus;
    List<MediaTasks> fetchjsonData1;
    String programReferenceID;
    StringBuilder responseOutput;
    String _programmeID;
    View header;
    String _loginID = null, _clientid = null, _preview_role = null;
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
    public static String _utfValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist);

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();

        AppCompatIntermediateActivity.taskTimerFlag = 0;

        // TaskListDescription.timerFinishFlag = 0;

//        TaskListDescription objForTaskTimer = new TaskListDescription();
//        objForTaskTimer.countDownTimerCode();
//
//        TaskListDescription.activityTaskTimer = this;
//        AppCompatIntermediateActivity.taskTimerFlag = 1;


        System.out.println("/*******************************  TASKLIST CLASS STARTED   *****************************************************/");
        /*******************************  TASKLIST CLASS STARTED   *****************************************************/


        Intent intent = getIntent();
        _programmeID = intent.getStringExtra("PROGRAMME_ID");
        _loginID = intent.getStringExtra("EMAIL_ID");
        _clientid = intent.getStringExtra("CLIENT_ID");

        System.out.println("TASK LIST PAGE PROGRAMME_ID :" + _programmeID + " CLIENT_ID : " + _clientid);
        _utfValue = _programmeID;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        initNavigationDrawer();

        System.out.println("image value in task list page: " + Imageclass.bitmapImageValue);

    }

    public int TaskTimerMethod() {

        if (Common.timerFinishFlag == 1) {

            AlertDialog.Builder alertReturnDateObject = new AlertDialog.Builder(TaskList.this, R.style.MyAlertDialogStyle);
            alertReturnDateObject.setMessage("Time For Current Active Task Is Finished.Please Contact Administrator.");
            alertReturnDateObject.setCancelable(false);
            alertReturnDateObject.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                    Intent intentgobackobj = new Intent(TaskList.this, TrainingProgrammes.class);
                    intentgobackobj.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentgobackobj);
                }
            });
            AlertDialog alertLogin = alertReturnDateObject.create();
            // alertLogin.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertLogin.show();

            return 1;

        } else {
            return 0;
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
            Toast.makeText(TaskList.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:
                        Intent aboutusintent = new Intent(TaskList.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
                        //Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(TaskList.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(TaskList.this, TaskHistory.class);
                        //historyintent.putExtra("CLIENT_ID", _clientid);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(TaskList.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);
                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(TaskList.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;

                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(TaskList.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(TaskList.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID", _loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(TaskList.this, MainActivity.class);
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
        _imageView3 = (ImageView) header.findViewById(R.id.imageView3);
        tv_email.setText(haredpreferences.getString("first_name", "Null") + " " + haredpreferences.getString("last_name", "Null"));
        _clientid = haredpreferences.getString("CLIENT_ID", "Null");
        _preview_role = haredpreferences.getString("preview_role", "Null");

        // System.out.println("tasklist shared prefernce" + _clientid);
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

    private void receiveGetRequest(View v) {

        new GetData(this).execute();
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

//            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
//
//            // setting list adapter
//            expListView.setAdapter(listAdapter);
//            expListView.expandGroup(0);

            final AlertDialog.Builder goLogin = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            //System.out.println("result output" + responseOutput.toString());

            CustomTaskList customList = new CustomTaskList(TaskList.this, task_name, task_status, task_description, task_id, _programmeID);

            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(customList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                  //  if (_preview_role.toString().equals("admin")) {
                    progress.show();

                   // } else {

                        if (task_status.get(i).equals("active")) {

                            SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                            SharedPreferences.Editor sharedEditor = haredpreferences.edit();

                            sharedEditor.putString("LOGIN_PROGRAMME_ID", _programmeID.toString());
                            sharedEditor.putString("LOGIN_TASK_ID", task_id.get(i).toString());
                            sharedEditor.putString("LOGIN_TASK_STATUS", task_status.get(i).toString());
                            sharedEditor.putString("LOGIN_TASK_NAME", task_name.get(i).toString());
                            sharedEditor.commit();

                            // initNavigationDrawer()

                        /* flag to make 0 for each active task so that in a session for every single task, expire msg is shown only once. */

                            Common.timerCreateObjFlag = 0;

                            Intent nextScreen3 = new Intent(getApplicationContext(), TaskListDescription.class);
                            nextScreen3.putExtra("TASKSTATUS", task_status.get(i));
                            nextScreen3.putExtra("TASKDESCRIPTION", task_description.get(i));
                            nextScreen3.putExtra("TASK_ID", task_id.get(i));
                            nextScreen3.putExtra("PROGRAMME_ID", _programmeID);

                            nextScreen3.putExtra("TASKNAME", task_name.get(i));
                            nextScreen3.putExtra("QUESTION_1", Questions_1.get(i));
                            nextScreen3.putExtra("QUESTION_2", Questions_2.get(i));

                            // System.out.println("TASK LIST PAGE TO DESC PAGE TASKNAME =" + task_name.get(i) + " TASK_ID = " + task_id.get(i) + " PROGRAMME_ID =" + _programmeID);
                            // System.out.println("days, hrs, min and sec are: " + task_days + "  " + task_hours + "  " + task_minutes + "  " + task_seconds);
                            startActivity(nextScreen3);
                            progress.dismiss();

                        } else if (task_status.get(i).equals("inactive")) {

                            AlertDialog.Builder taskstatus = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                            taskstatus.setMessage("Sorry, only one task allowed at a time.");
                            taskstatus.setCancelable(false);
                            taskstatus.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertLogin = taskstatus.create();
                            progress.dismiss();
                            alertLogin.show();


                        } else if (task_status.get(i).equals("complete")) {

                            Intent intent_completed = new Intent(getApplicationContext(), TaskListDescriptionCompleted.class);

                            intent_completed.putExtra("TASKNAME", task_name.get(i));
                            intent_completed.putExtra("TASKSTATUS", task_status.get(i));
                            intent_completed.putExtra("TASKDESCRIPTION", task_description.get(i));
                            intent_completed.putExtra("TASK_ID", task_id.get(i));
                            intent_completed.putExtra("PROGRAMME_ID", _programmeID);
                            startActivity(intent_completed);
                            progress.dismiss();

                        } else if (task_status.get(i).equals("expired")) {

                            AlertDialog.Builder taskStatusExpired = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                            taskStatusExpired.setMessage("Your Task Is Expired. Please Contact Administrator.");
                            taskStatusExpired.setCancelable(false);
                            taskStatusExpired.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertLogin = taskStatusExpired.create();
                            progress.dismiss();
                            alertLogin.show();
                        } else if (task_status.get(i).equals("preview")) {

                        Intent intent_completed = new Intent(getApplicationContext(), TaskListDescriptionCompleted.class);

                        intent_completed.putExtra("TASKNAME", task_name.get(i));
                        intent_completed.putExtra("TASKSTATUS", task_status.get(i));
                        intent_completed.putExtra("TASKDESCRIPTION", task_description.get(i));
                        intent_completed.putExtra("TASK_ID", task_id.get(i));
                        intent_completed.putExtra("PROGRAMME_ID", _programmeID);
                        startActivity(intent_completed);
                            progress.dismiss();
                    }

                    //   }
                }
            });


            /******************************* END OF TASK LIST CLASS ********************************/

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

            try {

                // final EditText txtEmail = (EditText) findViewById(R.id.editText);
                //final EditText txtPassword = (EditText) findViewById(R.id.editText2);

                URL url = null;
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

                TaskList.this.runOnUiThread(new Runnable() {

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

                        resultStatus = taskRoot.status;
                        resultOutputMsg = taskRoot.message;
                        AlertDialog.Builder goLogin = new AlertDialog.Builder(TaskList.this, R.style.MyAlertDialogStyle);
                        // System.out.println("result output" + resultOutput);

                        // if (resultOutput.contains("User already registered.")) {

                        if (resultStatus == 0) {

                            goLogin.setMessage(resultOutputMsg);
                            goLogin.setCancelable(false);
                            goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    Intent intentgobackobj = new Intent(TaskList.this, TrainingProgrammes.class);
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
//                            try {
//                                json2 = new JSONObject(resultOutput.toString());
//                                //resultOutputMedia = json2.getJSONArray("Media");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
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
//                                    if (media_type.equals("pdf")) {
//
//                                        MediaGetClass.media_pdf_url.add(fetchjsonData1.get(j).media);
//                                        MediaGetClass.media_pdf_name.add(fetchjsonData1.get(j).name);
//                                    }
                                    }
                                }
                                System.out.println("media counter value is : " + fetchjsonData1.size());
                            }
                            System.out.println("DESCRIPTION array data is:" + task_description.get(0) + "\n");

                            progress.dismiss();
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(context, "Got message: on received message popup", Toast.LENGTH_LONG).show();

            AlertDialog.Builder alertReturnDateObject = new AlertDialog.Builder(TaskList.this, R.style.MyAlertDialogStyle);

            alertReturnDateObject.setTitle("");
            alertReturnDateObject.setMessage("Time For This Task Is Finished. Please Contact Administrator.");
            alertReturnDateObject.setCancelable(false);
            alertReturnDateObject.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                    Intent intentgobackobj = new Intent(TaskList.this, TrainingProgrammes.class);
                    intentgobackobj.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentgobackobj);
                }
            });
            AlertDialog alertLogin = alertReturnDateObject.create();
            alertLogin.show();

            Button bq = alertLogin.getButton(DialogInterface.BUTTON_POSITIVE);
            bq.setTextColor(Color.BLACK);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        AppCompatIntermediateActivity.taskTimerFlag = 0;
        this.registerReceiver(mMessageReceiver, new IntentFilter("MyBroadcast"));

    }

    // unregister receiver on pause

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        try {
            this.unregisterReceiver(mMessageReceiver);

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }
}
