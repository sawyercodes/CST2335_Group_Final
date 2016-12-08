package com.cst2335_group_final;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by victo on 2016-10-12.
 */

public class TempScheduleDB extends SQLiteOpenHelper {

    protected static final String ACTIVITY_NAME = "TempScheduleDB";
    private static final String DATABASE_NAME = "tempSchedule.db";
    private static final int VERSION_NUM = 1;

    protected static final String TABLE_NAME = "TEMP_SCHEDULE_TABLE";
    protected static final String KEY_ID = "id";
    protected static final String KEY_SCHEDULE = "schedule";

    public TempScheduleDB(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_SCHEDULE + " TEXT);"
        );
        Log.i(ACTIVITY_NAME, "Calling onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpdate(), oldVersion=" + oldVersion + ", newVersion=" + newVersion);
    }
}
