package com.ibbumobile;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

public class DataContract implements BaseColumns {


    public static final String CONTENT_AUTHORITY = "com.ibbumobile";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WEATHER = "weather";
    public static final String PATH_TIMETABLE = "timetable";
    public static final String PATH_LOCATION = "location";
    public static final String PATH_NEWS = "news";
    public static final String PATH_NOTICE = "notice";
    public static final String PATH_EVENTS = "event";
    public static final String PATH_STAFFS = "staff";
    public static final String PATH_STUDENT = "student";
    public static final String PATH_USER = "user";


    public static long normalizeDate(long startDate) {

        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class StaffsEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STAFFS).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STAFFS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STAFFS;

        public static final String TABLE_NAME = "staffs";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_FIRST_NAME = "firstname";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_DEPARTMENT = "department";
        public static final String COLUMN_STAFF_ID = "staffid";
        public static final String COLUMN_STAFF_TYPE = "stafftype";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_GENDER = "gender";

        public static Uri buildNoticeEntryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class StudentsEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDENT).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STUDENT;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STUDENT;

        public static final String TABLE_NAME = "students";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_FIRST_NAME = "firstname";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_MATRIC = "matric";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_GENDER = "gender";
        //public static final String COLUMN_SURNAME = "surname";

        public static Uri buildNoticeEntryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class UserEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME = "user";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_FIRST_NAME = "name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_OTHERNAME = "othername";
        public static final String COLUMN_MATRIC = "matric";
        public static final String COLUMN_DEPARTMENT = "department";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_GENDER = "gender";

        public static Uri buildNoticeEntryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class EventEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENTS).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;

        public static final String TABLE_NAME = "event";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VENUE = "venue";
        public static final String COLUMN_START_DATE = "date";
        public static final String COLUMN_CLOSE_DATE = "cdate";

        public static Uri buildNoticeEntryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class NoticeEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTICE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTICE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTICE;

        public static final String TABLE_NAME = "notice";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";

        public static Uri buildNoticeEntryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class NewsEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;

        public static final String TABLE_NAME = "news";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_DATE = "date";

        public static Uri buildNewsEntryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TimeTableEntry {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TIMETABLE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TIMETABLE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TIMETABLE;

        public static final String TABLE_NAME = "timetable";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_C_CODE = "c_code";
        public static final String COLUMN_ROOM = "room";
        public static final String COLUMN_TEACHER = "teacher";

        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_PERIOD = "per";

        public static Uri buildTimeTableUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static final class LocationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        public static final String TABLE_NAME = "location";

        public static final String COLUMN_LOCATION_SETTING = "location_setting";
        public static final String COLUMN_CITY_NAME = "city_name";
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static final class WeatherEntry {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;

        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_LOC_KEY = "location_id";

        public static final String _ID = "_id";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_SHORT_DESC = "short_desc";
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_WIND_SPEED = "wind";
        public static final String COLUMN_DEGREES = "degrees";

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static Uri buildWeatherLocationWithStartDate(
                String locationSetting, long startDate) {
            long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendQueryParameter(COLUMN_DATE, Long.toString(normalizedDate)).build();
        }

        public static Uri buildWeatherLocationWithDate(String locationSetting, long date) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendPath(Long.toString(normalizeDate(date))).build();
        }

        public static String getLocationSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

        public static long getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DATE);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }

    }

}
