package com.ibbumobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
/**
 * Created by Eidris on 3/15/2018.
 */

public class DatabaseOpenHelper extends SQLiteAssetHelper {


    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "IBBUL Mobile.db";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }



}
