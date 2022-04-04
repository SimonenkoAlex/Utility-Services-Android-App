package com.example.hcspaymentapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BootLoad {
    public void onReceive(Context context, Intent intent) {
        // Получаем нашу БД для чтения
        SQLiteDatabase db = new ConnectionDatabase(context).getReadableDatabase();
        // Через запрос получаем все уведомления
        Cursor c = db.query("reminders", null, null, null, null, null, null);
        // Создаём объект AlarmManager, который по истечению указанного времени вызовет уведомление
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String id = c.getString(c.getColumnIndex("reminder_id"));
                    String title = c.getString(c.getColumnIndex("reminder_title"));
                    String text = c.getString(c.getColumnIndex("reminder_text"));
                    // Создаём BroadcastReceiver, который сработает при истечении времени
                    // (в данном случае это будет уведомление), и заносим в него данные (id, title и text)
                    Intent i = new Intent(context, ReminderNotification.class);
                    i.setAction(id);
                    i.putExtra("title", title);
                    i.putExtra("text", text);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
                    // Создаем наше напоминание, которое сработает независимо от того, находится ли
                    // телефон в спящем режиме или нет, в указанную дату и что должно будет вызвано по истечению времени
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getLong(c.getColumnIndex("reminder_date")), pendingIntent);
                } while (c.moveToNext());
            }
        }
    }
}
