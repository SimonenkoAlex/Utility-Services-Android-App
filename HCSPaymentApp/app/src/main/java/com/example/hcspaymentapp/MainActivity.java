package com.example.hcspaymentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ConnectionDatabase connect = new ConnectionDatabase();
    GlobalVariables gv = GlobalVariables.getInstance();
    // объявляем переменные
    final int MENU_QUIT_ID = 1;
    Button btnLogin;
    EditText loginText, passText;
    TextView registrationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // присваеваем значение id каждого элемента интерфейса переменным
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginText = (EditText) findViewById(R.id.loginText);
        registrationText = (TextView) findViewById(R.id.registrationText);
        passText = (EditText) findViewById(R.id.passText);

        btnLogin.setOnClickListener(this);
        registrationText.setOnClickListener(this);
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
            case R.id.registrationText:
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
        }
    }

    // создание функции для авторизации на PostgreSQL
    public void authorization(String login, String password){
        try {
            String storeProcedureCall = "SELECT * FROM to_login_android('" + login + "','" + password + "')";
            Statement stmt = connect.getDBConnection().createStatement();
            /*
            CallableStatement stmt = connect.connectPostgreSQL().prepareCall(storeProcedureCall);
            // первые два параметра являются входными
            stmt.setString(1, login);
            stmt.setString(2, password);
            // последние 6 выходных данных показывают какого типа параметры
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.registerOutParameter(5, Types.VARCHAR);
            stmt.registerOutParameter(6, Types.VARCHAR);
            stmt.registerOutParameter(7, Types.VARCHAR);
            stmt.registerOutParameter(8, Types.VARCHAR);

            stmt.executeUpdate();
            */
            ResultSet rs = stmt.executeQuery(storeProcedureCall);
            ResultSetMetaData md = rs.getMetaData();
            int _code = 0; String _last_name = null, _first_name = null, _patronymic = null;
            String _phone = null, _report = null, _passport = null;
            while(rs.next()) {
                ArrayList<Object> mass = new ArrayList();
                for(int i = 1; i <= md.getColumnCount(); i++) {
                    mass.add(rs.getObject(i));
                }
                mass.toArray();
                // объявляем переменные, получаемые от функции в PostgreSQL
                _code = Integer.valueOf(mass.get(1).toString());
                _last_name = mass.get(2).toString();
                _first_name = mass.get(3).toString();
                _patronymic = mass.get(4).toString();
                _passport = mass.get(5).toString();
                _phone = mass.get(6).toString();
                _report = mass.get(7).toString();
            }
            // условие проверки правильности введения имени пользователя и пароля
            if(_report.equals("OK")){
                gv.set_code_user_login(_code);
                gv.set_last_name(_last_name);
                gv.set_first_name(_first_name);
                gv.set_phone(_phone);
                gv.set_patronymic(_patronymic);
                gv.set_passport(_passport);
                Intent personalArea = new Intent(this, PersonalAreaActivity.class);
                startActivity(personalArea);
            } else {
                Toast.makeText(getApplicationContext(), _report.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception error) {
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }
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
