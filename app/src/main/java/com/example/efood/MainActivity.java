package com.example.efood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.efood.activity.auth.LoginActivity;
import com.example.efood.db.DBConnection;

public class MainActivity extends AppCompatActivity {

    private Button btnStart;
    private DBConnection dbHeplper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Context context = MainActivity.this;

        dbHeplper = new DBConnection(getApplicationContext());
        dbHeplper.createDataBase();

        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}