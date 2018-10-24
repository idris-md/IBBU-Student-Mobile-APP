package com.ibbumobile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.Time;
import android.util.Log;

import com.ibbumobile.common.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = SyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;

    private static final String OPEN_WEATHER_MAP_API_KEY = "ca5dcb6f59d460ad34edd46aeea7e985";

    private static final String[] NOTIFY_WEATHER_PROJECTION = new String[]{
            DataContract.WeatherEntry.COLUMN_WEATHER_ID,
            DataContract.WeatherEntry.COLUMN_MAX_TEMP,
            DataContract.WeatherEntry.COLUMN_MIN_TEMP,
            DataContract.WeatherEntry.COLUMN_SHORT_DESC
    };

    private static final String[] NOTIFY_NEWS_PROJECTION = new String[]{
            DataContract.WeatherEntry.COLUMN_WEATHER_ID,
            DataContract.WeatherEntry.COLUMN_MAX_TEMP,
            DataContract.WeatherEntry.COLUMN_MIN_TEMP,
            DataContract.WeatherEntry.COLUMN_SHORT_DESC
    };

    private static final String[] NOTIFY_NOTICE_PROJECTION = new String[]{
            DataContract.WeatherEntry.COLUMN_WEATHER_ID,
            DataContract.WeatherEntry.COLUMN_MAX_TEMP,
            DataContract.WeatherEntry.COLUMN_MIN_TEMP,
            DataContract.WeatherEntry.COLUMN_SHORT_DESC
    };

    // these indices must match the projection
    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_MAX_TEMP = 1;
    private static final int INDEX_MIN_TEMP = 2;
    private static final int INDEX_SHORT_DESC = 3;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        String locationQuery = "lapai";


        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;
        String newsJsonStr = null;
        String timetableJsonStr = null;
        String studentsJsonStr = null;
        String noticeJsonStr = null;
        String staffsJsonStr = null;

        String format = "json";
        String units = "metric";
        int numDays = 14;

        try {
            // Construct the URL for the web APIs query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String TIMETABLE_URL = "https://api.myjson.com/bins/1ckf5o";

            final String NEWS_URL = "https://api.myjson.com/bins/1f0qxw";
            final String NOTICE_URL = "https://api.myjson.com/bins/zrlng";
            final String STUDENT_URL = "https://api.myjson.com/bins/1dznak";
            final String STAFF_URL = "https://api.myjson.com/bins/1ecqqs";

            //////////////////// OWM PARAMS
            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID_PARAM = "APPID";

            //////////////////// TIMETABLE PARAMS

            Uri owmBuiltUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, locationQuery)
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                    .appendQueryParameter(APPID_PARAM, OPEN_WEATHER_MAP_API_KEY)
                    .build();

            URL urlOWM = new URL(owmBuiltUri.toString());
            URL urlTIMETABLE = new URL(TIMETABLE_URL);

            URL urlNEWS = new URL(NEWS_URL);
            URL urlNOTICE = new URL(NOTICE_URL);
            URL urlSTUDENT = new URL(STUDENT_URL);
            URL urlSTAFF = new URL(STAFF_URL);

            forecastJsonStr = getStringFromURL(urlOWM);

            newsJsonStr = getStringFromURL(urlNEWS);
            noticeJsonStr = getStringFromURL(urlNOTICE);
            timetableJsonStr = getStringFromURL(urlTIMETABLE);
            studentsJsonStr = getStringFromURL(urlSTUDENT);
            staffsJsonStr = getStringFromURL(urlSTAFF);

            getWeatherDataFromJson(forecastJsonStr, locationQuery);

            getStudentsDataFromJson(studentsJsonStr);
            getNewsDataFromJson(newsJsonStr);
            getNoticeDataFromJson(noticeJsonStr);
            getTimeTableDataFromJson(timetableJsonStr);
           /// getStaffsDataFromJson(staffsJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */

    private String getStringFromURL(URL url) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();

        try {
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }


        return buffer.toString();
    }

    private void getWeatherDataFromJson(String forecastJsonStr, String locationSetting) throws JSONException {

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        // Location information
        final String OWM_CITY = "city";
        final String OWM_CITY_NAME = "name";
        final String OWM_COORD = "coord";

        // Location coordinate
        final String OWM_LATITUDE = "lat";
        final String OWM_LONGITUDE = "lon";

        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "list";

        final String OWM_PRESSURE = "pressure";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_WINDSPEED = "speed";
        final String OWM_WIND_DIRECTION = "deg";

        // All temperatures are children of the "temp" object.
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";

        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";
        final String OWM_WEATHER_ID = "id";

        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
            String cityName = cityJson.getString(OWM_CITY_NAME);

            JSONObject cityCoord = cityJson.getJSONObject(OWM_COORD);
            double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
            double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);

            long locationId = addLocation(locationSetting, cityName, cityLatitude, cityLongitude);

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(weatherArray.length());

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            for (int i = 0; i < weatherArray.length(); i++) {
                // These are the values that will be collected.
                long dateTime;
                double pressure;
                int humidity;
                double windSpeed;
                double windDirection;

                double high;
                double low;

                String description;
                int weatherId;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay + i);

                pressure = dayForecast.getDouble(OWM_PRESSURE);
                humidity = dayForecast.getInt(OWM_HUMIDITY);
                windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
                windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

                // Description is in a child array called "weather", which is 1 element long.
                // That element also contains a weather code.
                JSONObject weatherObject =
                        dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);
                weatherId = weatherObject.getInt(OWM_WEATHER_ID);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                high = temperatureObject.getDouble(OWM_MAX);
                low = temperatureObject.getDouble(OWM_MIN);

                ContentValues weatherValues = new ContentValues();

                weatherValues.put(DataContract.WeatherEntry.COLUMN_LOC_KEY, locationId);
                weatherValues.put(DataContract.WeatherEntry.COLUMN_DATE, dateTime);
                weatherValues.put(DataContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
                weatherValues.put(DataContract.WeatherEntry.COLUMN_PRESSURE, pressure);
                weatherValues.put(DataContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
                weatherValues.put(DataContract.WeatherEntry.COLUMN_DEGREES, windDirection);
                weatherValues.put(DataContract.WeatherEntry.COLUMN_MAX_TEMP, high);
                weatherValues.put(DataContract.WeatherEntry.COLUMN_MIN_TEMP, low);
                weatherValues.put(DataContract.WeatherEntry.COLUMN_SHORT_DESC, description);
                weatherValues.put(DataContract.WeatherEntry.COLUMN_WEATHER_ID, weatherId);

                cVVector.add(weatherValues);
            }

            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(DataContract.WeatherEntry.CONTENT_URI, cvArray);

                // delete old data so we don't build up an endless history
                getContext().getContentResolver().delete(DataContract.WeatherEntry.CONTENT_URI,
                        DataContract.WeatherEntry.COLUMN_DATE + " <= ?",
                        new String[]{Long.toString(dayTime.setJulianDay(julianStartDay - 1))});

                notifyWeather();
            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getTimeTableDataFromJson(String timeTableJsonStr) throws JSONException {

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.
        final String TT_TIMETABLE = "timetable";

        final String TT_CODE = "course";
        final String TT_ROOM = "venue";
        final String TT_TEACHER = "staff";
        final String TT_DAY = "day";
        final String TT_STARTTIME = "time";
        final String TT_PERIOD = "per";

        final String TT_SESSION = "semester";
        final String TT_SEM = "semester";
        final String TT_SEM_EXM = "exam-date";
        final String TT_SEM_EXM_END = "exam-end";

        final String DAYS[] = {"mon", "tue", "wed", "thur", "fri"};

        try {

            JSONObject forecastJson = new JSONObject(timeTableJsonStr);
            JSONArray timeTableArray = forecastJson.getJSONArray(TT_TIMETABLE);

//          JSONObject sessionJson = forecastJson.getJSONObject(TT_SESSION);

            String semester = forecastJson.getString(TT_SEM);
            String semExam = forecastJson.getString(TT_SEM_EXM);
            String semExamEnd = forecastJson.getString(TT_SEM_EXM_END);
            String session = forecastJson.getString(TT_SESSION);

            //////////////////////////// Set up the session settings //////////////////////
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("semester", semester);
            editor.putString("examStart", semExam);
            editor.putString("examEnd", semExamEnd);
            editor.putString("session", session);

            editor.commit();

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<>();

            for (int i = 0; i < timeTableArray.length(); i++) {

                JSONObject dayObject = timeTableArray.getJSONObject(i);
                ContentValues timeTableValues = new ContentValues();

                JSONArray courseArray = dayObject.getJSONArray(DAYS[i]);
                int length = courseArray.length();

                for (int index = 0; index < length; index++) {

                    // These are the values that will be collected.
                    String day;
                    String code;
                    String room;
                    String teacher;
                    String starttime;
                    String period;

                    // Get the JSON object representing the day
                    JSONObject dayTimeTable = courseArray.getJSONObject(index);

                    day = DAYS[index];
                    room = dayTimeTable.getString(TT_ROOM);
                    code = dayTimeTable.getString(TT_CODE);
                    teacher = dayTimeTable.getString(TT_TEACHER);
                    starttime = dayTimeTable.getString(TT_STARTTIME);
                    period = "2";


                    timeTableValues.put(DataContract.TimeTableEntry.COLUMN_DAY, day);
                    timeTableValues.put(DataContract.TimeTableEntry.COLUMN_ROOM, room);
                    timeTableValues.put(DataContract.TimeTableEntry.COLUMN_C_CODE, code);
                    timeTableValues.put(DataContract.TimeTableEntry.COLUMN_TEACHER, teacher);
                    timeTableValues.put(DataContract.TimeTableEntry.COLUMN_START_TIME, starttime);
                    timeTableValues.put(DataContract.TimeTableEntry.COLUMN_PERIOD, period);

                    cVVector.add(timeTableValues);

                }
// add to database
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    getContext().getContentResolver().bulkInsert(DataContract.TimeTableEntry.CONTENT_URI, cvArray);

                    // delete old data so we don't build up an endless history
//                    getContext().getContentResolver().delete(DataContract.WeatherEntry.CONTENT_URI,
//                            DataContract.WeatherEntry.COLUMN_DATE + " <= ?",
//                            new String[]{Long.toString(dayTime.setJulianDay(julianStartDay - 1))});

                    // notifyWeather();

                }

            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getNewsDataFromJson(String newsJsonStr) throws JSONException {

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.
        final String NEWS_LIST = "list";

        final String NEWS_ID = "_id";
        final String NEWS_TITLE = "title";
        final String NEWS_CONTENT = "content";
        final String NEWS_PHOTO = "photo";
        final String NEWS_DATE = "date";

        try {

            JSONObject newstJson = new JSONObject(newsJsonStr);
            JSONArray newsArray = newstJson.getJSONArray(NEWS_LIST);

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(newsArray.length());

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            for (int i = 0; i < newsArray.length(); i++) {

                // These are the values that will be collected.

                int _id;
                String title;
                String content;
                String photo;
                String date;

                // Get the JSON object representing the day
                JSONObject newsObject = newsArray.getJSONObject(i);

                _id = newsObject.getInt(NEWS_ID);
                title = newsObject.getString(NEWS_TITLE);
                content = newsObject.getString(NEWS_CONTENT);
                photo = newsObject.getString(NEWS_PHOTO);
                date = newsObject.getString(NEWS_DATE);

                ContentValues newsValues = new ContentValues();

               // newsValues.put(DataContract.NewsEntry.COLUMN_ID, _id);
                newsValues.put(DataContract.NewsEntry.COLUMN_DATE, date);
                newsValues.put(DataContract.NewsEntry.COLUMN_CONTENT, content);
                newsValues.put(DataContract.NewsEntry.COLUMN_PHOTO, photo);
                newsValues.put(DataContract.NewsEntry.COLUMN_TITLE, title);

                cVVector.add(newsValues);
            }

            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {

                // delete old data so we don't build up an endless history
                getContext().getContentResolver().delete(DataContract.NewsEntry.CONTENT_URI,
                        null,
                        null);

                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(DataContract.NewsEntry.CONTENT_URI, cvArray);


//                notifyWeather();
            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getNoticeDataFromJson(String noticeJsonStr) throws JSONException {

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        final String NOTICE_LIST = "list";

        final String NOTICE_CONTENT = "content";
        final String NOTICE_AUTHOR = "author";
        final String NOTICE_DATE = "date";

        try {

            JSONObject noticeJson = new JSONObject(noticeJsonStr);
            JSONArray noticeArray = noticeJson.getJSONArray(NOTICE_LIST);

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(noticeArray.length());

            for (int i = 0; i < noticeArray.length(); i++) {

                // These are the values that will be collected.
                String content;
                String author;
                String date;

                // Get the JSON object representing the day
                JSONObject noticeObject = noticeArray.getJSONObject(i);

                // Cheating to convert this to UTC time, which is what we want anyhow
                content = noticeObject.getString(NOTICE_CONTENT);
                author = noticeObject.getString(NOTICE_AUTHOR);
                date = noticeObject.getString(NOTICE_DATE);

                ContentValues noticeValues = new ContentValues();

                noticeValues.put(DataContract.NoticeEntry.COLUMN_DATE, date);
                noticeValues.put(DataContract.NoticeEntry.COLUMN_AUTHOR, author);
                noticeValues.put(DataContract.NoticeEntry.COLUMN_CONTENT, content);

                cVVector.add(noticeValues);
            }

            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {

                // delete old data so we don't build up an endless history
                getContext().getContentResolver().delete(DataContract.NoticeEntry.CONTENT_URI,
                        null,
                        null);

                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(DataContract.NoticeEntry.CONTENT_URI, cvArray);

//                notifyWeather();
            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getStudentsDataFromJson(String eventJsonStr) throws JSONException {

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        // Location information
        final String STUDENTS_LIST = "students";

        final String STUDENT_FNAME = "first_name";
        final String STUDENT_SNAME = "surname";
        final String STUDENT_GENDER = "gender";
        final String STUDENT_EMAIL = "email";
        final String STUDENT_LEVEL = "level";
        final String STUDENT_MATRIC = "matric";

        try {

            JSONObject studentJson = new JSONObject(eventJsonStr);
            JSONArray studentArray = studentJson.getJSONArray(STUDENTS_LIST);

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(studentArray.length());

            for (int i = 0; i < studentArray.length(); i++) {
                // These are the values that will be collected.
                String firstName;
                String surname;
                String gender;
                String email;
                String level;
                String matric;

                // Get the JSON object representing the day
                JSONObject studentObject = studentArray.getJSONObject(i);

                firstName = studentObject.getString(STUDENT_FNAME);
                surname = studentObject.getString(STUDENT_SNAME);
                gender = studentObject.getString(STUDENT_GENDER);
                email = studentObject.getString(STUDENT_EMAIL);
                level = studentObject.getString(STUDENT_LEVEL);
                matric = studentObject.getString(STUDENT_MATRIC);

                ContentValues studentValues = new ContentValues();

                studentValues.put(DataContract.StudentsEntry.COLUMN_FIRST_NAME, firstName);
                studentValues.put(DataContract.StudentsEntry.COLUMN_SURNAME, surname);
                studentValues.put(DataContract.StudentsEntry.COLUMN_GENDER, gender);
                studentValues.put(DataContract.StudentsEntry.COLUMN_EMAIL, email);
                studentValues.put(DataContract.StudentsEntry.COLUMN_LEVEL, level);
                studentValues.put(DataContract.StudentsEntry.COLUMN_MATRIC, matric);

                cVVector.add(studentValues);
            }

            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(DataContract.StudentsEntry.CONTENT_URI, cvArray);

                // delete old data so we don't build up an endless history
//                getContext().getContentResolver().delete(DataContract.WeatherEntry.CONTENT_URI,
//                        DataContract.WeatherEntry.COLUMN_DATE + " <= ?",
//                        new String[]{Long.toString(dayTime.setJulianDay(julianStartDay - 1))});
//
//                notifyWeather();
            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getStaffsDataFromJson(String eventJsonStr) throws JSONException {

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        // Location information
        final String STAFFS_LIST = "staffs";

        final String STAFF_FNAME = "first_name";
        final String STAFF_SNAME = "surname";
        // final String STAFF_GENDER = "gender";
        final String STAFF_EMAIL = "email";
        final String STAFF_GLEVEL = "level";
        final String STAFF_NUMBER = "staffId";
        final String STAFF_DEPT = "department";

        try {

            JSONObject studentJson = new JSONObject(eventJsonStr);
            JSONArray studentArray = studentJson.getJSONArray(STAFFS_LIST);

            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(studentArray.length());

            for (int i = 0; i < studentArray.length(); i++) {
                // These are the values that will be collected.
                String firstName;
                String surname;
                String gender;
                String email;
                String level;
                String staffId;
                String dept;

                // Get the JSON object representing the day
                JSONObject studentObject = studentArray.getJSONObject(i);

                firstName = studentObject.getString(STAFF_FNAME);
                surname = studentObject.getString(STAFF_SNAME);
                //   gender = studentObject.getString(STAFF_GENDER);
                email = studentObject.getString(STAFF_EMAIL);
                level = studentObject.getString(STAFF_GLEVEL);
                staffId = studentObject.getString(STAFF_NUMBER);
                dept = studentObject.getString(STAFF_DEPT);

                ContentValues studentValues = new ContentValues();

                studentValues.put(DataContract.StaffsEntry.COLUMN_FIRST_NAME, firstName);
                studentValues.put(DataContract.StaffsEntry.COLUMN_SURNAME, surname);
                //    studentValues.put(DataContract.StaffsEntry.COLUMN_GENDER, gender);
                studentValues.put(DataContract.StaffsEntry.COLUMN_EMAIL, email);
                studentValues.put(DataContract.StaffsEntry.COLUMN_STAFF_ID, staffId);
                studentValues.put(DataContract.StaffsEntry.COLUMN_STAFF_TYPE, level);
                studentValues.put(DataContract.StaffsEntry.COLUMN_DEPARTMENT, dept);

                cVVector.add(studentValues);
            }

            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(DataContract.StudentsEntry.CONTENT_URI, cvArray);

                // delete old data so we don't build up an endless history
//                getContext().getContentResolver().delete(DataContract.WeatherEntry.CONTENT_URI,
//                        DataContract.WeatherEntry.COLUMN_DATE + " <= ?",
//                        new String[]{Long.toString(dayTime.setJulianDay(julianStartDay - 1))});
//
//                notifyWeather();
            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


    private void notifyWeather() {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        if (displayNotifications) {

            String lastNotificationKey = context.getString(R.string.pref_last_notification);
            long lastSync = prefs.getLong(lastNotificationKey, 0);

            if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
                // Last sync was more than 1 day ago, let's send a notification with the weather.
                String locationQuery = Utility.getPreferredLocation(context);

                Uri weatherUri = DataContract.WeatherEntry.buildWeatherLocationWithDate(locationQuery, System.currentTimeMillis());

                // we'll query our contentProvider, as always
                Cursor cursor = context.getContentResolver().query(weatherUri, NOTIFY_WEATHER_PROJECTION, null, null, null);

                if (cursor.moveToFirst()) {
                    int weatherId = cursor.getInt(INDEX_WEATHER_ID);
                    double high = cursor.getDouble(INDEX_MAX_TEMP);
                    double low = cursor.getDouble(INDEX_MIN_TEMP);
                    String desc = cursor.getString(INDEX_SHORT_DESC);

                    int iconId = Utility.getIconResourceForWeatherCondition(weatherId);
                    Resources resources = context.getResources();
                    Bitmap largeIcon = BitmapFactory.decodeResource(resources,
                            Utility.getArtResourceForWeatherCondition(weatherId));
                    String title = context.getString(R.string.app_name);

                    // Define the text of the forecast.
                    String contentText = String.format(context.getString(R.string.format_notification),
                            desc,
                            Utility.formatTemperature(context, high),
                            Utility.formatTemperature(context, low));

                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
                    // notifications.  Just throw in some data.
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setColor(resources.getColor(R.color.sunshine_light_blue))
                                    .setSmallIcon(iconId)
                                    .setLargeIcon(largeIcon)
                                    .setContentTitle(title)
                                    .setContentText(contentText);

                    // Make something interesting happen when the user clicks on the notification.
                    // In this case, opening the app is sufficient.
                    Intent resultIntent = new Intent(context, MainActivity.class);

                    // The stack builder object will contain an artificial back stack for the
                    // started Activity.
                    // This ensures that navigating backward from the Activity leads out of
                    // your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager =
                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
                    mNotificationManager.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());

                    //refreshing last sync
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(lastNotificationKey, System.currentTimeMillis());
                    editor.commit();
                }
                cursor.close();
            }
        }
    }

    /**
     * Helper method to handle insertion of a new location in the weather database.
     *
     * @param locationSetting The location string used to request updates from the server.
     * @param cityName        A human-readable city name, e.g "Mountain View"
     * @param lat             the latitude of the city
     * @param lon             the longitude of the city
     * @return the row ID of the added location.
     */
    long addLocation(String locationSetting, String cityName, double lat, double lon) {
        long locationId;

        // First, check if the location with this city name exists in the db
        Cursor locationCursor = getContext().getContentResolver().query(
                DataContract.LocationEntry.CONTENT_URI,
                new String[]{DataContract.LocationEntry._ID},
                DataContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{locationSetting},
                null);

        if (locationCursor.moveToFirst()) {
            int locationIdIndex = locationCursor.getColumnIndex(DataContract.LocationEntry._ID);
            locationId = locationCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues locationValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(DataContract.LocationEntry.COLUMN_CITY_NAME, cityName);
            locationValues.put(DataContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
            locationValues.put(DataContract.LocationEntry.COLUMN_COORD_LAT, lat);
            locationValues.put(DataContract.LocationEntry.COLUMN_COORD_LONG, lon);

            // Finally, insert location data into the database.
            Uri insertedUri = getContext().getContentResolver().insert(
                    DataContract.LocationEntry.CONTENT_URI,
                    locationValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            locationId = ContentUris.parseId(insertedUri);
        }

        locationCursor.close();
        // Wait, that worked?  Yes!
        return locationId;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

            /*
             * Add the account and account type, no password or user data
             * If successful, return the Account object, otherwise report an error.
             */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
