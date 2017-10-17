package com.SDT.servicedog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;

/**
 * Created by laitkor on 11/18/16.
 */

public class Common extends AppCompatIntermediateActivity {

    public static Activity activity;

    public static int timerFinishFlag; // 0 for timer started and in background or timer not started yet.

    public static long startTime = 15 * 60 * 1000; // time in milli seconds.(set to 15 minutes)
//    public static long startTime = 1 * 60 * 1000; // time in milli seconds.(for testing)

    public static int taskTimerStartTime = 0;

    public static CountDownTimer countDownTimerObj;

    public static int timerCreateObjFlag = 0;

    public static CountDownTimer countDownTimer = new CountDownTimer(startTime, 1000) {
        @Override
        public void onFinish() {
            //DO WHATEVER YOU WANT HERE

            Log.i("Logout timer value: Session Expired.", "");

//            Intent intentGoLogin = new Intent(activity, MainActivity.class);
//            intentGoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            activity.startActivity(intentGoLogin);

            AlertDialog.Builder alertReturnDateObject = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle);

            alertReturnDateObject.setTitle("");
            alertReturnDateObject.setMessage("Your Session Has Expired.");
            alertReturnDateObject.setCancelable(false);
            alertReturnDateObject.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                    Intent intentGoLogin = new Intent(activity, MainActivity.class);
                    intentGoLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intentGoLogin);
                }
            });
            AlertDialog alertLogin = alertReturnDateObject.create();
            // alertLogin.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertLogin.show();

            Button bq = alertLogin.getButton(DialogInterface.BUTTON_POSITIVE);
            bq.setTextColor(Color.BLACK);


//            Toast  toastObj = Toast.makeText(activity, "Your Session Is Expired.", Toast.LENGTH_LONG);
//            toastObj.setGravity(Gravity.CENTER, 0, 0);
//            toastObj.show();
        }

        @Override
        public void onTick(long millisUntilFinished) {

            Double countdownValue = Math.ceil((millisUntilFinished) / 1000);
            System.out.println("Logout timer value in mili seconds : " + String.valueOf(countdownValue));

        }
    };

    static int counterFlag = 0;

    public void browseSendMail(Activity context) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, Uri.parse("content://media/internal/images/media"));
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        context.startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);

    }
}
