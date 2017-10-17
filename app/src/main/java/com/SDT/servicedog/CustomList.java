package com.SDT.servicedog;

/**
 * Created by laitkor3 on 07/07/16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class CustomList extends ArrayAdapter<String> {

    private Activity context;
    ArrayList<String> listProgrms = new ArrayList<String>();
    ArrayList<String> ref_ids = new ArrayList<String>();
    ArrayList<String> programStatus_ = new ArrayList<String>();
    ArrayList<String> _tierLevel = new ArrayList<String>();
    ArrayList<Integer> _tierIndexValue = new ArrayList<Integer>();
    ArrayList<String> _programIDs = new ArrayList<String>();

    String fetchRefID;
    StringBuilder responseOutput;
    String resultOutputMsg;
    TextView imageText;
//    Integer counter = 0;


    public String get_clientID() {
        return _clientID;
    }

    public void set_clientID(String _clientID) {
        this._clientID = _clientID;
    }

    String _clientID = TrainingProgrammes._utfValue;
    ProgressDialog progress;
    String resultOutput;

    public String getFetchRefID() {
        return fetchRefID;
    }

    public void setFetchRefID(String fetchRefID) {
        this.fetchRefID = fetchRefID;
    }

    public CustomList(Activity context, ArrayList<String> listProgrms, ArrayList<String> ref_ids, ArrayList<String> programStatus_, ArrayList<String> _tierLevel, ArrayList<Integer> _tierIndexValue, ArrayList<String> _programIDs) {
        super(context, R.layout.content_main, listProgrms);
        this.context = context;
        this.listProgrms = listProgrms;
        this.ref_ids = ref_ids;
        this.programStatus_ = programStatus_;
        this._tierLevel = _tierLevel;
        this._tierIndexValue = _tierIndexValue;
        this._programIDs = _programIDs;

        System.out.println("/*************************** Custom list constructor page Starts *******************************/");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.listdata, null, true);
        TextView imageText = (TextView) listViewItem.findViewById(R.id.imageText);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        LinearLayout linearLayout = (LinearLayout) listViewItem.findViewById(R.id.firstLayout);
        final Button subscribebtn = (Button) listViewItem.findViewById(R.id.subscribeBtn);
        subscribebtn.setTag(position);

