package com.SDT.servicedog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskStoryLine extends ActivityIntermediateClass {

    ListView storylineListView;
    ProgressDialog progressdialog;
    RelativeLayout relativeLayout1;

    ArrayList<String> storyline_urls = new ArrayList<String>();
    ArrayList<String> storyline_names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_story_line);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        storylineListView = (ListView) findViewById(R.id.mediaStorylineListView);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);

        // code for countdown timer for no activity logout.
//        obj.countDownTimerObj.cancel();
//        obj.countDownTimerObj.start();

        TaskListDescription.activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        System.out.println("/*******************************  TASK STORYLINE LIST CLASS STARTED   *****************************************************/");

        Intent intent_storyline = getIntent();
        storyline_urls = intent_storyline.getStringArrayListExtra("TASK_STORY_URLS");
        storyline_names = intent_storyline.getStringArrayListExtra("TASK_STORY_NAMES");

        progressdialog = new ProgressDialog(TaskStoryLine.this, R.style.dialog);
        progressdialog.setMessage("Loading");
        progressdialog.show();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {

            CustomTaskStorylineList customTaskStorylineList = new CustomTaskStorylineList(TaskStoryLine.this, storyline_urls, storyline_names);
            storylineListView.setAdapter(customTaskStorylineList);
            storylineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Intent nextscreen5 = new Intent(getApplicationContext(), StorylinePlay.class);
                    //nextscreen5.putExtra("DOCUMENT_URL", media_storyline_url);
                    nextscreen5.putExtra("STORYLINE_URL", storyline_urls.get(position).toString());
                    startActivity(nextscreen5);

                }
            });

        } else {
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
            relativeLayout1.setVisibility(View.GONE);
        }
        progressdialog.dismiss();
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

}
