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
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.SDT.servicedog.about_us.AboutUsRootObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;


public class AboutUS extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ImageView thumbImage,playButton;
    ProgressDialog progress;
    JSONObject resultOutput;

    String header_task_name;
    String task_name_description;
    URL url = null;
    Bitmap myBitmap;
    private View header;

    String TaskListDescclientID;
    StringBuilder responseOutput;
    String _programmeID;
    String resultOutputMsg;
    int resultStatus;
    String _loginID, _clientid;
    String first_name, last_name;
    String about_us_description, video_url, thumb_url;

    private WebView webView2;

    public static String _utfValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        thumbImage = (ImageView) findViewById(R.id.video_thumb_image);
        webView2 = (WebView) findViewById(R.id.webView2);
        playButton = (ImageView) findViewById(R.id.imageView5);

        System.out.println("/*******************************  ABOUT US CLASS STARTED   *****************************************************/");
        Intent intent = getIntent();

        _programmeID = intent.getStringExtra("PROGRAMME_ID");
        _loginID = intent.getStringExtra("EMAIL_ID");

        System.out.println("client_id and client_email_id and firstname & lastname and _clientID" + _programmeID + _loginID + first_name + last_name + "\n" + _clientid);
        System.out.println("ProgrammeID :" + _programmeID);
        System.out.println("header task name and description  :" + header_task_name + "\n" + task_name_description);
        _utfValue = _programmeID;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initNavigationDrawer();


        final WebSettings webSettings = webView2.getSettings();
        webSettings.setDefaultFontSize(15);

    }


    public void initNavigationDrawer() {


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {
            // setContentView(R.layout.activity_main);
            new GetData(this).execute();
        } else {
            //no connection
            Toast toast = Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();

            webView2.setBackgroundColor(Color.TRANSPARENT);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:
                        Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        
                        Intent trainingintent = new Intent(AboutUS.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        
                        Intent historyintent = new Intent(AboutUS.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        
                        Intent newintent = new Intent(AboutUS.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(AboutUS.this, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(settingIintent);

                        break;
                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(AboutUS.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(AboutUS.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID",_loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;
//                    case R.id.faq_btn:
//
//                        Intent intentfaq = new Intent(AboutUS.this, FrequentlyAskedQuestions.class);
//                        intentfaq.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intentfaq);
//
//                        break;
                    case R.id.logout:

                        Intent intent = new Intent(AboutUS.this, MainActivity.class);
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


//        ImageView profileImageView = (ImageView) header.findViewById(R.id.imageView3);
//        profileImageView.setBackgroundResource(R.drawable.user_icon);
    }


    private void CheckProfileImageFlagMethod(View view) {


        if (Imageclass.image_getdata_flag == 1) {

            ImageView profileImageView = (ImageView) view.findViewById(R.id.imageView3);
            profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());

        }
//        else {
//
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

            if (!about_us_description.toString().equals("")) {


                String about_us_description_new = "<html><body style=color:black;>" + "<p align=\"justify\">" + about_us_description + "</p>" + "</body></html>";
                webView2.setBackgroundColor(Color.TRANSPARENT);
                webView2.getSettings().setJavaScriptEnabled(true);
                webView2.loadDataWithBaseURL(null, about_us_description_new.toString(), "text/html", "utf-8", null);

            } else {
                webView2.setVisibility(View.GONE);
            }

            if (!thumb_url.toString().equals("")) {
                try {

                    URL url = new URL(thumb_url);

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    InputStream in = url.openConnection().getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(in, 1024 * 8);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();

                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len = bis.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    bis.close();

                    byte[] data = out.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    thumbImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            playButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (!video_url.toString().equals("")) {

                        Intent newIntent = new Intent(getApplicationContext(), AboutUsVideoPlay.class);
                        newIntent.putExtra("VIDEO_URL", video_url);
                        newIntent.putExtra("CLIENT_ID", TaskListDescclientID);
                        startActivity(newIntent);
                    } else {
                        thumbImage.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "No video uploaded", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getApplicationContext(), "Thumb_image_clcked", Toast.LENGTH_SHORT).show();
                }
            });

            /******************************* END OF TASK LIST CLASS ********************************/
            progress.dismiss();

        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                URL url = null;
                try {
                    // url = new URL("http://servdog.dealopia.com/users/clientAuth/?email=brijesh@gmail.com&password=m2n1shlko");
                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/systems/aboutusapi/?api_key="+ActivityIntermediateClass.apiKeyValue);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                System.out.println("about us url: "+url);

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

                AboutUS.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (responseOutput.toString().equals("")) {

                            Toast.makeText(AboutUS.this, "No response from server.\n Please try again...", Toast.LENGTH_SHORT).show();

                        } else {
                            output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
                            System.out.println("response output===============" + responseOutput.toString());
                            JSONObject json = null;
                            Gson gson = new Gson();

                            AboutUsRootObject joc = gson.fromJson(responseOutput.toString(), AboutUsRootObject.class);

                            resultStatus = joc.status;
                            resultOutputMsg = joc.message;
                            AlertDialog.Builder goLogin = new AlertDialog.Builder(AboutUS.this, R.style.MyAlertDialogStyle);
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
                                    resultOutput = (JSONObject) json.get("data");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                about_us_description = joc.data.description;
                                video_url = joc.data.video_url;
                                thumb_url = joc.data.thumb_url;

                                //ImageView.setImageBitmap(myBitmap);

                                System.out.println(" about_us_description and video_url and thumb_url "
                                        + about_us_description + "\n" + video_url + "\n" + thumb_url);

                            }
                        }
                    }

                });

                //  progress.dismiss();

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


    class DownloadFileFromURL extends AsyncTask<Object, String, Integer> {

        private FileOutputStream output;
        private String name, filepath, fileName;

        private final Context context;

        public DownloadFileFromURL(Context c, String filepath) {
            this.context = c;
            this.filepath = filepath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            name = (filepath).substring(filepath.lastIndexOf("/") + 1);
            String result_value = isFilePresent(name);

            if (result_value.equals("false")) {
                // Create a progressbar
                progress = new ProgressDialog(this.context, R.style.dialog);
                progress.setMessage("Loading video");
                progress.show();

            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Integer doInBackground(Object... params) {

            try {

                name = (filepath).substring(filepath.lastIndexOf("/") + 1);
                String result_value = isFilePresent(name);

                if (result_value.equals("false")) {

                    URL url = new URL(filepath);

                    // Log.v("log_tag", "name Substring ::: " + name);
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    // getting file length
                    int lenghtOfFile = conection.getContentLength();

                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    File download = new File(Environment.getExternalStorageDirectory() + "/serviceDog/");
                    if (!download.exists()) {
                        download.mkdir();
                    }
                    String strDownloaDuRL = download + "/" + name;

                    Log.i("log_tag", " down url   " + strDownloaDuRL);

                    output = new FileOutputStream(strDownloaDuRL);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;

                    while ((count = input.read(data, 0, 1024)) != -1) {
                        output.write(data, 0, count);
                        System.out.println(count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                    progress.dismiss();

                } else if (result_value.equals("true")) {

                    Log.i("File Status: ", "File already exist");
                }

                AboutUS.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {

                            File file = new File(Environment.getExternalStorageDirectory() + "/serviceDog/" + name);
                            String filename = file.getName();
                            Intent intent = new Intent(getApplicationContext(), AboutUS.class);
                            Uri filetype = Uri.parse(file.getPath());

                            System.out.println("---Its a video file");
                            Uri path = Uri.fromFile(file);
                            Intent intent1 = new Intent(Intent.ACTION_VIEW);
                            intent1.setDataAndType(path, "video/*");
                            startActivity(intent1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return 0;
        }

        protected void onPostExecute(String file_url) {

            // Do after downloaded file
            Log.i("File download: ", "background complete: " + output);

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

    private class ProfileImageLongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //TODO your background code

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            // TODO your background code
                                            CheckProfileImageFlagMethod(header);
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }).start();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

    }
}







