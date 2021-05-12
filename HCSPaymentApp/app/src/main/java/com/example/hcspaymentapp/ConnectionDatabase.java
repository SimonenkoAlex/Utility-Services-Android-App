package com.example.hcspaymentapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConnectionDatabase extends SQLiteOpenHelper {
    public ConnectionDatabase(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String LOG_TAG = "myLogs";
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("CREATE TABLE tariffs ("
                        + "tariff_id integer primary key autoincrement,"
                        + "tariff_name text,"
                        + "tariff_tariff real" + ");"
                + "CREATE TABLE info_user ("
                        + "info_id integer primary key autoincrement,"
                        + "info_full_name text,"
                        + "info_address text,"
                        + "info_home_phone text,"
                        + "info_mobile_phone text);"
                + "CREATE TABLE history ("
                        + "history_id integer primary key autoincrement,"
                        + "history_name text,"
                        + "history_date text,"
                history_previous_value integer,
                history_present_value integer,
                history_tariff real,
                history_total real,
                history_info_user text);
        CREATE TABLE reminders (
                reminder_id integer primary key autoincrement,
                reminder_title text,
                reminder_text text,
                reminder_date text);
        CREATE TABLE selected_user (
                id integer primary key autoincrement,
                user_id integer);
);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
