package com.SDT.servicedog;

/**
 * Created by laitkor3 on 07/07/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomFeedBack extends BaseAdapter {

    private Context context;
    private final String[] gridValues;

    //Constructor to initialize values
    public CustomFeedBack(Context context, String[] gridValues) {

        this.context = context;
        this.gridValues = gridValues;

        System.out.println("grid values are: " + gridValues);
    }


    @Override
    public int getCount() {

        // Number of times getView method call depends upon gridValues.length
        return gridValues.length;
    }


    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }


    // Number of times getView method call depends upon gridValues.length

    public View getView(int position, View convertView, ViewGroup parent) {

        // LayoutInflator to call external grid_item.xml file

        // System.out.println("length of grid is: "+getCount());


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {


            gridView = new View(context);

            // get layout from grid_item.xml ( Defined Below )

            gridView = inflater.inflate(R.layout.grid_item, null);

            // set value into textview

            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);

            textView.setText(gridValues[position]);
//            textView.setText("hello java");

            // set image based on selected text

            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            String arrLabel = gridValues[position];

            if (arrLabel.equals("Task Completion")) {

                imageView.setImageResource(R.drawable.completion);

            }
            if (arrLabel.equals("Task not completed,\nI am unwell")) {

                imageView.setImageResource(R.drawable.client_unwell);

            }
            if (arrLabel.equals("Task not completed,\nI am not in a good space")) {

                imageView.setImageResource(R.drawable.client_not_good_space);

            }
            if (arrLabel.equals("Task not completed,\nI have issues in the family")) {

                imageView.setImageResource(R.drawable.client_family_issues);
            }
            if (arrLabel.equals("Task not completed,\nI am on holiday")) {
                //System.out.println("client holiday");
                imageView.setImageResource(R.drawable.client_holiday);
            }
            if (arrLabel.equals("I am requesting help")) {
                imageView.setImageResource(R.drawable.client_help);
            }

        } else {

            gridView = convertView;
        }

        return gridView;
    }
}