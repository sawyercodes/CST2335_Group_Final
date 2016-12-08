package com.cst2335_group_final;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;

public class HouseSettingsFragments extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_settings_fragments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentName = getIntent().getStringExtra("fragment name");
        garageFragment = new GarageFragment();
        houseTempFragment = new HouseTempFragment();
        weatherFragment = new WeatherFragment();

        if (fragmentName.equals("Garage")) {
            setTitle(R.string.title_garage_fragment);
            message = R.string.help_dialog_message_garage;
//            getSupportFragmentManager().beginTransaction().remove(garageFragment).commit();
//            getSupportFragmentManager().beginTransaction().remove(houseTempFragment).commit();
//            getSupportFragmentManager().beginTransaction().remove(weatherFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.house_settings_fragments, garageFragment).commit();
        }
        if (fragmentName.equals("House Temperature")) {
            setTitle(R.string.title_house_temp_fragment);
            message = R.string.help_dialog_message_house_temp;
//            getSupportFragmentManager().beginTransaction().remove(houseTempFragment).commit();
//            getSupportFragmentManager().beginTransaction().remove(garageFragment).commit();
//            getSupportFragmentManager().beginTransaction().remove(weatherFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.house_settings_fragments, houseTempFragment).commit();
        }
        if (fragmentName.equals("Weather")) {
            setTitle(R.string.title_weather_fragment);
            message = R.string.help_dialog_message_weather;
//            getSupportFragmentManager().beginTransaction().remove(houseTempFragment).commit();
//            getSupportFragmentManager().beginTransaction().remove(garageFragment).commit();
//            getSupportFragmentManager().beginTransaction().remove(weatherFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.house_settings_fragments, weatherFragment).commit();
        }
    }
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

    private String fragmentName;
    private GarageFragment garageFragment;
    private HouseTempFragment houseTempFragment;
    private WeatherFragment weatherFragment;
    private int message;

}
