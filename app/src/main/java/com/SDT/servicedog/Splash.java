package com.SDT.servicedog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by laitkor3 on 30/06/16.
 */


public class Splash extends Activity {

//    String lastLoginId, lastLoginPassword, lastLoginClientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

//        SharedPreferences loginpreference = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
//        lastLoginId = loginpreference.getString("email_id", "");
//        lastLoginPassword = loginpreference.getString("password", "");
//        lastLoginClientId = loginpreference.getString("CLIENT_ID", "");

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

//                    if (!lastLoginId.toString().equals("") && !lastLoginPassword.toString().equals("") && !lastLoginClientId.toString().equals("")) {
//                        Intent openOtherActivity = new Intent("com.laitkor.servicedog.TRAININGPROGRAMMES");
//                        startActivity(openOtherActivity);
//
//                    } else {
                        Intent openMainActivity = new Intent("com.laitkor.servicedog.MAINACTIVITY");
                        startActivity(openMainActivity);
//                    }

                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}