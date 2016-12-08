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

import java.util.ArrayList;
/**
 * A model for the data stored in the database for the menu
 * selection options for the Automobile Activity
 *
 * @author EVERETT HOLDEN
 * @version 1.0.0 2016.11.21
 */
public class MenuRow {

    /*the primary key for the selection option table in the database*/
    protected int id;
    /*the title of each selection option*/
    protected String option;
    /*the primary key for the selection option table in the database*/
    protected String description;
    /*the primary key for the selection option table in the database*/
    protected int image;
    /*the primary key for the selection option table in the database*/
    public MenuRow(){

    }

    /**
     * Constructor for modeling a row in the database
     * Sets the values for the option title, description and the
     * image resource
     * @param option the title of the option
     * @param description the description of the menu option
     * @param image the value of the resource for the image icon
     *************************************************************/
    public MenuRow(String option, String description, int image){
        this.option = option;
        this.description = description;
        this.image = image;
    }

    /**
     * Creates instances of this class, adding data that will be inserted
     * into the database
     * @return an ArrayList of MenuRow objects
     *************************************************************/
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
