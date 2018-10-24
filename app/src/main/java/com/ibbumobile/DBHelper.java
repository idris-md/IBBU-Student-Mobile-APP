package com.ibbumobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ibbumobile.model.Student;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ibbumobile.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //////////////////// SQL Query to create Students table
        final String SQL_CREATE_STUDENTS_TABLE = "CREATE TABLE " + DataContract.StudentsEntry.TABLE_NAME + "("
                + DataContract.StudentsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataContract.StudentsEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + DataContract.StudentsEntry.COLUMN_SURNAME + " TEXT NOT NULL, "
                + DataContract.StudentsEntry.COLUMN_GENDER + " TEXT NOT NULL ,"
                + DataContract.StudentsEntry.COLUMN_MATRIC + " TEXT NOT NULL, "
                + DataContract.StudentsEntry.COLUMN_LEVEL + " TEXT NOT NULL, "
                + DataContract.StudentsEntry.COLUMN_EMAIL + " TEXT NOT NULL "
                + ");";

        //////////////////// SQL Query to create Staffs table
        final String SQL_CREATE_STAFFS_TABLE = "CREATE TABLE " + DataContract.StaffsEntry.TABLE_NAME + "("
                + DataContract.StaffsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataContract.StaffsEntry.COLUMN_STAFF_ID + " TEXT NOT NULL, "
                + DataContract.StaffsEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + DataContract.StaffsEntry.COLUMN_SURNAME + " TEXT NOT NULL, "
                + DataContract.StaffsEntry.COLUMN_GENDER + " TEXT NOT NULL, "
                + DataContract.StaffsEntry.COLUMN_DEPARTMENT + " TEXT NOT NULL, "
                + DataContract.StaffsEntry.COLUMN_EMAIL + " TEXT NOT NULL, "
                + DataContract.StaffsEntry.COLUMN_STAFF_TYPE + " TEXT NOT NULL "
                + ");";

        //////////////////// SQL Query to create User table
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + DataContract.UserEntry.TABLE_NAME + "("
                + DataContract.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataContract.UserEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + DataContract.UserEntry.COLUMN_SURNAME + " TEXT NOT NULL, "
                + DataContract.UserEntry.COLUMN_OTHERNAME + " TEXT, "
                + DataContract.UserEntry.COLUMN_GENDER + " TEXT NOT NULL ,"
                + DataContract.UserEntry.COLUMN_MATRIC + " TEXT NOT NULL, "
                + DataContract.UserEntry.COLUMN_DEPARTMENT + " TEXT NOT NULL, "
                + DataContract.UserEntry.COLUMN_LEVEL + " TEXT NOT NULL, "
                + DataContract.UserEntry.COLUMN_EMAIL + " TEXT NOT NULL "
                + ");";

        //////////////////// SQL Query to create News table
        final String SQL_CREATE_NEWS_TABLE = "CREATE TABLE " + DataContract.NewsEntry.TABLE_NAME + "("
                + DataContract.NewsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataContract.NewsEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + DataContract.NewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, "
                + DataContract.NewsEntry.COLUMN_PHOTO + " TEXT, "
                + DataContract.NewsEntry.COLUMN_DATE + " TEXT NOT NULL "
                + ");";

        //////////////////// SQL Query to create Notice table
        final String SQL_CREATE_NOTICE_TABLE = "CREATE TABLE " + DataContract.NoticeEntry.TABLE_NAME + "("
                + DataContract.NoticeEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataContract.NoticeEntry.COLUMN_CONTENT + " TEXT NOT NULL, "
                + DataContract.NoticeEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
                + DataContract.NoticeEntry.COLUMN_DATE + " TEXT NOT NULL "
                + ");";

        //////////////////// SQL Query to create Events table
        final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + DataContract.EventEntry.TABLE_NAME + "("
                + DataContract.EventEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataContract.EventEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + DataContract.EventEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + DataContract.EventEntry.COLUMN_VENUE + " TEXT NOT NULL, "
                + DataContract.EventEntry.COLUMN_START_DATE + " TEXT NOT NULL, "
                + DataContract.EventEntry.COLUMN_CLOSE_DATE + " TEXT NOT NULL "
                + ");";

        //////////////////// SQL Query to create location table
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + DataContract.LocationEntry.TABLE_NAME + " (" +
                DataContract.LocationEntry._ID + " INTEGER PRIMARY KEY," +
                DataContract.LocationEntry.COLUMN_LOCATION_SETTING + " TEXT UNIQUE NOT NULL, " +
                DataContract.LocationEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                DataContract.LocationEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                DataContract.LocationEntry.COLUMN_COORD_LONG + " REAL NOT NULL " +
                " );";

        //////////////////// SQL Query to create timetable table
        final String SQL_CREATE_TIMETABLE_TABLE = "CREATE TABLE " + DataContract.TimeTableEntry.TABLE_NAME + "("
                + DataContract.TimeTableEntry.COLUMN_ID + " INTEGER PRIMARY KEY,"
                + DataContract.TimeTableEntry.COLUMN_C_CODE + " TEXT NOT NULL,"
                + DataContract.TimeTableEntry.COLUMN_ROOM + " TEXT NOT NULL,"
                + DataContract.TimeTableEntry.COLUMN_TEACHER + " TEXT,"
                + DataContract.TimeTableEntry.COLUMN_DAY + " TEXT NOT NULL,"
                + DataContract.TimeTableEntry.COLUMN_START_TIME + " INTEGER NOT NULL,"
                + DataContract.TimeTableEntry.COLUMN_PERIOD + " INTEGER NOT NULL);";

        //////////////////// SQL Query to create weather table
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + DataContract.WeatherEntry.TABLE_NAME + "("

                + DataContract.WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"

                + DataContract.WeatherEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, "
                + DataContract.WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL,"
                + DataContract.WeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL,"
                + DataContract.WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL,"

                + DataContract.WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL,"
                + DataContract.WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL,"

                + DataContract.WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL,"
                + DataContract.WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL,"
                + DataContract.WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL,"
                + DataContract.WeatherEntry.COLUMN_DEGREES + " REAL NOT NULL,"

                + "UNIQUE (" + DataContract.WeatherEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TIMETABLE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EVENT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTICE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STAFFS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STUDENTS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.TimeTableEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.WeatherEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.UserEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public void insertUser(Student student) {

        ContentValues values = new ContentValues();

        values.put(DataContract.UserEntry.COLUMN_FIRST_NAME, student.getFirstName());
        values.put(DataContract.UserEntry.COLUMN_SURNAME, student.getLastName());
        values.put(DataContract.UserEntry.COLUMN_OTHERNAME, student.getOtherName());
        values.put(DataContract.UserEntry.COLUMN_MATRIC, student.getMatNum());
        values.put(DataContract.UserEntry.COLUMN_LEVEL, student.getLevel());
        values.put(DataContract.UserEntry.COLUMN_DEPARTMENT, student.getDept());
        values.put(DataContract.UserEntry.COLUMN_EMAIL, student.getEmail());
        values.put(DataContract.UserEntry.COLUMN_GENDER, student.getGender());
        values.put(DataContract.UserEntry.COLUMN_EMAIL, student.getEmail());

        SQLiteDatabase database = getWritableDatabase();
        database.insert(DataContract.UserEntry.TABLE_NAME, null, values);
        database.close();
    }

    public void removeUser(String mat) {
        SQLiteDatabase database = getWritableDatabase();

        database.execSQL("DELETE FROM " + DataContract.UserEntry.TABLE_NAME + " WHERE " + DataContract.UserEntry.COLUMN_MATRIC + " =\"" + mat + "\";");
        database.close();
    }

    public Student getUser() {

        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DataContract.UserEntry.TABLE_NAME + " WHERE 1", null);
        cursor.moveToFirst();

        Student user = new Student(cursor.getString(cursor.getColumnIndex(DataContract.UserEntry.COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(DataContract.UserEntry.COLUMN_SURNAME)),
                cursor.getString(cursor.getColumnIndex(DataContract.UserEntry.COLUMN_OTHERNAME)),
                cursor.getString(cursor.getColumnIndex(DataContract.UserEntry.COLUMN_MATRIC)),
                cursor.getString(cursor.getColumnIndex(DataContract.UserEntry.COLUMN_DEPARTMENT)),
                cursor.getString(cursor.getColumnIndex(DataContract.UserEntry.COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(DataContract.UserEntry.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(DataContract.UserEntry.COLUMN_LEVEL))
        );

        database.close();

        return user;
    }

}
