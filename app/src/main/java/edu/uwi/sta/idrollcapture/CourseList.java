package edu.uwi.sta.idrollcapture;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import edu.uwi.sta.idrollcapture.Models.CourseContract;
import edu.uwi.sta.idrollcapture.Models.CourseListAdapter;
import edu.uwi.sta.idrollcapture.Models.DBHelper;
import edu.uwi.sta.idrollcapture.Models.ID;
import edu.uwi.sta.idrollcapture.Models.IDListAdapter;
import edu.uwi.sta.idrollcapture.Models.IDsContract;
import edu.uwi.sta.idrollcapture.Models.courses;


//Course List page where all the courses and course codes are displayed.
public class CourseList extends AppCompatActivity {
    List<courses> courseList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "CourseList ";


    String courseName;
    String courseCode;
    String oldTable;
    int isChecked=-1;
    int isAltered=0;
    String new_newtable;
    int x=0;
    ListView listView;

    CourseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        GetCoursesAsync getCoursesAsync = new  GetCoursesAsync();//create instance of GetCoursesAsync
         getCoursesAsync.execute("");//execute the asyncTask
        Log.v(TAG, "After async execute");


//         //listView= (ListView)findViewById(R.id.courseList_view);
//        adapter = new CourseListAdapter(CourseList.this, courseList);
//        //listView.setAdapter(adapter);
//
//        Thread thd = new Thread(new Runnable(){
//            public void run() {
//                 listView= (ListView)findViewById(R.id.courseList_view);
//                String selectQuery = "SELECT coursename,coursecode FROM course ";
//                DBHelper help = new DBHelper(CourseList.this);
//                SQLiteDatabase db = help.getWritableDatabase();
//                Cursor cursor = db.rawQuery(selectQuery, null);
//                List<courses> FavList = new ArrayList<>();
//                if (cursor.moveToFirst()) {
//                    do {
//                        courses list = new courses();//course class instantiation
//                        list.setCourse(cursor.getString(0));//first column query
//                        list.setCode(cursor.getString(1));//second column of query
//                        FavList.add(list);
//                    } while (cursor.moveToNext());
//                }
//                db.close();
//                cursor.close();
//                courseList = FavList;
//
//                //List<courses> FavList = new ArrayList<>();
//                //FavList=courseList;
//                listView.post(new Runnable() {
//                    public void run() {
//                        //adapter.notifyDataSetChanged();
//                        listView.setAdapter(adapter);
//
//
//                    }
//                });
//            }
//
//    });
//
//    thd.start();



//stop here


            //getCoursesAsync.execute("");//execute the asyncTask




            // new getCoursesAsync.execute("");//works this way in ContinuousCaptureActivity


            //Toast.makeText(CourseList.this, "Long press for options.", Toast.LENGTH_SHORT).show();

