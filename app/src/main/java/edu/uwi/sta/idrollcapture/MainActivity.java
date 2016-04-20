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
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private boolean hasPermission;
    private static final int REQUEST_NETWORK_ACCESS = 112;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        checkRequestPermission();

        final ImageView newCourse = (ImageView) findViewById(R.id.newCourse);
        newCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Intent intent = new Intent(MainActivity.this, Setup.class);
                Intent intent = new Intent(MainActivity.this, Setup.class);
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(id== R.id.action_about){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Project for COMP 3275")
                    .setMessage("This project was done by:\n Raydon Davis-813117991\n Kylie  Soogrim-811000178\nLeslie Yearwood-806002984\nAnnastacy Mohan-809000726\n Thank you for downloading this app")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
//        switch(requestCode){
//            case REQUEST_NETWORK_ACCESS:{
            //if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            //reload my activity
            //Toast.makeText(this,"Granted",Toast.LENGTH_LONG).show();

//                    Intent i = getIntent();
//                    finish();
//                    startActivity(i);
        }else{
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Denied")
                    .setMessage("The app was not granted permission.Please consider granting it permission.The app may not function properly.The app will now end.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            //Toast.makeText(this,"The app was not granted permission.Please consider granting it permission.The app may not function properly.The app will now end." ,Toast.LENGTH_LONG).show();

           // finish();
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
