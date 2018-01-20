package com.example.android.dm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ADITYA on 1/20/2018.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "User.db";
    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE Users (userid INTEGER PRIMARY KEY autoincrement,name TEXT NOT NULL, email TEXT NOT NULL,password TEXT NOT NULL,age TEXT NOT NULL,gender TEXT NOT NULL,address TEXT NOT NULL,phonenumber TEXT NOT NULL,preference TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS Users";
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);


    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public boolean insertRecord(String name,String email,String password,String age,String gender,String address, String phone,String prefernces) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password",password);
        contentValues.put("age",age);
        contentValues.put("gender",gender);
        contentValues.put("address",address);
        contentValues.put("phonenumber", phone);
        contentValues.put("preference", prefernces);
        db.insert("Users", null, contentValues);
        return true;
    }
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users", null);
        return cursor;
    }
    void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from Users");
    }

}
