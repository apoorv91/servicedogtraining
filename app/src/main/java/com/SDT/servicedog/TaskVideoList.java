package com.SDT.servicedog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.SDT.servicedog.TaskListsCollectionData.MediaTasks;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class TaskVideoList extends AppCompatIntermediateActivity {

    private ListView listView;

    ProgressDialog progress;
    JSONArray resultOutput, resultOutputMedia;
    String resultOutputMsg;
    int resultStatus;
    List<MediaTasks> fetchjsonData1;
    String programReferenceID;
    StringBuilder responseOutput;
    String _programmeID;
    String _loginID, _clientid;
    String first_name, last_name;
    ArrayList<String> task_name = new ArrayList<String>();
    ArrayList<String> task_description = new ArrayList<String>();
    ArrayList<String> task_status = new ArrayList<String>();
    ArrayList<String> task_id = new ArrayList<String>();
    ArrayList<String> task_videos = new ArrayList<>();
    ArrayList<String> task_thumb_url_images = new ArrayList<>();
    ArrayList<String> media_name = new ArrayList<>();
    ArrayList<String> media_type = new ArrayList<>();
    public static String _utfValue = "";
    private RelativeLayout relativeLayout1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_videos);

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();

        TaskListDescription.activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        /*******************************  TASKVIDEOLIST CLASS STARTED   *****************************************************/
        Intent intent = getIntent();
//
        task_videos = intent.getStringArrayListExtra("TASK_VIDEO_ARRAY");
        task_thumb_url_images = intent.getStringArrayListExtra("TASK_IMAGE_ARRAY");
        media_name = intent.getStringArrayListExtra("MEDIA_NAMES");
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        _utfValue = _programmeID;


        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        progress = new ProgressDialog(TaskVideoList.this, R.style.dialog);
        progress.setMessage("Loading");
        progress.show();
        // new GetData(this).execute();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {

            CustomTaskVideoList customList = new CustomTaskVideoList(TaskVideoList.this, task_thumb_url_images, media_name);
            listView = (ListView) findViewById(R.id.imagelistView);
            listView.setAdapter(customList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent newIntent = new Intent(getApplicationContext(), AboutUsVideoPlay.class);
                    newIntent.putExtra("VIDEO_URL", task_videos.get(i));
                    startActivity(newIntent);
                }
            });

        } else {
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
            relativeLayout1.setVisibility(View.GONE);
            //startActivity(new Intent(this, MainActivity.class));
        }

        progress.dismiss();
        //initNavigationDrawer();
    }
}
