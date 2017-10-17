package com.SDT.servicedog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DocumentPlay extends ActivityIntermediateClass {

    ProgressDialog pDialog, progress;
    URL url = null;
    String _DOCUMENT_URL = null;
    WebView webView;

    private Toolbar toolbar;
    private View header;
    String TaskListDescclientID, _loginID;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_play);

        // code for countdown timer for no activity logout.
//        obj.countDownTimerObj.cancel();
//        obj.countDownTimerObj.start();

        System.out.println("/*******************************  DOCUMENT PLAY CLASS STARTED  *****************************************************/");

        TaskListDescription.activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        Intent intent = getIntent();
        _DOCUMENT_URL = intent.getStringExtra("DOCUMENT_URL");
        System.out.println("Document url is : " + _DOCUMENT_URL);

        webView = (WebView) findViewById(R.id.webView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initNavigationDrawer();

        webView.requestFocus(View.FOCUS_DOWN);
//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                    case MotionEvent.ACTION_UP:
//                        if (!v.hasFocus()) {
//                            v.requestFocus();
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
    }

    public void initNavigationDrawer() {

        System.out.println("inside inside init navigation method");

        receiveGetFile(_DOCUMENT_URL);


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:
                        Intent aboutusintent = new Intent(DocumentPlay.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
//                        Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(DocumentPlay.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(DocumentPlay.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(DocumentPlay.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(DocumentPlay.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;
                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(DocumentPlay.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(DocumentPlay.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID", _loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(DocumentPlay.this, MainActivity.class);
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
// code for dowloading document in device storage and then playing.

    private void receiveGetFile(String documentpath) {

        new GetFileData(this, documentpath).execute();
    }

    private class GetFileData extends AsyncTask<String, Void, Void> {

        private final Context context;
        OutputStream output;
        // ProgressDialog progress;
        String fileName = null, documentpath = null;

        public GetFileData(Context c, String documentpath) {
            this.context = c;
            this.documentpath = documentpath;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(DocumentPlay.this, R.style.dialog);
            progress.setTitle("Loading Document");
            progress.setMessage("Please wait document is downloading...");
            progress.setCancelable(false);
            progress.show();

        }

        protected void onPostExecute(Void result) {

            Log.i("File download", "background complete: " + output);

            File file = new File(Environment.getExternalStorageDirectory() + "/serviceDog/" + fileName);
            String filename = file.getName();
            Uri filetype = Uri.parse(file.getPath());
            Uri path = Uri.fromFile(file);

            Log.i("FILE NAME IS : ", String.valueOf(filename));
            Log.i("DOC PATH IS : ", String.valueOf(path));
            Log.i("FILETYPE PATH IS : ", String.valueOf(filetype));

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + documentpath);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onLoadResource(WebView view, String url) {

//                    view.loadUrl("javascript:(function() { " +
//                            "document.getElementsByClassName('ndfHFb-c4YZDc-Wrql6b')[0].style.display='none'; })()");

                    webView.loadUrl("javascript:(function() { " +
                            "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.display='none'; })()");

                }

                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    view.loadUrl("javascript:(function() { " +
                            "document.getElementsByClassName('ndfHFb-c4YZDc-Wrql6b')[0].style.display='none'; })()");
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
//                    view.loadUrl("javascript:(function() { " +
//                            "document.getElementsByClassName('ndfHFb-c4YZDc-Wrql6b')[0].style.display='none'; })()");

                    webView.loadUrl("javascript:(function() { " +
                            "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.display='none'; })()");
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    view.loadUrl("javascript:(function() { " +
                            "document.getElementsByClassName('ndfHFb-c4YZDc-Wrql6b')[0].style.display='none'; })()");

//  view.loadUrl("javascript:(function() { " +
//                            "document.getElementsByClassName('ndfHFb-c4YZDc-Wrql6b')[0].style.display='none';})()");

                    progress.dismiss();
                    super.onPageFinished(view, url);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    view.loadUrl("javascript:(function() { " +
                            "document.getElementsByClassName('ndfHFb-c4YZDc-Wrql6b')[0].style.display='none';})()");
                }
            });
        }

        @Override
        protected Void doInBackground(String... params) {

            fileName = documentpath.substring(documentpath.lastIndexOf('/') + 1);

            String result_value = isFilePresent(fileName);

            if (result_value.equals("false")) {

                try {
                    File myDir = new File(Environment.getExternalStorageDirectory() + "/serviceDog");
                    // create the directory if it doesnt exist
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    // fileName = documentpath.substring(documentpath.lastIndexOf('/') + 1);

                    File outputFile = new File(myDir, fileName);

                    Log.i("File Status:", "Downloading document file in background.");

                    URL url = new URL(documentpath);

                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // getting file length

                    int fileLength = connection.getContentLength();

                    // download the file

                    // input stream to read file
                    InputStream input = new BufferedInputStream(url.openStream());

                    // Output stream to write file
                    output = new FileOutputStream(outputFile);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;

                    while ((count = input.read(data)) != -1) {

                        total += count;
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    Log.i("File download", "complete: " + output);


                    // closing streams
                    output.close();
                    input.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (result_value.equals("true")) {

                Log.i("File Status: ", "File already exist");
            }
            return null;
        }

    }

    public String isFilePresent(String fileName) {

        String path = Environment.getExternalStorageDirectory() + "/serviceDog/" + fileName;
        File file = new File(path);

        if (file.exists() == true) {
            return "true";
        } else {
            return "false";
        }
    }
}

