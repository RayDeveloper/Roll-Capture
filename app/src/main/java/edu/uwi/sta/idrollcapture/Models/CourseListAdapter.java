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

public class CourseListAdapter extends BaseAdapter {

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
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.course_view, null);

        }

        TextView tvSlNo = (TextView) convertView.findViewById(R.id.coursename_txtview);
        tvSlNo.setText(contactListItems.getCourse());
        TextView tvName = (TextView) convertView.findViewById(R.id.coursecode_txtview);
        tvName.setText(contactListItems.getCode());


        return convertView;
    }

}