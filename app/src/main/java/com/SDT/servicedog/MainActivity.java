package com.SDT.servicedog;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements View.OnClickListener {

    public static String baseApiUrl = "";
    public static

    Button loginbtn;
    Button registerbtn;
    Button fpbtn;
    ProgressDialog progress, dialogObject;
    StringBuilder responseOutput;
    String resultOutput;
    String resultOutputMsg;
    String clientId;
    String firstName;
    String lastName, phone_number, email_id, profile_picture, dnd_from, dnd_to;
    Intent intent;

    String em_tel_contact, state, city, postcode, any_disability, do_u_have_dog, dog_name, breed, dog_age, sex_of_dog, desexed, dog_training_history, current_task_status,preview_role;
    Bitmap bitmapImageValue;

    String loginProgrammeID, loginTaskID, loginTaskStatus, loginTaskName;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    private EditText txtloginEmail, txtloginPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtloginEmail = (EditText) findViewById(R.id.editText);
        txtloginPwd = (EditText) findViewById(R.id.editText2);

        loginbtn = (Button) findViewById(R.id.button1);
        registerbtn = (Button) findViewById(R.id.register);
        fpbtn = (Button) findViewById(R.id.button2);

        loginbtn.setOnClickListener(this);
        registerbtn.setOnClickListener(this); // calling onClick() method
        fpbtn.setOnClickListener(this);

        SharedPreferences loginpreference = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String lastLoginId = loginpreference.getString("email_id", "");
        txtloginEmail.setText(lastLoginId);
//        txtloginPwd.setText("123456789");

        Log.i("previous email id: ", lastLoginId);
        boolean result = Utility.checkPermission(MainActivity.this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button1:

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                if (!txtloginEmail.getText().toString().equals("") && !txtloginPwd.getText().toString().equals("")) {

                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    android.net.NetworkInfo wifi = cm
                            .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    android.net.NetworkInfo datac = cm
                            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {
                        // setContentView(R.layout.activity_main);

                        receiveGetRequest(v);
                    } else {
                        //no connection
                        Toast toast = Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
                        toast.show();
                        //startActivity(new Intent(this, MainActivity.class));
                    }

                } else if (!isValidEmail(txtloginEmail.getText().toString())) {
                    txtloginEmail.setError("Invalid Email");
                } else if (!isValidPassword(txtloginPwd.getText().toString())) {
                    txtloginPwd.setError("Invalid Password");
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter the field", Toast.LENGTH_LONG).show();
                }

                //   Intent activityChangeIntent = new Intent(MainActivity.this, TrainingProgrammes.class);
                //MainActivity.this.startActivity(activityChangeIntent);
                //Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(nextScreen);
                break;
            case R.id.register:

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {

                    Intent nextScreen2 = new Intent(getApplicationContext(), Register.class);
                    startActivity(nextScreen2);

                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
                    toast.show();

                }
                break;

            case R.id.button2:
                Intent nextScreen3 = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(nextScreen3);
                break;


            default:
                break;
        }
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

    private boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() > 6;
    }

    private void receiveGetRequest(View v) {

        new GetData(this).execute();
        // new SetImageData(this).execute();
    }

    private class GetData extends AsyncTask<String, Void, Void> {

        private final Context context;

        public GetData(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context, R.style.dialog);
            progress.setMessage("Loading");
            // progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            AlertDialog.Builder goLogin = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            System.out.println("result output" + resultOutput);

            // SetImageData();
            //if (resultOutput.contains("Invalid email or password. Please try again")) {

            if (resultOutput.equals("0")) {

                goLogin.setMessage(resultOutputMsg);
                goLogin.setCancelable(false);
                goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = goLogin.create();
                alertLogin.show();

                TextView textView = (TextView) alertLogin.findViewById(android.R.id.message);
                Typeface face = Typeface.SANS_SERIF;
                textView.setTypeface(face);

                Button bq = alertLogin.getButton(DialogInterface.BUTTON_POSITIVE);
                bq.setTextColor(Color.BLACK);
            }
            // else if (resultOutput.contains("Authorized User.")) {

            else if (resultOutput.equals("1")) {

//                dialogObject = new ProgressDialog(MainActivity.this);
//                dialogObject.setMessage("Loading Please wait...");
//                dialogObject.show();

                SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedEditor = haredpreferences.edit();
                sharedEditor.putString("first_name", firstName);
                sharedEditor.putString("last_name", lastName);
                sharedEditor.putString("CLIENT_ID", clientId);
                sharedEditor.putString("phone_number", phone_number);
                sharedEditor.putString("email_id", email_id);
                sharedEditor.putString("profile_pic", profile_picture);
                sharedEditor.putString("password", txtloginPwd.toString());
                sharedEditor.putString("dnd_from", dnd_from.toString());
                sharedEditor.putString("dnd_to", dnd_to.toString());


                // new registration fields

                sharedEditor.putString("em_tel_contact", em_tel_contact.toString());
                sharedEditor.putString("state", state.toString());
                sharedEditor.putString("city", city.toString());
                sharedEditor.putString("postcode", postcode.toString());
                sharedEditor.putString("any_disability", any_disability.toString());
                sharedEditor.putString("do_u_have_dog", do_u_have_dog.toString());
                sharedEditor.putString("dog_name", dog_name.toString());
                sharedEditor.putString("breed", breed.toString());
                sharedEditor.putString("dog_age", dog_age.toString());
                sharedEditor.putString("sex_of_dog", sex_of_dog.toString());
                sharedEditor.putString("desexed", desexed.toString());
                sharedEditor.putString("dog_training_history", dog_training_history.toString());
                sharedEditor.putString("preview_role", preview_role.toString());

                sharedEditor.putString("LOGIN_PROGRAMME_ID", loginProgrammeID.toString());
                sharedEditor.putString("LOGIN_TASK_ID", loginTaskID.toString());
                sharedEditor.putString("LOGIN_TASK_STATUS", loginTaskStatus.toString());
                sharedEditor.putString("LOGIN_TASK_NAME", loginTaskName.toString());

                sharedEditor.commit();

                System.out.println("main activity client_id" + clientId);

                txtloginEmail.setText("");
                txtloginPwd.setText("");
                txtloginEmail.setCursorVisible(false);
                txtloginPwd.setCursorVisible(false);

                goLogin.setMessage(resultOutputMsg);
                goLogin.setCancelable(false);
                goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {


                        dialogObject = new ProgressDialog(context, R.style.dialog);
                        dialogObject.setMessage("Loading Please wait...");
                        dialogObject.setIndeterminate(false);
                        dialogObject.setCancelable(false);
                        dialog.cancel();
                        dialogObject.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // do the thing that takes a long time

                                intent = new Intent(getApplicationContext(), UserMenuActivity.class);
                                intent.putExtra("EMAIL_ID", txtloginEmail.getText().toString());
                                intent.putExtra("CLIENT_ID", clientId.toString());
                                startActivity(intent);
                                dialogObject.dismiss();
                            }
                        }).start();

