package com.SDT.servicedog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
import java.util.HashMap;
import java.util.List;


public class TrainingProgrammes extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ListView listView;
    ProgressDialog progress;
    JSONArray resultOutput;
    String resultOutputMsg;
    int resultStatus;
    String first_name, last_name;
    StringBuilder responseOutput;
    //  Bitmap myBitmap;

    public String get_clientID() {
        return _clientID;
    }

    Button captureImageBtn;
    ImageView captureImageView;
    String _clientID;
    String profile_image_url;
    public static String _loginID;
    URL url = null;
    private static final int CAMERA_REQUEST = 1888;
    ArrayList<String> listProgrms = new ArrayList<String>();
    ArrayList<String> reference_IDs = new ArrayList<String>();
    ArrayList<String> programIDS = new ArrayList<String>();
    public ArrayList<String> programStatus = new ArrayList<String>();
    ArrayList<String> _tierLevel = new ArrayList<String>();
    ArrayList<String> _tierstatus = new ArrayList<String>();
    ArrayList<Integer> _tierIndexValue = new ArrayList<Integer>();
    public static String _utfValue = "";
    private static int RESULT_LOAD_IMAGE = 1;
    List<Program> fetchjsonData1;

    ExpandableListView expListView;
    ExpandableListAdapterNew listAdapter;
    public static ArrayList<String> listDataHeader = new ArrayList<String>();
    ArrayList<String> top250 = new ArrayList<String>();
    public static HashMap<String, ArrayList<String>> listDataChild = new HashMap<String, ArrayList<String>>();

    public static HashMap<String, ArrayList<Program3>> listTierwiseData = new HashMap<>();
    ArrayList<String> listProgrmsTierwise = null;
    ArrayList<String> tierTaskData = null;
    String _preview_role = null;
    ProgressBar _progressBarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingprogrammes);

        AppCompatIntermediateActivity.taskTimerFlag = 0;

        System.out.println("/*************************** TRAINING PROGRAMME CLASS OUTPUT RESULTS *******************************/");

        System.out.println("initial flag value is : " + Imageclass.image_getdata_flag);

        // code for countdown timer for no activity logout.
        Common.countDownTimer.start();
        Intent intent = getIntent();

        _loginID = intent.getStringExtra("EMAIL_ID");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        initNavigationDrawer();

    }

    boolean doubleBackToExitPressedOnce = false;

