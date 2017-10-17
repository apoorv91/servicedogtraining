package com.SDT.servicedog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class TaskDocumentList extends ActivityIntermediateClass {

    ArrayList<String> document_urls = new ArrayList<String>();
    ArrayList<String> document_names = new ArrayList<String>();
    ProgressDialog progressdialog;
    ListView mediaDocumentlistView;

   // public String sendFeedbackEmailId = "amit.rai@laitkor.com";
     public String sendFeedbackEmailId = "assess@servicedogtrainingapp.com.au";
    // public String sendFeedbackEmailId = "apoorv.mehrotra@laitkor.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_document_list);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // code for countdown timer for no activity logout.
//        obj.countDownTimerObj.cancel();
//        obj.countDownTimerObj.start();

        TaskListDescription.activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        System.out.println("/*******************************  TASK DOCUMENT LIST CLASS STARTED   *****************************************************/");

        Intent intent_document = getIntent();
        document_urls = intent_document.getStringArrayListExtra("TASK_DOCUMENT_URLS");
        document_names = intent_document.getStringArrayListExtra("TASK_DOCUMENT_NAMES");

        progressdialog = new ProgressDialog(TaskDocumentList.this, R.style.dialog);
        progressdialog.setMessage("Loading");
        progressdialog.show();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {

            CustomTaskDocumentList customTaskDocumentList = new CustomTaskDocumentList(TaskDocumentList.this, document_urls, document_names);
            mediaDocumentlistView = (ListView) findViewById(R.id.mediaDocumentlistView);
            mediaDocumentlistView.setAdapter(customTaskDocumentList);
            mediaDocumentlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                   code to open & display documents file here.
                    receiveGetFile(document_urls.get(i));

//                    Intent nextscreen5 = new Intent(getApplicationContext(), DocumentPlay.class);
//                    nextscreen5.putExtra("DOCUMENT_URL", document_urls.get(i));
//                    startActivity(nextscreen5);
                }
            });

        } else {
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();

        }

        progressdialog.dismiss();
    }

    private void receiveGetFile(String documentpath) {

        new GetFileData(this, documentpath).execute();
    }

    private class GetFileData extends AsyncTask<String, Void, Void> {

        private final Context context;
        OutputStream output;
        ProgressDialog progress;
        String fileName, documentpath, documentpath_new = null;

        public GetFileData(Context c, String documentpath) {
            this.context = c;
            this.documentpath = documentpath;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context, R.style.dialog);
            progress.setMessage("Loading Document");
            progress.show();

        }

        protected void onPostExecute(Void result) {

            Log.i("File download", "background complete: " + output);

            File file = new File(Environment.getExternalStorageDirectory() + "/serviceDog/" + fileName);
            String filename = file.getName();
            //Intent intent = new Intent(getApplicationContext(), TaskDocumentList.class);
            Uri filetype = Uri.parse(file.getPath());
            Uri path = Uri.fromFile(file);

            if (filename.endsWith(".doc") || filename.endsWith(".DOC") || filename.endsWith(".docx") || filename.endsWith(".DOCX")) {

                System.out.println("---Its a doc file");

                progress.dismiss();
                checkAppInstalled("com.google.android.apps.docs.editors.docs", filename, path);

//                Intent markerIntent = new Intent(Intent.ACTION_VIEW);
//                markerIntent.setDataAndType(path, "application/msword");
//                markerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                progress.dismiss();
//                startActivity(markerIntent);

            } else if (filename.endsWith(".pdf")) {

                System.out.println("---Its a pdf file");

                progress.dismiss();
                checkAppInstalled("com.adobe.reader", filename, path);

//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setDataAndType(path, "application/pdf");
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                progress.dismiss();
//                startActivity(i);
            }
        }

        @Override
        protected Void doInBackground(String... params) {

            fileName = documentpath.substring(documentpath.lastIndexOf('/') + 1);

            String result_value = isFilePresent(fileName);

            if (result_value.equals("false")) {

                // new code that works on both servers.

                try {
                    documentpath_new = documentpath.replaceAll(" ", "%20");
                    URL wallpaperURL = new URL(documentpath_new);
                    URLConnection connection = wallpaperURL.openConnection();
                    InputStream inputStream = new BufferedInputStream(wallpaperURL.openStream(), 10240);
                    File cacheDir = createLocalFolder(TaskDocumentList.this);
                    File cacheFile = new File(cacheDir, fileName);
                    FileOutputStream outputStream = new FileOutputStream(cacheFile);

                    byte buffer[] = new byte[1024];
                    int dataSize;
                    int loadedSize = 0;

                    while ((dataSize = inputStream.read(buffer)) != -1) {
                        loadedSize += dataSize;
                        outputStream.write(buffer, 0, dataSize);
                    }

                    outputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

//                try {
//                    File myDir = new File(Environment.getExternalStorageDirectory() + "/serviceDog");
//                    // create the directory if it doesnt exist
//                    if (!myDir.exists()) {
//                        myDir.mkdirs();
//                    }
//                    // fileName = documentpath.substring(documentpath.lastIndexOf('/') + 1);
//
//                    File outputFile = new File(myDir, fileName);
//
//                    Log.i("File Status:", "Downloading document file in background.");
//
//                    URL url = new URL(documentpath);
//
//                    URLConnection connection = url.openConnection();
//                    connection.connect();
//                    // getting file length
//
//                    int fileLength = connection.getContentLength();
//
//                    // download the file
//
//                    // input stream to read file
//                    InputStream input = new BufferedInputStream(url.openStream());
//
//                    // Output stream to write file
//                    output = new FileOutputStream(outputFile);
//
//                    byte data[] = new byte[1024];
//                    long total = 0;
//                    int count;
//
//                    while ((count = input.read(data)) != -1) {
//
//                        total += count;
//                        output.write(data, 0, count);
//                    }
//
//                    // flushing output
//                    output.flush();
//
//                    Log.i("File download", "complete: " + output);
//                    // progress.dismiss();
//
//                    // closing streams
//                    output.close();
//                    input.close();
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
            return null;
        }

        public File createLocalFolder(Context context) {
            File cacheDir = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheDir = new File(Environment.getExternalStorageDirectory(), "serviceDog");
                if (!cacheDir.isDirectory()) {
                    cacheDir.mkdirs();
                }
            }
            return cacheDir;
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

    public void checkAppInstalled(String packageName, String fileName, Uri path) {
                /*  This code checks for a particular app is installed or not or app is installed and it is disabled or not in either
                    cases user will be redirected to google play store.  */
        //  final String APPPACKAGE = "com.android.chrome";
        final String APPPACKAGE = packageName;
        final String filename = fileName;
        final Uri document_path = path;

        try {
            boolean installed = false;

            // this checks for app installed or not.

            installed = appInstalledOrNot(APPPACKAGE);

            if (installed) {

                Log.i("installed  status: ", "App is installed");

                // this checks for app is disabled or enabled.

                ApplicationInfo ai = getApplicationContext().getPackageManager().getApplicationInfo(APPPACKAGE, 0);
                boolean appStatus = ai.enabled;

                // if else condition to check wether installed app is enabled or disabled if disabled then redirect to play store to enable it.

                if (appStatus) {

                    if (filename.endsWith(".doc") || filename.endsWith(".DOC") || filename.endsWith(".docx") || filename.endsWith(".DOCX")) {

                        Intent markerIntent = new Intent(Intent.ACTION_VIEW);
                        markerIntent.setDataAndType(path, "application/msword");
                        markerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(markerIntent);

                    } else if (filename.endsWith(".pdf")) {

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setDataAndType(path, "application/pdf");
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }

                } else {

                    AlertDialog.Builder alertObject = new AlertDialog.Builder(TaskDocumentList.this, R.style.MyAlertDialogStyle);
                    alertObject.setMessage("Please Enable/Install App Inorder To Open Document.");
                    alertObject.setCancelable(false);
                    alertObject.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            ProgressDialog progress_app = new ProgressDialog(TaskDocumentList.this, R.style.dialog);
                            progress_app.setMessage("Redirecting Play Store");
                            progress_app.show();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APPPACKAGE)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + APPPACKAGE)));
                            }
                            dialog.cancel();
                            // progress_app.dismiss();
                        }
                    });

                    AlertDialog alertLogin = alertObject.create();
                    alertLogin.show();
                    Button bq = alertLogin.getButton(DialogInterface.BUTTON_POSITIVE);
                    bq.setTextColor(Color.BLACK);
                }

            } else {
                Log.i("installed  status: ", "App not installed");
              //  final ProgressDialog progress_app = new ProgressDialog(TaskDocumentList.this, R.style.dialog);
                TaskDocumentList.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            AlertDialog.Builder alertObject_1 = new AlertDialog.Builder(TaskDocumentList.this, R.style.MyAlertDialogStyle);
                            alertObject_1.setMessage("Please Enable/Install the App Inorder To Open Document.");
                            alertObject_1.setCancelable(false);
                            alertObject_1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
//                                    progress_app.setMessage("Redirecting to play store");
//                                    progress_app.show();

                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APPPACKAGE)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + APPPACKAGE)));
                                    }
