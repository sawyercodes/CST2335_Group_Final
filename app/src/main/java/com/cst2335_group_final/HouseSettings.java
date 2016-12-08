package com.cst2335_group_final;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//public class HouseSettings extends AppCompatActivity {
public class HouseSettings extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        houseSettingsList = (ListView) findViewById(R.id.house_settings_listview);
        String[] listContent = new String[] {"Garage", "House Temperature", "Weather"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.house_settings_textview, listContent);
        houseSettingsList.setAdapter(adapter);

        garageFragment = new GarageFragment();
        houseTempFragment = new HouseTempFragment();
        weatherFragment = new WeatherFragment();

        houseSettingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*if (mTwoPane == true) {
                    if (adapterView.getItemAtPosition(i).equals("Garage")) {
                        getSupportFragmentManager().beginTransaction().remove(garageFragment).commit();
                        getSupportFragmentManager().beginTransaction().remove(houseTempFragment).commit();
                        getSupportFragmentManager().beginTransaction().remove(weatherFragment).commit();
                        //garageFragment = new GarageFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.house_settings_layout_frame, garageFragment).commit();
                    }
                    if (adapterView.getItemAtPosition(i).equals("House Temperature")) {
                        getSupportFragmentManager().beginTransaction().remove(garageFragment).commit();
                        getSupportFragmentManager().beginTransaction().remove(houseTempFragment).commit();
                        getSupportFragmentManager().beginTransaction().remove(weatherFragment).commit();
                        //houseTempFragment = new HouseTempFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.house_settings_layout_frame, houseTempFragment).commit();
                    }
                    if (adapterView.getItemAtPosition(i).equals("Weather")) {
                        getSupportFragmentManager().beginTransaction().remove(garageFragment).commit();
                        getSupportFragmentManager().beginTransaction().remove(houseTempFragment).commit();
                        getSupportFragmentManager().beginTransaction().remove(weatherFragment).commit();
                        //weatherFragment = new WeatherFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.house_settings_layout_frame, weatherFragment).commit();
                    }
                } else {*/
                    Context context = view.getContext();
                    Intent intent = new Intent(context, HouseSettingsFragments.class);
                    intent.putExtra("fragment name", adapterView.getItemAtPosition(i).toString());
                    context.startActivity(intent);
                //}
            }
        });

        /*if (findViewById(R.id.house_settings_layout_frame) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }*/
    }

    //@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                //HelpDialog.onCreateDialog(R.string.version_number, R.string.help_dialog_title_house_settings, R.string.help_dialog_message_house_settings, getBaseContext());

               // String messageStr = String.format(this.getResources().getString(R.string.version_number),
               //         this.getResources().getString(R.string.help_dialog_message_house_settings));
                int version = R.string.version_number;
                String versionStr = this.getResources().getString(version);
                int message = R.string.help_dialog_message_house_settings;
                String messageStr = String.format( this.getResources().getString(message),
                        String.valueOf(versionStr));
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


    private ListView houseSettingsList;
    private GarageFragment garageFragment;
    private HouseTempFragment houseTempFragment;
    private WeatherFragment weatherFragment;
    private boolean mTwoPane;

}
