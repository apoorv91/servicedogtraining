package com.SDT.servicedog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.SDT.servicedog.TaskReminderCollectionData.ReminderRootObject;

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


public class TaskReminders extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ListView listView;
    ProgressDialog progress;
    JSONArray resultOutput;
    String resultOutputMsg;
    int resultStatus;
    String rem_clientid,_loginID;
    View header;
    StringBuilder responseOutput;
    ArrayList<String> task_reminder_name = new ArrayList<String>();
    ArrayList<String> task_reminder_description = new ArrayList<String>();
    SharedPreferences haredpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskreminders);

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();

        System.out.println("/**********************TASK REMINDER CLASS *************************************/");

        haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        rem_clientid = haredpreferences.getString("CLIENT_ID", "Null");
        _loginID = haredpreferences.getString("email_id", "Null");
        System.out.println("client id on task reminder page is : " + rem_clientid);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        initNavigationDrawer();
    }

    public void initNavigationDrawer() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {
            // setContentView(R.layout.activity_main);
            new GetData(this).execute();
        } else {
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
            //startActivity(new Intent(this, MainActivity.class));
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:
                        Intent aboutusintent = new Intent(TaskReminders.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(TaskReminders.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(TaskReminders.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();

//                        Intent newintent = new Intent(TaskReminders.this, TaskReminders.class);
//                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(newintent);


                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(TaskReminders.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;

                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(TaskReminders.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(TaskReminders.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID",_loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(TaskReminders.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish(); // Call once you redirect to another activity

                }
                return true;
            }
        });

        header = navigationView.getHeaderView(0);

        TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
        tv_email.setText(haredpreferences.getString("first_name", "Null") + " " + haredpreferences.getString("last_name", "Null"));

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

            CustomTaskReminders customList = new CustomTaskReminders(TaskReminders.this, task_reminder_name);

            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(customList);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Toast.makeText(getApplicationContext(), "You Clicked " + task_list.get(i), Toast.LENGTH_SHORT).show();
                }
            });

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
                URL url = null;
                try {

                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/tasks/taskReminder/?api_key="+ActivityIntermediateClass.apiKeyValue+"&client_id=" + rem_clientid.toString());
                    Log.i("API url is :", url.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

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

                TaskReminders.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        System.out.println("response output===============" + responseOutput.toString());


                        ArrayList<String> programlist = new ArrayList<String>();
                        JSONObject json = null;

                        Gson gson = new Gson();

                        ReminderRootObject joc = gson.fromJson(responseOutput.toString(), ReminderRootObject.class);
                        // System.out.println(joc.data.get(0).status);
                        // System.out.println(joc.data.get(0).program.Program.name);
                        resultStatus = joc.status;
                        resultOutputMsg = joc.message;
                        AlertDialog.Builder goLogin = new AlertDialog.Builder(TaskReminders.this, R.style.MyAlertDialogStyle);
                        System.out.println("result output" + resultOutput);

                        // if (resultOutput.contains("User already registered.")) {

                        if (resultStatus == 0) {

                            goLogin.setMessage(resultOutputMsg);
                            goLogin.setCancelable(false);
                            goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
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
                            System.out.println("the output" + resultOutput);
                            for (int i = 0; i < resultOutput.length(); i++) {

                                task_reminder_name.add(joc.data.get(i).Task.Task.title);
                                task_reminder_description.add(joc.data.get(i).Task.Task.description);


                            }
                            System.out.println(" status message and status and task list history and task_history_description"
                                    + task_reminder_name + "\n" + task_reminder_description + "\n" + resultOutputMsg + "\n" + resultStatus);
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
}
