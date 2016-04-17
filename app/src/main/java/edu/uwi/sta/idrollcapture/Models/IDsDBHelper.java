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


public class IDsDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "IDschecked";
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private SQLiteDatabase db;
    public String tablename;

    public IDsDBHelper(Context context,String table_name) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        tablename=table_name;
    }
    public void onCreate(SQLiteDatabase db) {

//        String createIDtable= "CREATE TABLE IF NOT EXISTS " + tablename + " ( " +
//                IDsContract.IDsEntry._ID + "INTEGER" + " PRIMARY KEY, "+
//                IDsContract.IDsEntry.COLUMN_NAME_idnumber + " TEXT " + " , " +
//                IDsContract.IDsEntry.COLUMN_NAME_time + " TEXT " + " , " +
//                IDsContract.IDsEntry.COLUMN_NAME_DATE_CREATED + " TEXT " + " );";
//
//        db.execSQL(createIDtable);

//        String createIDtable= "CREATE TABLE IF NOT EXISTS " + tablename + " ( " +
//                IDsContract.IDsEntry._ID + "INTEGER" + " PRIMARY KEY, "+
//                IDsContract.IDsEntry.COLUMN_NAME_idnumber + " TEXT " + " , " +
//                IDsContract.IDsEntry.COLUMN_NAME_time + " TEXT " + " , " +
//                IDsContract.IDsEntry.COLUMN_NAME_DATE_CREATED + " TEXT " + " );";
//
//
//        db.execSQL(createIDtable);
        db.execSQL(IDsContract.SQL_CREATE_ENTRIES);
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

    public void createFriendTable(String friendID) {
       String createIDtable= "CREATE TABLE IF NOT EXISTS " + friendID + " ( " +
                IDsContract.IDsEntry._ID + "INTEGER" + " PRIMARY KEY, "+
                IDsContract.IDsEntry.COLUMN_NAME_idnumber + " TEXT " + " , " +
                IDsContract.IDsEntry.COLUMN_NAME_time + " TEXT " + " , " +
                IDsContract.IDsEntry.COLUMN_NAME_DATE_CREATED + " TEXT " + " );";

        db.execSQL(createIDtable);
    }

    public  List<ID> getIDs(String tablename){
        //String selectQuery = "SELECT idnumber,time FROM "+  tablename ;
        Cursor cursor = getReadableDatabase().rawQuery("Select idnumber,time from " + tablename, null);
        List<ID> FavList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ID list = new ID();
                list.setID(cursor.getString(1));
                list.setTime(cursor.getString(2));
                FavList.add(list);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return FavList;
    }


}