//                      if(current_task_status.toString().equals("") || current_task_status.toString().equals(null)){


                    }
                });
                AlertDialog alertLogin = goLogin.create();
                alertLogin.show();

                TextView textView = (TextView) alertLogin.findViewById(android.R.id.message);
                Typeface face = Typeface.SANS_SERIF;
                textView.setTypeface(face);

                Button bq = alertLogin.getButton(DialogInterface.BUTTON_POSITIVE);
                bq.setTextColor(Color.BLACK);

                //Intent activityChangeIntent = new Intent(MainActivity.this, TrainingProgrammes.class);
                // MainActivity.this.startActivity(activityChangeIntent);
                Imageclass.image_getdata_flag = 0;

            } else if (resultOutput.equals("")) {

                goLogin.setMessage("please Try again.");
                goLogin.setCancelable(false);
                goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = goLogin.create();
                alertLogin.show();

                TextView textView = (TextView) alertLogin.findViewById(android.R.id.message);
                Typeface face = Typeface.SANS_SERIF;
                textView.setTypeface(face);

                Button bq = alertLogin.getButton(DialogInterface.BUTTON_POSITIVE);
                bq.setTextColor(Color.BLACK);
            }
            // receiveGetImage();

            // captureImageView.setImageBitmap(bitmapImageValue);
            progress.dismiss();
