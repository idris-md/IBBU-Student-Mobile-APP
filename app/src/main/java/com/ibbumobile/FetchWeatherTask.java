package com.ibbumobile;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;


public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
    HttpURLConnection urlConnection;
    BufferedReader bufferedReader;
    InputStream inputStream;
    ArrayAdapter<String> mForecastAdapter;

    String forecastString;
    final String APIKEY = "ca5dcb6f59d460ad34edd46aeea7e985";


    @Override
    protected String[] doInBackground(String... params) {
        StringBuffer buffer = null;
        String postal = "lapai";
        String format = "json";
        String units = "metric";
        int numDays = 7;


        try {
            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";

            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID_PARAM = "APPID";


            Uri builtURi = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, postal)
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                    .appendQueryParameter(APPID_PARAM, APIKEY)
                    .build();

            URL url = new URL(builtURi.toString());
            Log.e("URI", url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();


            inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                forecastString = null;
            }

            forecastString = buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


        try {
            return getWeatherDataFromJSON(forecastString, numDays);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] getWeatherDataFromJSON(String forecastString, int numDays) throws JSONException {

        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJSON = new JSONObject(forecastString);
        JSONArray weatherArra = forecastJSON.getJSONArray(OWM_LIST);

        Time dayTime = new Time();

        dayTime.setToNow();

        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        dayTime = new Time();

        String[] resultStr = new String[numDays];
        for (int i = 0; i < weatherArra.length(); i++) {

            String day, description, highAndLow;
            JSONObject dayForecast = weatherArra.getJSONObject(i);

            long dateTime;
            dateTime = dayTime.setJulianDay(julianStartDay + i);
            day = getReadableDateString(dateTime);

            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            JSONObject tempObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = tempObject.getDouble(OWM_MAX);
            double low = tempObject.getDouble(OWM_MIN);

            highAndLow = formatHighLow(high, low);
            resultStr[i] = day + " - " + description + " - " + highAndLow;


        }
        for (String s : resultStr) {
            Log.e("IBBU Mobile", "Forecast entry " + s);
        }
        return resultStr;
    }

    private String formatHighLow(double high, double low) {

        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    private String getReadableDateString(long dateTime) {

        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EE MM DD");
        return shortenedDateFormat.format(dateTime);
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);

//        Gson gson = new Gson();
////        Type type = new TypeToken<OpenWeatherMap>() {
//        }.getType();
////        OpenWeatherMap openWeatherMap = gson.fromJson(s, type);

//        if (result != null) {
////            mForecastAdapter.clear();
//  //          mForecastAdapter.addAll(result);
//        }
    }

}
