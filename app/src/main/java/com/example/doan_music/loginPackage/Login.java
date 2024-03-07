package com.example.doan_music.loginPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan_music.R;

public class Login extends AppCompatActivity {

    Button LoginID, RegisterID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
        LoginID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Login_user.class);
                startActivity(intent);
            }
        });
        RegisterID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register_email.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        LoginID = findViewById(R.id.LoginID);
        RegisterID = findViewById(R.id.RegisterID);
    }
}