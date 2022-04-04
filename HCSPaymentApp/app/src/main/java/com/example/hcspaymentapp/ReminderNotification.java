package com.example.hcspaymentapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class ReminderNotification {
    // Создаём переменную типа NotificationManager
    NotificationManager manager;

    public void onReceive(Context context, Intent intent) {
        // Получаем id напоминания
        String id = intent.getAction();
        // Проверяем является ли полученный id числом
        if (TextUtils.isDigitsOnly(id)) {
            // Извлекаем заголовок напоминания и текст
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            // Создаём объект NotificationManager
            manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            // Указываем какое Activity будет открываться при нажатии на уведомление
            Intent i = new Intent(context, PersonalAreaActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);
            // Строим уведомление
            Notification notification = new Notification.Builder(context)
                    .setContentTitle(title)// Заголовок
                    .setContentText(text)// Текст
                    .setSmallIcon(android.R.mipmap.sym_def_app_icon) // Иконка
                    .setContentIntent(pIntent) // Activity при нажатии
                    .setPriority(Notification.PRIORITY_DEFAULT) // Приоритет уведомления по умолчанию
                    // Все настройки уведомления по умолчанию (вибрация, звук и т.д.)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .build(); // Строим уведомление
            // Указываем, что при нажатии на уведомление оно должно удалиться из строки состояния
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            // Вызываем уведомление
            manager.notify(Integer.parseInt(id), notification);
        }
    }
}
