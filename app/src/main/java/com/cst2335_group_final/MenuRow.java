package com.cst2335_group_final;

import java.util.ArrayList;

/**
 * Created by eberhard on 23-Nov-16.
 */

public class MenuRow {

    protected int id;
    protected String option;
    protected String description;
    protected int image;

    public MenuRow(){

    }

    public MenuRow(String option, String description, int image){
        this.option = option;
        this.description = description;
        this.image = image;
    }

    public static ArrayList<MenuRow> makeRows(){

        ArrayList<MenuRow> rows = new ArrayList<>();
        rows.add(new MenuRow("Temperature Settings","Driver & passenger side climate control",R.drawable.snowflake_white));
        rows.add(new MenuRow("Fuel Level","Displays current fuel level, range and economy",R.drawable.fuel_pump_white));
        rows.add(new MenuRow("Light","Control light settings, interior and exterior",R.drawable.high_beam_white));
        rows.add(new MenuRow("Radio","Radio and entertainment controls",R.drawable.music_white));
        rows.add(new MenuRow("GPS Navigation","Launches Google Navigator",R.drawable.navigation_white));
        rows.add(new MenuRow("Start!","Push button ignition",R.drawable.start_white));
        rows.add(new MenuRow("Odometer","Displays total distance and trip info",R.drawable.odometer_white));
        return rows;
    }
}
