package com.cst2335_group_final;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The HouseTempFragment extends Fragment.
 * In this Fragment the user can set the temperature of the house
 * and set a schedule for the temperature.
 * This is displayed in HouseSettingsFragments when
 * the House Temperature item in HouseSetting's ListView is selected.
 *
 * Created by Victoria Sawyer on 2016-12-07.
 */
public class HouseTempFragment extends Fragment {

    /**
     * The HouseTempFragment's onCreateView inflates the layout
     * to appear within HouseSettingsFragment activity.
     * This sets up a ListView to hold the temperature schedule.
     * Items for the ListView are loaded from a database.
     * The schedule is saved in a database to be accessed after leaving the fragment.
     * The buttons Edit and Delete, used to add and delete schedule items,
     * are handled here.
     *
     * @param   inflater    LayoutInflater
     * @param   container   ViewGroup
     * @param   savedInstanceState  Bundle
     * @return  View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.house_temp_fragment, container, false);

        ListView houseTempList = (ListView) view.findViewById(R.id.house_temp_listview);
        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.house_temp_number_picker);
        Button tempScheduleButton = (Button) view.findViewById(R.id.edit_temp_schdule);
        Button deleteScheduleButton = (Button) view.findViewById(R.id.delete_temp_schdule);

        tempSchedule = new ArrayList<>();
        adapter = new ArrayAdapter<String>(view.getContext(), R.layout.house_temp_textview, tempSchedule);
        houseTempList.setAdapter(adapter);

        TempScheduleDB tempScheduleDB = new TempScheduleDB(view.getContext());
        tempDB = tempScheduleDB.getWritableDatabase();
        Cursor cursor = tempDB.query(tempScheduleDB.TABLE_NAME, new String[]{tempScheduleDB.KEY_ID, tempScheduleDB.KEY_SCHEDULE}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String schedule = cursor.getString(cursor.getColumnIndex(tempScheduleDB.KEY_SCHEDULE));
                tempSchedule.add(schedule);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        numberPicker.setMaxValue(10);
        numberPicker.setMaxValue(30);

        tempScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateEditDialog();
            }
        });

        deleteScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateDeleteDialog();
            }
        });


        return view;
    }

    /**
     * This creates the Dialog to be used by the Edit button.
     * This adds new items to the database and to the ListView.
     *
     * @return  Dialog
     */
    public Dialog onCreateEditDialog() {
        final String degree = Character.toString((char) 0x00B0);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle(R.string.edit_schedule_title)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText name = (EditText) view.findViewById(R.id.dialog_name_edittext);
                        EditText temp = (EditText) view.findViewById(R.id.dialog_temp_edittext);
                        EditText time = (EditText) view.findViewById(R.id.dialog_time_edittext);
                        String str = name.getText().toString() + ": " + temp.getText().toString() + degree + " @ " + time.getText().toString();
                        tempSchedule.add(str);
                        adapter.notifyDataSetChanged();
                        cValues.put("schedule",str);
                        tempDB.insert(TempScheduleDB.TABLE_NAME, null, cValues);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
        return builder.create();
    }

    /**
     * This creates the Dialog to be used by the Delete button.
     * This deletes items from the ListView by the name of the scheduled item.
     * It also removes the item from the database.
     *
     * @return  Dialog
     */
    public Dialog onCreateDeleteDialog() {
        final String degree = Character.toString((char) 0x00B0);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_delete_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle(R.string.delete_schedule_title)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText deleteName = (EditText) view.findViewById(R.id.dialog_name_delete_edittext);
                        String str = deleteName.getText().toString();
                        ArrayList<String> removeList = new ArrayList<String>();
                        ArrayList<String> removeID = new ArrayList();
                        Cursor cursor;
                        Iterator iter = tempSchedule.iterator();
                        String scheduleItem;
                        while (iter.hasNext()) {
                            scheduleItem = (String) iter.next();
                            if (scheduleItem.contains(str)) {
                                removeList.add(scheduleItem);
                                cursor = tempDB.rawQuery("SELECT " + TempScheduleDB.KEY_ID + " FROM " + TempScheduleDB.TABLE_NAME + " WHERE " + TempScheduleDB.KEY_SCHEDULE + " = '" + scheduleItem + "'", null);
                                if (cursor.moveToFirst()) {
                                    do {
                                        removeID.add(cursor.getString(cursor.getPosition()));
                                        cursor.moveToNext();
                                    } while (!cursor.isAfterLast());
                                }
                            }
                        }
                        tempSchedule.removeAll(removeList);

                        iter = removeID.iterator();
                        while (iter.hasNext()) {
                            tempDB.execSQL("DELETE FROM " + TempScheduleDB.TABLE_NAME + " WHERE " + TempScheduleDB.KEY_ID + " = '" + iter.next() + "'");
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
        return builder.create();
    }

    /**
     * Close the database when the fragment is closed.
     */
    public void onDestroy() {
        super.onDestroy();
        tempDB.close();
    }

    /**
     * The container for the fragment to appear within.
     */
    private View view;

    /**
     * The adapter used to display the items in the ListView.
     */
    ArrayAdapter<String> adapter;

    /**
     * The ArrayList of Strings holding the temperature schedule items.
     */
    private ArrayList<String> tempSchedule;

    /**
     * The database that holds the values of the temperature schedule.
     */
    private static SQLiteDatabase tempDB;

    /**
     * This stores a set of values to be stored by the database.
     */
    private final ContentValues cValues = new ContentValues();

}
