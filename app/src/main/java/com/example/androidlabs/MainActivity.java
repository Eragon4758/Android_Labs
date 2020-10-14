package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.*;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        //SharedPreferences pref = new getSharedPreferences(Context.MODE_PRIVATE, 1); anything with shared preferences shows errors

        final Button logBtn = findViewById(R.id.loginButton);
        Intent nextPage = new Intent(this, ProfileActivity.class);
        logBtn.setOnClickListener((click) -> {
            startActivity(nextPage);
        });
    }
    @Override
    protected void onPause() {
        super.onPause();

    }
}