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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
/**
 * Selection option details for the Automobile activity
 *
 * @author EVERETT HOLDEN
 * @version 1.0.0 2016.11.21
 */
public class MenuOptionDetailActivity extends AppCompatActivity implements CustomDialog.NoticeDialogListener {

    /**
     * Inflates the layout for the menu detail options
     * Starts the fragment for each option
     * @param savedInstanceState the bundle data saved from previous state
     *************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        setContentView(R.layout.activity_menu_option_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString(MenuOptionDetailFragment.ITEM_ID, getIntent().getStringExtra(MenuOptionDetailFragment.ITEM_ID));
            MenuOptionDetailFragment fragment = new MenuOptionDetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.option_detail_container, fragment, AUTO_FRAGMENT).commit();
        } else {
            MenuOptionDetailFragment fragment = (MenuOptionDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, AUTO_FRAGMENT);
        }
    }

    /**
     * Toolbar menu handler
     * @param item the menu item clicked in the ToolBar
     *************************************************************/
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(ACTIVITY_NAME, "In onOptionsItemSelected()");
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles button click for custom dialog box
     * Displayed in the Start option view
     * @param dialog the instance of the custom dialog displayed
     *************************************************************/
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        EditText text = (EditText) dialog.getDialog().findViewById(R.id.milageText);
        settings = getSharedPreferences(MenuOptionDetailActivity.SETTINGS_FUEL, Context.MODE_PRIVATE);//opens settings for fuel option
        String trip = text.getText().toString();//saves the input from the dialog
        int range = settings.getInt("range", 0);//retrived the value for range from settings
        //check if the trip exceeds the range, if so display alert
        if (Integer.parseInt(trip) > range) {

            AlertDialog alert = new AlertDialog.Builder(this)
                    .setTitle(R.string.alert_title)
                    .setMessage(R.string.alert_message)
                    .setPositiveButton("Ok",null)
                    .create();
            alert.show();

        }else{

            //open start option settings
            settings = getSharedPreferences(MenuOptionDetailActivity.SETTINGS_START, Context.MODE_PRIVATE);
            editor = settings.edit();
            editor.putInt("engine_started", 1).commit();//save value for engine running
            //open odometer settings
            settings = getSharedPreferences(MenuOptionDetailActivity.SETTINGS_ODOMETER, Context.MODE_PRIVATE);
            editor = settings.edit();
            int oldValue = Integer.parseInt(settings.getString("milage_total", "0"));//retrieve the old milage number
            int newValue = Integer.parseInt(trip);//new milage to add to odometer
            editor.putInt("milage_trip", newValue).commit();//save trip milage
            newValue += oldValue;//add trip milage to current odometer milage
            editor.putString("milage_total", String.valueOf(newValue)).commit();//save new odometer milage
            int fuelUsed = (int)(settings.getInt("milage_trip", 0) * 0.096f);//calc fuel used
            //open fuel settings
            settings = getSharedPreferences(MenuOptionDetailActivity.SETTINGS_FUEL, Context.MODE_PRIVATE);
            editor = settings.edit();
            int fuelRemaining = settings.getInt("fuel_level", 0) - fuelUsed;//calc remaining fuel value
            editor.putInt("fuel_level",fuelRemaining).commit();//save remaining fuel value
        }
    }

    /*stored the name of the Activity for debugging purposes*/
    protected static final String ACTIVITY_NAME = "DetailActivity";
    /*the name for the file storing fragment data*/
    protected static final String AUTO_FRAGMENT = "auto_fragment";
    /*the name of the file for storing start option settings*/
    protected static final String SETTINGS_START = "settings_start";
    /*the name of the file for storing odometer option settings*/
    protected static final String SETTINGS_ODOMETER = "settings_odometer";
    /*the name of the file for storing climate control option settings*/
    protected static final String SETTINGS_CLIMATE = "settings_climate";
    /*the name of the file for storing radio option settings*/
    protected static final String SETTINGS_RADIO = "settings_radio";
    /*the name of the file for storing light settings*/
    protected static final String SETTINGS_LIGHTS = "settings_lights";
    /*the name of the file for storing fuel option settings*/
    protected static final String SETTINGS_FUEL = "settings_fuel";
    /*an instance of SharedPreferences used to save and retrieve setting data*/
    protected SharedPreferences settings;
    /*an instance of the editor used to write SharedPreferences data*/
    protected SharedPreferences.Editor editor;
}

