package edu.uwi.sta.idrollcapture;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;


import edu.uwi.sta.idrollcapture.Models.CourseContract;
import edu.uwi.sta.idrollcapture.Models.CourseListAdapter;
import edu.uwi.sta.idrollcapture.Models.DBHelper;
import edu.uwi.sta.idrollcapture.Models.IDsContract;
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
    String oldTable;
    int isChecked=-1;
    int isAltered=0;
    String new_newtable;
    int x=0;
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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(CourseList.this,Setup.class);
//                startActivity(intent);
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Toast.makeText(CourseList.this, "Long press for options.", Toast.LENGTH_SHORT).show();


        sqlHandler = new SqlHandler(this);

        final ListView listView = (ListView) findViewById(R.id.courseList_view);
        final DBHelper help = new DBHelper(getBaseContext());
        //final CourseListAdapter adapter = new CourseListAdapter(CourseList.this, courseList);
        listView.setEmptyView(findViewById(android.R.id.empty));
        //listView.setAdapter(adapter);
        new Thread(new Runnable() {
            public void run() {
                courseList = help.getCourse();
                final CourseListAdapter adapter = new CourseListAdapter(CourseList.this, courseList);
                listView.setAdapter(adapter);

                runOnUiThread(new Runnable() {
                    public void run() {
                        //adapter.notifyDataSetChanged();

                    }
                });
            }
        }).start();

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
                String new_coursename=courseName.replaceAll("\\s+","_");
                String new_coursecode=courseCode.replaceAll("\\s+","_");
                oldTable=new_coursename+new_coursecode;
                //oldTable=courseName+courseCode;



