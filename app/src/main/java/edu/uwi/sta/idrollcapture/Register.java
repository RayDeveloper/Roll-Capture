package edu.uwi.sta.idrollcapture;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uwi.sta.idrollcapture.Models.CourseListAdapter;
import edu.uwi.sta.idrollcapture.Models.DBHelper;
import edu.uwi.sta.idrollcapture.Models.ID;
import edu.uwi.sta.idrollcapture.Models.IDListAdapter;
import edu.uwi.sta.idrollcapture.Models.IDsContract;
import edu.uwi.sta.idrollcapture.Models.IDsDBHelper;
import edu.uwi.sta.idrollcapture.Models.courses;

public class Register extends AppCompatActivity {
    TextView coursename_txtview;
    TextView coursecode_txtview;
    List<ID> courseList;
    String table_name;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String course_name;
    String course_code;
    String new_coursecode;
    String new_coursename;
    IDListAdapter adapter;
    ListView listView;
    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getPrefs();//get the preferences saved in scan_home
        getIDsAsync IDASync=new getIDsAsync();//create a new instance of getIDsAsync
        IDASync.execute("");//execute the asyncTask







    }

    public void getPrefs(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        course_name = prefs.getString("coursename",null);//"No name defined" is the default value.
        course_code = prefs.getString("coursecode",null);//"No name defined" is the default value.


        coursename_txtview = (TextView) findViewById(R.id.coursename_reg);
        coursename_txtview.setText(course_name);
        coursecode_txtview = (TextView) findViewById(R.id.coursecode_reg);
        coursecode_txtview.setText(course_code);

        new_coursename= course_name.replaceAll("\\s+","_");//replaces spaces with underscores
        new_coursecode= course_code.replaceAll("\\s+","_");//replaces spaces with underscores


        table_name=new_coursename+new_coursecode;//concatenates the two to make table name

    }




private class getIDsAsync extends AsyncTask<String,Void,String>{//AsyncTask to retrieve ID's


    @Override
    protected String doInBackground(String... params) {

            listView=(ListView) findViewById(R.id.register_lv);
            List<ID> FavList = new ArrayList<>();

            DBHelper mDbHelper = new DBHelper(Register.this);
            final SQLiteDatabase db = mDbHelper.getReadableDatabase();



            String[] projection = {
                    IDsContract.IDsEntry.COLUMN_NAME_idnumber,
                    IDsContract.IDsEntry.COLUMN_NAME_time

            };


            Cursor cursor = db.query(
                    table_name,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if (cursor.moveToFirst()) {
                do {
                    ID list = new ID();
                    list.setID(cursor.getString(0));//first column of query
                    list.setTime(cursor.getString(1));//second column query

                    FavList.add(list);
                } while (cursor.moveToNext());
            }
            db.close();
            cursor.close();
            courseList=FavList;

         adapter = new IDListAdapter(Register.this, courseList);


        Log.v(TAG, "doInBackGround method");



        return null;
    }


    @Override
    protected void onPostExecute(String result) {//always returns void

           listView.setEmptyView(findViewById(android.R.id.empty));

           listView.setAdapter(adapter);

        Log.v(TAG, "OnPostExecute method");



    }
}



}
