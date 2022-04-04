package com.example.hcspaymentapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Log;
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
    final String LOG_TAG = "myLogs";
    final int MENU_QUIT_ID = 1;
    Button btnRegistration;
    EditText textEmail, textPass, textPassport, textPhone,
            textPatronymic, textFirstname, textLastname;
    ConnectionDatabase database;
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
        textEmail = (EditText) findViewById(R.id.loginText);
        textPass = (EditText) findViewById(R.id.passText);
        btnRegistration = (Button) findViewById(R.id.btnRegistration);

        textPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        // создаем объект для создания и управления версиями БД
        database = new ConnectionDatabase(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // создаем объект для данных
                final ContentValues cv = new ContentValues();
                // получаем данные из полей ввода
                if (TextUtils.isEmpty(textEmail.getText().toString().toLowerCase())) {
                    Toast.makeText(getApplicationContext(), "Введите свой email", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email = textEmail.getText().toString();
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(),"Пожалуйста введите корректный Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(textLastname.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите свою фамилию", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String lastname = textLastname.getText().toString();
                if (TextUtils.isEmpty(textFirstname.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите своё имя", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String firstname = textFirstname.getText().toString();
                if (TextUtils.isEmpty(textPatronymic.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите своё отчество", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String patronymic = textPatronymic.getText().toString();
                if (TextUtils.isEmpty(textPhone.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите свой номер телефона", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String phone = textPhone.getText().toString();
                if (TextUtils.isEmpty(textPassport.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Введите номер своего паспорта", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String passport = textPassport.getText().toString();
                if (textPass.getText().toString().length() < 6) {
                    Toast.makeText(getApplicationContext(), "Введите пароль, который больше 6 символов", Toast.LENGTH_SHORT).show();
                    return;
                }
                // подключаемся к БД
                final SQLiteDatabase sqldb = database.getWritableDatabase();

                auth.createUserWithEmailAndPassword(textEmail.getText().toString(), textPass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // подготовим данные для вставки в виде пар: наименование столбца - значение
                                cv.put("last_name", lastname);
                                cv.put("first_name", firstname);
                                cv.put("patronymic", patronymic);
                                cv.put("phone", phone);
                                cv.put("email", email);
                                cv.put("passport", passport);
                                GlobalVariables user = new GlobalVariables();
                                user.setId_user(users.getKey());
                                user.setEmail(textEmail.getText().toString());
                                user.setLast_name(textLastname.getText().toString());
                                user.setFirst_name(textFirstname.getText().toString());
                                user.setPatronymic(textPatronymic.getText().toString());
                                user.setPhone(textPhone.getText().toString());
                                user.setPhone(textPassport.getText().toString());
                                user.setPassword(textPass.getText().toString());
                                // вставляем запись и получаем ее ID
                                long rowID = sqldb.insert("renter", null, cv);
                                Log.d(LOG_TAG, "row inserted, ID = " + rowID);

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