//        subscribebtn.setBackgroundResource(R.drawable.gray_subs);
        if (convertView == null) {

            if (position % 2 != 0) {

                linearLayout.setBackgroundResource(R.color.white);
                subscribebtn.setBackgroundResource(R.drawable.gray_subs);

            } else {

                linearLayout.setBackgroundResource(R.color.sky_blue);
                subscribebtn.setBackgroundResource(R.drawable.sky_subs);


            }

            if (_tierLevel.contains(listProgrms.get(position))) {

                // count = 1;
                textViewName.setText(listProgrms.get(position).toString().toUpperCase());
                textViewName.setTextColor(Color.parseColor("#FFFFFF"));
                subscribebtn.setVisibility(View.GONE);
                textViewName.setGravity(Gravity.START);
                linearLayout.setBackgroundResource(R.color.colorPrimary);
                linearLayout.setPadding(16, 10, 10, 16);
                imageText.setVisibility(View.GONE);
                linearLayout.setClickable(false);

            } else {


                if (position % 2 != 0) {

                    imageText.setBackgroundResource(R.drawable.gray_circle);

                } else {

                    imageText.setBackgroundResource(R.drawable.sky_circle);

//                    imageText.setBackgroundResource(R.drawable.emoji_bg);
//                    imageText.setTextColor(Color.WHITE);

                }
                imageText.setText(_tierIndexValue.get(position).toString());
                textViewName.setText(listProgrms.get(position));
                fetchRefID = ref_ids.get(position);
                subscribebtn.setText(programStatus_.get(position));

                subscribebtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // if(subscribebtn.getTag().equals(0)) {

                        if (programStatus_.get(position).equals("Subscribe")) {
                            fetchRefID = ref_ids.get((Integer) subscribebtn.getTag());
                            subscribebtn.setText("Subscribed");
                            programStatus_.set(position, "Subscribed");
                            sendToSomeActivity(_programIDs.get(position));
                        } else if (programStatus_.get(position).equals("Subscribed")) {
                            AlertDialog.Builder goLogin = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                            goLogin.setMessage("Oops! You are already subscribed to this programme. Just GO AHEAD with the tasks.");
                            goLogin.setCancelable(false);
                            goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertLogin = goLogin.create();
                            alertLogin.show();
                        } else {

                        }
                    }
                });
            }
        } else {

            if (position % 2 != 0) {

                linearLayout.setBackgroundResource(R.color.white);
                subscribebtn.setBackgroundResource(R.drawable.gray_subs);

            } else {

                linearLayout.setBackgroundResource(R.color.sky_blue);
                subscribebtn.setBackgroundResource(R.drawable.sky_subs);

            }

            if (_tierLevel.contains(listProgrms.get(position))) {

                // count = 1;

                textViewName.setText(listProgrms.get(position).toString().toUpperCase());
                textViewName.setTextColor(Color.WHITE);
                subscribebtn.setVisibility(View.GONE);
                textViewName.setGravity(Gravity.START);
                linearLayout.setBackgroundResource(R.color.colorPrimary);
                linearLayout.setPadding(16, 10, 10, 16);
                imageText.setVisibility(View.GONE);
                linearLayout.setClickable(false);


            } else {


                if (position % 2 != 0) {

                    imageText.setBackgroundResource(R.drawable.gray_circle);
                    //imageText.setText(Integer.toString(position));

                } else {

                    imageText.setBackgroundResource(R.drawable.sky_circle);

//                    imageText.setBackgroundResource(R.drawable.emoji_bg);
//                    imageText.setTextColor(Color.WHITE);
                }

                imageText.setText(_tierIndexValue.get(position).toString());
//                imageText.setText(String.valueOf(Math.abs(position - counter)));

                textViewName.setText(listProgrms.get(position));
                fetchRefID = ref_ids.get(position);
                subscribebtn.setText(programStatus_.get(position));

                subscribebtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // if(subscribebtn.getTag().equals(0)) {
                        if (programStatus_.get(position).equals("Subscribe")) {
                            fetchRefID = ref_ids.get((Integer) subscribebtn.getTag());
                            subscribebtn.setText("Subscribed");
                            programStatus_.set(position, "Subscribed");
//                            Toast.makeText(context,"message from if condition",Toast.LENGTH_SHORT).show();
                            sendToSomeActivity(_programIDs.get(position));
                        } else if (programStatus_.get(position).equals("Subscribed")) {
                            AlertDialog.Builder goLogin = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                            goLogin.setMessage("Oops..You are already subscribed to this program! Just GO AHEAD with tasks.");
                            goLogin.setCancelable(false);
                            goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertLogin = goLogin.create();
                            alertLogin.show();
                        } else {

                        }
                    }
                });
            }
        }

        return listViewItem;
    }

    public void sendToSomeActivity(String progrmaID) {

        new PostClass(progrmaID).execute();
    }

    private class PostClass extends AsyncTask<String, Void, Void> {

        String _programID;
        JSONObject json = null;

        public PostClass(String progrmaID) {

            this._programID = progrmaID;
        }

        protected void onPreExecute() {
            /*progress = new ProgressDialog();
            progress.setMessage("Loading");
            progress.show();*/
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            AlertDialog.Builder goLogin = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

            //if (resultOutput.contains("Alreay subscribed for this program.")) {
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
            }
            //else if (resultOutput.contains("Successfully subscribed.")) {
            if (resultOutput.equals("1")) {

                JSONObject fetchProgrammeDetails = null;
                String loginProgrammeID = null, loginTaskID = null, loginTaskStatus = null, loginTaskName = null;

                try {

                    fetchProgrammeDetails = new JSONObject(json.getString("task").toString());
                    System.out.println("required data is=" + fetchProgrammeDetails);
                    loginProgrammeID = fetchProgrammeDetails.getString("program_id");
                    loginTaskID = fetchProgrammeDetails.getString("task_id");
                    loginTaskStatus = fetchProgrammeDetails.getString("status");

                    if (fetchProgrammeDetails.has("task_name")) {
                        loginTaskName = fetchProgrammeDetails.getString("task_name");
                    } else {
                        loginTaskName = "";
                    }
                    System.out.println("program subscribed....Login loginProgrammeID,loginTaskID,loginTaskStatus, loginTaskName : \n" + loginProgrammeID.toString() + "\n" + loginTaskID + "\n" + loginTaskStatus + "\n" + loginTaskName);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                SharedPreferences haredpreferences = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor sharedEditor = haredpreferences.edit();
                sharedEditor.putString("LOGIN_PROGRAMME_ID", loginProgrammeID.toString());
                sharedEditor.putString("LOGIN_TASK_ID", loginTaskID.toString());
                sharedEditor.putString("LOGIN_TASK_STATUS", loginTaskStatus.toString());
                sharedEditor.putString("LOGIN_TASK_NAME", loginTaskName.toString());
                sharedEditor.commit();

              //  sharedEditor.putString("LOGIN_PROGRAMME_ID", _programID.toString());

                goLogin.setMessage(resultOutputMsg);
                goLogin.setCancelable(false);
                goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = goLogin.create();
                alertLogin.show();

            }
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                URL url = null;
                try {
                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/programs/subscribe/?api_key="+ActivityIntermediateClass.apiKeyValue+"&ref_id=" + fetchRefID + "&client_id=" + _clientID);

                    System.out.println("url in custom list page is: " + url);

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
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                responseOutput = new StringBuilder();
//                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                System.out.println("JSON output is =" + responseOutput);

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                try {
                    json = new JSONObject(responseOutput.toString());
                    resultOutput = json.getString("status");
                    resultOutputMsg = json.getString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //   progress.dismiss();

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








