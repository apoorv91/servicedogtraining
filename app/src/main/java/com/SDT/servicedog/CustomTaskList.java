package com.SDT.servicedog;

/**
 * Created by laitkor3 on 07/07/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomTaskList extends ArrayAdapter<String> {

    private Activity context;
    ArrayList<String> listTasks = new ArrayList<String>();
    ArrayList<String> task_status = new ArrayList<String>();
    ArrayList<String> task_description = new ArrayList<String>();
    ArrayList<String> taskIds = new ArrayList<String>();

    String fetchRefID, _currentProgrammeID;
    StringBuilder responseOutput;
    ProgressDialog progress;
    String resultOutput;

    public String getFetchRefID() {
        return fetchRefID;
    }

    public void setFetchRefID(String fetchRefID) {
        this.fetchRefID = fetchRefID;
    }


    public CustomTaskList(Activity context, ArrayList<String> listTasks, ArrayList<String> task_status, ArrayList<String> task_description, ArrayList<String> taskIds, String _programmeID) {
        super(context, R.layout.content_main, listTasks);
        this.context = context;
        this.listTasks = listTasks;
        this.task_status = task_status;
        this.task_description = task_description;
        this.taskIds = taskIds;
        this._currentProgrammeID = _programmeID;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_task_data, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView taskStatusImage = (TextView) listViewItem.findViewById(R.id.taskStatusImage);
        TextView textSerialNumber = (TextView) listViewItem.findViewById(R.id.textSerialNumber);
        //TextView taskStatus = (TextView) listViewItem.findViewById(R.id.textViewStatus);
        LinearLayout linearLayout = (LinearLayout) listViewItem.findViewById(R.id.firstLayout);


        //System.out.println("client_id" + _clientID);
        //TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        //ImageView image = (ImageView) listViewItem.findViewById(R.id.imageView);
        if (position % 2 != 0) {
            linearLayout.setBackgroundResource(R.color.sky_blue);
            textSerialNumber.setBackgroundResource(R.drawable.sky_circle);


        } else {
            linearLayout.setBackgroundResource(R.color.white);
            textSerialNumber.setBackgroundResource(R.drawable.gray_circle);

        }

        if (task_status.get(position).equals("active")) {

            taskStatusImage.setBackgroundResource(R.drawable.active_status_image);

            SharedPreferences haredpreferences = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedEditor = haredpreferences.edit();
            sharedEditor.putString("LOGIN_PROGRAMME_ID", _currentProgrammeID);
            sharedEditor.putString("LOGIN_TASK_ID", taskIds.get(position).toString());
            sharedEditor.putString("LOGIN_TASK_STATUS", task_status.get(position).toString());
            sharedEditor.putString("LOGIN_TASK_NAME", listTasks.get(position).toString());
            sharedEditor.commit();


        } else if (task_status.get(position).equals("inactive")) {

            taskStatusImage.setBackgroundResource(R.drawable.inactive_status_button);
        } else if (task_status.get(position).equals("complete")) {

            taskStatusImage.setBackgroundResource(R.drawable.inactive_status_button);
        } else if (task_status.get(position).equals("expired")) {

            SharedPreferences haredpreferences = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedEditor = haredpreferences.edit();
            sharedEditor.putString("LOGIN_PROGRAMME_ID", _currentProgrammeID);
            sharedEditor.putString("LOGIN_TASK_ID", taskIds.get(position).toString());
            sharedEditor.putString("LOGIN_TASK_STATUS", task_status.get(position).toString());
            sharedEditor.putString("LOGIN_TASK_NAME", listTasks.get(position).toString());
            sharedEditor.commit();

            taskStatusImage.setBackgroundResource(R.drawable.expired_status_image);

        }else if (task_status.get(position).equals("preview")) {

            taskStatusImage.setBackgroundResource(R.drawable.active_status_image);

            SharedPreferences haredpreferences = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedEditor = haredpreferences.edit();
            sharedEditor.putString("LOGIN_PROGRAMME_ID", _currentProgrammeID);
            sharedEditor.putString("LOGIN_TASK_ID", taskIds.get(position).toString());
            sharedEditor.putString("LOGIN_TASK_STATUS", task_status.get(position).toString());
            sharedEditor.putString("LOGIN_TASK_NAME", listTasks.get(position).toString());
            sharedEditor.commit();

        }

        System.out.println("task status are : " + task_status);
        textViewName.setText(listTasks.get(position));
        textSerialNumber.setText(Integer.toString(position + 1));
        //taskStatus.setText(task_status.get(position));

        return listViewItem;
    }
}