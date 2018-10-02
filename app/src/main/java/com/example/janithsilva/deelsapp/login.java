package com.example.janithsilva.deelsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {

    EditText name;
    EditText password;
    Button signIn;

    String nameS, passwordS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.adminName);
        password = findViewById(R.id.adminPassword);
        signIn = findViewById(R.id.button);

        nameS = name.toString().trim();
        passwordS = password.toString().trim();

       // System.out.println(nameS+passwordS+"***********************************************");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(login.this, FoodListActivity.class));


            }
        });
    }
}
