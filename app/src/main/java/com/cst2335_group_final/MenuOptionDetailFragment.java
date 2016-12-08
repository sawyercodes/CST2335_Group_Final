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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * The fragment for the Automobile activity that inflates
 * the layout for each selection option in the menu.
 * Implements View.OnClickLister and handles the events for
 * buttons found in the option views
 * @author EVERETT HOLDEN
 * @version 1.0.0 2016.11.21
 */
public class MenuOptionDetailFragment extends Fragment implements View.OnClickListener {

    /**
     * Default constructor as required by the Fragment API
     *************************************************************/
    public MenuOptionDetailFragment() {
    }

    /**
     * Created the fragment. Checks if the arguments passed into the
     * fragment contain the ITEM_ID file that stores the option
     * that was selected in the ListView
     * @param savedInstanceState the bundle data saved from prior state
     *************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(ACTIVITY_NAME, "In onCreate()");
        //check if ITEM_ID file is in fragment arguments
        if (getArguments().containsKey(ITEM_ID)) {
            //stores the data passed into the fragment identifying the option selected
            item = getArguments().getString(ITEM_ID);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Inflates the correct view for the Fragment depending on the
     * option selected and the string value pass in from ITEM_ID and
     * stored in the item instance variable
     *
     * @param inflater instance of LayoutInflater that inflates the required view
     * @param inflater container the ViewGroup that contains the current view
     * @param savedInstanceState the bundle data saved from prior state
     *************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i(ACTIVITY_NAME, "In onCreateView()");
        View view;
        //check if item is not null
        if (item != null) {
            /* switch statment maps the requirement for each option selected. Depending on the selection
               a specific layout is inflated into the view and the widgets in that view are instantiated */
            switch (item) {
                case "Temperature Settings":
                    view = inflater.inflate(R.layout.option_climate_control, container, false);
                    Snackbar.make(container, "Temperature Settings", Snackbar.LENGTH_SHORT).show();
                    return view;
                case "Fuel Level":
                    view = inflater.inflate(R.layout.option_fuel, container, false);
                    //open fuel settings
                    settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_FUEL, Context.MODE_PRIVATE);
                    btnRefuel = (Button) view.findViewById(R.id.btn_refuel);//instantiate refuel button
                    btnRefuel.setOnClickListener(this);//add event handler
                    fuelBar = (ProgressBar) view.findViewById(R.id.barFuelLevel);//instantiate fuel gauge
                    fuelBar.setProgress(2 * settings.getInt("fuel_level", 0));//set fuel level from data in settings
                    textRange = (TextView) view.findViewById(R.id.textRange);//instantiate
                    textFuel.setText(String.valueOf(settings.getInt("fuel_level", 0)));
                    textRange.setText(String.valueOf(calculateRange(settings.getInt("fuel_level", 0))));
                    return view;
                case "Light":
                    view = inflater.inflate(R.layout.option_lights, container, false);
                    settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_LIGHTS, Context.MODE_PRIVATE);
                    editor = settings.edit();
                    btnFog = (ImageButton) view.findViewById(R.id.btn_fog);
                    if (settings.getInt("btn_fog", 0) == 1) {
                        btnFog.setImageResource(R.drawable.fog_light_on);
                        editor.putInt("btn_fog", 1).commit();
                    } else {
                        btnFog.setImageResource(R.drawable.fog_light);
                        editor.putInt("btn_fog", 0).commit();
                    }
                    btnHighBeam = (ImageButton) view.findViewById(R.id.btn_high_beam);
                    if (settings.getInt("btn_high_beam", 0) == 1) {
                        editor.putInt("btn_high_beam", 1).commit();
                        btnHighBeam.setImageResource(R.drawable.high_beam_on);
                    } else {
                        editor.putInt("btn_high_beam", 0).commit();
                        btnHighBeam.setImageResource(R.drawable.high_beam);
                    }
                    btnLowBeam = (ImageButton) view.findViewById(R.id.btn_low_beam);
                    if (settings.getInt("btn_low_beam", 0) == 1) {
                        editor.putInt("btn_low_beam", 1).commit();
                        btnLowBeam.setImageResource(R.drawable.low_beam_on);
                    } else {
                        editor.putInt("btn_low_beam", 0).commit();
                        btnLowBeam.setImageResource(R.drawable.low_beam);
                    }
                    btnLamp = (ImageButton) view.findViewById(R.id.btn_lamp);

                    if (settings.getInt("btn_lamp", 0) == 1) {
                        editor.putInt("btn_lamp", 1).commit();
                        btnLamp.setImageResource(R.drawable.dome_light_on);
                    } else {
                        editor.putInt("btn_lamp", 0).commit();
                        btnLamp.setImageResource(R.drawable.dome_light);
                    }
                    fabFuelLight = (FloatingActionButton) view.findViewById(R.id.fab_fuel_light);
                    fabBootLight = (FloatingActionButton) view.findViewById(R.id.fab_boot_light);
                    btnFog.setOnClickListener(this);
                    btnHighBeam.setOnClickListener(this);
                    btnLowBeam.setOnClickListener(this);
                    btnLamp.setOnClickListener(this);
                    fabFuelLight.setOnClickListener(this);
                    fabBootLight.setOnClickListener(this);
                    Snackbar.make(container, "Light Controls", Snackbar.LENGTH_SHORT).show();
                    return view;
                case "Radio":
                    Snackbar.make(container, "Radio", Snackbar.LENGTH_SHORT).show();
                    break;
                case "Start!":
                    view = inflater.inflate(R.layout.option_start_car, container, false);
                    settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_START, Context.MODE_PRIVATE);
                    textStart = (TextView) view.findViewById(R.id.textStart);
                    textRunning = (TextView) view.findViewById(R.id.textRunning);
                    btnStart = (ImageButton) view.findViewById(R.id.btn_start);
                    btnStart.setOnClickListener(this);
                    if (settings.getInt("engine_started", 0) == 0) {
                        textStart.setText(R.string.engine_start);
                        btnStart.setImageResource(R.drawable.start_red);
                    } else {
                        textStart.setText(R.string.engine_stop);
                        btnStart.setImageResource(R.drawable.start_green);
                    }
                    if (settings.getInt("check_oil", 0) == 1) {
                        oil = (ImageView) view.findViewById(R.id.imageOil);
                        oil.setImageResource(R.drawable.oil_light);
                    }
                    return view;
                case "Odometer":
                    view = inflater.inflate(R.layout.option_odometer, container, false);
                    settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_ODOMETER, Context.MODE_PRIVATE);
                    fabResetTrip = (FloatingActionButton) view.findViewById(R.id.fab_reset_trip);
                    fabResetTrip.setOnClickListener(this);
                    textMilageTotal = (TextView) view.findViewById(R.id.totalMilageText);
                    textMilageTotal.setText(settings.getString("milage_total", "0"));
                    textTrip = (TextView) view.findViewById(R.id.tripText);
                    textTrip.setText(String.valueOf(settings.getInt("milage_trip", 0)));
                    String odometer = textMilageTotal.getText().toString();
                    int milage = Integer.parseInt(odometer);
                    if (milage % 6000 == 0) {
                        settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_START, Context.MODE_PRIVATE);
                        editor = settings.edit();
                        editor.putInt("check_oil", 1).commit();
                    }
                    Snackbar.make(container, "Odometer", Snackbar.LENGTH_SHORT).show();
                    return view;
            }
        }
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_start:
                settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_START, Context.MODE_PRIVATE);
                editor = settings.edit();
                if (settings.getInt("engine_started", 0) == 0) {
                    startEngine();
                } else {
                    stopEngine();
                }
                break;
            case R.id.btn_refuel:
                new Fuel().execute();
                break;
            case R.id.fab_reset_trip:
                settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_ODOMETER, Context.MODE_PRIVATE);
                editor = settings.edit();
                editor.putInt("milage_trip", 0).commit();
                textTrip.setText(String.valueOf(settings.getInt("milage_trip", 0)));
                break;
            case R.id.btn_fog:
                settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_LIGHTS, Context.MODE_PRIVATE);
                editor = settings.edit();
                if (settings.getInt("btn_fog", 0) == 0) {
                    editor.putInt("btn_fog", 1).commit();
                    btnFog.setImageResource(R.drawable.fog_light_on);
                } else {
                    editor.putInt("btn_fog", 0).commit();
                    btnFog.setImageResource(R.drawable.fog_light);
                }
                break;
            case R.id.btn_high_beam:
                settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_LIGHTS, Context.MODE_PRIVATE);
                editor = settings.edit();
                if (settings.getInt("btn_high_beam", 0) == 0) {
                    editor.putInt("btn_high_beam", 1).commit();
                    btnHighBeam.setImageResource(R.drawable.high_beam_on);
                } else {
                    editor.putInt("btn_high_beam", 0).commit();
                    btnHighBeam.setImageResource(R.drawable.high_beam);
                }
                break;
            case R.id.btn_low_beam:
                settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_LIGHTS, Context.MODE_PRIVATE);
                editor = settings.edit();
                if (settings.getInt("btn_low_beam", 0) == 0) {
                    editor.putInt("btn_low_beam", 1).commit();
                    btnLowBeam.setImageResource(R.drawable.low_beam_on);
                } else {
                    editor.putInt("btn_low_beam", 0).commit();
                    btnLowBeam.setImageResource(R.drawable.low_beam);
                }
                break;
            case R.id.btn_lamp:
                settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_LIGHTS, Context.MODE_PRIVATE);
                editor = settings.edit();
                if (settings.getInt("btn_lamp", 0) == 0) {
                    editor.putInt("btn_lamp", 1).commit();
                    btnLamp.setImageResource(R.drawable.dome_light_on);
                } else {
                    editor.putInt("btn_lamp", 0).commit();
                    btnLamp.setImageResource(R.drawable.dome_light);
                }
                break;
            case R.id.fab_fuel_light:
                break;
            case R.id.fab_boot_light:
                break;
        }
    }

    private void startEngine() {
        settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_START, Context.MODE_PRIVATE);
        btnStart.setImageResource(R.drawable.start_green);
        textStart.setText(R.string.engine_stop);
        textRunning.setText(R.string.engine_running);
        editor = settings.edit();
        editor.putInt("engine_started", 1).commit();
        Toast.makeText(getContext(), "engine started", Toast.LENGTH_SHORT).show();
        CustomDialog dialog = new CustomDialog();
        dialog.show(getFragmentManager(), "Drive");
    }

    private void stopEngine() {
        btnStart.setImageResource(R.drawable.start_red);
        textStart.setText(R.string.engine_start);
        textRunning.setVisibility(View.INVISIBLE);
        settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_START, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt("engine_started", 0).commit();
        Toast.makeText(getContext(), "engine stopped", Toast.LENGTH_SHORT).show();
    }

    private int calculateRange(int fuel) {
        float fuelEconomy = 0.096f;
        int range = (int) (fuel / fuelEconomy);
        settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_FUEL, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt("range", range).commit();
        return range;
    }

    protected class Fuel extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {

            for (int i = 0; i < 50; i++) {
                SystemClock.sleep(100);
                publishProgress((i * 2 + 1));
            }
            return null;
        }

        protected void onProgressUpdate(Integer... params) {
            fuelBar.setProgress(params[0]);
            textFuel.setText(String.valueOf(params[0] / 2));
            textRange.setText(String.valueOf(calculateRange(params[0] / 2)));
        }

        protected void onPostExecute(Integer result) {
            settings = getActivity().getSharedPreferences(MenuOptionDetailActivity.SETTINGS_FUEL, Context.MODE_PRIVATE);
            editor = settings.edit();
            editor.putInt("fuel_level", 50).commit();
            int range = calculateRange(settings.getInt("fuel_level", 0));
            editor.putInt("range", range).commit();
        }
    }

    protected static final String ACTIVITY_NAME = "DetailFragment";
    public static final String ITEM_ID = "item_id";
    protected SharedPreferences settings;
    protected SharedPreferences.Editor editor;
    private final float fuelEconomy = 0.096f;
    private String item;
    private ImageButton btnStart;
    private ImageView oil;
    private TextView textStart;
    private TextView textRunning;
    private TextView textMilageTotal;
    private TextView textTrip;
    private ProgressBar fuelBar;
    private TextView textFuel;
    private TextView textRange;
    private Button btnRefuel;
    private FloatingActionButton fabResetTrip;
    private ImageButton btnFog;
    private ImageButton btnHighBeam;
    private ImageButton btnLowBeam;
    private ImageButton btnLamp;
    private FloatingActionButton fabFuelLight;
    private FloatingActionButton fabBootLight;
}
