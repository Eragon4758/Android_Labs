package com.example.androidlabs;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.*;
import android.widget.*;
import android.content.Intent;

import android.os.Bundle;

import java.util.*;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private TextView mTextView;
    private ImageButton mImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mTextView = (TextView) findViewById(R.id.text);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageButton = findViewById(R.id.PicButton);
        ListView myList = findViewById(R.id.theListView);
        mImageButton.setOnClickListener((click) ->{
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        });
        Intent chat = new Intent(this, ChatRoomActivity.class );
        final Button chatty = findViewById(R.id.ChatButton);
        chatty.setOnClickListener((click) -> {
            startActivity(chat);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }

}