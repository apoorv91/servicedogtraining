package com.SDT.servicedog;

/**
 * Created by laitkor3 on 07/07/16.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomTaskReminders extends ArrayAdapter<String> {

    private Activity context;
    ArrayList<String> listTasks = new ArrayList<String>();

    String fetchRefID;
    StringBuilder responseOutput;
    ProgressDialog progress;
    String resultOutput;


    public String getFetchRefID() {
        return fetchRefID;
    }

    public void setFetchRefID(String fetchRefID) {
        this.fetchRefID = fetchRefID;
    }


    public CustomTaskReminders(Activity context, ArrayList<String> listTasks) {
        super(context, R.layout.content_main, listTasks);
        this.context = context;
        this.listTasks = listTasks;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_task_reminders, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        LinearLayout linearLayout = (LinearLayout) listViewItem.findViewById(R.id.firstLayout);


        //System.out.println("client_id" + _clientID);
        //TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        //ImageView image = (ImageView) listViewItem.findViewById(R.id.imageView);
        if (position % 2 != 0) {
            linearLayout.setBackgroundResource(R.color.sky_blue);


        } else {
            linearLayout.setBackgroundResource(R.color.white);

        }

        textViewName.setText(listTasks.get(position));


        return listViewItem;
    }

}