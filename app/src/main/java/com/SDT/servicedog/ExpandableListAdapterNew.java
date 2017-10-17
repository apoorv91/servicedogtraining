package com.SDT.servicedog;

/**
 * Created by laitkor on 1/10/17.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

class ExpandableListAdapterNew extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<String> _listDataHeader = null; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<Program3>> _listDataChild = null;
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
    String resultOutput;

    public String get_clientID() {
        return _clientID;
    }

    public void set_clientID(String _clientID) {
        this._clientID = _clientID;
    }

    String _clientID = TrainingProgrammes._utfValue;
    String _loginid = TrainingProgrammes._loginID;


    public ExpandableListAdapterNew(Context context, ArrayList<String> listDataHeader, HashMap<String, ArrayList<Program3>> listChildData, ArrayList<String> ref_ids, ArrayList<String> programStatus_, ArrayList<Integer> _tierIndexValue, ArrayList<String> _programIDs) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.ref_ids = ref_ids;
        this.programStatus_ = programStatus_;
        this._tierLevel = _tierLevel;
        this._tierIndexValue = _tierIndexValue;
        this._programIDs = _programIDs;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon).getName();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.listdata, null);
        TextView imageText = (TextView) convertView.findViewById(R.id.imageText);
        TextView textViewName = (TextView) convertView.findViewById(R.id.textViewName);
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.firstLayout);
        final Button subscribebtn = (Button) convertView.findViewById(R.id.subscribeBtn);
        subscribebtn.setTag(childPosition);

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {

            if (childPosition % 2 != 0) {

                imageText.setBackgroundResource(R.drawable.gray_circle);
                linearLayout.setBackgroundResource(R.color.white);
                subscribebtn.setBackgroundResource(R.drawable.gray_subs);

            } else {

                imageText.setBackgroundResource(R.drawable.sky_circle);
                linearLayout.setBackgroundResource(R.color.sky_blue);
                subscribebtn.setBackgroundResource(R.drawable.sky_subs);

            }
            imageText.setText(_tierIndexValue.get(childPosition).toString());
            textViewName.setText(childText);
            fetchRefID = ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getRef_id();
            subscribebtn.setText(ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getStatus());

            final int gp = groupPosition;
            subscribebtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    // if(subscribebtn.getTag().equals(0)) {

                    if (ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getStatus().toString().equals("Subscribe")) {
                        fetchRefID = ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(gp)).get(childPosition).getRef_id();
                        subscribebtn.setText("Subscribed");
                        programStatus_.set(childPosition, "Subscribed");
                        sendToSomeActivity(ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(gp)).get(childPosition).getRef_id(),groupPosition,childPosition);
                    } else if (ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getStatus().toString().equals("Subscribed")) {
                        AlertDialog.Builder goLogin = new AlertDialog.Builder(_context, R.style.MyAlertDialogStyle);
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

            textViewName.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Toast.makeText(_context, "text clicked : ", Toast.LENGTH_SHORT).show();

                    if (ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getStatus().toString().equals("Subscribed")) {

                        System.out.println("Program id is trianing programme new : " + ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(gp)).get(childPosition).getId() + " CLIENT_ID " + _clientID);
                        Intent nextScreen2 = new Intent(_context, TaskList.class);
                        nextScreen2.putExtra("EMAIL_ID", _loginid);
                        nextScreen2.putExtra("PROGRAMME_ID", ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(gp)).get(childPosition).getId());
                        nextScreen2.putExtra("CLIENT_ID", _clientID);
                        _context.startActivity(nextScreen2);

                    } else if (ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getStatus().equals("Subscribe")) {


                        AlertDialog.Builder goLogin = new AlertDialog.Builder(_context, R.style.MyAlertDialogStyle);
                        goLogin.setMessage("Sorry, you can't jump to task list without subscribed to this programme.");
                        goLogin.setCancelable(false);
                        goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertLogin = goLogin.create();
                        alertLogin.show();
                    } else if (ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getStatus().equals("Completed")) {

                        Intent intent_completed = new Intent(_context, TaskList.class);
                        intent_completed.putExtra("EMAIL_ID", _loginid);
                        intent_completed.putExtra("PROGRAMME_ID", ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(gp)).get(childPosition).getId());
                        //  intent_completed.putExtra("PROGRAMME_STATUS", programStatus.get(i));
                        intent_completed.putExtra("CLIENT_ID", _clientID);
                        _context.startActivity(intent_completed);

                    } else if (ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getStatus().equals("Inactive")) {

                        AlertDialog.Builder goLogin = new AlertDialog.Builder(_context, R.style.MyAlertDialogStyle);
                        goLogin.setMessage("Sorry, you need to complete this programme before jumping to the next.");
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
            });

        } else {


            if (childPosition % 2 != 0) {

                imageText.setBackgroundResource(R.drawable.gray_circle);
                linearLayout.setBackgroundResource(R.color.white);
                subscribebtn.setBackgroundResource(R.drawable.gray_subs);

            } else {

                imageText.setBackgroundResource(R.drawable.sky_circle);
                linearLayout.setBackgroundResource(R.color.sky_blue);
                subscribebtn.setBackgroundResource(R.drawable.sky_subs);
            }

            imageText.setText(_tierIndexValue.get(childPosition).toString());
            textViewName.setText(childText);
            fetchRefID = ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getRef_id();
            subscribebtn.setText(ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getStatus());

            final int gp = groupPosition;
            subscribebtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getStatus().toString().equals("Subscribe")) {
                        fetchRefID = ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(gp)).get(childPosition).getRef_id();
                        subscribebtn.setText("Subscribed");
                        programStatus_.set(childPosition, "Subscribed");
                        sendToSomeActivity(ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(gp)).get(childPosition).getRef_id(), groupPosition, childPosition);
                    } else if (ExpandableListAdapterNew.this._listDataChild.get(ExpandableListAdapterNew.this._listDataHeader.get(groupPosition)).get(childPosition).getStatus().toString().equals("Subscribed")) {
                        AlertDialog.Builder goLogin = new AlertDialog.Builder(_context, R.style.MyAlertDialogStyle);
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

//        TextView txtListChild = (TextView) convertView
//                .findViewById(R.id.lblListItem);
//
//        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.list_group, null);
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.firstLayoutGroup);
        linearLayout.setBackgroundResource(R.color.colorPrimary);

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.toString().toUpperCase());
        lblListHeader.setTextColor(Color.WHITE);
        lblListHeader.setBackgroundResource(R.color.colorSecondary);
        linearLayout.setBackgroundResource(R.color.colorSecondary);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void sendToSomeActivity(String progrmaID, int grpPosition, int chldPosition) {

        new PostClassNew(progrmaID,grpPosition,chldPosition).execute();
    }

    private class PostClassNew extends AsyncTask<String, Void, Void> {

        String _programID;
        JSONObject json = null;
        int _grpPosition, _chldPosition;

        public PostClassNew(String progrmaID, int grpPosition, int chldPosition) {

            this._programID = progrmaID;
            this._grpPosition = grpPosition;
            this._chldPosition = chldPosition;
        }

        protected void onPreExecute() {
            /*progress = new ProgressDialog();
            progress.setMessage("Loading");
            progress.show();*/
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            AlertDialog.Builder goLogin = new AlertDialog.Builder(_context, R.style.MyAlertDialogStyle);

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

                TrainingProgrammes.listTierwiseData.get(TrainingProgrammes.listDataHeader.get(_grpPosition)).get(_chldPosition).setStatus("Subscribed");


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

                SharedPreferences haredpreferences = _context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
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
