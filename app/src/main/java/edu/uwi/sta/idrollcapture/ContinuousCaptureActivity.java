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
import android.os.Bundle;
import android.os.Vibrator;
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
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivity extends Activity implements  CompoundBarcodeView.TorchListener {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private CompoundBarcodeView barcodeView;
    private Button switchFlashlightButton;
   // private SQLiteDatabase db;
    //String[] Scans = new String[100];
    List<String> Scans = new ArrayList<String>();
    int x = 0;
String coursename;
String coursecode;
    String table_name="";
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.continuous_scan);

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
//            Bundle bundle = getIntent().getExtras();
//            if(bundle != null){
////        if (bundle.containsKey("coursename")) {
//                coursename = bundle.getString("coursename");
//                coursecode = bundle.getString("coursecode");
//            }
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String course_name = prefs.getString("coursename",null);//"No name defined" is the default value.
            String course_code = prefs.getString("coursecode",null);//"No name defined" is the default value.

            String new_coursename=course_name.replaceAll("\\s+","");
            String new_coursecode=course_code.replaceAll("\\s+", "");
            //String new_coursecode=coursecode.replaceAll("\\s+","");
            //String new_coursename=coursename.replaceAll("\\s+","");

            table_name=new_coursename+new_coursecode;
            //Toast.makeText(ContinuousCaptureActivity.this,table_name, Toast.LENGTH_SHORT).show();

            //IDsDBHelper id = new IDsDBHelper(ContinuousCaptureActivity.this);
            //id.createFriendTable(table_name);

            if (result.getText() != null) {

                if (Scans.contains(result.getText())) {
               // if (Arrays.asList(Scans).contains(result.getText())) {
                    Toast.makeText(ContinuousCaptureActivity.this, "ID number already in list", Toast.LENGTH_SHORT).show();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 400 milliseconds
                    v.vibrate(400);
                } else {

                    //barcodeView.setStatusText(result.getText());
                    //Scans[x] = result.getText();
                    Scans.add(result.getText());
                    x++;
                    Toast.makeText(ContinuousCaptureActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 400 milliseconds
                    v.vibrate(200);
//                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC-04:00"));
//                Date currentLocalTime = cal.getTime();
//                DateFormat date = new SimpleDateFormat("HH:MM");
//// you can get seconds by adding  "...:ss" to it
//                date.setTimeZone(TimeZone.getTimeZone("UTC-04:00"));
//
//                String localTime = date.format(currentLocalTime);
//               // String datecreated = DateFormat.getDateTimeInstance().format(new Date());
//                Calendar c = Calendar.getInstance();
//                SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
//                String formattedDate = df.format(c.getTime());
                    //Toast.makeText(ContinuousCaptureActivity.this,"localTIme: "+localTime +"\n"+ "Datecreated:" +formattedDate, Toast.LENGTH_SHORT).show();

                    String datecreated = DateFormat.getDateTimeInstance().format(new Date());
                    String newDatecreated = datecreated.replace(",", "");
                    //IDsDBHelper mDbHelper = new IDsDBHelper(ContinuousCaptureActivity.this,table_name);
                    // Gets the data repository in write mode
                    //final SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    DBHelper mDbHelper = new DBHelper(ContinuousCaptureActivity.this);
                    // Gets the data repository in write mode
                    final SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    //ContentValues values = new ContentValues();
                    //values.put(CourseContract.CourseEntry.COLUMN_NAME_ID, courseID);
                    //values.put(IDsContract.IDsEntry.COLUMN_NAME_idnumber, result.getText());
                    //values.put(IDsContract.IDsEntry.COLUMN_NAME_time, localTime);
                    //values.put(IDsContract.IDsEntry.COLUMN_NAME_DATE_CREATED, datecreated);

                    String sql = "insert into " + table_name + " (idnumber,time) values('" + result.getText() + "', '" + newDatecreated + "');";
                    db.execSQL(sql);
                    db.close();
                    //final long newRowId = db.insert(table_name, null, values);

                    //Toast.makeText(ContinuousCaptureActivity.this,"Value added to db", Toast.LENGTH_SHORT).show();


                }
            }
            //Added preview of scanned barcode
            //ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            // imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
            //Scans.clear();
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

//    public void pause(View view) {
//        barcodeView.pause();
//    }
//
//    public void resume(View view) {
//        barcodeView.resume();
//    }
//
//    public void triggerScan(View view) {
//        barcodeView.decodeSingle(callback);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


//    @Override
//    public void onBackPressed() {
//       // Bundle b = new Bundle();
//        //b.putStringArray("ARRAY_LIST", Scans);
//        Intent i = new Intent(ContinuousCaptureActivity.this, scan_home.class);
//        //i.putExtras(b);
//        startActivity(i);
//    }

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
}