//     code for double click back button to logout


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goLogin = new Intent(TrainingProgrammes.this, UserMenuActivity.class);
        goLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goLogin);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.MyAlertDialogStyle);
        builder.setMessage("are you sure ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
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
            Toast toast = Toast.makeText(TrainingProgrammes.this, "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
            // startActivity(new Intent(this, MainActivity.class));
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:

                        Intent aboutusintent = new Intent(TrainingProgrammes.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
                        // Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();

                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(TrainingProgrammes.this, TaskHistory.class);
                        //historyintent.putExtra("CLIENT_ID", _clientID);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(TrainingProgrammes.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent settingIintent = new Intent(TrainingProgrammes.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;

                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(TrainingProgrammes.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;
                    // code for menu shortcut in navigation drawer

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(TrainingProgrammes.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID", _loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

//                        make this at all places where Drawer is called.

                    case R.id.logout:
                        Intent intent = new Intent(TrainingProgrammes.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                        SharedPreferences preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                        if (preferences.contains("password")) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("password");
                            editor.commit();
                        }
                        finish(); // Call once you redirect to another activity

                }
                return true;
            }
        });

        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        profile_image_url = haredpreferences.getString("profile_pic", "null");
        _preview_role = haredpreferences.getString("preview_role", "Null");

        System.out.println("value of image url from shared preference " + profile_image_url);

        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
        _progressBarImage = (ProgressBar) header.findViewById(R.id.progressBarImage);

        if (profile_image_url.equals("")) {

            Bitmap bitmap_default_image = BitmapFactory.decodeResource(this.getResources(), R.drawable.user_icon);
            Imageclass.bitmapImageValue = bitmap_default_image;
            ImageView profileImageView = (ImageView) header.findViewById(R.id.imageView3);
            _progressBarImage.setVisibility(View.VISIBLE);
            profileImageView.setImageBitmap(bitmap_default_image);
            _progressBarImage.setVisibility(View.INVISIBLE);
            Imageclass.image_getdata_flag = 1;
        }

        tv_email.setText(haredpreferences.getString("first_name", "Null") + " " + haredpreferences.getString("last_name", "Null"));
        _clientID = haredpreferences.getString("CLIENT_ID", "Null");
        _utfValue = _clientID;

        captureImageView = (ImageView) header.findViewById(R.id.imageView3);
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

        //Bitmap bitmap = Imageclass.ResizeBitmapImage();

        if (Imageclass.image_getdata_flag == 1) {

            ImageView profileImageView = (ImageView) header.findViewById(R.id.imageView3);
            _progressBarImage.setVisibility(View.VISIBLE);
            profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());
            _progressBarImage.setVisibility(View.INVISIBLE);
        }
    }

    private void receiveGetRequest(View v) {

        new GetData(this).execute();
    }

    private class GetData extends AsyncTask<String, Void, Void> {

        private final Context context;
        int previousGroup = -1;

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


            listAdapter = new ExpandableListAdapterNew(TrainingProgrammes.this, listDataHeader, listTierwiseData, reference_IDs, programStatus, _tierIndexValue, programIDS);
            // setting list adapter
            expListView.setAdapter(listAdapter);

            int activeTierIndex = _tierstatus.indexOf("incomplete");

            if (activeTierIndex == -1) {

                expListView.expandGroup(0);
                previousGroup = 0;
            } else {
                expListView.expandGroup(activeTierIndex);
                previousGroup = activeTierIndex;
            }


            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                    //   Toast.makeText(context, "Group clicked---" + groupPosition, Toast.LENGTH_SHORT).show();

                    if (!parent.isGroupExpanded(groupPosition)) {
                        parent.expandGroup(groupPosition);
                    } else {
                        parent.collapseGroup(groupPosition);

                    }
                    //  parent.setSelectedGroup(groupPosition);
                    parent.setSelectedGroup(0);

                    return true;
                }
            });

            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                // Keep track of previous expanded parent

                @Override
                public void onGroupExpand(int groupPosition) {

                    // Collapse previous parent if expanded.
                    if ((previousGroup != -1) && (groupPosition != previousGroup)) {
                        expListView.collapseGroup(previousGroup);
                    }
                    previousGroup = groupPosition;
                }
            });


            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    //    Toast.makeText(context, "programme clicked & id is : \n grp:" + groupPosition + " chld: " + childPosition, Toast.LENGTH_SHORT).show();

                    if (listTierwiseData.get(listDataHeader.get(groupPosition)).get(childPosition).getStatus().toString().equals("Subscribed")) {

                        System.out.println("Program id is trianing programme new : " + listTierwiseData.get(listDataHeader.get(groupPosition)).get(childPosition).getId() + " CLIENT_ID " + _clientID);
                        Intent nextScreen2 = new Intent(getApplicationContext(), TaskList.class);
                        nextScreen2.putExtra("EMAIL_ID", _loginID);
                        nextScreen2.putExtra("PROGRAMME_ID", listTierwiseData.get(listDataHeader.get(groupPosition)).get(childPosition).getId());
                        nextScreen2.putExtra("CLIENT_ID", _clientID);
                        startActivity(nextScreen2);

                    } else if (listTierwiseData.get(listDataHeader.get(groupPosition)).get(childPosition).getStatus().toString().equals("Subscribe")) {


                      //  if (_preview_role.toString().equals("admin")) {

//                            Intent intent_completed = new Intent(getApplicationContext(), TaskList.class);
//                            intent_completed.putExtra("EMAIL_ID", _loginID);
//                            intent_completed.putExtra("PROGRAMME_ID", listTierwiseData.get(listDataHeader.get(groupPosition)).get(childPosition).getId());
//                            intent_completed.putExtra("CLIENT_ID", _clientID);
//                            startActivity(intent_completed);

                      //  }else{

                            AlertDialog.Builder goLogin = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                            goLogin.setMessage("Sorry, you can't jump to task list without subscribed to this programme.");
                            goLogin.setCancelable(false);
                            goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertLogin = goLogin.create();
                            alertLogin.show();
                      //  }

                    } else if (listTierwiseData.get(listDataHeader.get(groupPosition)).get(childPosition).getStatus().toString().equals("Completed")) {

                        Intent intent_completed = new Intent(getApplicationContext(), TaskList.class);
                        intent_completed.putExtra("EMAIL_ID", _loginID);
                        intent_completed.putExtra("PROGRAMME_ID", listTierwiseData.get(listDataHeader.get(groupPosition)).get(childPosition).getId());
                        intent_completed.putExtra("CLIENT_ID", _clientID);
                        startActivity(intent_completed);

                    } else if (listTierwiseData.get(listDataHeader.get(groupPosition)).get(childPosition).getStatus().toString().equals("Inactive")) {

                        if (_preview_role.toString().equals("admin")) {

                            Intent intent_completed = new Intent(getApplicationContext(), TaskList.class);
                            intent_completed.putExtra("EMAIL_ID", _loginID);
                            intent_completed.putExtra("PROGRAMME_ID", listTierwiseData.get(listDataHeader.get(groupPosition)).get(childPosition).getId());
                            intent_completed.putExtra("CLIENT_ID", _clientID);
                            startActivity(intent_completed);

                        }else{

                            AlertDialog.Builder goLogin = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                            goLogin.setMessage("Sorry, you need to complete the programme before jumping to the next.");
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

                    return false;
                }
            });

        }

        /**************************************
         * END OF TRAINING PROGRAMME CLASS
         ******************************/

        @Override
        protected Void doInBackground(String... params) {


            SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
            _clientID = haredpreferences.getString("CLIENT_ID", "Null");
            try {
                // final EditText txtEmail = (EditText) findViewById(R.id.editText);
                //final EditText txtPassword = (EditText) findViewById(R.id.editText2);
                try {
                    // url = new URL("http://servdog.dealopia.com/programs/program/?api_key=uXO39sJA8JU7OOPrLZkxmR183larMagL");
                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/programs/program/?api_key="+ActivityIntermediateClass.apiKeyValue+"&client_id=" + _clientID);

                    System.out.println("Url is: " + url);
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
//                System.out.println("output===============" + br);


                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                TrainingProgrammes.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

//                        System.out.println("response output===============" + responseOutput.toString());

                        JSONObject json = null;
                        Gson gson = new Gson();

                        RootObject joc = gson.fromJson(responseOutput.toString(), RootObject.class);
                        resultStatus = joc.status;
                        resultOutputMsg = joc.message;


                        AlertDialog.Builder goLogin = new AlertDialog.Builder(TrainingProgrammes.this, R.style.MyAlertDialogStyle);
//                        System.out.println("result output" + resultOutput);

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
                            progress.hide();
                        }
                        //else if (resultOutput.contains("Registration Successful")) {

                        else {
                            if (resultStatus == 1) {

                                if (tierTaskData != null && !tierTaskData.isEmpty()) {
                                    tierTaskData.clear();
                                }
                                if (listDataHeader != null && !listDataHeader.isEmpty()) {
                                    listDataHeader.clear();
                                }
                                if (listTierwiseData != null && !listTierwiseData.isEmpty()) {
                                    listTierwiseData.clear();
                                }

                                try {
                                    json = new JSONObject(responseOutput.toString());
                                    resultOutput = json.getJSONArray("data");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                System.out.println("data length = " + resultOutput.length());

                                for (int i = 0; i < resultOutput.length(); i++) {

                                    _tierstatus.add(joc.data.get(i).status);
                                    _tierLevel.add(joc.data.get(i).Tier);
                                    listDataHeader.add(joc.data.get(i).Tier);

                                    fetchjsonData1 = joc.data.get(i).program;
                                    listProgrmsTierwise = new ArrayList<String>();

                                    ArrayList<Program3> tierTaskData = new ArrayList<Program3>();

                                    for (int j = 0; j < fetchjsonData1.size(); j++) {
                                        Program3 program3 = new Program3();

                                        program3.setName(fetchjsonData1.get(j).program.Program.name);
                                        program3.setId(fetchjsonData1.get(j).program.Program.id);
                                        program3.setStatus(fetchjsonData1.get(j).status);
                                        program3.setRef_id(fetchjsonData1.get(j).program.Program.ref_id);

                                        tierTaskData.add(program3);

                                        _tierIndexValue.add(j + 1);
                                        listProgrms.add(fetchjsonData1.get(j).program.Program.name);
                                        reference_IDs.add(fetchjsonData1.get(j).program.Program.ref_id);
                                        programStatus.add(fetchjsonData1.get(j).status);
                                        programIDS.add(fetchjsonData1.get(j).program.Program.id);
                                        listProgrmsTierwise.add(fetchjsonData1.get(j).program.Program.name);

                                    }

                                    listTierwiseData.put(listDataHeader.get(i), tierTaskData);
                                    listDataChild.put(listDataHeader.get(i), listProgrms);
                                }

                                for (String name : listTierwiseData.keySet()) {

                                    String key = name.toString();
                                    String value = listTierwiseData.get(name).toString();
                                    System.out.println("HASHMAP key----" + key + " value---" + value);


                                }

                                progress.dismiss();
                            }
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