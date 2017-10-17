package com.SDT.servicedog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;

public class StorylinePlay extends ActivityIntermediateClass {

    ProgressDialog progress;
    URL url = null;
    String _STORYLINE_URL = null;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storyline_play);

        System.out.println("/*******************************  STORYLINE PLAY CLASS STARTED  *****************************************************/");

        TaskListDescription.activityTaskTimer = this;
        AppCompatIntermediateActivity.taskTimerFlag = 1;

        Intent intent = getIntent();
        _STORYLINE_URL = intent.getStringExtra("STORYLINE_URL");
        System.out.println("Storyline url is--- : " + _STORYLINE_URL);

        webView = (WebView) findViewById(R.id.webViewStoryline);
        initNavigationDrawer();

    }

    public void initNavigationDrawer() {
        receiveGetFile(_STORYLINE_URL);
    }

    private void receiveGetFile(String storylinePath) {
        new StorylinePlay.GetFileData(this, storylinePath).execute();
    }

    private class GetFileData extends AsyncTask<String, Void, Void> {

        private final Context context;
        String storylinePath = null;

        public GetFileData(Context c, String storylinePath) {
            this.context = c;
            this.storylinePath = storylinePath;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(StorylinePlay.this, R.style.dialog);
            progress.setMessage("Loading Storyline \n please wait...");
            progress.show();

        }

        protected void onPostExecute(Void result) {

//            webView.setWebChromeClient(new WebChromeClient());
//            webView.setWebViewClient(new WebViewClient());
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.loadUrl("http://servdog.dealopia.com/uploads/storyline/Etiquette/story_html5.html");
            //  webView.loadDataWithBaseURL("http://servdog.dealopia.com/uploads/storyline/CanineHusbandry/", "story_html5.html", "text/html", "UTF-8", null);
            WindowManager wm = (WindowManager) context.getSystemService(StorylinePlay.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels;
            final int height = metrics.heightPixels;

            //   System.out.println("Window Screen Size: height : " + height + " and width : " + width);
            WebSettings settings = webView.getSettings();
            settings.setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            // webView.setWebChromeClient(new WebChromeClient());
            // webView.setWebViewClient(new WebViewClient());
            webView.setInitialScale(10);

//                        webView.loadData("<meta name='viewport' content='width=320, initial-scale=1.0, maximum-scale=1.0, user-scalable=yes'/>" +
//                    "<meta name='viewport' content='width=device-width; initial-scale=1.0; maximum-scale=1.0; minimum-scale=1.0; user-scalable=0;'/>" +
//                    "<meta name='viewport' content='width=device-width, target-densityDpi=medium-dpi'/>" +
//                    "<iframe width='100%' src=" + storylinePath + "frameborder='0' style='margin: 0; padding: 0;'></iframe>", "text/html", "utf-8");
//             webView.loadData("<iframe width='100%' height='" + height + "px;' frameborder=0 border=0 src=" + storylinePath + "></iframe>", "text/html", "utf-8");

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    progress.dismiss();
                    //    System.out.println("Storyline Url===: " + "<iframe width='100%' height='" + height + "px;' frameborder=0 border=0 align='center' src=" + storylinePath + "></iframe>");
                    super.onPageFinished(view, url);
                }
            });
            webView.loadUrl(storylinePath);


            // progress.dismiss();
        }

        @Override
        protected Void doInBackground(String... params) {

            return null;
        }

    }
}