            //final ListView listView = (ListView) findViewById(R.id.courseList_view);
//        final DBHelper help = new DBHelper(getBaseContext());
            //final CourseListAdapter adapter = new CourseListAdapter(CourseList.this, courseList);
//        listView.setEmptyView(findViewById(android.R.id.empty));
            //listView.setAdapter(adapter);
//        new Thread(new Runnable() {
//            public void run() {//I hope this is correct
//                //courseList = help.getCourse();
////                final CourseListAdapter adapter = new CourseListAdapter(CourseList.this, courseList);
////                listView.setAdapter(adapter);
//
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        //adapter.notifyDataSetChanged();
//
//                    }
//                });
//            }
//        }).start();

//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
//                    Log.v(TAG, "inside setOnItemClickListener ");
//
//                    courses selectedFromList = (courses) (listView.getItemAtPosition(position));
//                    courseName = selectedFromList.getCourse();
//                    courseCode = selectedFromList.getCode();
//                    setPrfs();//sets the preferences to be transfered
//                }
//            });
//
//            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//long press gives different options
//                @Override
//                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
//                    Log.v(TAG, "inside setOnItemLongClickListener ");
//
//                    courses selectedFromList = (courses) (listView.getItemAtPosition(pos));
//
//                    courseName = selectedFromList.getCourse();
//                    courseCode = selectedFromList.getCode();
//                    String new_coursename = courseName.replaceAll("\\s+", "_");//replaces spaces with underscores
//                    String new_coursecode = courseCode.replaceAll("\\s+", "_");//replaces spaces with underscores
//                    oldTable = new_coursename + new_coursecode;
//                    Listdialog();//list out the options for when long press is activated
//
//
//                    return true;
//                }
//            });

    }

    public  void setPrefs(){
        Intent i = new Intent(CourseList.this, scan_home.class);
        Bundle bundle = new Bundle();
        bundle.putString("coursename",courseName); // place the position of the selected item
        bundle.putString("coursecode",courseCode); // place the position of the selected item
        i.putExtras(bundle);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();//saves these value across the whole app  until a next course is selected
        editor.putString("coursename", courseName);
        editor.putString("coursecode", courseCode);
        editor.apply();
        startActivity(i);
    }

    public void Listdialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(CourseList.this);
        //builder.setTitle("Make your selection");
        builder.setItems(R.array.options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(item== 0){
                    deleteCourse();
                }
                if(item==1){
                    editCourse();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);//allows the user to touch outside to dismiss
        alert.show();
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
                                " = '" + courseName + "'" + " and " + " coursecode " + " = '" + courseCode + "' ;";
                        db.execSQL(sql);

                        String new_coursename = courseName.replaceAll("\\s+", "_");//replaces spaces with underscores
                        String new_coursecode = courseCode.replaceAll("\\s+", "_");//replaces spaces with underscores
                        String table_name = new_coursename + new_coursecode;
                        String delsql = "DROP TABLE '" + table_name + "';";
                        db.execSQL(delsql);
                        db.close();
                        restartActivity();
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


//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for LOLLIPOP and newer versions



       // }

    }
    public void editCourse() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CourseList.this);
        alertDialog.setTitle("Change course name and code.");
        alertDialog.setMessage("Enter new names.");
        final EditText coursename = new EditText(CourseList.this);
        final EditText coursecode = new EditText(CourseList.this);
        coursename.setText(courseName);//sets the edittext tothe old values
        coursecode.setText(courseCode);//sets the edittext tothe old values
        coursecode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);//sets all CAPS for  coursecode

        String new_coursename = courseName.replaceAll("\\s+", "_");//replaces spaces with underscores
        String new_coursecode = courseCode.replaceAll("\\s+", "_");//replaces spaces with underscores
        final String table_name = new_coursename+new_coursecode;

        //programmatically creating edittext to allow course to be edited
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
                String new_newcoursename = new_courseName.replaceAll("\\s+", "_");//replaces spaces with underscores
                String new_newcoursecode = new_courseCode.replaceAll("\\s+", "_");//replaces spaces with underscores
                 new_newtable=new_newcoursename+new_newcoursecode;


                int cmp = oldTable.compareTo(new_newtable);//compares the new table and old table with case
                if(cmp==0) {
                    //isAltered=1;
                     isChecked=-1;//this means they are equal
                }else {
                     isChecked = duplicateCheck(new_courseName, new_courseCode);//not == then do a duplicateCheck
                 }
                    if (isChecked == 1) {//not duplicate so proceed with adding to database

                        String new_coursename = new_courseName.replaceAll("\\s+", "_");//replaces spaces with underscores
                        String new_coursecode = new_courseCode.replaceAll("\\s+", "_");//replaces spaces with underscores

                        String new_tablename = new_coursename + new_coursecode;

                            ContentValues values = new ContentValues();
                            values.put(CourseContract.CourseEntry.COLUMN_NAME_COURSE_NAME, new_courseName);
                            values.put(CourseContract.CourseEntry.COLUMN_NAME_COURSE_CODE, new_courseCode);
                            db.update(CourseContract.CourseEntry.TABLE_NAME, values, "coursename='" + courseName + "'", null);

                            int oldcmp=oldTable.compareToIgnoreCase(new_tablename);//compares to see if there is a change
                            if(oldcmp!=0) {//if != 0 change table name
                                String sql = "ALTER TABLE '" + table_name + "' RENAME TO '" + new_tablename + "' ";
                                db.execSQL(sql);
                            }
                            db.close();
                            restartActivity();//restart Activity to show changes

                    } else {
                        if (isChecked == -1) {
                            //do nothing
                        } else {
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

    }

    public int duplicateCheck(String coursename,String coursecode){

        DBHelper mDbHelper = new DBHelper(CourseList.this);
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //String selectQuery = "SELECT * FROM course where coursename = '"+ coursename + "'"+" and coursecode = '"+ coursecode + "' ; " ;
         Cursor cursor = db.rawQuery ("SELECT * FROM course where coursename = ? and coursecode = ?  ",new String[] {String.valueOf(coursename),String.valueOf(coursecode)})  ;

        List<String> checkList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String db_coursename= cursor.getString(cursor.getColumnIndex("coursename"));
                String db_coursecode= cursor.getString(cursor.getColumnIndex("coursecode"));
                if(db_coursename.equals(coursename)&& db_coursecode.equals(coursecode)){
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
        inflater.inflate(R.menu.scanhome_menuitems, menu);//a different menu that offers more options
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
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



    private void restartActivity() {//restartActiivty method use to see changes when it restarts.
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }



    //first parameter is for onPostExecutelast is for do inBackGround
    private class GetCoursesAsync extends AsyncTask<String,Void,String> {//AsyncTask to retrieve Courses


        @Override
        protected String doInBackground(String... params) {

             DBHelper help = new DBHelper(CourseList.this);
            listView = (ListView)findViewById(R.id.courseList_view);

            courseList = help.getCourse();
            List<courses> FavList = new ArrayList<>();
            FavList=courseList;
            adapter = new CourseListAdapter(CourseList.this, FavList);

//            String selectQuery = "SELECT coursename,coursecode FROM course ";
//            SQLiteDatabase db = help.getWritableDatabase();
//            Cursor cursor = db.rawQuery(selectQuery, null);
//            List<courses> FavList = new ArrayList<>();
//            if (cursor.moveToFirst()) {
//                do {
//                    courses list = new courses();//course class instantiation
//                    list.setCourse(cursor.getString(0));//first column query
//                    list.setCode(cursor.getString(1));//second column of query
//                    FavList.add(list);
//                } while (cursor.moveToNext());
//            }
//            db.close();
//            cursor.close();
            //courseList= FavList;

            //List<courses> FavList = new ArrayList<>();
            //FavList=courseList;




            Log.v(TAG, "doInBackGround method");




            return null;
        }


        @Override
        protected void onPostExecute(String result) {//had to put onlciks here because it executes the methods in the UI

            listView.setEmptyView(findViewById(android.R.id.empty));
            listView.setAdapter(adapter);

            //adapter.notifyDataSetChanged();

            Log.v(TAG, "OnPostExecute method");

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                    Log.v(TAG, "inside setOnItemClickListener ");

                    courses selectedFromList = (courses) (listView.getItemAtPosition(position));
                    courseName = selectedFromList.getCourse();
                    courseCode = selectedFromList.getCode();
                    setPrefs();//sets the preferences to be transfered
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//long press gives different options
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                    Log.v(TAG, "inside setOnItemLongClickListener ");

                    courses selectedFromList = (courses) (listView.getItemAtPosition(pos));

                    courseName = selectedFromList.getCourse();
                    courseCode = selectedFromList.getCode();
                    String new_coursename = courseName.replaceAll("\\s+", "_");//replaces spaces with underscores
                    String new_coursecode = courseCode.replaceAll("\\s+", "_");//replaces spaces with underscores
                    oldTable = new_coursename + new_coursecode;
                    Listdialog();//list out the options for when long press is activated


                    return true;
                }
            });




        }
    }

}
