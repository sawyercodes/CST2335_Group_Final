package com.cst2335_group_final;

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

/**
 * The GarageFragment extends Fragment.
 * In this Fragment the user can open the garage door
 * and turn on the light.
 * This is displayed in HouseSettingsFragments when
 * the garage item in HouseSetting's ListView is selected.
 *
 * Created by Victoria Sawyer on 2016-12-07.
 */
public class GarageFragment extends Fragment {

    /**
     * The GarageFragment's onCreateView inflates the layout
     * to appear within HouseSettingsFragment activity.
     * This is where the ToggleButtons for garage door and
     * garage light are handled.
     *
     * @param   inflater    LayoutInflater
     * @param   container   ViewGroup
     * @param   savedInstanceState  Bundle
     * @return  View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.garage_fragment, container, false);
        this.container = container;

        final ToggleButton toggleGarageDoor = (ToggleButton) view.findViewById(R.id.toggle_garage_door);
        final ToggleButton toggleGarageLight = (ToggleButton) view.findViewById(R.id.toggle_garage_light);

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
                } else {
                    Toast.makeText(view.getContext(), "Closing Garage", Toast.LENGTH_LONG).show();
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
                } else {
                    toggleFlashOff();
                }
            }
        });

        return view;
    }

    /**
     * The method implemented when the garage light is toggled on.
     * If possible, it toggles the phone's flashlight on.
     * It also produces a Snackbar.
     */
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

    /**
     * The method implemented when the garage light is toggled off.
     * If possible, it toggles the phone's flashlight off.
     * It also produces a Snackbar.
     */
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

    /**
     * This variable holds the ViewGroup of the fragment, the parent activity which it appears in.
     */
    private ViewGroup container;

    /**
     * The camera of the phone.
     */
    private Camera cam;

}
