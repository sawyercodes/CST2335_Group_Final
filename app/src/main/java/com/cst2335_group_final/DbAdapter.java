/* **************************************************************
 * Algonquin College - School of Advanced Technology
 * CST 2335 - Graphical Interface Programming
 * Final Group Project
 *
 * Author: EVERETT HOLDEN
 * Student #: 040812130
 * Network login name: hold0052
 * Lab instructor: ABDUL
 * Section: 014
 * Due date: 2016.12.09
 *
 *  -- Class definition
 * Purpose --
 * **************************************************************/
package com.cst2335_group_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database adapter for the Automobile Activity
 *
 * @author EVERETT HOLDEN
 * @version 1.0.0 2016.11.21
 */
public class DbAdapter {

    /**
     * Constructor for the database adapter
     * Sets the context instance variable
     *
     * @param context the context of the Activity
     *************************************************************/
    public DbAdapter(Context context) {
        this.context = context;
    }

    /**
     * Opens a connection to the SQLite database
     *************************************************************/
    public DbAdapter open() throws SQLException {
        dbHelper = new AutoMenuDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Closes the connection to the database
     *************************************************************/
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    /**
     * Inserts values into the database using the ContentValues class.
     * Any conflict in the insert that occur, the new data replaces the current data
     *************************************************************/
    public long insert(ContentValues initialValues) {
        return database.insertWithOnConflict(TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * Retrieves a row of data from the database, specifically
     * SELECT key_id, option, description, image FROM menu_table
     *************************************************************/
    public Cursor getRows() {
        return database.query(TABLE_NAME, FIELDS, null, null, null, null, null);
    }

    public void upgrade() throws SQLException {
        dbHelper = new AutoMenuDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(database, 100, 101);
    }

    public static MenuRow getRowFromCursor(Cursor cursor) {
        MenuRow row = new MenuRow();
        row.id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        row.option = cursor.getString(cursor.getColumnIndex(OPTION));
        row.description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
        row.image = cursor.getInt(cursor.getColumnIndex(IMAGE));
        return row;
    }

    /**
     * adds the selection option data for the main ListView.
     * Uses the MenuRow object which is a model of the seleciton items
     *************************************************************/
    public void add() {
        for (MenuRow row : MenuRow.makeRows()) {
            ContentValues newValues = new ContentValues();
            newValues.put(OPTION, row.option);
            newValues.put(DESCRIPTION, row.description);
            newValues.put(IMAGE, row.image);
            this.insert(newValues);
        }
    }

    private final Context context;
    private static SQLiteDatabase database;
    private AutoMenuDatabaseHelper dbHelper;
    protected static final String ACTIVITY_NAME = "AutoMenuDatabaseHelper";//stores the name of the activity fof debugging purposes
    public static final String DATABASE_NAME = "auto_menu.db";//the name of the database for storing selection options
    public static final int DATABASE_VERSION = 001;//the database version
    public static final String TABLE_NAME = "MENU_TABLE";//the name of the table storing selection option data
    public static final String KEY_ID = "_id";//and auto incremented primary key for identifying each row in the database
    public static final String OPTION = "option";//the name of the selection option such as, fuel level, start, odometer, temperature etc
    public static final String DESCRIPTION = "description";//the text description for each selection option
    public static final String IMAGE = "image";//the resource id for the image icon associated with the selection option
    public static final String[] FIELDS = new String[]{KEY_ID, OPTION, DESCRIPTION, IMAGE};//a string array of the attributes in the database table
    //the SQL statement that creates the table schema
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + OPTION + " NOT NULL UNIQUE,"
                    + DESCRIPTION + " TEXT,"
                    + IMAGE + " INTEGER UNIQUE"
                    + ");";


    /**
     * A custom database helper class
     *************************************************************/
    private class AutoMenuDatabaseHelper extends SQLiteOpenHelper {

        /**
         * Callse the SQLiteOpenHelper constructor, passing in the
         * current context, the name of the selection option database etc.
         *************************************************************/
        public AutoMenuDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Executes the SQL statement upon creation of the database
         * @database the instance of the SQLite database
         *************************************************************/
        @Override
        public void onCreate(SQLiteDatabase database) {
            Log.i(ACTIVITY_NAME, "onCreate()");
            database.execSQL(CREATE_TABLE);//executes the SQL statement for creating the selection option table
        }

        /**
         * Upgrade to the database
         * @param database the instance of the SQLite database
         * @param oldVersion the old database version integer
         * @param newVersion the new database version integer
         *************************************************************/
        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            Log.i(ACTIVITY_NAME, "onUpgrade()");
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);//drops the current table in the database
            onCreate(database);//calls onCreate to recreated the table
        }

        /**
         * Downgrade to the database
         * @param database the instance of the SQLite database
         * @param oldVersion the old database version integer
         * @param newVersion the new database version integer
         *************************************************************/
        @Override
        public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            Log.i(ACTIVITY_NAME, "onDowngrade() >> oldVersion=" + oldVersion + " newVersion=" + newVersion);
        }
    }


}
