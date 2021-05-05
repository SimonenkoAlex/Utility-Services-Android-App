package com.example.hcspaymentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

public class PersonalAreaActivity extends AppCompatActivity {
    //GlobalVariables gv = GlobalVariables.getInstance();
    // объявляем переменные
    TextView lastnameText, firstnameText, patronymicText, phoneText, passportText;

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
        /*
        if(gv.get_code_user_login() != 0) {
            lastnameText.setText(gv.get_last_name().toString());
            firstnameText.setText(gv.get_first_name().toString());
            patronymicText.setText(gv.get_patronymic().toString());
            phoneText.setText(gv.get_phone().toString());
            passportText.setText(gv.get_passport().toString());
        }
         */
    }
}