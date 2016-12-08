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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * The main Activity for the Automobile Activity
 * Shows a ListView of options that can be selected
 * @author    EVERETT HOLDEN
 * @version   1.0.0 2016.11.21
 */
public class AutoMenuListActivity extends AppCompatActivity {

    /**
     * OnCreate inflates the listView, creates an ArrayList of options
     * that can be selected by retrieving them in a database.
     * A custom cursor adapter is added to the listView
     *
     * @param savedInstanceState bundle data stored from previous instance
     *************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_menu_list);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        if(findViewById(R.id.option_detail_container)!= null){
            twoPane = true;
        }

        listView = (ListView)findViewById(R.id.menuListView);
        options = new ArrayList<String>();
        adapter = new DbAdapter(this);
        adapter.open();
        adapter.add();
        cursor = adapter.getRows();
        listView.setAdapter(new AutoMenuCursorAdapter(this, cursor, 0));
    }

    /**
     * The database cursor is closed as well as the custom cursor adapter
     *************************************************************/
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        if(cursor != null){
            cursor.close();
        }
        if(adapter != null){
            adapter.close();
        }
    }

    protected static final String ACTIVITY_NAME = "AutoMenuListActivity";//string that stores the name of the Activity
    private ListView listView;//listview that displays the selection options
    private DbAdapter adapter;//database adapter that retrived the options from a database
    private Cursor cursor;//a cursor to retrieve a row from the database
    private boolean twoPane;//boolean to flag whether the screen size is a tablet

    /**
     * AutoMenuCursorAdapter class retrieves data from a database an
     * populates a layout with one row from the database. The layout is
     * places inside a cell of the listView. This is repeated for all the
     * data in the database.
     * @author    EVERETT HOLDEN
     * @version   1.0.0 2016.11.21     *
     *************************************************************/
    public class AutoMenuCursorAdapter extends CursorAdapter {

        public AutoMenuCursorAdapter(Context context, Cursor cursor, int flags){
            super(context, cursor, flags);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /**
         * Inflates the layout for the selection options
         *************************************************************/
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            Log.i(ACTIVITY_NAME, "In newView()");
            View view = inflater.inflate(R.layout.option_layout, null);//the layout for individual selection in the ListView is inflated
            return view ;
        }

        /**
         * The database cursor is closed as well as the custom cursor adapter
         *************************************************************/
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Log.i(ACTIVITY_NAME, "In getView()");
            if(convertView == null){
                convertView = inflater.inflate(R.layout.option_layout, parent, false);
            }
            convertView.setTag(position);
            return super.getView(position, convertView, parent);
        }

        /**
         * The data from the cursor is used to fill each selection layout
         *************************************************************/
        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            Log.i(ACTIVITY_NAME, "In bindView()");
            String item = cursor.getString(cursor.getColumnIndex(DbAdapter.OPTION));//a string of each selection option
            options.add(item);//each option is stored in an ArrayList to be used for selecting purposes
            TextView menuViewOpton = (TextView)view.findViewById(R.id.menuViewOption);
            menuViewOpton.setText(item);//the selection option is set to its place in the layout
            item = cursor.getString(cursor.getColumnIndex(DbAdapter.DESCRIPTION));//the selection description is retrieved from the cursor
            TextView menuViewDescription = (TextView)view.findViewById(R.id.menuViewDescription);
            menuViewDescription.setText(item);//the description is set to its place in the layout
            int resID = cursor.getInt(cursor.getColumnIndex(DbAdapter.IMAGE));//the image icon for each option is retrieved
            ImageView menuImage = (ImageView)view.findViewById(R.id.menuImage);
            menuImage.setImageResource(resID);//the image icon is set in the selection layout

            final ListView listView = (ListView)findViewById(R.id.menuListView);//the main ListView is referenced for adding a click handler

            //handels clicking for each selection options
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Cursor item = (Cursor)listView.getItemAtPosition(position);
                   String stuff = item.getString(item.getColumnIndex(DbAdapter.OPTION));
                   //check if selection is 'Navigation" option, to launch google maps
                   if(stuff.equals("GPS Navigation")){
                       Uri uri = Uri.parse("geo:45.4215,-75.6972");
                       Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                       intent.setPackage("com.google.android.apps.maps");
                       startActivity(intent);
                       //if screen size is large, show fragment next to ListView
                   }else if(twoPane){
                       Bundle bundle = new Bundle();
                       bundle.putString(MenuOptionDetailFragment.ITEM_ID,stuff );
                       MenuOptionDetailFragment fragment = new MenuOptionDetailFragment();
                       fragment.setArguments(bundle);
                       getSupportFragmentManager().beginTransaction()
                               .replace(R.id.option_detail_container, fragment)
                               .commit();
                   }else{
                       //launch the detailed option view for each selection item
                       Context context = view.getContext();
                       Intent intent = new Intent(context, MenuOptionDetailActivity.class);
                       intent.putExtra(MenuOptionDetailFragment.ITEM_ID, stuff);
                       context.startActivity(intent);
                   }
               }
           });
        }
        /*string stores the name of the class for debugging purposes*/
        protected static final String ACTIVITY_NAME = "AutoMenuCursorAdapter";
    }
    /*inflates layour resources*/
    private LayoutInflater inflater;
    /*stores the name of each resource*/
    private ArrayList<String> options;

}
