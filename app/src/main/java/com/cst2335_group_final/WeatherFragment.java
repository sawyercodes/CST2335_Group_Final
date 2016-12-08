package com.cst2335_group_final;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The WeatherFragment extends Fragment.
 * In this Fragment the user view the weather for Ottawa.
 * It uses ASyncTask to get the weather information form a given URL.
 * This is displayed in HouseSettingsFragments when
 * the Weather item in HouseSetting's ListView is selected.
 *
 * Created by Victoria Sawyer on 2016-12-07.
 */
public class WeatherFragment extends Fragment{

    /**
     * The WeatherFragment's onCreateView inflates the layout
     * to appear within HouseSettingsFragment activity.
     * This calls ForecastQuery to display the weather.
     *
     * @param   inflater    LayoutInflater
     * @param   container   ViewGroup
     * @param   savedInstanceState  Bundle
     * @return  View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        context = view.getContext();

        progressBar = (ProgressBar) view.findViewById(R.id.weather_progressbar);
        currentTempText = (TextView) view.findViewById(R.id.current_temp_text);
        minTempText = (TextView) view.findViewById(R.id.min_temp_text);
        maxTempText = (TextView) view.findViewById(R.id.max_temp_text);
        weatherIcon = (ImageView) view.findViewById(R.id.weather_image);

        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery forecast = new ForecastQuery();
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
        forecast.execute(urlString);

        return view;
    }

    /**
     * Get the Bitmap image from the given URL.
     *
     * @param   url     URL
     * @return  Bitmap
     */
    protected static Bitmap getImage(URL url) {
        HttpURLConnection iconConn = null;
        try {
            iconConn = (HttpURLConnection) url.openConnection();
            iconConn.connect();
            int response = iconConn.getResponseCode();
            if (response == 200) {
                return BitmapFactory.decodeStream(iconConn.getInputStream());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (iconConn != null) {
                iconConn.disconnect();
            }
        }
    }

    /**
     * Check if a file already exists on the device.
     *
     * @param   fileName    String
     * @return  boolean
     */
    public boolean fileExistence(String fileName) {
        File file = getActivity().getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }

    /**
     * ForecastQuery is an inner class that extends ASyncTask
     * to load the weather information by parsing the XML from a given URL.
     */
    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        String current;
        String min;
        String max;
        String iconName;
        Bitmap icon;

        /**
         * Connect to the URL in the background to allow the activity to
         * continue running while the weather information is retrieved.
         * Parse through the XML to get the necessary data.
         * Publish the progress of the task in the progressbar as tasks
         * are completed.
         *
         * @param   strings   String
         * @return  String
         */
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (parser.getName().equals("temperature")) {
                        current = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        min = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        max = parser.getAttributeValue(null, "max");
                        publishProgress(75);
                    }
                    if (parser.getName().equals("weather")) {
                        iconName = parser.getAttributeValue(null, "icon");
                        String iconFile = iconName+".png";
                        if (fileExistence(iconFile)) {
                            FileInputStream inputStream = null;
                            try {
                                inputStream = new FileInputStream(getActivity().getBaseContext().getFileStreamPath(iconFile));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            icon = BitmapFactory.decodeStream(inputStream);
                        } else {
                            URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                            icon = getImage(iconUrl);
                            FileOutputStream outputStream = context.openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        }
                        publishProgress(100);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Update the progress bar to a given value.
         *
         * @param   value   Integer
         */
        @Override
        public void onProgressUpdate(Integer... value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        /**
         * When the task has finished executing add the data retrieved for the icon,
         * current temperature, minimum temperature, and maximum temperature
         * to the appropriate location.
         * Set the progress bar to invisible.
         *
         * @param   result    String
         */
        @Override
        protected void onPostExecute(String result) {
            String degree = Character.toString((char) 0x00B0);
            currentTempText.setText(currentTempText.getText()+" "+current+degree+"C");
            minTempText.setText(minTempText.getText()+" "+min+degree+"C");
            maxTempText.setText(maxTempText.getText()+" "+max+degree+"C");
            weatherIcon.setImageBitmap(icon);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * The container in which this fragment appears.
     */
    private Context context;

    /**
     * The progress bar that loads while weather information is retrieved.
     */
    private ProgressBar progressBar;

    /**
     * The TextView to display the current temperature.
     */
    private TextView currentTempText;

    /**
     * The TextView to display the minimum temperature
     */
    private TextView minTempText;

    /**
     * The TextView to display the maximum temperature
     */
    private TextView maxTempText;

    /**
     * The ImageView to hold the weather icon.
     */
    private ImageView weatherIcon;
}