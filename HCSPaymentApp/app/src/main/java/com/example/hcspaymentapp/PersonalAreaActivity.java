package com.example.hcspaymentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PersonalAreaActivity extends AppCompatActivity {
    // объявляем переменные
    TextView lastnameText, firstnameText, patronymicText, phoneText, passportText;
    private List<GlobalVariables> listTemp;
    private  static final String TAG = "Main";


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

        Intent i = getIntent();
        if (i != null) {
            lastnameText.setText(i.getStringExtra("user_last_name"));
            firstnameText.setText(i.getStringExtra("user_first_name"));
            patronymicText.setText(i.getStringExtra("user_patronymic"));
            phoneText.setText(i.getStringExtra("user_phone"));
            passportText.setText(i.getStringExtra("user_passport"));
        }
        /*
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("users"); // Key

        // Attach listener
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve latest value
                GlobalVariables user = dataSnapshot.getValue(GlobalVariables.class);
                lastnameText.setText(user.getLast_name());
                Log.d(TAG, "Value is: " + user.getLast_name());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Error handling
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
         */
    }
}