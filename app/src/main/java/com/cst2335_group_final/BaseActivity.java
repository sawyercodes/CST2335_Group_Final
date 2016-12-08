package com.cst2335_group_final;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * The BaseActivity class is the root of the Activity classes.
 * It extends AppCompatActivity.
 * It enables the use of a shared menu.
 *
 * Created by Victoria Sawyer on 2016-12-07.
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * Create the Menu to be included in the activity.
     * It uses the Menu Resource file menu_house_settings.xml
     *
     * @param   menu    Menu
     * @return  true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_house_settings, menu);
        return true;
    }
}