//            dialogObject.dismiss();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                final EditText txtEmail = (EditText) findViewById(R.id.editText);
                final EditText txtPassword = (EditText) findViewById(R.id.editText2);

                URL url = null;
                try {
                    // url = new URL("http://servdog.dealopia.com/users/clientAuth/?email=brijesh@gmail.com&password=m2n1shlko");

                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/users/clientAuth/?api_key="+ActivityIntermediateClass.apiKeyValue+"&email=" + txtEmail.getText().toString() +
                            "&password=" + txtPassword.getText().toString());

                    System.out.println("Login API url is : " + url);

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

                int responseCode = connection.getResponseCode();

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

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        System.out.println("Login API Response is : " + responseOutput.toString());

                        JSONObject json = null;
                        JSONObject fetchClientId = null, fetchProgrammeDetails = null;
                        try {
                            json = new JSONObject(responseOutput.toString());
                            resultOutput = json.getString("status");
                            resultOutputMsg = json.getString("message");
                            fetchClientId = new JSONObject(json.getString("Auth").toString());
                            System.out.println("required data is=" + fetchClientId);
                            for (int i = 0; i < fetchClientId.length(); i++) {

                                clientId = fetchClientId.getString("id");
                                firstName = fetchClientId.getString("firstname");
                                lastName = fetchClientId.getString("lastname");
                                phone_number = fetchClientId.getString("phone");
                                email_id = fetchClientId.getString("email");
                                profile_picture = fetchClientId.getString("profile_pic");
                                dnd_from = fetchClientId.getString("dnd_from");
                                dnd_to = fetchClientId.getString("dnd_to");


                                // registration new fields
                                em_tel_contact = fetchClientId.getString("em_tel_contact");
                                state = fetchClientId.getString("state");
                                city = fetchClientId.getString("city");
                                postcode = fetchClientId.getString("postcode");
                                any_disability = fetchClientId.getString("any_disability");
                                do_u_have_dog = fetchClientId.getString("do_u_have_dog");
                                dog_name = fetchClientId.getString("dog_name");
                                breed = fetchClientId.getString("breed");
                                dog_age = fetchClientId.getString("dog_age");
                                sex_of_dog = fetchClientId.getString("sex_of_dog");
                                desexed = fetchClientId.getString("desexed");
                                dog_training_history = fetchClientId.getString("dog_training_history");
                                preview_role = fetchClientId.getString("preview_role");

                            }
                            System.out.println("status msg and status message" + resultOutput + resultOutputMsg);
                            System.out.println("fetchClientId" + clientId);
                            System.out.println("first_name  and last_name" + firstName + lastName);

                            fetchProgrammeDetails = new JSONObject(json.getString("task").toString());

                            System.out.println("required data is=" + fetchProgrammeDetails);
                            //  for (int i = 0; i < fetchProgrammeDetails.length(); i++) {

                            loginProgrammeID = fetchProgrammeDetails.getString("program_id");
                            loginTaskID = fetchProgrammeDetails.getString("task_id");
                            loginTaskStatus = fetchProgrammeDetails.getString("status");

//                              if(!loginTaskStatus.toString().equals("subscribe")){
//
//                                  loginTaskName = fetchProgrammeDetails.getString("task_name");
//                              }

                            if (fetchProgrammeDetails.has("task_name")) {

                                loginTaskName = fetchProgrammeDetails.getString("task_name");

                            } else {

                                loginTaskName = "null";
                            }

                            //  }

                            System.out.println("Login loginProgrammeID,loginTaskID,loginTaskStatus, loginTaskName : \n" + loginProgrammeID.toString() + "\n" + loginTaskID + "\n" + loginTaskStatus + "\n" + loginTaskName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // progress.dismiss();
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
