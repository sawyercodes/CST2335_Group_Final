package com.cst2335_group_final;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * The TempScheduleDB extends SQLiteOpenHelper
 * to create a the tempSchedule database
 * which holds the values of temperature schedule
 * from HouseTempFragments ListView.
 *
 * Created by Victoria Sawyer on 2016-12-07.
 */
public class TempScheduleDB extends SQLiteOpenHelper {

    /**
     * Create the helper object to manage the database by calling to the
     * super constructor, SQLiteOenHelper.
     * @param   ctx   Context
     */
    public TempScheduleDB(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Create the table in the database.
     *
     * @param   db  The database to be used.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_SCHEDULE + " TEXT);"
        );
        Log.i(ACTIVITY_NAME, "Calling onCreate()");
    }

    /**
     * Uprgrade the database version number.
     *
     * @param db            SQLiteDatabase
     * @param oldVersion    int
     * @param newVersion    int
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpdate(), oldVersion=" + oldVersion + ", newVersion=" + newVersion);
    }

    /**
     * The name of the current activity.
     */
    protected static final String ACTIVITY_NAME = "TempScheduleDB";

    /**
     * The name of the database.
     */
    private static final String DATABASE_NAME = "tempSchedule.db";

    /**
     * The version number of the database.
     */
    private static final int VERSION_NUM = 1;

    /**
     * The name of the table that stores the temperature schedule values.
     */
    protected static final String TABLE_NAME = "TEMP_SCHEDULE_TABLE";

    /**
     * The name of the column that holds the ID of the temperature schedule values.
     */
    protected static final String KEY_ID = "id";

    /**
     * The name of the column that holds the string of the temperature schedule values.
     */
    protected static final String KEY_SCHEDULE = "schedule";
}
