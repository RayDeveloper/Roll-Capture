package edu.uwi.sta.idrollcapture;

/**
 * Created by Raydon on 3/18/2016.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import edu.uwi.sta.idrollcapture.Models.DBHelper;

public class scan_home extends AppCompatActivity  {
    Button button;
    String coursename;
    String coursecode;
    String filename;
    int numberofIDS=0;
    String TotalIDs="";
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_home);
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


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String course_name = prefs.getString("coursename",null);//"No name defined" is the default value.
            String course_code = prefs.getString("coursecode",null);//"No name defined" is the default value.
            TextView coursename_txtview = (TextView) findViewById(R.id.coursename_txtview);
            coursename_txtview.setText(course_name);
            TextView coursecode_txtview = (TextView) findViewById(R.id.coursecode_txtview);
            coursecode_txtview.setText(course_code);


        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
//        if (bundle.containsKey("coursename")) {
            coursename = bundle.getString("coursename");
             coursecode = bundle.getString("coursecode");
            String new_coursename=coursename.replaceAll("\\s+","");
            String new_coursecode=coursecode.replaceAll("\\s+","");
             filename=new_coursename+new_coursecode;

//            TextView coursename_txtview = (TextView) findViewById(R.id.coursename_txtview);
//            coursename_txtview.setText(coursename);
//            TextView coursecode_txtview = (TextView) findViewById(R.id.coursecode_txtview);
//            coursecode_txtview.setText(coursecode);

            //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);


            // Toast.makeText(scan_home.this, coursename, Toast.LENGTH_SHORT).show();
        }




        final ImageButton StartCamera = (ImageButton) findViewById(R.id.StartCamera);
        StartCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(scan_home.this, ContinuousCaptureActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("coursename",coursename); // place the position of the selected item
//                bundle.putString("coursecode", coursecode); // place the position of the selected item
//                intent.putExtras(bundle);
                startActivity(intent);


            }


        });

        final ImageButton Register = (ImageButton) findViewById(R.id.Register);
        Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(scan_home.this, Register.class);
                //startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("coursename", coursename); // place the position of the selected item
                bundle.putString("coursecode", coursecode); // place the position of the selected item
                intent.putExtras(bundle);
                //startActivityForResult(intent, 2);
                startActivity(intent);

//                Bundle b=MainActivity.this.getIntent().getExtras();
//                String[] array=b.getStringArray("ARRAY_LIST");
//                String str = Arrays.toString(array);
//                Toast.makeText(MainActivity.this,"Register so far\n"+str, Toast.LENGTH_SHORT).show();
            }

        });
        final ImageButton Export = (ImageButton) findViewById(R.id.Export);
        Export.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new AlertDialog.Builder(scan_home.this)
                        .setTitle("Export Register")
                        .setMessage("Are you sure you want to export the register.This may take a while depending on the number of students scanned.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper mDbHelper = new DBHelper(scan_home.this);
                                // Gets the data repository in write mode
                                final SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                Cursor cursor = db.query(filename, new String[]{"idnumber", "time"}, null, null, null, null, null);
                                //cursor is now at the first result returned by this query
                                String fileHeading=coursename+" "+coursecode+"\n";
                                writeToFile(fileHeading, filename);
                                writeToFile("StudentID         Time Scanned", filename);
                                //writeToFile("Time Scanned        ", filename);

                                if (cursor.moveToFirst()) {
                                    do {
                                        String db_idnumber = cursor.getString(cursor.getColumnIndex("idnumber"));
                                        String db_time = cursor.getString(cursor.getColumnIndex("time"));
                                        String line = db_idnumber +" "+ db_time;
                                        writeToFile(line, filename);
                                        numberofIDS++;
                                        //do something with name
                                    }
                                    while (cursor.moveToNext()); //loop will terminate when all the rows are exhausted
                                }
                                TotalIDs=TotalIDs+numberofIDS;
                                writeToFile("Total Number of Students: "+TotalIDs, filename);

                                //DBHelper mDbHelper = new DBHelper(CourseList.this);
                                //final SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                //delete by course code instead or name
                                //fix delete to use the correct way of deleting
                                // String sql = "DELETE FROM " +
//                                            " course " +
//                                            " WHERE " + "coursename" +
//                                            " LIKE '" + courseName + "'"+" and "+ " coursecode "+ " LIKE '" + courseCode+ "' ;";
                                // db.execSQL(sql);
                                // String new_coursename=courseName.replaceAll("\\s+","");
                                // String new_coursecode=courseCode.replaceAll("\\s+","");
                                //String table_name=new_coursename+new_coursecode;
                                //String delsql="DROP TABLE "+ table_name +";";
                                //db.execSQL(delsql);
                                //db.close();
                                //restartActivity();

                                //Toast.makeText(CourseList.this,"Course deleted at :\n"+"POS: "+newpos+"\n"+"ID: "+id, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(CourseList.this, "Course deleted at :\n" + courseName, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        });





//        final ImageButton Cam = (ImageButton) findViewById(R.id.Cam);
//
//
//        Cam.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ContinuousCaptureActivity.class);
//                startActivity(intent);
//            }
//
//        });

    }

    public void writeToFile(String data,String filename) {

        String fileName = filename + ".txt";//like 2016_01_12.txt


        try
        {
            File root = new File(Environment.getExternalStorageDirectory()+File.separator+"Student Roll Capture");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists())
            {
                root.mkdirs();
            }
            File gpxfile = new File(root, fileName);


            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append(data+"\n\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Please wait writing to file....", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //int val=item.getItemId();
//        //if(val==R.id.home){
////        switch (item.getItemId()) {
////            // Respond to the action bar's Up/Home button
////            case android.R.id.home:
////                Intent intent = new Intent(scan_home.this, scan_home.class);
////            //startActivity(intent);
////            Bundle bundle = new Bundle();
////            bundle.putString("coursename", coursename); // place the position of the selected item
////            bundle.putString("coursecode", coursecode); // place the position of the selected item
////            intent.putExtras(bundle);
////            startActivity(intent);
////                return true;
////       }
//        return super.onOptionsItemSelected(item);
//    }


//    boolean doubleBackToExitPressedOnce = false;

//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//    }


//        @Override
//    public void onBackPressed() {
//
//            Intent intent = new Intent(scan_home.this, CourseList.class);
//            //startActivity(intent);
////            Bundle bundle = new Bundle();
////            bundle.putString("coursename", coursename); // place the position of the selected item
////            bundle.putString("coursecode", coursecode); // place the position of the selected item
////            intent.putExtras(bundle);
////            //startActivityForResult(intent, 2);
//            startActivity(intent);
//
//           // super.onBackPressed();
//    }


}