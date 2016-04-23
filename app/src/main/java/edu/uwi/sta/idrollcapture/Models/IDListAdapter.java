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

public class IDListAdapter extends BaseAdapter {//custom adapter to display IDs

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


        ID contactListItems = courseList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);//get a reference to layout service
            convertView = inflater.inflate(R.layout.idnumber_view, null);//using custom layout



        }

        TextView id_txtview = (TextView) convertView.findViewById(R.id.id_txtview);
        id_txtview.setText(contactListItems.getID());
        TextView time_txtview = (TextView) convertView.findViewById(R.id.time_txtview);
        time_txtview.setText(contactListItems.getTime());




        return convertView;
    }

}