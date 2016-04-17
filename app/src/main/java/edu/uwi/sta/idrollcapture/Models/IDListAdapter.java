package edu.uwi.sta.idrollcapture.Models;

/**
 * Created by Raydon on 3/15/2016.
 */

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.uwi.sta.idrollcapture.R;
import edu.uwi.sta.idrollcapture.Register;

public class IDListAdapter extends BaseAdapter {

    Context context;
    List<ID> courseList;

    public IDListAdapter(Context context, List<ID> list) {

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

        // Snackbar.make(convertView, "Inside getView", Snackbar.LENGTH_LONG).show();

        ID contactListItems = courseList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.idnumber_view, null);

        }

        TextView tvSlNo = (TextView) convertView.findViewById(R.id.id_txtview);
        tvSlNo.setText(contactListItems.getID());
        TextView tvName = (TextView) convertView.findViewById(R.id.time_txtview);
        tvName.setText(contactListItems.getTime());
//        TextView Date = (TextView) convertView.findViewById(R.id.date_txtview);
//        Date.setText(contactListItems.getDate());




        return convertView;
    }

}