package com.example.hcspaymentapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // присваеваем значение id каждого элемента интерфейса переменным
        textLastname = (EditText) findViewById(R.id.textLastname);
        textFirstname = (EditText) findViewById(R.id.textFirstname);
        textPatronymic = (EditText) findViewById(R.id.textPatronymic);
        textPhone = (EditText) findViewById(R.id.textPhone);
        textPassport = (EditText) findViewById(R.id.textPassport);
        email = (EditText) findViewById(R.id.loginText);
        password = (EditText) findViewById(R.id.passText);
        btnRegistration = (Button) findViewById(R.id.btnRegistration);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите свой email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textLastname.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите свою фамилию", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textFirstname.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите своё имя", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textPatronymic.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите своё отчество", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textPhone.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите свой номер телефона", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textPassport.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите номер своего паспорта", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().length() < 5) {
                    Toast.makeText(getApplicationContext(), "Введите пароль, который больше 5 символов", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                GlobalVariables user = new GlobalVariables();
                                user.setId_user(users.getKey());
                                user.setEmail(email.getText().toString());
                                user.setLast_name(textLastname.getText().toString());
                                user.setFirst_name(textFirstname.getText().toString());
                                user.setPatronymic(textPatronymic.getText().toString());
                                user.setPhone(textPhone.getText().toString());
                                user.setPhone(textPassport.getText().toString());
                                user.setPassword(password.getText().toString());
                                users.push().setValue(user);

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Регтстрация прошла УСПЕШНО", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Ошибка регистрации. Пользователь с подобным email'ом уже существует", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
            }
        });
    }

    // создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_QUIT_ID, 0, "Назад");
        return super.onCreateOptionsMenu(menu);
    }

    // обработка нажатий на пункты меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_QUIT_ID: // выход из приложения
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
