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
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class ChangePassword extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private EditText txtOldPswd, txtPswd, txtCnfPswd;
    private Button changePswdButton;
    ProgressDialog progress;
    JSONArray resultOutput;
    String fetchClientID;
    StringBuilder responseOutput;
    String _programmeID;
    String _loginID, pswd_msg, pswd_error_string, CnfrmPswd_error_msg, OldPswd_error_msg, PswdLength_error_msg;
    int pswd_status;
    String first_name, last_name;
    ArrayList<String> task_list = new ArrayList<String>();
    public static String _utfValue = "";

    private View header;
    String TaskListDescclientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);

        System.out.println("/******************************* CHANGE PASSWORD CLASS STARTED ************************/");

        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        fetchClientID = haredpreferences.getString("CLIENT_ID", "Null");

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();

        txtOldPswd = (EditText) findViewById(R.id.oldPswd);
        txtPswd = (EditText) findViewById(R.id.newPswd);
        txtCnfPswd = (EditText) findViewById(R.id.cnfPswd);
        changePswdButton = (Button) findViewById(R.id.changepswdBtn);

        _utfValue = _programmeID;


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initNavigationDrawer();

        changePswdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {
                    // setContentView(R.layout.activity_main);
                    if (!txtOldPswd.getText().toString().equals("") && !txtPswd.getText().toString().equals("") && !txtCnfPswd.getText().toString().equals("")) {

                        new GetData(ChangePassword.this).execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //no connection
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(this, MainActivity.class));
                }
            }
        });
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:
                        Intent aboutusintent = new Intent(ChangePassword.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
//                        Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(ChangePassword.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(ChangePassword.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(ChangePassword.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(ChangePassword.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;
                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(ChangePassword.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(ChangePassword.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID",_loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(ChangePassword.this, MainActivity.class);
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

    private void receiveGetRequest(View v) {

        new GetData(this).execute();
    }

    private class GetData extends AsyncTask<String, Void, Void> {

        private final Context context;
        StringBuilder errorMessageChngPswrd = new StringBuilder(100);

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

            Intent intent = getIntent();

            _programmeID = intent.getStringExtra("PROGRAMME_ID");
            _loginID = intent.getStringExtra("EMAIL_ID");
            first_name = intent.getStringExtra("FIRSTNAME");
            last_name = intent.getStringExtra("LASTNAME");
            System.out.println("client_id and client_email_id and firstname & lastname" + fetchClientID + _programmeID + _loginID + first_name + last_name);

            AlertDialog.Builder goLogin = new AlertDialog.Builder(ChangePassword.this, R.style.MyAlertDialogStyle);
            if (pswd_status == 0) {

                goLogin.setMessage(errorMessageChngPswrd);
                goLogin.setCancelable(false);
                goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = goLogin.create();
                alertLogin.show();
                progress.hide();
            }
            //else if (resultOutput.contains("Registration Successful")) {

            else if (pswd_status == 1) {

                txtOldPswd.setText("");
                txtPswd.setText("");
                txtCnfPswd.setText("");

                goLogin.setMessage(pswd_msg);
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
            pswd_error_string = null;
            CnfrmPswd_error_msg = null;
            OldPswd_error_msg = null;
            PswdLength_error_msg = null;

        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                // final EditText txtEmail = (EditText) findViewById(R.id.editText);
                //final EditText txtPassword = (EditText) findViewById(R.id.editText2);

                URL url = null;
                try {
                    // url = new URL("http://servdog.dealopia.com/users/clientAuth/?email=brijesh@gmail.com&password=m2n1shlko");
                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/users/changePassword/?api_key="+ActivityIntermediateClass.apiKeyValue+"&client_id=" + fetchClientID + "&old_password=" + txtOldPswd.getText().toString() +
                            "&password_update=" + txtPswd.getText().toString() + "&password_confirm_update=" + txtCnfPswd.getText().toString());

                    System.out.println("change password url is : " + url.toString());

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
                    connection.setRequestMethod("POST");
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

                ChangePassword.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        System.out.println("changePswd output===============" + responseOutput.toString());

                        JSONObject json = null, json1 = null;
                        try {
                            json = new JSONObject(responseOutput.toString());
                            pswd_status = (int) json.get("status");
                            pswd_msg = json.getString("message");


                            if (pswd_status == 0) {

                                pswd_error_string = json.getString("error");

                                try {

                                    if (!pswd_error_string.toString().equals("")) {

                                        json1 = new JSONObject(pswd_error_string.toString());

                                        try {

                                            if (json1.getJSONArray("password_confirm_update") != null) {

                                                JSONArray CnfrmPswd_error = json1.getJSONArray("password_confirm_update");
                                                CnfrmPswd_error_msg = CnfrmPswd_error.get(0).toString();

//                                            CnfrmPswd_error_msg = json1.getString("password_confirm_update");
                                                errorMessageChngPswrd.append(CnfrmPswd_error_msg);
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json1.getJSONArray("password") != null) {

                                                JSONArray PswdLength_error = json1.getJSONArray("password");
                                                PswdLength_error_msg = PswdLength_error.get(0).toString();
                                                errorMessageChngPswrd.append(PswdLength_error_msg);

                                            } else if (json1.getJSONArray("password_update") != null) {

                                                JSONArray PswdLength_error = json1.getJSONArray("password_update");
                                                PswdLength_error_msg = PswdLength_error.get(0).toString();
                                                errorMessageChngPswrd.append(PswdLength_error_msg);
                                            } else {

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } else {

                                        errorMessageChngPswrd.append(pswd_msg.toString());

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
