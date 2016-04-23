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
import android.os.AsyncTask;
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
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.pm.PackageManager;

import edu.uwi.sta.idrollcapture.Models.DBHelper;
import edu.uwi.sta.idrollcapture.Models.ID;
import edu.uwi.sta.idrollcapture.Models.IDListAdapter;
import edu.uwi.sta.idrollcapture.Models.IDsContract;

public class scan_home extends AppCompatActivity {
    String coursename;
    String coursecode;
    String filename;
    int numberofIDS = 0;
    String TotalIDs = "";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private boolean hasPermission;
    private static final int REQUEST_NETWORK_ACCESS = 112;
    int check=3;
    private static final String TAG = "scan_home";
    int  finalTotal=0;
    int filew=0;
    int toast=0;
    int sleepFor=0;
    Thread thread;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkRequestPermission();//check permissions again

        getPrefs();//gets shared preferences from courseList file

        setupOnClickListeners();//sets up the Onclicks of the imageButtons



    }

    public void getPrefs(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String course_name = prefs.getString("coursename", null);//"No name defined" is the default value.
        String course_code = prefs.getString("coursecode", null);//"No name defined" is the default value.
        TextView coursename_txtview = (TextView) findViewById(R.id.coursename_txtview);
        coursename_txtview.setText(course_name);
        TextView coursecode_txtview = (TextView) findViewById(R.id.coursecode_txtview);
        coursecode_txtview.setText(course_code);


        Bundle bundle = getIntent().getExtras();//get Extras from courseList file
        if (bundle != null) {
            coursename = bundle.getString("coursename");
            coursecode = bundle.getString("coursecode");
            String new_coursename = coursename.replaceAll("\\s+", "_");//replaces spaces with underscores
            String new_coursecode = coursecode.replaceAll("\\s+", "_");//replaces spaces with underscores
            filename = new_coursename+new_coursecode;//concatenates to create file name
        }

    }

    public void setupOnClickListeners(){

        final ImageButton StartCamera = (ImageButton) findViewById(R.id.StartCamera);
        StartCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                PackageManager pm = getApplicationContext().getPackageManager();

                if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {//if camera is missing
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
                    startActivity(intent);
                }

            }


        });

        final ImageButton Register = (ImageButton) findViewById(R.id.Register);
        Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(scan_home.this, Register.class);
                Bundle bundle = new Bundle();
                bundle.putString("coursename", coursename); // place the position of the selected item
                bundle.putString("coursecode", coursecode); // place the position of the selected item
                intent.putExtras(bundle);//puts the course name and course code to be sent to Register file
                //startActivityForResult(intent, 2);
                startActivity(intent);
            }

        });

        final ImageButton Export = (ImageButton) findViewById(R.id.Export);//export the register
        Export.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(scan_home.this)
                        .setTitle("Download Register")
                        .setMessage(getString(R.string.dialog_export))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                WritetoFileAsync writetoFileAsync = new WritetoFileAsync();//create a new instance of getIDsAsync
                                writetoFileAsync.execute("");//async task to write to file in the background
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.download)
                        .show();




            }



        });


        final ImageButton viewFolder = (ImageButton) findViewById(R.id.folder);
        viewFolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFile(filename);//opens file with the name
            }

        });

    }



    public void writeToFile(String data, String filename) {
//method will be called in AsycTask - WritetoFileAsync


        FileWriter writer;
        String fileName = filename + ".txt";//create a .txt file. the .doc were not working properly

        try {
            File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Student Roll Capture");//get directory
            if (!root.exists()) {//if it doesn't exist it creates it
                root.mkdirs();
            }
            File gpxfile = new File(root, fileName);

             writer = new FileWriter(gpxfile, true);
            writer.append(data + "\n\n");//append to the already present file

            writer.flush();
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();

        }



    }

    public void openFile(String filename) {//opens the file for viewing (read mode)


        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Student Roll Capture"+File.separator+filename+".txt");//gets absolute path
        if(file.exists()) {//checks if file exist first
            // Toast.makeText(this,"Path: \n"+Environment.getExternalStorageDirectory() + File.separator + "Student Roll Capture"+File.separator+filename+".doc", Toast.LENGTH_LONG).show();
            Intent i = new Intent();
            i.setAction(android.content.Intent.ACTION_VIEW);//open chooser dialog
            i.setDataAndType(Uri.fromFile(file), "text/plain"); //set type of data
            PackageManager pm = getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(i, 0);
            if (activities.size() > 0) {//counts the nunber if available apps
                startActivity(i);
            } else {
                //Toast.makeText(getApplicationContext(), "There is no app to open the file.", Toast.LENGTH_LONG).show();

                // Do something else here. Maybe pop up a Dialog or Toast
                new AlertDialog.Builder(scan_home.this)
                        .setTitle("No app available")
                        .setMessage("There is no app to open this file.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();

                            }
                        })
                        .setIcon(R.drawable.error)
                        .show();
            }

        }else{//if file does not exist as yet
            new AlertDialog.Builder(scan_home.this)
                    .setTitle("Open Error")
                    .setMessage("The file does not exist.Download it to create it.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();

                        }
                    })
                    .setIcon(R.drawable.error)
                    .show();
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

        }

    }

    private class WritetoFileAsync extends AsyncTask<String,Void,String> {//AsyncTask to do the write to file in the background


        @Override
        protected String doInBackground(String... params) {

            DBHelper mDbHelper = new DBHelper(scan_home.this);
            final SQLiteDatabase db = mDbHelper.getWritableDatabase();//only works with getWritableDatabase
            Cursor cursor = db.query(filename, new String[]{"idnumber", "time"}, null, null, null, null, null);
            //cursor is now at the first result returned by this query

            String fileHeading = coursename + " " + coursecode + "\n";
            writeToFile(fileHeading, filename);//writes the course name and code to file first
            Log.v(TAG, "line written to file: " + fileHeading);

            writeToFile("StudentID         Time Scanned", filename);//then writes the headings
            Log.v(TAG, "line written to file: " + "StudentID         Time Scanned");



            if (cursor.moveToFirst()) {//loop thru curosr to write IDs to file
                do {
                    String db_idnumber = cursor.getString(cursor.getColumnIndex("idnumber"));
                    String db_time = cursor.getString(cursor.getColumnIndex("time"));
                    String line = db_idnumber + "  " + db_time;
                    writeToFile(line, filename);//writes each line at a time
                    Log.v(TAG,"line written to file: "+line);

                    numberofIDS++;//counts the number of lines added
                }
                while (cursor.moveToNext()); //loop will terminate when all the rows are exhausted
            }
            TotalIDs = TotalIDs + numberofIDS;//total number of IDs
            writeToFile("Total Number of Students: " + TotalIDs, filename);//writes the total to file
            Log.v(TAG, "line written to file: " + "Total Number of Students: " + TotalIDs);
            cursor.close();


            return null;
        }


        @Override
        protected void onPostExecute(String result) {//always returns void

            new AlertDialog.Builder(scan_home.this)
                    .setTitle("Download Report")
                    .setMessage("Download Completed.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();

                        }
                    })
                    .setIcon(R.drawable.download)
                    .show();
            Log.v(TAG, "OnPostExecute method");



        }
    }


}