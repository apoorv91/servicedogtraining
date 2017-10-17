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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.SDT.servicedog.TaskHistoryCollectionData.HistoryRootObject;

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


public class TaskHistory extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    ProgressDialog progress;
    JSONArray resultOutput;
    private ListView listView;
    String hist_clientid;
    String resultOutputMsg;
    int resultStatus;
    View header;

    StringBuilder responseOutput;
    String Client_ID, _loginID;
    String first_name, last_name;
    ArrayList<String> task_history_name = new ArrayList<String>();
    ArrayList<String> task_history_description = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskhistory);


        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();

        System.out.println("***************************************** TASK HISTORY CLASS ********************************");

        /***************************************** TASK HISTORY CLASS ********************************/

        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        Client_ID = haredpreferences.getString("CLIENT_ID", "Null");
        _loginID = haredpreferences.getString("email_id", "Null");
        System.out.println("client_id: " + Client_ID);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        initNavigationDrawer();

        // code for countdown timer for no activity logout.

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
            Toast.makeText(TaskHistory.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:
                        Intent aboutusintent = new Intent(TaskHistory.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
                        //Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(TaskHistory.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();

//                        Intent historyintent = new Intent(TaskHistory.this, TaskHistory.class);
//                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(TaskHistory.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);


                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(TaskHistory.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;

                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(TaskHistory.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(TaskHistory.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID", _loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(TaskHistory.this, MainActivity.class);
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

        hist_clientid = haredpreferences.getString("CLIENT_ID", "Null");

        System.out.println("tasklist shared prefernce" + hist_clientid);

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

            AlertDialog.Builder goLogin = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            System.out.println("result output" + responseOutput.toString());

            CustomTaskHistory customList = new CustomTaskHistory(TaskHistory.this, task_history_name);

            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(customList);

           /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent nextScreen2 = new Intent(getApplicationContext(), SubscribeProgrammes.class);
                    nextScreen2.putExtra("CLIENT_ID", _clientID);
                    nextScreen2.putExtra("REFERENCE_ID", reference_IDs.get(i));
                    startActivity(nextScreen2);

                    Toast.makeText(getApplicationContext(), "You Clicked " + task_list.get(i), Toast.LENGTH_SHORT).show();
                }
            });*/

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


            /*************************************** TASK history doinBackground **************************************/
            try {

                URL url = null;
                try {
                    // url = new URL("http://servdog.dealopia.com/users/clientAuth/?email=brijesh@gmail.com&password=m2n1shlko");
                    // url = new URL("http://servdog.dealopia.com/programs/tasks/?api_key=uXO39sJA8JU7OOPrLZkxmR183larMagL&id="+Client_ID);
                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/feedbacks/history/?api_key="+ActivityIntermediateClass.apiKeyValue+"&client_id=" + Client_ID.toString());

                    Log.i("TASK HISTORY URL :", url.toString());

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

                TaskHistory.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        System.out.println("============response output===============" + responseOutput.toString());


                        ArrayList<String> programlist = new ArrayList<String>();
                        JSONObject json = null;

                        Gson gson = new Gson();
//
                        HistoryRootObject joc = gson.fromJson(responseOutput.toString(), HistoryRootObject.class);
                        resultStatus = joc.status;
                        resultOutputMsg = joc.message;
                        AlertDialog.Builder goLogin = new AlertDialog.Builder(TaskHistory.this, R.style.MyAlertDialogStyle);
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

                                task_history_name.add(joc.data.get(i).Feedback.Task.title);
                                task_history_description.add(joc.data.get(i).Feedback.Task.description);


                            }
                            System.out.println("task list history and task_history_description"
                                    + task_history_name + "\n" + task_history_description);


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