//                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                editor.putString("coursename", courseName);
//                editor.putString("coursecode", courseCode);
//                editor.apply();



                //Toast.makeText(CourseList.this,"CourseName:"+courseName, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(CourseList.this);
                //builder.setTitle("Make your selection");
                builder.setItems(R.array.options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if(item== 0){
                            deleteCourse();
                            //Toast.makeText(CourseList.this,"Choice 0", Toast.LENGTH_SHORT).show();

                        }
                        if(item==1){
                            editCourse();
                            //Toast.makeText(CourseList.this,"Edit Course", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();

                //also delete table too
//                new AlertDialog.Builder(CourseList.this)
//                        .setTitle("Delete Course")
//                        .setMessage("Are you sure you want to delete this course? The register will also be deleted.")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                DBHelper mDbHelper = new DBHelper(CourseList.this);
//                                final SQLiteDatabase db = mDbHelper.getWritableDatabase();
//                                //delete by course code instead or name
//                                //fix delete to use the correct way of deleting
//                                String sql = "DELETE FROM " +
//                                        " course " +
//                                        " WHERE " + "coursename" +
//                                        " LIKE '" + courseName + "'"+" and "+ " coursecode "+ " LIKE '" + courseCode+ "' ;";
//                                db.execSQL(sql);
//                                String new_coursename=courseName.replaceAll("\\s+","");
//                                String new_coursecode=courseCode.replaceAll("\\s+","");
//                                String table_name=new_coursename+new_coursecode;
//                                String delsql="DROP TABLE '"+ table_name +"';";
//                                db.execSQL(delsql);
//                                db.close();
//                                restartActivity();
//
//                                //Toast.makeText(CourseList.this,"Course deleted at :\n"+"POS: "+newpos+"\n"+"ID: "+id, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(CourseList.this, "Course deleted at :\n" + courseName, Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
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

    public void deleteCourse(){
//also delete table
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
                                " = '" + courseName + "'"+" and "+ " coursecode "+ " = '" + courseCode+ "' ;";
                        db.execSQL(sql);

                        String new_coursename=courseName.replaceAll("\\s+","_");
                        String new_coursecode=courseCode.replaceAll("\\s+","_");
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
                .setIcon(R.drawable.delete)
                .show();

    }
    public void editCourse() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CourseList.this);
        alertDialog.setTitle("Change course name and code.");
        alertDialog.setMessage("Enter new names.");
        final EditText coursename = new EditText(CourseList.this);
        final EditText coursecode = new EditText(CourseList.this);
        coursename.setText(courseName);
        coursecode.setText(courseCode);
        String new_coursename = courseName.replaceAll("\\s+", "_");
        String new_coursecode = courseCode.replaceAll("\\s+", "_");
        final String table_name = new_coursename+new_coursecode;
       // final String table_name = courseName+courseCode;


        // quantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        // lot.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //Project=arr[0].toString();
        // Item=arr[1].toString();

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(coursename);
        ll.addView(coursecode);
        alertDialog.setView(ll);

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            DBHelper mDbHelper = new DBHelper(CourseList.this);
            final SQLiteDatabase db = mDbHelper.getWritableDatabase();

            public void onClick(DialogInterface dialog, int id) {
                String new_courseName = coursename.getText().toString();
                String new_courseCode = coursecode.getText().toString();
                String new_newcoursename = new_courseName.replaceAll("\\s+", "_");
                String new_newcoursecode = new_courseCode.replaceAll("\\s+", "_");
                 new_newtable=new_newcoursename+new_newcoursecode;
                //new_newtable=new_courseName+new_courseCode;


                int cmp = oldTable.compareTo(new_newtable);
                if(cmp==0) {
                //if(new_courseName.equals(courseName)&& new_courseCode.equals(courseCode)) {
                    isAltered=1;
                    //x++;
                     isChecked=-1;
                   // Toast.makeText(CourseList.this,"isAltered=1", Toast.LENGTH_SHORT).show();

                }else {
                     isChecked = duplicateCheck(new_courseName, new_courseCode);
                 }
                    if (isChecked == 1) {

                        String new_coursename = new_courseName.replaceAll("\\s+", "_");
                        String new_coursecode = new_courseCode.replaceAll("\\s+", "_");

                        String new_tablename = new_coursename + new_coursecode;
                        //String new_tablename = new_courseName+new_courseCode;

                        // String sql="Update course set coursename = '" + new_courseName + "' and coursecode = '" + new_courseCode + "' where coursecode = '" + courseCode +"' ";
                        //db.execSQL(sql);
                        //Toast.makeText(CourseList.this,sql, Toast.LENGTH_LONG).show();

                            ContentValues values = new ContentValues();
                            values.put(CourseContract.CourseEntry.COLUMN_NAME_COURSE_NAME, new_courseName);
                            values.put(CourseContract.CourseEntry.COLUMN_NAME_COURSE_CODE, new_courseCode);
                            db.update(CourseContract.CourseEntry.TABLE_NAME, values, "coursename='" + courseName + "'", null);

                            int oldcmp=oldTable.compareToIgnoreCase(new_tablename);
                            if(oldcmp!=0) {
                                String sql = "ALTER TABLE '" + table_name + "' RENAME TO '" + new_tablename + "' ";
                                db.execSQL(sql);
                            }
                            //Toast.makeText(CourseList.this,"tableName:"+CourseContract.CourseEntry.TABLE_NAME, Toast.LENGTH_SHORT).show();

                            //db.update(IDsContract.IDsEntry.TABLE_NAME, values, "coursename='" + courseName + "'", null);

                            //db.update(table_name, values, "coursename='" + courseName + "'", null);

                            // IDsDBHelper mDbHelper2 = new IDsDBHelper(getApplicationContext(),table_name);
                            //final SQLiteDatabase db2 = mDbHelper2.getWritableDatabase();

                            //db.update(IDsContract.IDsEntry.TABLE_NAME, values,null, null);
//                ContentValues value2 = new ContentValues();
//                value2.put(IDsContract.IDsEntry.TABLE_NAME,new_tablename);
//                //db.update(IDsContract.IDsEntry.TABLE_NAME, value2,null, null);

                            // String sql="ALTER TABLE '"+table_name+"' RENAME TO '"+new_tablename+"' ";
                            // db2.execSQL(sql);
                            //db.update(IDsContract.IDsEntry.TABLE_NAME, values, null, null);

                            //Toast.makeText(CourseList.this, "new names:\n" + new_courseName + "\n" + new_courseCode, Toast.LENGTH_SHORT).show();
                            db.close();
                            restartActivity();

                    } else {
                        if (isChecked == -1) {
                            //do nothing
                        } else {
                            //Toast.makeText(CourseList.this, "Course name or course code already exist", Toast.LENGTH_LONG).show();
                            new AlertDialog.Builder(CourseList.this)
                                    .setTitle("Name already exist")
                                    .setMessage("Course name or course code already exist.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(R.drawable.icon)
                                    .show();

                        }
                    }

            }

        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                //ACTION
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();



//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CourseList.this);
//        alertDialog.setTitle("New Course");
//        alertDialog.setMessage("Enter new course name");
//
//        final EditText input = new EditText(CourseList.this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        input.setLayoutParams(lp);
//        alertDialog.setView(input);
//        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//
//        alertDialog.setPositiveButton("YES",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                      String  coursename = input.getText().toString();
//                        Toast.makeText(CourseList.this, coursename, Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//        alertDialog.setNegativeButton("NO",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//        alertDialog.show();
    }

    public int duplicateCheck(String coursename,String coursecode){
        //Toast.makeText(Setup.this,"Checking for duplicates", Toast.LENGTH_SHORT).show();

        DBHelper mDbHelper = new DBHelper(CourseList.this);
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //String selectQuery = "SELECT * FROM course where coursename = '"+ coursename + "'"+" and coursecode = '"+ coursecode + "' ; " ;
         Cursor cursor = db.rawQuery ("SELECT * FROM course where coursename = ? and coursecode = ?  ",new String[] {String.valueOf(coursename),String.valueOf(coursecode)})  ;



//        = db.rawQuery("select latitude,longitude from savedlocation where latitude = ? and
//                longitude = ? ", new String[] {String.valueOf(latitude),String.valueOf(longitude)});
//        String[] projection = {
//                CourseContract.CourseEntry.COLUMN_NAME_COURSE_NAME,
//                CourseContract.CourseEntry.COLUMN_NAME_COURSE_CODE
//
//        };
//        String selection={
//          CourseContract.CourseEntry.COLUMN_NAME_COURSE_NAME +"LIKE ?"+
//          + CourseContract.CourseEntry.COLUMN_NAME_COURSE_CODE+"LIKE ?"
//        };

//        String[]slectionArgs= {
//                String.valueOf(coursename),
//                String.valueOf(coursecode),
//        };
//        Cursor cursor = db.query(
//                CourseContract.CourseEntry.TABLE_NAME,  // The table to query
//                projection,                               // The columns to return
//                selection,                                // The columns for the WHERE clause
//                slectionArgs,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                null                                 // The sort order
//        );
                //Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> checkList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String db_coursename= cursor.getString(cursor.getColumnIndex("coursename"));
                String db_coursecode= cursor.getString(cursor.getColumnIndex("coursecode"));
                //Toast.makeText(Setup.this,db_coursename+ "\n"+db_coursecode , Toast.LENGTH_SHORT).show();
                if(db_coursename.equals(coursename)&& db_coursecode.equals(coursecode)){
                    //Toast.makeText(Setup.this, "comparision "+"\n"+"\n"+"\n" , Toast.LENGTH_SHORT).show();
                    return 0;//already in list
                }
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return 1; //not in list
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scanhome_menuitems, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                new AlertDialog.Builder(CourseList.this)
                        .setTitle("Help")
                        .setMessage("Long Press a course for more options.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .setIcon(R.drawable.signs)
                        .show();
                //Toast.makeText(scan_home.this,"Settings Selected",Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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



