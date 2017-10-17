package com.SDT.servicedog;

/**
 * Created by laitkor3 on 07/07/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class CustomTaskVideoList extends ArrayAdapter<String> {

    private Activity context;
    ArrayList<String> listvideos = new ArrayList<String>();
    ArrayList<String> medianames = new ArrayList<String>();

    StringBuilder responseOutput;
    ProgressDialog progress;
    String resultOutput;
    HashMap<Integer, Bitmap> photomap = new HashMap<Integer, Bitmap>();

    private Toolbar toolbar;
    private View header;
    String TaskListDescclientID, _loginID;
    private DrawerLayout drawerLayout;


    public CustomTaskVideoList(Activity context, ArrayList<String> listvideos, ArrayList<String> medianames) {
        super(context, R.layout.list_videos, listvideos);
        this.context = context;
        this.listvideos = listvideos;
        this.medianames = medianames;

        System.out.println("/******************* CustomTaskVideoList page *****************************/");

        System.out.println("list video images array: " + listvideos);
        System.out.println("\nlist video names: " + medianames);

        toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        initNavigationDrawer();
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) context.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:
                        Intent aboutusintent = new Intent(context, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(aboutusintent);
//                        Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(context, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(context, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(context, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        //Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();

                        Intent settingIintent = new Intent(context, Settings.class);
                        settingIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(settingIintent);

                        break;
                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(context, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(context, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID", _loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        context.finish(); // Call once you redirect to another activity

                }
                return true;
            }
        });

        SharedPreferences haredpreferences = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
        tv_email.setText(haredpreferences.getString("first_name", "Null") + " " + haredpreferences.getString("last_name", "Null"));

        TaskListDescclientID = haredpreferences.getString("CLIENT_ID", "Null");

        System.out.println("training prog lcient id shared prefernce" + TaskListDescclientID);
        // System.out.println("shared preference values are: "+"first="+haredpreferences.getString("first_name", "Null")+"last="+haredpreferences.getString("last_name", "Null")+"client="+haredpreferences.getString("CLIENT_ID", "Null")+"phone="+haredpreferences.getString("phone_number", "Null")+"email="+haredpreferences.getString("email_id", "Null"));
        drawerLayout = (DrawerLayout) context.findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(context, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TaskListDescription.activityTaskTimer = context;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_videos_data, null, true);
        ImageView img = (ImageView) listViewItem.findViewById(R.id.thumb_image);
        ImageView videoPlayImg = (ImageView) listViewItem.findViewById(R.id.imageView6);
        TextView txtmedia = (TextView) listViewItem.findViewById(R.id.txtmedianame);
        LinearLayout linearLayout = (LinearLayout) listViewItem.findViewById(R.id.firstLayout);
        ProgressBar progressBar = (ProgressBar) listViewItem.findViewById(R.id.progressBar);

        if (convertView == null) {
            if (photomap.containsKey(position)) {
                img.setImageBitmap(photomap.get(position));
                videoPlayImg.setBackgroundResource(R.drawable.play_icon);
                progressBar.setVisibility(View.INVISIBLE);

            } else {
                progressBar.setVisibility(View.VISIBLE);
                new SetImageView(getContext(), img, progressBar, position).execute();
            }

            if (position % 2 != 0) {

                linearLayout.setBackgroundResource(R.color.white);

            } else {

                linearLayout.setBackgroundResource(R.color.sky_blue);

            }

            // setting the thumb image for videos
            // img.setImageResource(Integer.parseInt(listvideos.get(position)));


            txtmedia.setText(medianames.get(position));


            return listViewItem;

        } else {
            if (photomap.containsKey(position)) {
                img.setImageBitmap(photomap.get(position));
                videoPlayImg.setBackgroundResource(R.drawable.play_icon);
                progressBar.setVisibility(View.INVISIBLE);

            } else {

                progressBar.setVisibility(View.VISIBLE);
                new SetImageView(getContext(), img, progressBar, position).execute();
            }

            if (position % 2 != 0) {

                linearLayout.setBackgroundResource(R.color.white);

            } else {

                linearLayout.setBackgroundResource(R.color.sky_blue);
            }
            // setting the thumb image for videos
            // img.setImageResource(Integer.parseInt(listvideos.get(position)));

            txtmedia.setText(medianames.get(position));

            return listViewItem;
        }
    }

    private class SetImageView extends AsyncTask<String, Void, Bitmap> {

        private final Context context;
        ImageView img;
        private final Integer position;
        ProgressBar progressBar;

        public SetImageView(Context c, ImageView img, ProgressBar progressBar, Integer position) {
            this.context = c;
            this.img = img;
            this.position = position;
            this.progressBar = progressBar;
        }

        protected void onPreExecute() {

        }

        protected void onPostExecute(Bitmap resized) {
            super.onPostExecute(resized);

            img.setImageBitmap(resized);
            progressBar.setVisibility(View.INVISIBLE);
            photomap.put(position, resized);
            System.out.println("photomap position:" + position + "::" + photomap.get(position));
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap resized = null;
            try {

                URL url = new URL(listvideos.get(position));

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
                resized = Bitmap.createScaledBitmap(bitmap, 120, 120, true);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return resized;
        }
    }
}