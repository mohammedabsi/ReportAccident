package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class AccidentActivity extends AppCompatActivity {
    ImageView backBtnAccident;
    FrameLayout accident_container ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);

        backBtnAccident = findViewById(R.id.backBtnAccident);
        accident_container = findViewById(R.id.accident_container);

        String sessionId = getIntent().getStringExtra("accident");
        if (sessionId.equalsIgnoreCase("1")){
            getSupportFragmentManager().beginTransaction().replace(R.id.accident_container, new ChatFragment()).commit();

        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.accident_container, new AddAccidentFragment()).commit();

        }
    }

    public void ReturnHome(View view) {
        startActivity(new Intent(this , MainActivity.class));
        finish();

    }
}