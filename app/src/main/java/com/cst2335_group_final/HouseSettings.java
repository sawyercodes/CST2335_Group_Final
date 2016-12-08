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

/**
 * The HouseSettings activity extends BaseActivity
 * in order to use the common menu resource.
 * This is the main activity for house settings.
 * All fragments to control house settings are accessible
 * through its ListView.
 *
 * Created by Victoria Sawyer on 2016-12-07.
 */
public class HouseSettings extends BaseActivity {

    /**
     * Set up the fragments to be used.
     * Set up the list view to appear in this activity.
     * Add the common toolbar.
     *
     * @param   savedInstanceState  Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView houseSettingsList = (ListView) findViewById(R.id.house_settings_listview);
        String[] listContent = new String[] {"Garage", "House Temperature", "Weather"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.house_settings_textview, listContent);
        houseSettingsList.setAdapter(adapter);

        GarageFragment garageFragment = new GarageFragment();
        HouseTempFragment houseTempFragment = new HouseTempFragment();
        WeatherFragment weatherFragment = new WeatherFragment();

        houseSettingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Context context = view.getContext();
            Intent intent = new Intent(context, HouseSettingsFragments.class);
            intent.putExtra("fragment name", adapterView.getItemAtPosition(i).toString());
            context.startActivity(intent);
            }
        });

    }

    /**
     * Handle the Items within the common menu resource.
     * This one produces a dialog box with the author's name and version number.
     * It gives an explanation of how to use this page.
     *
     * @param   item    MenuItem
     * @return  boolean
     */
    //@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
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

}
