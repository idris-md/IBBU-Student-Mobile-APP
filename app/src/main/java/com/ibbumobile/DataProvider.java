package com.ibbumobile;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DataProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mOpenHelper;

    static final int WEATHER = 100;
    static final int WEATHER_WITH_LOCATION = 101;
    static final int WEATHER_WITH_LOCATION_AND_DATE = 102;

    static final int LOCATION = 300;
    static final int TIMETABLE = 400;
    static final int NEWS = 500;
    static final int EVENT = 600;
    static final int NOTICE = 700;
    static final int STUDENTS = 800;
    static final int STAFFS = 900;
    static final int USER = 1000;


    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

    static {
        sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sWeatherByLocationSettingQueryBuilder.setTables(
                DataContract.WeatherEntry.TABLE_NAME + " INNER JOIN " +
                        DataContract.LocationEntry.TABLE_NAME +
                        " ON " + DataContract.WeatherEntry.TABLE_NAME +
                        "." + DataContract.WeatherEntry.COLUMN_LOC_KEY +
                        " = " + DataContract.LocationEntry.TABLE_NAME +
                        "." + DataContract.LocationEntry._ID);
    }

    //location.location_setting = ?
    private static final String sLocationSettingSelection =
            DataContract.LocationEntry.TABLE_NAME +
                    "." + DataContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? ";

    //location.location_setting = ? AND date >= ?
    private static final String sLocationSettingWithStartDateSelection =
            DataContract.LocationEntry.TABLE_NAME +
                    "." + DataContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    DataContract.WeatherEntry.COLUMN_DATE + " >= ? ";

    //location.location_setting = ? AND date = ?
    private static final String sLocationSettingAndDaySelection =
            DataContract.LocationEntry.TABLE_NAME +
                    "." + DataContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    DataContract.WeatherEntry.COLUMN_DATE + " = ? ";

    private Cursor getWeatherByLocationSetting(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = DataContract.WeatherEntry.getLocationSettingFromUri(uri);
        long startDate = DataContract.WeatherEntry.getStartDateFromUri(uri);

        String[] selectionArgs;
        String selection;

        if (startDate == 0) {
            selection = sLocationSettingSelection;
            selectionArgs = new String[]{locationSetting};
        } else {
            selectionArgs = new String[]{locationSetting, Long.toString(startDate)};
            selection = sLocationSettingWithStartDateSelection;
        }

        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getWeatherByLocationSettingAndDate(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = DataContract.WeatherEntry.getLocationSettingFromUri(uri);
        long date = DataContract.WeatherEntry.getDateFromUri(uri);

        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelection,
                new String[]{locationSetting, Long.toString(date)},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, DataContract.PATH_WEATHER, WEATHER);
        matcher.addURI(authority, DataContract.PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
        matcher.addURI(authority, DataContract.PATH_WEATHER + "/*/#", WEATHER_WITH_LOCATION_AND_DATE);

        matcher.addURI(authority, DataContract.PATH_LOCATION, LOCATION);
        matcher.addURI(authority, DataContract.PATH_TIMETABLE, TIMETABLE);
        matcher.addURI(authority, DataContract.PATH_EVENTS, EVENT);
        matcher.addURI(authority, DataContract.PATH_NEWS, NEWS);
        matcher.addURI(authority, DataContract.PATH_NOTICE, NOTICE);
        matcher.addURI(authority, DataContract.PATH_STAFFS, STAFFS);
        matcher.addURI(authority, DataContract.PATH_STUDENT, STUDENTS);
        matcher.addURI(authority, DataContract.PATH_USER, USER);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case WEATHER_WITH_LOCATION_AND_DATE:
                return DataContract.WeatherEntry.CONTENT_ITEM_TYPE;
            case WEATHER_WITH_LOCATION:
                return DataContract.WeatherEntry.CONTENT_TYPE;
            case WEATHER:
                return DataContract.WeatherEntry.CONTENT_TYPE;
            case LOCATION:
                return DataContract.LocationEntry.CONTENT_TYPE;
            case TIMETABLE:
                return DataContract.TimeTableEntry.CONTENT_TYPE;
            case NEWS:
                return DataContract.NewsEntry.CONTENT_TYPE;
            case NOTICE:
                return DataContract.NoticeEntry.CONTENT_TYPE;
            case EVENT:
                return DataContract.EventEntry.CONTENT_TYPE;
            case STAFFS:
                return DataContract.StaffsEntry.CONTENT_TYPE;
            case STUDENTS:
                return DataContract.StudentsEntry.CONTENT_TYPE;
            case USER:
                return DataContract.UserEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather with location and setting/*/*"
            case WEATHER_WITH_LOCATION_AND_DATE: {
                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
                break;
            }
            // "weather with location/*"
            case WEATHER_WITH_LOCATION: {
                retCursor = getWeatherByLocationSetting(uri, projection, sortOrder);
                break;
            }
            // "weather"
            case WEATHER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.WeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "location"
            case LOCATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            }

            // "timetable"
            case TIMETABLE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.TimeTableEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "timetable"
            case NOTICE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.NoticeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "timetable"
            case NEWS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.NewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            } // "timetable"
            case EVENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.EventEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case STAFFS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.StaffsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case STUDENTS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.StudentsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case USER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case WEATHER: {
                normalizeDate(values);
                long _id = db.insert(DataContract.WeatherEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.WeatherEntry.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case LOCATION: {
                long _id = db.insert(DataContract.LocationEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.LocationEntry.buildLocationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case TIMETABLE: {
                long _id = db.insert(DataContract.TimeTableEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.TimeTableEntry.buildTimeTableUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case NEWS: {
                long _id = db.insert(DataContract.NewsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.NewsEntry.buildNewsEntryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case NOTICE: {
                long _id = db.insert(DataContract.NoticeEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.NoticeEntry.buildNoticeEntryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case EVENT: {
                long _id = db.insert(DataContract.EventEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.EventEntry.buildNoticeEntryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case STAFFS: {
                long _id = db.insert(DataContract.StaffsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.StaffsEntry.buildNoticeEntryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case STUDENTS: {
                long _id = db.insert(DataContract.StudentsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.StudentsEntry.buildNoticeEntryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case USER: {
                long _id = db.insert(DataContract.UserEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = DataContract.EventEntry.buildNoticeEntryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case WEATHER:
                rowsDeleted = db.delete(
                        DataContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LOCATION:
                rowsDeleted = db.delete(
                        DataContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TIMETABLE:
                rowsDeleted = db.delete(
                        DataContract.TimeTableEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case NEWS:
                rowsDeleted = db.delete(
                        DataContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTICE:
                rowsDeleted = db.delete(
                        DataContract.NoticeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EVENT:
                rowsDeleted = db.delete(
                        DataContract.EventEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STAFFS:
                rowsDeleted = db.delete(
                        DataContract.StaffsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STUDENTS:
                rowsDeleted = db.delete(
                        DataContract.StudentsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                rowsDeleted = db.delete(
                        DataContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(DataContract.WeatherEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(DataContract.WeatherEntry.COLUMN_DATE);
            values.put(DataContract.WeatherEntry.COLUMN_DATE, DataContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case WEATHER:
                normalizeDate(values);
                rowsUpdated = db.update(DataContract.WeatherEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case LOCATION:
                rowsUpdated = db.update(DataContract.LocationEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TIMETABLE:
                rowsUpdated = db.update(DataContract.TimeTableEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case NEWS:
                rowsUpdated = db.update(DataContract.NewsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case NOTICE:
                rowsUpdated = db.update(DataContract.NoticeEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case EVENT:
                rowsUpdated = db.update(DataContract.EventEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case STAFFS:
                rowsUpdated = db.update(DataContract.StaffsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case STUDENTS:
                rowsUpdated = db.update(DataContract.StudentsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case USER:
                rowsUpdated = db.update(DataContract.UserEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {

            ////////////////// weather
            case WEATHER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long _id = db.insert(DataContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;


            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
