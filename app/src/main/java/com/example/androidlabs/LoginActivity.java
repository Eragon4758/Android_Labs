package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    static final String LAB_3 = "lab3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadEmail();
        setListeners();
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveEmail();
    }

    /**
     * Set event listeners on Views of this Activity
     */
    private void setListeners() {
        findViewById(R.id.login)
            .setOnClickListener(v ->
                startActivity(
                    new Intent(LoginActivity.this, ProfileActivity.class)
                )
            );
    }

    /**
     * Saves the text entered in #email field in the shared preferences.
     */
    private void saveEmail() {
        EditText email = findViewById(R.id.email);
        getSharedPreferences(LAB_3, Context.MODE_PRIVATE).edit()
            .putString("email", email.getText().toString())
            .apply();
    }

    /**
     * Loads the text in the email field of shared preferences into the #email field.
     */
    private void loadEmail() {
        EditText email = findViewById(R.id.email);
        email.setText(
            getSharedPreferences(LAB_3, Context.MODE_PRIVATE)
                .getString("email", "")
        );
    }
}
