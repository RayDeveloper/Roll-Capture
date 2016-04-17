package edu.uwi.sta.idrollcapture;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;


import edu.uwi.sta.idrollcapture.Models.CourseContract;
import edu.uwi.sta.idrollcapture.Models.CourseListAdapter;
import edu.uwi.sta.idrollcapture.Models.DBHelper;
import edu.uwi.sta.idrollcapture.Models.SqlHandler;
import edu.uwi.sta.idrollcapture.Models.courses;

public class CourseList extends AppCompatActivity {
    SqlHandler sqlHandler;
    List<courses> courseList;
    TextView coursename_view;
    TextView coursecode_view;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    String courseName;
    String courseCode;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
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

        sqlHandler = new SqlHandler(this);

        final ListView listView = (ListView) findViewById(R.id.courseList_view);
        DBHelper help = new DBHelper(getBaseContext());
        courseList = help.getCourse();
        CourseListAdapter adapter = new CourseListAdapter(CourseList.this, courseList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {



                //coursename_view = (TextView) findViewById(R.id.coursename_txtview);
                //coursecode_view = (TextView) findViewById(R.id.coursecode_txtview);

                courses selectedFromList =(courses) (listView.getItemAtPosition(position));

                 courseName= selectedFromList.getCourse();
               courseCode = selectedFromList.getCode();




                //course_name=coursename_view.getText().toString();
                //course_code=coursecode_view.getText().toString();
               //Toast.makeText(CourseList.this, "List string value :\n" +courseName+"\n"+courseCode, Toast.LENGTH_SHORT).show();


                //textView.getText().toString();
                Intent i = new Intent(CourseList.this, scan_home.class);
                Bundle bundle = new Bundle();
                bundle.putString("coursename",courseName); // place the position of the selected item
                bundle.putString("coursecode",courseCode); // place the position of the selected item
                i.putExtras(bundle);
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("coursename", courseName);
                editor.putString("coursecode", courseCode);
                editor.apply();
                startActivity(i);


                //int value = (int)adapter.getItemAtPosition(position);s
//                String selectedFromList =(String) (adapter.getItemAtPosition(position));
                //Toast.makeText(CourseList.this,"Pos:"+position, Toast.LENGTH_SHORT).show();
                //Toast.makeText(CourseList.this,coursecode_view.getText().toString(), Toast.LENGTH_SHORT).show();


                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                courses selectedFromList =(courses) (listView.getItemAtPosition(pos));

                courseName= selectedFromList.getCourse();
                courseCode = selectedFromList.getCode();


                //also delete table too
                new AlertDialog.Builder(CourseList.this)
                        .setTitle("Delete Course")
                        .setMessage("Are you sure you want to delete this course? The register will also be deleted.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper mDbHelper = new DBHelper(CourseList.this);
                                final SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                //delete by course code instead or name
                                //fix delete to use the correct way of deleting
                                String sql = "DELETE FROM " +
                                        " course " +
                                        " WHERE " + "coursename" +
                                        " LIKE '" + courseName + "'"+" and "+ " coursecode "+ " LIKE '" + courseCode+ "' ;";
                                db.execSQL(sql);
                                String new_coursename=courseName.replaceAll("\\s+","");
                                String new_coursecode=courseCode.replaceAll("\\s+","");
                                String table_name=new_coursename+new_coursecode;
                                String delsql="DROP TABLE '"+ table_name +"';";
                                db.execSQL(delsql);
                                db.close();
                                restartActivity();

                                //Toast.makeText(CourseList.this,"Course deleted at :\n"+"POS: "+newpos+"\n"+"ID: "+id, Toast.LENGTH_SHORT).show();
                                Toast.makeText(CourseList.this, "Course deleted at :\n" + courseName, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                //I have to add a dialog box to confirm deleting.
                //coursename_view = (TextView) findViewById(R.id.coursename_txtview);
                //coursecode_view = (TextView) findViewById(R.id.coursecode_txtview);



                //int newpos = pos + 1;

//                DBHelper mDbHelper = new DBHelper(CourseList.this);
//                final SQLiteDatabase db = mDbHelper.getWritableDatabase();
////delete by course code instead or name
//                //fix delete to use the correct way of deleting
//                String sql = "DELETE FROM " +
//                        " course " +
//                        " WHERE " + "coursename" +
//                        " LIKE '" + courseName + "';";
//                db.execSQL(sql);
//                restartActivity();
//
//                //Toast.makeText(CourseList.this,"Course deleted at :\n"+"POS: "+newpos+"\n"+"ID: "+id, Toast.LENGTH_SHORT).show();
//                Toast.makeText(CourseList.this, "Course deleted at :\n" + courseName, Toast.LENGTH_SHORT).show();


                return true;
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


//    @Override
//    public void onBackPressed() {
//
//        Intent intent = new Intent(CourseList.this, MainActivity.class);
//        //startActivity(intent);
////            Bundle bundle = new Bundle();
////            bundle.putString("coursename", coursename); // place the position of the selected item
////            bundle.putString("coursecode", coursecode); // place the position of the selected item
////            intent.putExtras(bundle);
////            //startActivityForResult(intent, 2);
//        startActivity(intent);
//
//        super.onBackPressed();
//    }

}



