package com.example.hcspaymentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PersonalAreaActivity extends AppCompatActivity implements ConnectionDatabase.Listener{
    final String LOG_TAG = "myLogs";
    private CounterList mList;
    private boolean mIsListLayout = true;
    // объявляем переменные
    TextView lastnameText, firstnameText, patronymicText, phoneText, passportText;
    ConnectionDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_area);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // присваеваем значение id каждого элемента интерфейса переменным
        lastnameText = findViewById(R.id.lastnameText);
        firstnameText = findViewById(R.id.firstnameText);
        patronymicText = findViewById(R.id.patronymicText);
        phoneText = findViewById(R.id.phoneText);
        passportText = findViewById(R.id.passportText);
        // создаем объект для создания и управления версиями БД
        database = new ConnectionDatabase(this);
        auth = FirebaseAuth.getInstance();
        // подключаемся к БД
        SQLiteDatabase sqldb = database.getWritableDatabase();
        FirebaseUser curUser = auth.getCurrentUser();
        // переменные для query
        String selection = "email LIKE ?";
        String userName = curUser.getEmail();
        String[] selectionArgs = new String[] { userName };
        Log.d(LOG_TAG, "email = " + userName);
        // курсор
        Cursor c = sqldb.query("renter", null, selection, selectionArgs, null, null, null);
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int lastNameColIndex = c.getColumnIndex("last_name");
            int firstNameColIndex = c.getColumnIndex("first_name");
            int patronymicColIndex = c.getColumnIndex("patronymic");
            int phoneColIndex = c.getColumnIndex("phone");
            int passportColIndex = c.getColumnIndex("passport");

            do {
                //Log.d(LOG_TAG, "last_name = " + c.getString(lastNameColIndex) +
                //        ", first_name = " + c.getString(firstNameColIndex));
                // получаем значения по номерам столбцов
                lastnameText.setText(c.getString(lastNameColIndex));
                firstnameText.setText(c.getString(firstNameColIndex));
                patronymicText.setText(c.getString(patronymicColIndex));
                phoneText.setText(c.getString(phoneColIndex));
                passportText.setText(c.getString(passportColIndex));
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();

        mList = new CounterList(findViewById(R.id.list), new CounterList.Listener() {
            @Override
            public void onPlus(Counter counter) {
                ConnectionDatabase.getInstance(PersonalAreaActivity.this).setValue(counter, counter.result + 1);
            }

            @Override
            public void onMinus(Counter counter) {
                ConnectionDatabase.getInstance(PersonalAreaActivity.this).setValue(counter, counter.result - 1);
            }

            @Override
            public void onOpen(Counter counter) {
                startActivity(new Intent(PersonalAreaActivity.this, WaterMeterActivity.class)
                        .putExtra(WaterMeterActivity.EXTRA_ID, counter.id));
            }
        });
        if (savedInstanceState != null) {
            mIsListLayout = savedInstanceState.getBoolean("list_layout", true);
            mList.setLayout(mIsListLayout);
        }
        onDataChanged();
        ConnectionDatabase.getInstance(this).addListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("list_layout", mIsListLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectionDatabase.getInstance(this).removeListener(this);
    }

    @Override
    public void onDataChanged() {
        mList.setCounters(ConnectionDatabase.getInstance(this).getCounters());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.adding_readings: startActivity(new Intent(this, WaterMeterActivity.class)); break;
            //case R.id.action_reminder:  break;
            //case R.id.action_settings:  break;
            //case R.id.action_help:  break;
            //case R.id.action_about_app:  break;
            case R.id.m_main_layout: {
                mIsListLayout = !mIsListLayout;
                mList.setLayout(mIsListLayout);
                break;
            }
            case R.id.action_exit: finish(); break;
        }
        return super.onOptionsItemSelected(item);
    }
}