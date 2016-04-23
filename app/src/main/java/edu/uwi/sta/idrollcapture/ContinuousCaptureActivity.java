package edu.uwi.sta.idrollcapture;

/**
 * Created by Raydon on 3/7/2016.
 */


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import edu.uwi.sta.idrollcapture.Models.CourseContract;
import edu.uwi.sta.idrollcapture.Models.DBHelper;
import edu.uwi.sta.idrollcapture.Models.IDsDBHelper;
import edu.uwi.sta.idrollcapture.Models.IDsContract;

/**
 * This  performs continuous scanning of barcodes and adds them to database
 */
public class ContinuousCaptureActivity extends Activity implements  CompoundBarcodeView.TorchListener {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private CompoundBarcodeView barcodeView;
    private Button switchFlashlightButton;
    List<String> Scans = new ArrayList<String>();
    int x = 0;
    String table_name="";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String newDatecreated;
    String scanned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.continuous_scan);//set special layout for scanning

        barcodeView = (CompoundBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        barcodeView.setTorchListener(this);
        switchFlashlightButton = (Button) findViewById(R.id.switch_flashlight);
        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }
    }

    private BarcodeCallback callback = new BarcodeCallback() {

        @Override

        public void barcodeResult(BarcodeResult result) {

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String course_name = prefs.getString("coursename",null);//"No name defined" is the default value.
            String course_code = prefs.getString("coursecode",null);//"No name defined" is the default value.

            String new_coursename=course_name.replaceAll("\\s+","_");//replaces spaces with underscores
            String new_coursecode=course_code.replaceAll("\\s+", "_");//replaces spaces with underscores
            table_name=new_coursename+new_coursecode;


            if (result.getText() != null) {
                Log.v(TAG,"ID found: "+result.getText());

                    scanned=result.getText();
                if (Scans.contains(result.getText())) {//checks if entry was already scanned in the particular session
                    Toast.makeText(ContinuousCaptureActivity.this,getString(R.string.toast_scan), Toast.LENGTH_SHORT).show();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 400 milliseconds
                    v.vibrate(400);//vibrate if it is
                } else {//not in list,add to database
                    Scans.add(result.getText());
                    x++;
                    Toast.makeText(ContinuousCaptureActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 200 milliseconds
                    v.vibrate(200);//vibrate to show addition

                    String datecreated = DateFormat.getDateTimeInstance().format(new Date());//gets the exact date and time
                     newDatecreated = datecreated.replace(",", "");//takes out the , to put nothing -blank

                    new addIDAsync().execute("");//calling async method to add ID numbers Asynchronously each time




                }
            }

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
            barcodeView.setTorchOn();
        } else {
            barcodeView.setTorchOff();
        }
    }

    @Override
    public void onTorchOn() {
        switchFlashlightButton.setText(R.string.turn_off_flashlight);
    }

    @Override
    public void onTorchOff() {
        switchFlashlightButton.setText(R.string.turn_on_flashlight);
    }



    private class addIDAsync extends AsyncTask<String,Void,Long> {//Async task for adding ID number in the background because it may be alot of ID's being scanned
        ContentValues values;
        @Override
        protected void onPreExecute(){
            values = new ContentValues();
            values.put(IDsContract.IDsEntry.COLUMN_NAME_idnumber, scanned);
            values.put(IDsContract.IDsEntry.COLUMN_NAME_time, newDatecreated);
            super.onPreExecute();

        }

        @Override
        protected Long doInBackground(String...params){
            long id=0;
            try{
                DBHelper mDbHelper = new DBHelper(ContinuousCaptureActivity.this);
                // Gets the data repository in write mode
                final SQLiteDatabase db = mDbHelper.getWritableDatabase();

                 id = db.insert(table_name, null, values);
                db.close();


            }catch (SQLiteException e){
                Log.v(TAG,"Exception "+e.getMessage());
            }
            return id;
        }

        @Override
        protected  void onPostExecute(Long id){
            super.onPostExecute(id);
        }

    }
}


