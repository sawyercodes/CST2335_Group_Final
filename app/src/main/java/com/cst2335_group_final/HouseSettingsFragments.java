package com.cst2335_group_final;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;

/**
 * The HouseSettingsFragments activity extends BaseActivity
 * in order to use the common menu resource.
 * This is the activity loaded from HouseSettings which
 * holds all the fragments. The fragment shown depends on the
 * user's ListView selection.
 *
 * Created by Victoria Sawyer on 2016-12-07.
 */
public class HouseSettingsFragments extends BaseActivity {

    /**
     * Set up the fragments to be used.
     * The fragment shown depends on the String passed through the intent
     * when the activity is launched.
     * Add the common toolbar.
     *
     * @param   savedInstanceState  Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_settings_fragments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String fragmentName = getIntent().getStringExtra("fragment name");
        GarageFragment garageFragment = new GarageFragment();
        HouseTempFragment houseTempFragment = new HouseTempFragment();
        WeatherFragment weatherFragment = new WeatherFragment();

        if (fragmentName.equals("Garage")) {
            setTitle(R.string.title_garage_fragment);
            message = R.string.help_dialog_message_garage;
            getSupportFragmentManager().beginTransaction().add(R.id.house_settings_fragments, garageFragment).commit();
        }
        if (fragmentName.equals("House Temperature")) {
            setTitle(R.string.title_house_temp_fragment);
            message = R.string.help_dialog_message_house_temp;
            getSupportFragmentManager().beginTransaction().add(R.id.house_settings_fragments, houseTempFragment).commit();
        }
        if (fragmentName.equals("Weather")) {
            setTitle(R.string.title_weather_fragment);
            message = R.string.help_dialog_message_weather;
            getSupportFragmentManager().beginTransaction().add(R.id.house_settings_fragments, weatherFragment).commit();
        }
    }

    /**
     * Handle the Items within the common menu resource.
     * This one produces a dialog box with an explanation of each fragment.
     * The message that appears depends on the fragment that is shown.
     *
     * @param   item    MenuItem
     * @return  boolean
     */
    //@Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                String messageStr = this.getResources().getString(message);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.help_dialog_title_house_settings)
                        .setMessage(Html.fromHtml(messageStr))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
                builder.create();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This holds the message to be passed into the AlertDialog.
     */
    private int message;

}
