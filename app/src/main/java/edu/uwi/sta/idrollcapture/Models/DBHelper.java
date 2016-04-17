package edu.uwi.sta.idrollcapture.Models;

/**
 * Created by Raydon on 3/14/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.uwi.sta.idrollcapture.CourseList;


public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "course.db";
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CourseContract.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(CourseContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public  List<courses> getCourse(){
        String selectQuery = "SELECT coursename,coursecode FROM course ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<courses> FavList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                courses list = new courses();
                list.setCourse(cursor.getString(0));
                list.setCode(cursor.getString(1));
                FavList.add(list);
            } while (cursor.moveToNext());
        }
        db.close();
        return FavList;
    }


}