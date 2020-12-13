package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    SharedPreferences sharedPreferences;
    public static final String loggedIn = "false";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        sharedPreferences = getSharedPreferences(String.valueOf(loggedIn), Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(loggedIn,"");
        if(value.equals("true")){
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(intent);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                    if(user.equals("admin") && pass.equals("admin")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                        editor.putString(loggedIn, "true");
                        editor.apply();
                        startActivity(intent);
                    }
            }
        });
    }
}