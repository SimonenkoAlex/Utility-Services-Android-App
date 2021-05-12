package com.example.hcspaymentapp;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    Button btnLogin, btnRegistration, btnStart;
    EditText loginText, passText;
    TextView textUserEmail;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser curUser = auth.getCurrentUser();
        if (curUser != null) {
            btnStart.setVisibility(View.VISIBLE);
            textUserEmail.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            btnRegistration.setVisibility(View.GONE);
            loginText.setVisibility(View.GONE);
            passText.setVisibility(View.GONE);
            String userName = "Вы вошли как : " + curUser.getEmail();
            textUserEmail.setText(userName);
        } else {
            btnStart.setVisibility(View.GONE);
            textUserEmail.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnRegistration.setVisibility(View.VISIBLE);
            loginText.setVisibility(View.VISIBLE);
            passText.setVisibility(View.VISIBLE);
        }
    }

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
        btnStart = (Button) findViewById(R.id.btnStart);
        passText = (EditText) findViewById(R.id.passText);
        textUserEmail = (TextView) findViewById(R.id.textUserEmail);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users");

        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
        btnStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin: {
                authorization(loginText.getText().toString(), passText.getText().toString());
                break;
            }
            case R.id.btnRegistration: {
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
            }
            case R.id.btnStart: {
                startActivity(new Intent(this, PersonalAreaActivity.class));
                finish();
                break;
            }
        }
    }

    // создание функции для авторизации на PostgreSQL
    public void authorization(String login, String password){
        if (TextUtils.isEmpty(login)) {
            Toast.makeText(getApplicationContext(), "Введите свой email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 5) {
            Toast.makeText(getApplicationContext(), "Введите пароль, который больше 5 символов", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(login, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        GlobalVariables user = new GlobalVariables();
                        Intent i = new Intent(MainActivity.this, PersonalAreaActivity.class);
                        i.putExtra("user_last_name", user.getLast_name());
                        i.putExtra("user_first_name", user.getFirst_name());
                        i.putExtra("user_patronymic", user.getPatronymic());
                        i.putExtra("user_phone", user.getPhone());
                        i.putExtra("user_passport", user.getPassport());
                        i.putExtra("user_email", user.getEmail());
                        i.putExtra("user_password", user.getPassword());
                        startActivity(i);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка авторизации. " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        FirebaseUser curUser = auth.getCurrentUser();
        switch (item.getItemId()) {
            case MENU_QUIT_ID: { // выход из приложения
                if (curUser != null) {
                    FirebaseAuth.getInstance().signOut();
                    btnStart.setVisibility(View.GONE);
                    textUserEmail.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    btnRegistration.setVisibility(View.VISIBLE);
                    loginText.setVisibility(View.VISIBLE);
                    passText.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmailVerification() {

    }
}
    // Постоянный адрес текущей цветовой схемы:
    // https://colorscheme.ru/#1g422makQw0w0