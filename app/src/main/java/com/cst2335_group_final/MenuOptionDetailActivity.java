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

public class MenuOptionDetailActivity extends AppCompatActivity implements CustomDialog.NoticeDialogListener {

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

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(ACTIVITY_NAME, "In onOptionsItemSelected()");
        return super.onOptionsItemSelected(item);
    }

    protected static final String ACTIVITY_NAME = "DetailActivity";
    protected static final String AUTO_FRAGMENT = "auto_fragment";
    protected static final String SETTINGS_START = "settings_start";
    protected static final String SETTINGS_ODOMETER = "settings_odometer";
    protected static final String SETTINGS_CLIMATE = "settings_climate";
    protected static final String SETTINGS_RADIO = "settings_radio";
    protected static final String SETTINGS_LIGHTS = "settings_lights";
    protected static final String SETTINGS_FUEL = "settings_fuel";
    protected SharedPreferences settings;
    protected SharedPreferences.Editor editor;

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        EditText text = (EditText) dialog.getDialog().findViewById(R.id.milageText);
        settings = getSharedPreferences(MenuOptionDetailActivity.SETTINGS_FUEL, Context.MODE_PRIVATE);
        String trip = text.getText().toString();
        int range = settings.getInt("range", 0);
        if (Integer.parseInt(trip) > range) {

            AlertDialog alert = new AlertDialog.Builder(dialog.getActivity())
                    .setTitle(R.string.alert_title)
                    .setMessage(R.string.alert_message)
                    .setPositiveButton("Ok",null)
                    .create();
            alert.show();

        }else{

            settings = getSharedPreferences(MenuOptionDetailActivity.SETTINGS_START, Context.MODE_PRIVATE);
            editor = settings.edit();
            editor.putInt("engine_started", 1).commit();
            settings = getSharedPreferences(MenuOptionDetailActivity.SETTINGS_ODOMETER, Context.MODE_PRIVATE);
            editor = settings.edit();
            int oldValue = Integer.parseInt(settings.getString("milage_total", "0"));
            int newValue = Integer.parseInt(text.getText().toString());
            editor.putInt("milage_trip", newValue).commit();
            newValue += oldValue;
            editor.putString("milage_total", String.valueOf(newValue)).commit();
            int fuelUsed = (int)(settings.getInt("milage_trip", 0) * 0.096f);
            settings = getSharedPreferences(MenuOptionDetailActivity.SETTINGS_FUEL, Context.MODE_PRIVATE);
            editor = settings.edit();
            int fuelRemaining = settings.getInt("fuel_level", 0) - fuelUsed;
            editor.putInt("fuel_level",fuelRemaining).commit();
        }
    }


}

