package com.SDT.servicedog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskAudioList extends AppCompatIntermediateActivity {

    ProgressDialog progress;

    ArrayList<String> audio_urls = new ArrayList<String>();
    ArrayList<String> audio_names = new ArrayList<String>();
    ListView mediaAudiolistView;
    String _clientId, _programmeId, _taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_audio_list);

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();

        TaskListDescription.activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        System.out.println("/*******************************  TASK AUDIO LIST CLASS STARTED   *****************************************************/");

        Intent intent_audio = getIntent();
        audio_urls = intent_audio.getStringArrayListExtra("TASK_AUDIO_URLS");
        audio_names = intent_audio.getStringArrayListExtra("TASK_AUDIO_NAMES");
        _clientId = intent_audio.getStringExtra("CLIENT_ID");
        _programmeId = intent_audio.getStringExtra("PROGRAMME_ID");
        _taskId = intent_audio.getStringExtra("TASK_ID");


        progress = new ProgressDialog(TaskAudioList.this, R.style.dialog);
        progress.setMessage("Loading");
        progress.show();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {

            CustomTaskAudioList customAudioList = new CustomTaskAudioList(TaskAudioList.this, audio_urls, audio_names);
            mediaAudiolistView = (ListView) findViewById(R.id.mediaAudiolistView);
            mediaAudiolistView.setAdapter(customAudioList);
            mediaAudiolistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    // code to play audio file here
                    Intent newIntent = new Intent(getApplicationContext(), AudioPlay.class);
                    newIntent.putExtra("AUDIO_URL", audio_urls.get(i));
                    newIntent.putExtra("CLIENT_ID", _clientId);
                    newIntent.putExtra("PROGRAMME_ID", _programmeId);
                    newIntent.putExtra("TASK_ID", _taskId);
                    startActivity(newIntent);

                }
            });

        } else {
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
            //startActivity(new Intent(this, MainActivity.class));
        }

        progress.dismiss();
    }
}