//                                    try {
//                                        Thread.sleep(2000);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertLogin = alertObject_1.create();
                            alertLogin.show();
                            Button bq = alertLogin.getButton(DialogInterface.BUTTON_POSITIVE);
                            bq.setTextColor(Color.BLACK);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        finally {
//                            progress_app.dismiss();
//                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

         /*  This code checks for a particular app is installed or not or app is installed and it is disabled or not in either
                    cases user will be redirected to google play store.  */
    }

    public boolean appInstalledOrNot(String uri) {

        // uri = "com.android.chrome";
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                ClipData clipData = data.getClipData();
                int attach_fileType_flag = 0;  // 0 = default for images only , 1 = other than image type.
                ArrayList uris = new ArrayList();
                int onlyDocAttachFlag = 0;

                if (clipData != null) {
                    int count = clipData.getItemCount();

                    for (int i = 0; i < count; ++i) {

                        Uri uri_new = clipData.getItemAt(i).getUri();
                        String filename = GetFileName(uri_new);

                        String filenameArray[] = filename.split("\\.");
                        String extension = filenameArray[filenameArray.length - 1];

                        if (filename.endsWith(".doc") || filename.endsWith(".DOC") || filename.endsWith(".docx") || filename.endsWith(".DOCX")) {

                            try {
                                uris.add(uri_new);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            attach_fileType_flag = 1;
                            Toast.makeText(getApplicationContext(), "Please Select Only Word Document Files", Toast.LENGTH_SHORT).show();
                            uris.clear();
                            onlyDocAttachFlag++;
                        }
                        // }
                    }
                    // email code here

                    if (uris != null && onlyDocAttachFlag==0) {

                        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        // set the type to 'email'
                        emailIntent.setType("vnd.android.cursor.dir/email");
                        String to[] = {sendFeedbackEmailId};
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                        // the attachment
                        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                        // the mail subject
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }

                } else {

                    String filename = GetFileName(uri);

                    String filenameArray[] = filename.split("\\.");
                    String extension = filenameArray[filenameArray.length - 1];

                    if (filename.endsWith(".doc") || filename.endsWith(".DOC") || filename.endsWith(".docx") || filename.endsWith(".DOCX")) {

                        uris.add(uri);

                    } else {
                        attach_fileType_flag = 1;
                        Toast toast = Toast.makeText(getApplicationContext(), " Please Select Only Word Document Files ", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 120);
                        toast.show();
                        uris.clear();
                        onlyDocAttachFlag++;

                    }
                    // email code here
                    if (uris != null && onlyDocAttachFlag==0) {

                        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        // set the type to 'email'
                        emailIntent.setType("vnd.android.cursor.dir/email");
                        String to[] = {sendFeedbackEmailId};
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                        // the attachment
                        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                        // the mail subject
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String GetFileName(Uri uri) {
//
        String fileName;
        String path = "";
        String data_path = "";
        // String[] projection = {MediaStore.MediaColumns.DATA};
        String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
        ContentResolver cr = getApplicationContext().getContentResolver();
        try (Cursor metaCursor = cr.query(uri, projection, null, null, null)) {
            if (metaCursor != null) {
                try {
                    if (metaCursor.moveToFirst()) {
                        path = metaCursor.getString(0);
                    }
                } finally {
                    metaCursor.close();
                }
            }
        }
        fileName = path;
        System.out.println("Hello: " + uri);
        System.out.println("Hello: " + path);

        String[] projection_data = {MediaStore.MediaColumns.DATA};

        try (Cursor metaCursor = cr.query(uri, projection_data, null, null, null)) {
            if (metaCursor != null) {
                try {
                    if (metaCursor.moveToFirst()) {
                        data_path = metaCursor.getString(0);
                    }
                } finally {
                    metaCursor.close();
                }
            }
        }
        System.out.println("Hello: " + data_path);
        return fileName;

    }
}
