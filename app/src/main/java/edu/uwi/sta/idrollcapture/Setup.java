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
int courseID=0;
    int dbnum=0;
 int   duplicatecheck=-1;
    String coursename;
    String coursecode;

    List<courses> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
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

        final EditText name_editText = (EditText) findViewById(R.id.name_editText);
        final EditText code_editText = (EditText) findViewById(R.id.code_editText);


        Button b = (Button) findViewById(R.id.btn_save);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 coursename = name_editText.getText().toString();
                 coursecode = code_editText.getText().toString();
                String datecreated = DateFormat.getDateTimeInstance().format(new Date());

                  duplicatecheck = duplicateCheck(coursename,coursecode);
                //Toast.makeText(Setup.this, "Duplicatecheck: "+duplicatecheck, Toast.LENGTH_SHORT).show();
                if (duplicatecheck== 0) {

                    new AlertDialog.Builder(Setup.this)
                            .setTitle("Course Addition")
                            .setMessage("Course name and course code already used.Please choose another name for either.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
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
                    //Snackbar.make(v, "Course name and course code already used", Snackbar.LENGTH_LONG).show();
                    //name_editText.setText("");
                    //code_editText.setText("");

                    //Toast.makeText(Setup.this, "Course already there", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(Setup.this, "Course not here ", Toast.LENGTH_SHORT).show();

                    DBHelper mDbHelper = new DBHelper(Setup.this);
                    // Gets the data repository in write mode
                    final SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    //DBHelper help = new DBHelper(getBaseContext());
                    //courseList = help.getCourse();
//                courses[] customs = new courses[courseList.size()];
//                courseList.toArray(customs);
//                Object last = coursename;
//                for (courses str : courseList) {
//                    if (str.equals(last)) {
//                        Toast.makeText(Setup.this, "Course already exists"+coursename, Toast.LENGTH_SHORT).show();
//                    }
//                }
                    //Toast.makeText(Setup.this, "Course name going in: "+coursename, Toast.LENGTH_SHORT).show();


                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    //values.put(CourseContract.CourseEntry.COLUMN_NAME_ID, courseID);
                    values.put(CourseContract.CourseEntry.COLUMN_NAME_COURSE_NAME, coursename);
                    values.put(CourseContract.CourseEntry.COLUMN_NAME_COURSE_CODE, coursecode);
                    values.put(CourseContract.CourseEntry.COLUMN_NAME_DATE_CREATED, datecreated);

                    //Toast.makeText(Setup.this,"Values.put area", Toast.LENGTH_SHORT).show();

// Insert the new row, returning the primary key value of the new row

                    final long newRowId = db.insert(CourseContract.CourseEntry.TABLE_NAME, null, values);
                    //dbnum++;
                    //courseID++;
                    //Toast.makeText(Setup.this,"db.insert area", Toast.LENGTH_SHORT).show();
                    if (newRowId != 0) {
                        //Toast.makeText(Setup.this, "RowID:" + newRowId, Toast.LENGTH_SHORT).show();

                        Snackbar.make(v, "Course successfully added", Snackbar.LENGTH_LONG)
                                .setAction("Delete Course", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //SQLiteDatabase .delete("course", KEY_ID+"="+id, null);

                                        String sql = "DELETE FROM " +
                                                " course " +
                                                " WHERE " + "courseID" +
                                                " LIKE " + newRowId + ";";
                                        db.execSQL(sql);
                                        // dbnum--;
//                                    String selection = CourseContract.CourseEntry._ID + " LIKE ?";
//                                    String[] selectionArgs = { String.valueOf(newRowId) };
//                                    db.delete("course", selection, selectionArgs);
                                        Snackbar.make(v, "Course removed", Snackbar.LENGTH_LONG).show();
                                    }
                                })
                                .show();

                    }
                    //final EditText name_editText = (EditText) findViewById(R.id.name_editText);
                    //final EditText code_editText = (EditText) findViewById(R.id.code_editText);
                    //coursename = name_editText.getText().toString();
                    //coursecode = code_editText.getText().toString();

                    String new_coursename=coursename.replaceAll("\\s+","");
                    String new_coursecode=coursecode.replaceAll("\\s+","");
                    String table_name=new_coursename+new_coursecode;
                    //Toast.makeText(Setup.this,"TableName: " + table_name,Toast.LENGTH_LONG).show();
                    AddDesiredTable(table_name);
//                    IDsDBHelper idhelp = new IDsDBHelper(Setup.this,table_name);
//                    //idhelp.onCreate(db);
//                    idhelp.createFriendTable(table_name);

                    name_editText.setText("");
                    code_editText.setText("");

                }
            }
        });
    }


    public int duplicateCheck(String coursename,String coursecode){
        //Toast.makeText(Setup.this,"Checking for duplicates", Toast.LENGTH_SHORT).show();

        DBHelper mDbHelper = new DBHelper(Setup.this);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selectQuery = "SELECT * FROM course where coursename = '"+ coursename + "'"+" and coursecode = '"+ coursecode + "' ; " ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> checkList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String db_coursename= cursor.getString(cursor.getColumnIndex("coursename"));
                String db_coursecode= cursor.getString(cursor.getColumnIndex("coursecode"));
                //Toast.makeText(Setup.this,db_coursename+ "\n"+db_coursecode , Toast.LENGTH_SHORT).show();
                if(db_coursename.equals(coursename)&& db_coursecode.equals(coursecode)){
                    //Toast.makeText(Setup.this, "comparision "+"\n"+"\n"+"\n" , Toast.LENGTH_SHORT).show();
                    return 0;
                }
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return 1;
    }


    public void AddDesiredTable(String TableName){
    /*At first you will need a Database object.Lets create it.*/
        DBHelper mDbHelper = new DBHelper(Setup.this);
        // Gets the data repository in write mode
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

    /*then call 'execSQL()' on it. Don't forget about using TableName Variable as tablename.*/
        db.execSQL( "CREATE TABLE IF NOT EXISTS '" + TableName + "' ( " +
                IDsContract.IDsEntry._ID + "INTEGER" + " PRIMARY KEY, "+
                IDsContract.IDsEntry.COLUMN_NAME_idnumber + " TEXT " + " , " +
                IDsContract.IDsEntry.COLUMN_NAME_time + " TEXT " + " , " +
                IDsContract.IDsEntry.COLUMN_NAME_DATE_CREATED + " TEXT " + " );");

        //Toast.makeText(Setup.this,"TableName Created: " + TableName,Toast.LENGTH_LONG).show();

    }

    }

