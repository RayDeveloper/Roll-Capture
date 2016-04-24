package edu.uwi.sta.idrollcapture;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private boolean hasPermission;
    private static final int REQUEST_NETWORK_ACCESS = 112;

//from anna

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkRequestPermission();//checks permissions when app is first launched
        setUponClicks();//methods that houses all the onCLickListeners for buttons and textViews


    }

    public void setUponClicks(){
        final TextView viewCourses_txt= (TextView) findViewById(R.id.viewCourses_txt);
        viewCourses_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CourseList.class);
                startActivity(intent);
            }

        });

        final TextView Course_heading= (TextView) findViewById(R.id.Course_heading);
        Course_heading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CourseList.class);
                startActivity(intent);
            }

        });
        final ImageView courseList = (ImageView) findViewById(R.id.courseList);
        courseList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CourseList.class);
                startActivity(intent);
            }
        });



        final ImageView newCourse = (ImageView) findViewById(R.id.newCourse);
        newCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Intent intent = new Intent(MainActivity.this, Setup.class);
                Intent intent = new Intent(MainActivity.this, Setup.class);
                startActivity(intent);
            }
        });

        final TextView setup_courseRole_txtView = (TextView) findViewById(R.id.setup_courseRole_txtView);
        setup_courseRole_txtView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Intent intent = new Intent(MainActivity.this, Setup.class);
                Intent intent = new Intent(MainActivity.this, Setup.class);
                startActivity(intent);
            }
        });

        final TextView setup_textview = (TextView) findViewById(R.id.setup_textview);
        setup_textview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Intent intent = new Intent(MainActivity.this, Setup.class);
                Intent intent = new Intent(MainActivity.this, Setup.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(id== R.id.action_about){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("COMP 3275 Project")
                    .setMessage(getString(R.string.dialog_about))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(R.drawable.diploma)
                    .show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//check permissions when activity is first launched
    public void checkRequestPermission(){


        hasPermission = (ActivityCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED );
//check group permissons
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
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Denied")
                    .setMessage(getString(R.string.dialog_permissions))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();//closes off the app if it is not granted

                        }
                    })
                    .setIcon(R.drawable.lock)
                    .show();

        }

    }
    // }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {//if true go back
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {//resets value after 2secs
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


}
