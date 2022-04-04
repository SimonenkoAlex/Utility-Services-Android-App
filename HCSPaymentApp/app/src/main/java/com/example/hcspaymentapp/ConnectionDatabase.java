package com.example.hcspaymentapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ConnectionDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "housingSector";
    private static final int VERSION = 1;

    public interface Listener {

        void onDataChanged();

    }

    private static ConnectionDatabase sInstance;

    public static ConnectionDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ConnectionDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    private final Set<Listener> mListeners = new HashSet<>();

    public ConnectionDatabase(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String LOG_TAG = "myLogs";
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("CREATE TABLE tariffs ("
                + "tariff_id integer primary key autoincrement,"
                + "tariff_name text,"
                + "tariff_tariff real DEFAULT 0.00" + ");" );
        db.execSQL("CREATE TABLE renter ("
                + "renter_id integer primary key autoincrement,"
                + "last_name text,"
                + "first_name text,"
                + "patronymic text,"
                + "phone text,"
                + "email text,"
                + "passport text" + ");" );
        db.execSQL("CREATE TABLE history ("
                + "history_id integer primary key autoincrement,"
                + "history_name text,"
                + "history_date text,"
                + "history_previous_value integer,"
                + "history_present_value integer,"
                + "history_tariff real,"
                + "history_total real,"
                + "history_info_user text" + ");" );
        db.execSQL("CREATE TABLE reminders ("
                + "reminder_id integer primary key autoincrement,"
                + "reminder_title text,"
                + "reminder_text text,"
                + "reminder_date text" + ");" );
        db.execSQL("CREATE TABLE selected_user ("
                + "id integer primary key autoincrement,"
                + "user_id integer" + ");" );
        db.execSQL("CREATE TABLE counters ("
                + "id integer primary key autoincrement,"
                + "view_number text NOT NULL,"
                + "made_in text NOT NULL,"
                + "when_made text NOT NULL,"
                + "number text NOT NULL,"
                + "date_view text NOT NULL,"
                + "result integer NOT NULL,"
                + "pay_month integer NOT NULL,"
                + "color integer NOT NULL,"+ ");" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void addCounterInner(SQLiteDatabase db, String view_number, String made_in, String when_made,
                                 String number, String date_view, int result, int pay_month, int color) {
        ContentValues cv = new ContentValues();
        cv.put("view_number", view_number);
        cv.put("made_in", made_in);
        cv.put("when_made", when_made);
        cv.put("number", number);
        cv.put("date_view", date_view);
        cv.put("result", result);
        cv.put("pay_month", pay_month);
        cv.put("color", color);
        db.insert("counters", null, cv);
    }

    public void deleteCounter(long id) {
        getWritableDatabase().delete("counters", "id = " + id, null);
        notifyChanged();
    }

    public void changeTariff(long tariffId, String tariff) {
        ContentValues cv = new ContentValues();
        cv.put("tariff_tariff", tariff);
        updateTariff(tariffId, cv);
    }

    public void changeColor(long counterId, int color) {
        ContentValues cv = new ContentValues();
        cv.put("color", color);
        updateCounter(counterId, cv);
    }

    private void updateTariff(long tariffId, ContentValues cv) {
        getWritableDatabase().update("tariffs", cv,  "tariff_id = " + tariffId, null);
        notifyChanged();
    }

    private void updateCounter(long counterId, ContentValues cv) {
        getWritableDatabase().update("counters", cv,  "id = " + counterId, null);
        notifyChanged();
    }

    public List<Counter> getCounters() {
        String[] cols = {"id", "view_number", "made_in", "when_made", "number", "date_view", "result", "pay_month", "color"};
        Cursor c = getReadableDatabase().query("counters", cols, null, null,
                null, null,   "id DESC");
        List<Counter> list = new ArrayList<>(c.getColumnCount());
        while (c.moveToNext()) {
            long id = c.getLong(0);
            int result = c.getInt(6);
            String view_number = c.getString(1);
            String made_in = c.getString(2);
            String when_made = c.getString(3);
            String number = c.getString(4);
            String date_view = c.getString(5);
            int pay_month = c.getInt(7);
            int color = c.getInt(8);
            list.add(new Counter(id, view_number, made_in, when_made,
                    number, date_view, result, pay_month, color));
        }
        c.close();
        return list;
    }

    @Nullable
    public Counter getCounter(long id) {
        String[] cols = {"id", "view_number", "made_in", "when_made", "number", "date_view", "result", "pay_month", "color"};
        try (Cursor c = getReadableDatabase().query("counters", cols,  "id = " + id, null,
                null, null, null)) {
            if (c.moveToFirst()) {
                return new Counter(c.getLong(0), c.getString(1),
                        c.getString(2), c.getString(3), c.getString(4),
                        c.getString(5), c.getInt(6),
                        c.getInt(7), c.getInt(8));
            }
            return null;
        }
    }

    public void setValue(Counter counter, int result) {
        ContentValues cv = new ContentValues();
        cv.put("result", result);
        getWritableDatabase().update("counters", cv,  "id = " + counter.id, null);
        notifyChanged();
    }

    @Nullable
    public Tariff getTariff(long id) {
        String[] cols = {"tariff_id", "tariff_name", "tariff_tariff"};
        try (Cursor c = getReadableDatabase().query("tariffs", cols,  "tariff_id = " + id, null,
                null, null, null)) {
            if (c.moveToFirst()) {
                return new Tariff(c.getLong(0), c.getString(1), c.getDouble(2));
            }
            return null;
        }
    }

    private void notifyChanged() {
        for (Listener listener : mListeners) listener.onDataChanged();
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }
}
