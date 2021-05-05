package com.example.hcspaymentapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegistrationActivity extends AppCompatActivity {
    // объявляем переменные
    final int MENU_QUIT_ID = 1;
    Button btnRegistration;
    EditText email, password, textPassport, textPhone,
            textPatronymic, textFirstname, textLastname;
    CoordinatorLayout root;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // присваеваем значение id каждого элемента интерфейса переменным
        textLastname = (EditText) findViewById(R.id.textLastname);
        textFirstname = (EditText) findViewById(R.id.textFirstname);
        textPatronymic = (EditText) findViewById(R.id.textPatronymic);
        textPhone = (EditText) findViewById(R.id.textPhone);
        textPassport = (EditText) findViewById(R.id.textPassport);
        email = (EditText) findViewById(R.id.loginText);
        password = (EditText) findViewById(R.id.passText);
        btnRegistration = (Button) findViewById(R.id.btnRegistration);

        root = findViewById(R.id.activity_element);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите свой email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textLastname.getText().toString())) {
                    Snackbar.make(root, "Введите свою фамилию", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textFirstname.getText().toString())) {
                    Snackbar.make(root, "Введите своё имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textPatronymic.getText().toString())) {
                    Snackbar.make(root, "Введите своё отчество", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textPhone.getText().toString())) {
                    Snackbar.make(root, "Введите свой номер телефона", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textPassport.getText().toString())) {
                    Snackbar.make(root, "Введите номер своего паспорта", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль, который больше 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    // создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_QUIT_ID, 0, "Выход");
        return super.onCreateOptionsMenu(menu);
    }

    // обработка нажатий на пункты меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_QUIT_ID: // выход из приложения
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
