package com.ibbumobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ibbumobile.model.TimeTable;

import java.util.ArrayList;

/**
 * Created by Eidris on 3/15/2018.
 */

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void addCourse(TimeTable timeTable) {

        ContentValues values = new ContentValues();

        values.put("c_code",timeTable.getCourseCode() );
        values.put("room",timeTable.getRoom());
        values.put("teacher", timeTable.getTeacherName());
        values.put("day", timeTable.getDay());
        values.put("start_time",timeTable.getTime());
        values.put("per", timeTable.getPer());

        database.insert("TimeTable",null,values);

    }

    public ArrayList<TimeTable> getTimeTables(String day) {

        ArrayList<TimeTable> timeTables = new ArrayList<>();
        //"SELECT * FROM  TimeTable WHERE day1" + "=\"" + day + "\"" + " OR day2 " + "=\"" + day + "\"";
        String query = "SELECT * FROM  TimeTable WHERE day" + "=\"" + day + "\" ORDER by start_time ASC";
        Cursor cursor;
        cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                /////////////////////Format date object
         /* String s = cursor.getString(7);
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try {
                date= dateFormat.parse(s);
            }catch (ParseException e){
                e.printStackTrace();
            }*/////////////////Create student object

                timeTables.add(new TimeTable(

                        cursor.getString(cursor.getColumnIndex("c_code")),
                        cursor.getString(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("start_time")),
                        cursor.getInt(cursor.getColumnIndex("per")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("room")
                        )));
            }
        }
        return timeTables;
    }

}
