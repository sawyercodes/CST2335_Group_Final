package com.cst2335_group_final;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.content.Context.MODE_PRIVATE;

public class GarageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.garage_fragment, container, false);
        this.container = container;

        toggleGarageDoor = (ToggleButton) view.findViewById(R.id.toggle_garage_door);
        toggleGarageLight = (ToggleButton) view.findViewById(R.id.toggle_garage_light);

        //snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT);

        try {
            cam = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        toggleGarageDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleGarageDoor.isChecked()) {
                    Toast.makeText(view.getContext(), "Opening Garage", Toast.LENGTH_LONG).show();
                    garageDoorSetting = "open";
                } else {
                    Toast.makeText(view.getContext(), "Closing Garage", Toast.LENGTH_LONG).show();
                    garageDoorSetting = "closed";
                }
                toggleGarageLight.toggle();
                toggleFlashOn();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toggleGarageLight.toggle();
                        toggleFlashOff();
                    }
                }, 5000);
            }
        });

        toggleGarageLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleGarageLight.isChecked()) {
                    toggleFlashOn();
                    garageLightSetting = "on";
                } else {
                    toggleFlashOff();
                    garageLightSetting = "off";
                }
            }
        });

        return view;
    }

    private void toggleFlashOn() {
        try {
            if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Camera.Parameters params = cam.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(params);
                cam.startPreview();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        Snackbar.make(container.getRootView(), "Light On", Snackbar.LENGTH_SHORT).show();
    }

    private void toggleFlashOff() {
        try {
            if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Camera.Parameters params = cam.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                cam.setParameters(params);
                cam.stopPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Snackbar.make(container.getRootView(), "Light Off", Snackbar.LENGTH_SHORT).show();
    }

    private void loadSavedSettings() {
        SharedPreferences preferences = getActivity().getSharedPreferences(SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        garageDoorSetting = preferences.getString(STORED_GARAGE_DOOR, garageDoorSetting);
        garageLightSetting = preferences.getString(STORED_GARAGE_LIGHT, garageLightSetting);

        if (garageDoorSetting.equals("open") && (toggleGarageDoor.isChecked())) {
            toggleGarageDoor.toggle();
        }

        if (garageLightSetting.equals("on") && !(toggleGarageLight.isChecked())) {
            toggleGarageLight.toggle();
            toggleFlashOn();
        }
    }

    private void saveSettings() {
        SharedPreferences preferences = getActivity().getSharedPreferences(SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(STORED_GARAGE_DOOR, garageDoorSetting);
        editor.putString(STORED_GARAGE_LIGHT, garageLightSetting);
        editor.commit();
    }

    private View view;
    private ViewGroup container;
    private ToggleButton toggleGarageDoor;
    private ToggleButton toggleGarageLight;
    private Camera cam;

    public static final String SETTINGS = "com.example.victo.cst2335-finalproject.settings";
    public static final String STORED_GARAGE_DOOR = "storedGarageDoor";
    public static final String STORED_GARAGE_LIGHT = "storedGarageLight";

    private String garageDoorSetting = null;
    private String garageLightSetting = null;

}
