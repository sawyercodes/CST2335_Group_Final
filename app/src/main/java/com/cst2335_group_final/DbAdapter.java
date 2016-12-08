package com.cst2335_group_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by eberhard on 23-Nov-16.
 */

public class DbAdapter {

    public DbAdapter(Context context){
        this.context = context;
    }

    public DbAdapter open() throws SQLException{
        dbHelper = new AutoMenuDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(dbHelper != null){
            dbHelper.close();
        }
    }

    public long insert(ContentValues initialValues){
        return database.insertWithOnConflict(TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Cursor getRows(){
        return database.query(TABLE_NAME, FIELDS,null,null,null,null,null);
    }

    public boolean update(int id, ContentValues newValues){
        String[] selectionArgs = {String.valueOf(id)};
        return database.update(TABLE_NAME, newValues, KEY_ID + "=?", selectionArgs) > 0;
    }

    public boolean delete(int id){
        String[] selectionArgs = {String.valueOf(id)};
        return database.delete(TABLE_NAME, KEY_ID + "=?", selectionArgs) > 0;
    }

    public void upgrade() throws SQLException{
        dbHelper = new AutoMenuDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(database, 100, 101);
    }

    public static MenuRow getRowFromCursor(Cursor cursor){
        MenuRow row = new MenuRow();
        row.id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        row.option = cursor.getString(cursor.getColumnIndex(OPTION));
        row.description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
        row.image = cursor.getInt(cursor.getColumnIndex(IMAGE));
        return row;
    }

    public void add(){
        for(MenuRow row : MenuRow.makeRows()){
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
    protected static final String ACTIVITY_NAME = "AutoMenuDatabaseHelper";
    public static final String DATABASE_NAME = "auto_menu.db";
    public static final int DATABASE_VERSION = 001;
    public static final String TABLE_NAME = "MENU_TABLE";
    public static final String KEY_ID = "_id";
    public static final String OPTION = "option";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";
    public static final String[] FIELDS = new String[]{KEY_ID,OPTION,DESCRIPTION,IMAGE};
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + OPTION + " NOT NULL UNIQUE,"
                    + DESCRIPTION + " TEXT,"
                    + IMAGE + " INTEGER UNIQUE"
                    + ");";



    private class AutoMenuDatabaseHelper extends SQLiteOpenHelper {

        public AutoMenuDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            Log.i(ACTIVITY_NAME, "onCreate()");
            database.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            Log.i(ACTIVITY_NAME, "onUpgrade()");
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(database);
        }

        @Override
        public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            Log.i(ACTIVITY_NAME, "onDowngrade() >> oldVersion=" + oldVersion + " newVersion=" + newVersion);
        }
    }


    }
