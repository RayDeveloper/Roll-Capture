package edu.uwi.sta.idrollcapture.Models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.sql.Date;

/**
 * Created by 813117991 on 2/23/2016.
 */
public class CourseContract {
    private static final String INT_TYPE=" INTEGER ";
    private static final String TEXT_TYPE= " TEXT ";
    private static final String COMMA_SEP= " , ";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CourseEntry.TABLE_NAME + " ( " +
                    CourseEntry._ID + INT_TYPE + " PRIMARY KEY, "+
                    CourseEntry.COLUMN_NAME_COURSE_NAME + TEXT_TYPE + COMMA_SEP +
                    CourseEntry.COLUMN_NAME_COURSE_CODE + TEXT_TYPE + COMMA_SEP +
                    CourseEntry.COLUMN_NAME_DATE_CREATED + TEXT_TYPE+ " );";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CourseEntry.TABLE_NAME;

    public static abstract class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "course";

        public static final String _ID = "courseID";
        public static final String COLUMN_NAME_COURSE_NAME = "coursename";
        public static final String COLUMN_NAME_COURSE_CODE = "coursecode";
        public static final String COLUMN_NAME_DATE_CREATED = "datecreated";

    }





}
