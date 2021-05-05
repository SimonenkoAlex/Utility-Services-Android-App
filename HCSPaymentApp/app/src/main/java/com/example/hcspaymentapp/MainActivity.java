package com.example.hcspaymentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ConnectionDatabase connect = new ConnectionDatabase();
    // объявляем переменные
    final int MENU_QUIT_ID = 1;
    Button btnLogin, btnRegistration;
    EditText loginText, passText;
    CoordinatorLayout root;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // присваеваем значение id каждого элемента интерфейса переменным
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginText = (EditText) findViewById(R.id.loginText);
        btnRegistration = (Button) findViewById(R.id.btnRegistration);
        passText = (EditText) findViewById(R.id.passText);

        root = findViewById(R.id.activity_element);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if(connect.connectPostgreSQL()) {
                    Toast.makeText(getApplicationContext(), "Подключение прошло успешно", Toast.LENGTH_SHORT).show();
                    //authorization(loginText.getText().toString(), passText.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Не удалось подключиться к серверу PostgreSQL", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRegistration:
                showRegistrationWindow();
                break;
        }
    }

    private void showRegistrationWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");

        LayoutInflater inflater = LayoutInflater.from(this);
        View activity_registration = inflater.inflate(R.layout.activity_registration, null);
        dialog.setView(activity_registration);

        final MaterialEditText lastname = activity_registration.findViewById(R.id.textLastname);
        final MaterialEditText firstname = activity_registration.findViewById(R.id.textFirstname);
        final MaterialEditText patronymic = activity_registration.findViewById(R.id.textPatronymic);
        final MaterialEditText phone = activity_registration.findViewById(R.id.textPhone);
        final MaterialEditText email = activity_registration.findViewById(R.id.loginText);
        final MaterialEditText password = activity_registration.findViewById(R.id.passText);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите свой email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lastname.getText().toString())) {
                    Snackbar.make(root, "Введите свою фамилию", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(firstname.getText().toString())) {
                    Snackbar.make(root, "Введите своё имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(patronymic.getText().toString())) {
                    Snackbar.make(root, "Введите своё отчество", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(root, "Введите свой номер телефона", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль, который больше 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                GlobalVariables user = new GlobalVariables();
                                user.setEmail(email.getText().toString());
                                user.setLast_name(lastname.getText().toString());
                                user.setFirst_name(firstname.getText().toString());
                                user.setPatronymic(patronymic.getText().toString());
                                user.setPhone(phone.getText().toString());
                                user.setPassword(password.getText().toString());

                                users.child(user.getEmail()).setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Snackbar.make(root, "Регтстрация прошла УСПЕШНО", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
            }
        });
        dialog.show();
    }

    // создание функции для авторизации на PostgreSQL
    public void authorization(String login, String password){

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
    // Постоянный адрес текущей цветовой схемы:
    // https://colorscheme.ru/#1g422makQw0w0