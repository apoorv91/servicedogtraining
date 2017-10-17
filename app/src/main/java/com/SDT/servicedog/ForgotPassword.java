package com.SDT.servicedog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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


/**
 * Created by laitkor3 on 01/07/16.
 */
public class ForgotPassword extends Activity {

    Button btnfp;
    ProgressDialog progress;
    StringBuilder responseOutput;
    private EditText txtEmailFP;
    String resultOutput, resultOutputMsg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);


        System.out.println("/******************************* RESET PASSWORD CLASS STARTED ************************/");

        txtEmailFP = (EditText) findViewById(R.id.editText);

        btnfp = (Button) findViewById(R.id.fp);
        btnfp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Perform action on click

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                ConnectivityManager cm_object = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi_connect = cm_object.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo data_connect = cm_object.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if ((wifi_connect != null & data_connect != null) && (wifi_connect.isConnected() | data_connect.isConnected())) {

                    if (!txtEmailFP.getText().toString().equals("")) {
                        sendPostRequest(v);
                    } else if (!isValidEmail(txtEmailFP.getText().toString())) {
                        txtEmailFP.setError("Invalid Email");
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter the field", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean isValidEmail(String email) {

                String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(email);
                return matcher.matches();

            }
        });
    }

    public void sendPostRequest(View View) {
        new PostClass(this).execute();
    }

    private class PostClass extends AsyncTask<String, Void, Void> {

        private final Context context;

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

            AlertDialog.Builder goLogin = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            System.out.println("result output" + resultOutput);

            // if (resultOutput.contains("Email don't exist. please check your email id")) {

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
            // else if (resultOutput.contains("Your password has been changed")) {

            if (resultOutput.equals("1")) {

                goLogin.setMessage(resultOutputMsg);
                goLogin.setCancelable(false);
                goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent activityChangeIntent = new Intent(ForgotPassword.this, MainActivity.class);
                        activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ForgotPassword.this.startActivity(activityChangeIntent);
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
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                final EditText txtEmail = (EditText) findViewById(R.id.editText);

                URL url = null;
                try {
                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/users/forgetpassword/?api_key="+ActivityIntermediateClass.apiKeyValue+"&email=" + txtEmail.getText().toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Log.i("forget password url is^^^ : ", url.toString());

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
                int responseCode = connection.getResponseCode();

                final StringBuilder output = new StringBuilder("Request URL " + url);
                // output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                // output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                // output.append(System.getProperty("line.separator") + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                System.out.println("reset page json response is :" + responseOutput.toString());

                ForgotPassword.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

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



