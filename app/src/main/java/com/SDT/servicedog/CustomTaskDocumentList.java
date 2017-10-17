package com.SDT.servicedog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by laitkor on 06/09/16.
 */
public class CustomTaskDocumentList extends ArrayAdapter<String> {

    Activity context;
    ArrayList<String> documentUrls = new ArrayList<String>();
    ArrayList<String> documentNames = new ArrayList<String>();
    HashMap<Integer, String> documentmap = new HashMap<Integer, String>();

    private Toolbar toolbar;
    private View header;
    String TaskListDescclientID, _loginID;
    private DrawerLayout drawerLayout;
//    AppCompatActivity object = new AppCompatActivity();


    public CustomTaskDocumentList(Activity context, ArrayList<String> document_urls, ArrayList<String> document_names) {
        super(context, R.layout.activity_task_document_list, document_urls);
        this.context = context;
        this.documentUrls = document_urls;
        this.documentNames = document_names;

        System.out.println("/******************* Custom Task Document List page *****************************/");

        System.out.println("document urls array: " + documentUrls);
        System.out.println("\n document names array: " + documentNames);

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

        LayoutInflater layoutinflater = context.getLayoutInflater();
        View listViewItem = layoutinflater.inflate(R.layout.list_videos_data, null, true);
        ImageView img = (ImageView) listViewItem.findViewById(R.id.thumb_image);
        TextView txtmedia = (TextView) listViewItem.findViewById(R.id.txtmedianame);
        LinearLayout linearLayout = (LinearLayout) listViewItem.findViewById(R.id.firstLayout);
        ProgressBar progressBar = (ProgressBar) listViewItem.findViewById(R.id.progressBar);
        TextView sendBtn = (TextView) listViewItem.findViewById(R.id.sendfeedbackBtn);

        img.getLayoutParams().width = 60;
        img.getLayoutParams().height = 60;
        final Common commonObj = new Common();

        if (convertView == null) {

            String extension = documentUrls.get(position).substring(documentUrls.get(position).lastIndexOf("."));

            System.out.println("file extension is:" + extension);

            if (extension.equals(".pdf")) {

                sendBtn.setVisibility(View.GONE);

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pdf_icon);
                img.setImageBitmap(bitmap);
            } else {

                sendBtn.setVisibility(View.VISIBLE);

                sendBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        commonObj.browseSendMail(context);

                    }
                });

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.word_icon);
                img.setImageBitmap(bitmap);
            }

            progressBar.setVisibility(View.INVISIBLE);

            if (position % 2 != 0) {

                linearLayout.setBackgroundResource(R.color.white);

            } else {

                linearLayout.setBackgroundResource(R.color.sky_blue);

            }
            txtmedia.setText(documentNames.get(position));

            return listViewItem;

        } else {

            String extension = documentUrls.get(position).substring(documentUrls.get(position).lastIndexOf("."));

            System.out.println("file extension is:" + extension);

            if (extension.equals(".pdf")) {

                sendBtn.setVisibility(View.GONE);

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pdf_icon);
                img.setImageBitmap(bitmap);
            } else {

                sendBtn.setVisibility(View.VISIBLE);

                sendBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

//                        File file = new File(Environment.getExternalStorageDirectory() + "/serviceDog/" + "pdf.pdf");
//                        Uri path = Uri.fromFile(file);
//
//                        File file1 = new File(Environment.getExternalStorageDirectory() + "/serviceDog/" + "testing.docx");
//                        Uri path1 = Uri.fromFile(file1);
//
//                        ArrayList uris = new ArrayList();
//                        uris.add(path);
//                        uris.add(path1);
//
//                        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
//                        // set the type to 'email'
//                        emailIntent.setType("vnd.android.cursor.dir/email");
//                        String to[] = {"apoorv.mehrotra@laitkor.com"};
//                        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
//                        // the attachment
//                        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//
////                        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
////                        emailIntent.putExtra(Intent.EXTRA_STREAM, path1);
//                        // the mail subject
//                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));

                        //  Toast.makeText(getContext(), "button clickedyyyy! ", Toast.LENGTH_SHORT).show();

                        commonObj.browseSendMail(context);
                    }
                });


                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.word_icon);
                img.setImageBitmap(bitmap);
            }

            progressBar.setVisibility(View.INVISIBLE);

            if (position % 2 != 0) {

                linearLayout.setBackgroundResource(R.color.white);

            } else {

                linearLayout.setBackgroundResource(R.color.sky_blue);

            }
            txtmedia.setText(documentNames.get(position));

            return listViewItem;
        }

    }

}


