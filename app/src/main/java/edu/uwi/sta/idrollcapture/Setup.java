package edu.uwi.sta.idrollcapture;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.uwi.sta.idrollcapture.Models.CourseContract;
import edu.uwi.sta.idrollcapture.Models.DBHelper;
import edu.uwi.sta.idrollcapture.Models.IDsContract;
import edu.uwi.sta.idrollcapture.Models.IDsDBHelper;
import edu.uwi.sta.idrollcapture.Models.courses;

public class Setup extends AppCompatActivity {

 int   duplicatecheck=-1;
    String coursename;
    String coursecode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addCourse();//call the add course method


    }




    public void addCourse(){//contains operations for adding a course successfully
        final EditText name_editText = (EditText) findViewById(R.id.name_editText);
        final EditText code_editText = (EditText) findViewById(R.id.code_editText);


        Button b = (Button) findViewById(R.id.btn_save);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coursename = name_editText.getText().toString();
                coursecode = code_editText.getText().toString();
                String datecreated = DateFormat.getDateTimeInstance().format(new Date());//gets the exact date and time
                boolean emptyField = isfieldEmpty(coursename, coursecode);
                duplicatecheck = duplicateCheck(coursename, coursecode);//check if names are already in database
                if (emptyField) {//if the field is empty
                    Toast.makeText(Setup.this, "Please enter a course name or course code.", Toast.LENGTH_LONG).show();
                }else{
                    if (duplicatecheck == 0) {//if in list already

                        new AlertDialog.Builder(Setup.this)
                                .setTitle("Add Course")
                                .setMessage("Course name or course code already exist.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //do nothing

                                    }
                                })
                                .setIcon(R.drawable.icon)
                                .show();
                    } else {//if not in list

                        DBHelper mDbHelper = new DBHelper(Setup.this);
                        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        //values.put(CourseContract.CourseEntry.COLUMN_NAME_ID, courseID);
                        values.put(CourseContract.CourseEntry.COLUMN_NAME_COURSE_NAME, coursename);
                        values.put(CourseContract.CourseEntry.COLUMN_NAME_COURSE_CODE, coursecode);
                        values.put(CourseContract.CourseEntry.COLUMN_NAME_DATE_CREATED, datecreated);

                        // Insert the new row, returning the primary key value of the new row

                        final long newRowId = db.insert(CourseContract.CourseEntry.TABLE_NAME, null, values);
                        if (newRowId != -1) {
                            Snackbar.make(v, "Course successfully added", Snackbar.LENGTH_LONG)
                                    .setAction("Delete Course", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //SQLiteDatabase .delete("course", KEY_ID+"="+id, null);

//                                            String sql = "DELETE FROM " +
//                                                    " course " +
//                                                    " WHERE " + "courseID" +
//                                                    " LIKE " + newRowId + ";";
//                                            db.execSQL(sql);
                                            db.delete(CourseContract.CourseEntry.TABLE_NAME,CourseContract.CourseEntry._ID+"="+newRowId,null);
                                            db.close();
                                            Snackbar.make(v, "Course removed", Snackbar.LENGTH_LONG).show();
                                        }
                                    })
                                    .show();

                        }

                        String new_coursename = coursename.replaceAll("\\s+", "_");//replaces spaces with underscores
                        String new_coursecode = coursecode.replaceAll("\\s+", "_");//replaces spaces with underscores
                        String table_name = new_coursename + new_coursecode;
                        AddDesiredTable(table_name);//creates table for ID's

                        name_editText.setText("");//after OK is pressed it sets it to empty
                        code_editText.setText("");//after OK is pressed it sets it to empty

                    }
                }
            }

        });

    }


public boolean isfieldEmpty(String coursename,String coursecode) {//checks if fields are empty

    if (coursename.equals("") || coursecode.equals("")) {

        return true;
    }
    return  false;

}

    public int duplicateCheck(String coursename,String coursecode){//duplicate check

        DBHelper mDbHelper = new DBHelper(Setup.this);
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //String selectQuery = "SELECT * FROM course where coursename = '"+ coursename + "'"+" and coursecode = '"+ coursecode + "' ; " ;
        //Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor = db.rawQuery ("SELECT * FROM course where coursename = ? and coursecode = ?  ",new String[] {String.valueOf(coursename),String.valueOf(coursecode)})  ;
        if (cursor.moveToFirst()) {
            do {
                String db_coursename= cursor.getString(cursor.getColumnIndex("coursename"));
                String db_coursecode= cursor.getString(cursor.getColumnIndex("coursecode"));

                if(db_coursename.equals(coursename)&& db_coursecode.equals(coursecode)){//..compares the two using &&
                    return 0;//already in list
                }
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return 1;//not in list
    }


    public void AddDesiredTable(String TableName){//creates a table for ID's
        DBHelper mDbHelper = new DBHelper(Setup.this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL( "CREATE TABLE IF NOT EXISTS '" + TableName + "' ( " +
                IDsContract.IDsEntry._ID + "INTEGER" + " PRIMARY KEY, "+
                IDsContract.IDsEntry.COLUMN_NAME_idnumber + " TEXT " + " , " +
                IDsContract.IDsEntry.COLUMN_NAME_time + " TEXT " + " , " +
                IDsContract.IDsEntry.COLUMN_NAME_DATE_CREATED + " TEXT " + " );");
        db.close();


        //Toast.makeText(Setup.this,"TableName Created: " + TableName,Toast.LENGTH_LONG).show();

    }

    }

