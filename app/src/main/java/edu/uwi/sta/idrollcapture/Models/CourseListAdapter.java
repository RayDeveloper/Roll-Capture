package edu.uwi.sta.idrollcapture.Models;

/**
 * Created by Raydon on 3/15/2016.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.uwi.sta.idrollcapture.R;

public class CourseListAdapter extends BaseAdapter {//custom adapter to display courses

    Context context;
    List<courses> courseList;

    public CourseListAdapter(Context context, List<courses> list) {

        this.context = context;
        courseList = list;
    }

    @Override
    public int getCount() {

        return courseList.size();
    }

    @Override
    public Object getItem(int position) {

        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        courses contactListItems = courseList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);//get a reference to layout service
            convertView = inflater.inflate(R.layout.course_view, null);//using custom layout

        }

        TextView coursename = (TextView) convertView.findViewById(R.id.coursename_txtview);
        coursename.setText(contactListItems.getCourse());
        TextView coursecode = (TextView) convertView.findViewById(R.id.coursecode_txtview);
        coursecode.setText(contactListItems.getCode());


        return convertView;
    }

}