package edu.uwi.sta.idrollcapture;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uwi.sta.idrollcapture.Models.CourseListAdapter;
import edu.uwi.sta.idrollcapture.Models.DBHelper;
import edu.uwi.sta.idrollcapture.Models.ID;
import edu.uwi.sta.idrollcapture.Models.IDListAdapter;
import edu.uwi.sta.idrollcapture.Models.IDsContract;
import edu.uwi.sta.idrollcapture.Models.IDsDBHelper;
import edu.uwi.sta.idrollcapture.Models.courses;

public class Register extends AppCompatActivity {
    String coursename;
    String coursecode;
    TextView coursename_txtview;
    TextView coursecode_txtview;
    List<ID> courseList;
    String table_name;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String course_name;
    String course_code;
    String new_coursecode;
    String new_coursename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




       // Bundle bundle = getIntent().getExtras();
        //if(bundle !=null) {
//        if (bundle.containsKey("coursename")) {
             //coursename = bundle.getString("coursename");
             //coursecode = bundle.getString("coursecode");

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
         course_name = prefs.getString("coursename",null);//"No name defined" is the default value.
         course_code = prefs.getString("coursecode",null);//"No name defined" is the default value.


             coursename_txtview = (TextView) findViewById(R.id.coursename_reg);
            coursename_txtview.setText(course_name);
             coursecode_txtview = (TextView) findViewById(R.id.coursecode_reg);
            coursecode_txtview.setText(course_code);



             new_coursename= course_name.replaceAll("\\s+","");
             new_coursecode= course_code.replaceAll("\\s+","");

            table_name=new_coursename+new_coursecode;

        //}
        //Toast.makeText(Register.this,"TableName"+ table_name, Toast.LENGTH_SHORT).show();

        final ListView listView = (ListView) findViewById(R.id.register_lv);

        courseList = getIDs(table_name);
        IDListAdapter adapter = new IDListAdapter(Register.this, courseList);
        listView.setAdapter(adapter);

//        Intent intent = new Intent();
//        intent.putExtra("coursename", coursename);
//        intent.putExtra("coursecode", coursecode);
//        setResult(2, intent);
//        finish();//finishing activity

    }


    public  List<ID> getIDs(String tablename){
        DBHelper mDbHelper = new DBHelper(Register.this);
        // Gets the data repository in write mode
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //String selectQuery = "SELECT idnumber,time FROM "+ tablename ;
        //Cursor cursor =db.rawQuery(selectQuery, null);
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                IDsContract.IDsEntry.COLUMN_NAME_idnumber,
                IDsContract.IDsEntry.COLUMN_NAME_time

        };

// How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                IDsContract.IDsEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor cursor = db.query(
                tablename,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        List<ID> FavList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ID list = new ID();
                list.setID(cursor.getString(0));
                list.setTime(cursor.getString(1));

                FavList.add(list);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return FavList;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int val=item.getItemId();
//        if(val==R.id.home){
//        //switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            //case android.R.id.home:
//                Intent intent = new Intent(Register.this, scan_home.class);
//            //startActivity(intent);
////            Bundle bundle = new Bundle();
////            bundle.putString("coursename", coursename); // place the position of the selected item
////            bundle.putString("coursecode", coursecode); // place the position of the selected item
////            intent.putExtras(bundle);
//            startActivity(intent);
//                //return true;
//       }
//        return super.onOptionsItemSelected(item);
//    }

//@Override
//public void onBackPressed(){
//    Intent intent = new Intent(Register.this, scan_home.class);
//    Bundle bundle = new Bundle();
//    bundle.putString("coursename", coursename); // place the position of the selected item
//    bundle.putString("coursecode", coursecode); // place the position of the selected item
//    intent.putExtras(bundle);
//    startActivity(intent);
//
//}


}
