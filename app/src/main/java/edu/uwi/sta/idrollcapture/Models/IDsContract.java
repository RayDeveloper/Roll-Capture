package edu.uwi.sta.idrollcapture.Models;

import android.provider.BaseColumns;

/**
 * Created by 813117991 on 2/23/2016.
 */
public class IDsContract {
    private static final String INT_TYPE=" INTEGER ";
    private static final String TEXT_TYPE= " TEXT ";
    private static final String VAR_CHAR= " TEXT ";
    private static final String COMMA_SEP= " , ";


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + IDsEntry.TABLE_NAME + " ( " +
                    IDsEntry._ID + INT_TYPE + " PRIMARY KEY, "+
                    IDsEntry.COLUMN_NAME_idnumber + TEXT_TYPE + COMMA_SEP +
                    IDsEntry.COLUMN_NAME_time + TEXT_TYPE + COMMA_SEP +
                    IDsEntry.COLUMN_NAME_DATE_CREATED + TEXT_TYPE+ " );";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + IDsEntry.TABLE_NAME;

    public static abstract class IDsEntry implements BaseColumns {
        public static  String TABLE_NAME = "IDschecked";

        public static final String _ID = "ID";
        public static final String COLUMN_NAME_idnumber = "idnumber";
        public static final String COLUMN_NAME_time = "time";
        public static final String COLUMN_NAME_DATE_CREATED = "date";

    }





}
