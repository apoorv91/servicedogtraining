package com.SDT.servicedog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.SDT.servicedog.TaskListsCollectionData.MediaTasks;

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

public class MenuSubActivity extends AppCompatIntermediateActivity {

    private TextView textViewMenuHeader, textview1Menu;
    private WebView textViewMenuDescription;
    private Button docsBtn, btnVideo, audioBtn;
    private ProgressDialog progress;
    private StringBuilder responseOutput;
    private String headerName,_loginID,profile_image_url,_clientID;
    public static String _utfValue = "";
    ImageView captureImageView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_sub);

        textViewMenuHeader = (TextView) findViewById(R.id.textViewMenu);
        textViewMenuDescription = (WebView) findViewById(R.id.textview2Menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        textview1Menu = (TextView) findViewById(R.id.textview1Menu);
        textview1Menu.setPaintFlags(textview1Menu.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // to make textveiew underlined

        docsBtn = (Button) findViewById(R.id.docsBtn);
        audioBtn = (Button) findViewById(R.id.audioBtn);
        btnVideo = (Button) findViewById(R.id.btnVideo);

        Intent intentobj = getIntent();
        headerName = intentobj.getStringExtra("HEADER_NAME");
        _loginID = intentobj.getStringExtra("EMAIL_ID");



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progress.dismiss();
            }
        }, 3000); // 3000 milliseconds delay


        MenuMediaGetClass.task_videos.clear();
        MenuMediaGetClass.task_thumb_url_images.clear();
        MenuMediaGetClass.media_name.clear();

        MenuMediaGetClass.media_audio_url.clear();
        MenuMediaGetClass.media_audio_name.clear();

        MenuMediaGetClass.media_document_url.clear();
        MenuMediaGetClass.media_document_name.clear();

        new GetData(MenuSubActivity.this).execute();


        docsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (MenuMediaGetClass.media_document_url.size() == 0) {

                } else {

                    Intent nextscreen4 = new Intent(getApplicationContext(), TaskDocumentList.class);
                    nextscreen4.putExtra("TASK_DOCUMENT_URLS", MenuMediaGetClass.media_document_url);
                    nextscreen4.putExtra("TASK_DOCUMENT_NAMES", MenuMediaGetClass.media_document_name);
                    startActivity(nextscreen4);
                }
                // Toast.makeText(MenuSubActivity.this, "Docs button clicked !", Toast.LENGTH_SHORT).show();
            }
        });

        audioBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "audio button clicked", Toast.LENGTH_LONG).show();


                if (MenuMediaGetClass.media_audio_url.size() == 0) {

                } else {

                    Intent nextscreen3 = new Intent(getApplicationContext(), TaskAudioList.class);
                    nextscreen3.putExtra("TASK_AUDIO_URLS", MenuMediaGetClass.media_audio_url);
                    nextscreen3.putExtra("TASK_AUDIO_NAMES", MenuMediaGetClass.media_audio_name);
                    startActivity(nextscreen3);

                }

                System.out.println("media urls and media names are: " + MenuMediaGetClass.media_audio_url + "\n" + MenuMediaGetClass.media_audio_name);
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MenuMediaGetClass.task_videos.size() == 0) {

                } else {

                    Intent nextScreen2 = new Intent(getApplicationContext(), TaskVideoList.class);
                    nextScreen2.putExtra("TASK_VIDEO_ARRAY", MenuMediaGetClass.task_videos);
                    nextScreen2.putExtra("TASK_IMAGE_ARRAY", MenuMediaGetClass.task_thumb_url_images);
                    nextScreen2.putExtra("MEDIA_NAMES", MenuMediaGetClass.media_name);
                    startActivity(nextScreen2);
                }
            }
        });

        initNavigationDrawer();
    }

    public void initNavigationDrawer() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:

                        Intent aboutusintent = new Intent(MenuSubActivity.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
                        break;
                    case R.id._trainingprog:

                        Intent trainingintent = new Intent(MenuSubActivity.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
//                        Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
//                        break;
                    case R.id._taskhis:
                        Intent historyintent = new Intent(MenuSubActivity.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        Intent newintent = new Intent(MenuSubActivity.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        Intent settingIintent = new Intent(MenuSubActivity.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;

                    case R.id.help_btn:
                        Intent helpIintent = new Intent(MenuSubActivity.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;
                    case R.id.menu_btn:

                        Intent menuIintent = new Intent(MenuSubActivity.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID", _loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:
                        Intent intent = new Intent(MenuSubActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

//                        make this at all places where Drawer is called.

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

        System.out.println("value of image url from shared preference " + profile_image_url);

        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) header.findViewById(R.id.tv_email);

        if (profile_image_url.equals("")) {

            Bitmap bitmap_default_image = BitmapFactory.decodeResource(this.getResources(), R.drawable.user_icon);
            Imageclass.bitmapImageValue = bitmap_default_image;
            ImageView profileImageView = (ImageView) header.findViewById(R.id.imageView3);
            profileImageView.setImageBitmap(bitmap_default_image);
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

        if (Imageclass.image_getdata_flag == 1) {

            ImageView profileImageView = (ImageView) header.findViewById(R.id.imageView3);
            profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());

        }
    }



    private class GetData extends AsyncTask<String, Void, Void> {

        private final Context context;
        private int resultStatus;
        private String resultOutputMsg, media_type;
        private JSONArray resultDataOutput;
        ArrayList<String> task_title = new ArrayList<String>();
        ArrayList<String> task_description = new ArrayList<String>();
        List<MediaTasks> fetchjsonData1;

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

            if (MenuMediaGetClass.media_document_url.size() == 0) {

                docsBtn.setAlpha((float) 0.5);

            }
            if (MenuMediaGetClass.media_audio_url.size() == 0) {

                audioBtn.setAlpha((float) 0.5);
            }
            if (MenuMediaGetClass.task_videos.size() == 0) {

                btnVideo.setAlpha((float) 0.5);
            }

            Log.i("MEDIA GET STATUS: ", "media successfully saved.");

        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                URL url = null;
                try {

                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/pages/page/?api_key="+ActivityIntermediateClass.apiKeyValue);
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
                Log.i("MEDIA GET STATUS: ", "media Saving Started.");

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                MenuSubActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //  System.out.println("response output===============" + responseOutput.toString());

                        ArrayList<String> programlist = new ArrayList<String>();
                        JSONObject json = null;
                        JSONObject json2 = null;
                        JSONObject json3 = null;

                        Gson gson = new Gson();

                        MenuRootObject menuRoot = gson.fromJson(responseOutput.toString(), MenuRootObject.class);
                        MenuMediaGetClass.menuRoot = menuRoot;

                        resultStatus = menuRoot.status;
                        resultOutputMsg = menuRoot.message;
                        AlertDialog.Builder goLogin = new AlertDialog.Builder(MenuSubActivity.this, R.style.MyAlertDialogStyle);

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
                        } else if (resultStatus == 1) {


                            try {
                                json = new JSONObject(responseOutput.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                resultDataOutput = json.getJSONArray("data");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            System.out.println("result output is : " + resultDataOutput);

                            task_title.clear();
                            task_description.clear();


                            for (int i = 0; i < resultDataOutput.length(); i++) {

                                if (menuRoot.data.get(i).Page.keyword.toString().equals(headerName.toString())) {

                                    task_title.add(menuRoot.data.get(i).Page.title);
                                    task_description.add(menuRoot.data.get(i).Page.description.toString().replace("\r\n\r\n", "\n\n"));

                                    fetchjsonData1 = menuRoot.data.get(i).PageMedia;
                                    // System.out.println("media data" + fetchjsonData1);
////
                                    textViewMenuHeader.setText(menuRoot.data.get(i).Page.title);
                                    textViewMenuDescription.setBackgroundColor(Color.TRANSPARENT);
                                    textViewMenuDescription.getSettings().setJavaScriptEnabled(true);
                                    String textview_message_content = "<html><body>" + "<p align=\"justify\">" + menuRoot.data.get(i).Page.description.toString().replace("\r\n\r\n", "\n\n") + "</p></body></html>";
                                    textViewMenuDescription.loadDataWithBaseURL(null, textview_message_content, "text/html", "utf-8", null);


                                    for (int j = 0; j < fetchjsonData1.size(); j++) {
                                        media_type = (fetchjsonData1.get(j).media_type);


                                        if (media_type.equals("docs")) {

                                            MenuMediaGetClass.media_document_url.add(fetchjsonData1.get(j).media);
                                            MenuMediaGetClass.media_document_name.add(fetchjsonData1.get(j).name);

                                        }
                                        if (media_type.equals("audio")) {

                                            MenuMediaGetClass.media_audio_url.add(fetchjsonData1.get(j).media);
                                            MenuMediaGetClass.media_audio_name.add(fetchjsonData1.get(j).name);

                                        }
                                        if (media_type.equals("videos")) {

                                            MenuMediaGetClass.task_videos.add(fetchjsonData1.get(j).media);  // video urls
                                            MenuMediaGetClass.media_name.add(fetchjsonData1.get(j).name);   //  video names
                                            MenuMediaGetClass.task_thumb_url_images.add(fetchjsonData1.get(j).thumb_url);   //  video thunmb image urls

                                        }
                                    }
                                    System.out.println("media counter value is : " + fetchjsonData1.size());

                                    break;

                                } else {
                                    textViewMenuDescription.setBackgroundColor(Color.TRANSPARENT);
                                    textViewMenuDescription.getSettings().setJavaScriptEnabled(true);
                                    String textview_message_content = "<html><body style=font-size:23px;>" + "<p align=\"justify\">" + "NO DATA FOUND" + "</p></body></html>";
                                    textViewMenuDescription.loadDataWithBaseURL(null, textview_message_content, "text/html", "utf-8", null);
                                }
                            }
                            //    System.out.println("DESCRIPTION array data is:" + task_description.get(0) + "\n");
                        }
//                        for (int b = 0; b < task_title.size(); b++) {
//
//                            if (task_title.get(b).equals(headerName.toString())) {
//
//                                textViewMenuHeader.setText(task_title.get(b));
//
//                                textViewMenuDescription.setBackgroundColor(Color.TRANSPARENT);
//                                textViewMenuDescription.getSettings().setJavaScriptEnabled(true);
//                                String textview_message_content = "<html><body>" + "<p align=\"justify\">" + task_description.get(b) + "</p></body></html>";
//                                textViewMenuDescription.loadDataWithBaseURL(null, textview_message_content, "text/html", "utf-8", null);
//
//                            }
//                        }
                    }
                });
                progress.dismiss();

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


    public void SegregateImagesAndVideosForTask(String ButtonName) {

        MenuRootObject menuroot = MenuMediaGetClass.menuRoot;

    }

}
