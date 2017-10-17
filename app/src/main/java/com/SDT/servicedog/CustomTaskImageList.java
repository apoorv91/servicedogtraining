package com.SDT.servicedog;

/**
 * Created by laitkor3 on 07/07/16.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
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


public class CustomTaskImageList extends ArrayAdapter<String> {

    private Activity context;
    ArrayList<String> listimages = new ArrayList<String>();
    ArrayList<String> medianames = new ArrayList<String>();

    StringBuilder responseOutput;
    ProgressDialog progress;
    String resultOutput;
    HashMap<Integer, Bitmap> photomap = new HashMap<Integer, Bitmap>();
    WebView zoomImage;
    ProgressBar progressBar;

    private Toolbar toolbar;
    private View header;
    String TaskListDescclientID, _loginID;
    private DrawerLayout drawerLayout;

    public CustomTaskImageList(Activity context, ArrayList<String> listimages, ArrayList<String> medianames) {
        super(context, R.layout.list_images, listimages);
        this.context = context;
        this.listimages = listimages;
        this.medianames = medianames;

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

            System.out.println("IF CONDITION");

            ImageView profileImageView = (ImageView) header.findViewById(R.id.imageView3);
            profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());

        }else{
            System.out.println("ELSE CONDITION");
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_images_data, null, true);
        ImageView img = (ImageView) listViewItem.findViewById(R.id.thumbImages);
        TextView txtmedia = (TextView) listViewItem.findViewById(R.id.txtMediaNames);
        LinearLayout linearLayout = (LinearLayout) listViewItem.findViewById(R.id.firstLayout);
        ProgressBar progressBar = (ProgressBar) listViewItem.findViewById(R.id.progressBar);


        if (convertView == null) {

            if (photomap.containsKey(position)) {
                img.setImageBitmap(photomap.get(position));
                progressBar.setVisibility(View.INVISIBLE);
//                img.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        Toast.makeText(context, "---" + photomap.get(position).toString() + "---", Toast.LENGTH_SHORT).show();
//                        // ImageZoomPopupMethod(photomap.get(position));
//                    }
//                });

            } else {
                progressBar.setVisibility(View.VISIBLE);
                new SetImageView(getContext(), img, progressBar, position).execute();
            }

            linearLayout.setBackgroundResource(R.color.white);
            txtmedia.setText(medianames.get(position));


        } else {

            if (photomap.containsKey(position)) {
                img.setImageBitmap(photomap.get(position));
                progressBar.setVisibility(View.INVISIBLE);

//                img.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        Toast.makeText(context, "---" + photomap.get(position).toString() + "---", Toast.LENGTH_SHORT).show();
//                        //  ImageZoomPopupMethod(photomap.get(position));
//                    }
//                });


            } else {
                progressBar.setVisibility(View.VISIBLE);
                new SetImageView(getContext(), img, progressBar, position).execute();
            }

            linearLayout.setBackgroundResource(R.color.white);
            txtmedia.setText(medianames.get(position));


        }

        return listViewItem;
    }

    private class SetImageView extends AsyncTask<String, Void, Bitmap> {

        private final Context context;
        ImageView img;
        private final Integer position;
        ProgressBar progressBar;
        Bitmap resized = null, bitmap = null;
        URL url = null;

        public SetImageView(Context c, ImageView img, ProgressBar progressBar, Integer position) {
            this.context = c;
            this.img = img;
            this.position = position;
            this.progressBar = progressBar;
        }

        protected void onPreExecute() {

        }

        protected void onPostExecute(final Bitmap resized) {
            super.onPostExecute(resized);
            img.setImageBitmap(resized);
            progressBar.setVisibility(View.INVISIBLE);
            photomap.put(position, resized);
            System.out.println("photomap position:" + position + "::" + photomap.get(position));

            img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                   // Toast.makeText(context, "---##" + position.toString() + "##---", Toast.LENGTH_SHORT).show();

                    ImageZoomPopupMethod(bitmap,url.toString());
                }
            });
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {

                //  URL url = new URL(listimages.get(position));
                url = new URL(listimages.get(position).replaceAll(" ", "%20"));

                //  System.out.println("image url without encoding: " + listimages.get(position));
                //  System.out.println("\nimage url with replacing: " + url + "\n");
                // System.out.println("\nimage url with url encoding: "+URLEncoder.encode(listimages.get(position),"UTF-8")+ "\n");

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
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //   resized = Bitmap.createScaledBitmap(bitmap,512,512, true);

                resized = bitmap;
                // resized = ResizeBitmapImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return resized;
        }
    }


    public static Bitmap ResizeBitmapImage(Bitmap bitmapImageValue) {

        Bitmap targetBitmap = null;
        AppCompatIntermediateActivity.taskTimerFlag = 0;

        if (bitmapImageValue != null) {

            int targetWidth = 200;
            int targetHeight = 200;
            targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
            BitmapShader shader;
            shader = new BitmapShader(bitmapImageValue, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(shader);
            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();
            path.addCircle(((float) targetWidth) / 2,
                    ((float) targetHeight) / 2,
                    (Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            paint.setStyle(Paint.Style.STROKE);
            canvas.clipPath(path);
            Bitmap sourceBitmap = bitmapImageValue;
            canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                    new Rect(0, 0, targetWidth, targetHeight), null);

            // _profileImageView.setImageBitmap(targetBitmap);   //set the circular image to your imageview
            //captureImageView.setImageBitmap(targetBitmap);
            //System.out.println("gallery image value is: " + targetBitmap);
        }
        return targetBitmap;
    }


    private void ImageZoomPopupMethod(Bitmap bitmapImage,String imageURL) {

        final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customimagezoompopuplayout);
        // Grab the window of the dialog, and change the width
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        lp.width = size.x;
        lp.height = size.y;
        lp.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        // This makes the dialog take up the full width
        window.setAttributes(lp);

        ImageView icon_close = (ImageView) dialog.findViewById(R.id.imageCloseBtn);
        icon_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        zoomImage = (WebView) dialog.findViewById(R.id.imageZoom);
        zoomImage.getSettings().setJavaScriptEnabled(true);
        zoomImage.getSettings().setBuiltInZoomControls(true);
        zoomImage.getSettings().setDisplayZoomControls(false);
        zoomImage.getSettings().setLoadWithOverviewMode(true);
        zoomImage.getSettings().setUseWideViewPort(true);
        zoomImage.loadUrl(imageURL);
        dialog.show();
    }
}