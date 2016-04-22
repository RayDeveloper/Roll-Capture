package edu.uwi.sta.idrollcapture;

/**
 * Created by Raydon on 3/18/2016.
 */
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityEvent;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import android.content.pm.PackageManager;

import edu.uwi.sta.idrollcapture.Models.DBHelper;

public class scan_home extends AppCompatActivity {
    Button button;
    String coursename;
    String coursecode;
    String filename;
    int numberofIDS = 0;
    String TotalIDs = "";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private boolean hasPermission;
    private static final int REQUEST_NETWORK_ACCESS = 112;
    public final String[] allFiles = new String[0];
    public  String SCAN_PATH="" ;
    public  String FILE_TYPE="text/*";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String course_name = prefs.getString("coursename", null);//"No name defined" is the default value.
        String course_code = prefs.getString("coursecode", null);//"No name defined" is the default value.
        TextView coursename_txtview = (TextView) findViewById(R.id.coursename_txtview);
        coursename_txtview.setText(course_name);
        TextView coursecode_txtview = (TextView) findViewById(R.id.coursecode_txtview);
        coursecode_txtview.setText(course_code);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
//        if (bundle.containsKey("coursename")) {
            coursename = bundle.getString("coursename");
            coursecode = bundle.getString("coursecode");
            String new_coursename = coursename.replaceAll("\\s+", "_");
            String new_coursecode = coursecode.replaceAll("\\s+", "_");
            filename = new_coursename+new_coursecode;

//            TextView coursename_txtview = (TextView) findViewById(R.id.coursename_txtview);
//            coursename_txtview.setText(coursename);
//            TextView coursecode_txtview = (TextView) findViewById(R.id.coursecode_txtview);
//            coursecode_txtview.setText(coursecode);

            //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);


            //Toast.makeText(scan_home.this, "File Name:" + filename, Toast.LENGTH_LONG).show();
        }


        checkRequestPermission();

        final ImageButton StartCamera = (ImageButton) findViewById(R.id.StartCamera);
        StartCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // checkRequestPermission();


                PackageManager pm = getApplicationContext().getPackageManager();

                if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    //Toast.makeText(getApplicationContext(), "This device does not have a camera.", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(scan_home.this)
                            .setTitle("Camera missing ")
                            .setMessage("This device has no camera.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //finish();

                                }
                            })
                            .setIcon(R.drawable.icon)
                            .show();

                }else {
                    Intent intent = new Intent(scan_home.this, ContinuousCaptureActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("coursename",coursename); // place the position of the selected item
//                bundle.putString("coursecode", coursecode); // place the position of the selected item
//                intent.putExtras(bundle);
                    startActivity(intent);
                }

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
                //checkRequestPermission2();
                new AlertDialog.Builder(scan_home.this)
                        .setTitle("Export Register")
                        .setMessage(getString(R.string.dialog_export))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper mDbHelper = new DBHelper(scan_home.this);
                                // Gets the data repository in write mode
                                final SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                Cursor cursor = db.query(filename, new String[]{"idnumber", "time"}, null, null, null, null, null);
                                //cursor is now at the first result returned by this query
                                String fileHeading = coursename + " " + coursecode + "\n";
                                writeToFile(fileHeading, filename);
                                writeToFile("StudentID         Time Scanned", filename);
                                //writeToFile("Time Scanned        ", filename);

                                if (cursor.moveToFirst()) {
                                    do {
                                        String db_idnumber = cursor.getString(cursor.getColumnIndex("idnumber"));
                                        String db_time = cursor.getString(cursor.getColumnIndex("time"));
                                        String line = db_idnumber + "  " + db_time;
                                        writeToFile(line, filename);
                                        numberofIDS++;
                                        //do something with name
                                    }
                                    while (cursor.moveToNext()); //loop will terminate when all the rows are exhausted
                                }
                                TotalIDs = TotalIDs + numberofIDS;
                                writeToFile("Total Number of Students: " + TotalIDs, filename);
                                cursor.close();


//                                new AlertDialog.Builder(scan_home.this)
//                                        .setTitle("Export Status")
//                                        .setMessage("Export Completed")
//                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                //finish();
//
//                                            }
//                                        })
//                                        .setIcon(R.drawable.export2)
//                                        .show();


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
                        .setIcon(R.drawable.export2)
                        .show();



            }



        });


        final ImageButton viewFolder = (ImageButton) findViewById(R.id.folder);
        viewFolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFile(filename);
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void dialog(){
        new AlertDialog.Builder(scan_home.this)
                .setTitle("Export Status")
                .setMessage("Export Completed")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();

                    }
                })
                .setIcon(R.drawable.export2)
                .show();
    }

    public void writeToFile(String data, String filename) {

        String fileName = filename + ".txt";//like 2016_01_12.txt


        try {
            File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Student Roll Capture");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, fileName);


            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(data + "\n\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Please wait writing to file....", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

        }
        //Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();





    }

    public void openFile(String filename) {


        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Student Roll Capture"+File.separator+filename+".txt");
        if(file.exists()) {
            // Toast.makeText(this,"Path: \n"+Environment.getExternalStorageDirectory() + File.separator + "Student Roll Capture"+File.separator+filename+".doc", Toast.LENGTH_LONG).show();
            Intent i = new Intent();
            i.setAction(android.content.Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(file), "text/plain");
            //startActivity(i);
            //intent.setType("text/plain");
            PackageManager pm = getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(i, 0);
            if (activities.size() > 0) {
                startActivity(i);
            } else {
                //Toast.makeText(getApplicationContext(), "There is no app to open the file.", Toast.LENGTH_LONG).show();

                // Do something else here. Maybe pop up a Dialog or Toast
                new AlertDialog.Builder(scan_home.this)
                        .setTitle("No app available ")
                        .setMessage("There is no app to open this file.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();

                            }
                        })
                        .setIcon(R.drawable.icon)
                        .show();
            }

            //Toast.makeText(this,Environment.getExternalStorageDirectory(),filename +".txt", Toast.LENGTH_SHORT).show();
//
//        File file = new File(Environment.getExternalStorageDirectory(),filename+".txt");
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        Uri uri = Uri.fromFile(file);
//        intent.setDataAndType(uri, "text/plain");
//        startActivity(intent);


//        //File file = new File(Environment.getExternalStorageDirectory(),filename+".txt");
//               Uri uri=Uri.parse("Local storage/Device storage/Student Roll Capture/"+filename+".txt");
//                Toast.makeText(this,"Local storage/Device storage/Student Roll Capture/"+filename+".txt", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        //intent.setType("text/plain"); // Optional
//        startActivity(intent);


//        File file = new File(Environment.getExternalStorageDirectory(),filename+".txt");
//        //Uri uri = Uri.parse("file://" + file.getAbsolutePath());
//       Uri uri=Uri.parse( file.getAbsolutePath());
//
//        Toast.makeText(this,"path: "+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);


            //intent.setAction(android.content.Intent.ACTION_VIEW);

            //startActivity(intent);
            //startActivity(Intent.createChooser(intent, "Open folder"));

//        Intent intent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse("Student Roll Capture/"+filename+".txt"));
//        Toast.makeText(this, "Student Roll Capture "+filename+".txt", Toast.LENGTH_SHORT).show();
//
//        intent.setType("text/plain");
//        PackageManager pm = getPackageManager();
//        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
//        if (activities.size() > 0) {
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Else statement", Toast.LENGTH_SHORT).show();
//
//            // Do something else here. Maybe pop up a Dialog or Toast
//        }

//        File file = new File(filename+".txt");
//
//// Just example, you should parse file name for extension
//        //String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(".txt");
//
//        Intent intent = new Intent();
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), ".txt");
//        startActivityForResult(intent, 10);


//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
//                + "/myFolder/");
//        intent.setDataAndType(uri, "text/csv");
//        startActivity(Intent.createChooser(intent, "Open folder"));

//        File file = new File(Environment.getExternalStorageDirectory()+ File.separator + "/Student Roll Capture/");
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "application/file");
//        startActivity(Intent.createChooser(intent,"Open Folder"));
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
//                + "Student Roll Capture");
//        intent.setDataAndType(uri, "text/plain");
//        startActivity(Intent.createChooser(intent, "Open folder"));
        }else{
            Toast.makeText(getApplicationContext(),"The file does not exist try exporting it first to create it.",Toast.LENGTH_LONG).show();
            //change to dialog
        }
    }




    public void checkRequestPermission(){


        hasPermission = (ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED );

        if (!hasPermission){
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.VIBRATE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE

                    },
                    REQUEST_NETWORK_ACCESS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                == PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
//        switch(requestCode){
//            case REQUEST_NETWORK_ACCESS:{
            //if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            //reload my activity
            //Toast.makeText(this,"Granted",Toast.LENGTH_LONG).show();

//                    Intent i = getIntent();
//                    finish();
//                    startActivity(i);
        }else{
            new AlertDialog.Builder(scan_home.this)
                    .setTitle("Permission Denied")
                    .setMessage(getString(R.string.dialog_permissions))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    })
                    .setIcon(R.drawable.lock)
                    .show();
            //Toast.makeText(this,"The app was not granted permission.Please consider granting it permission.The app may not function properly.The app will now end." ,Toast.LENGTH_LONG).show();

            // finish();
        }

    }


    // }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }


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