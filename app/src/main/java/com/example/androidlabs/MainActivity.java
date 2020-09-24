package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.*;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gridlayout);
        final Button clickBtn = findViewById(R.id.button1);
        clickBtn.setOnClickListener((click) -> {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message),Toast.LENGTH_LONG).show();});
        final CheckBox chBox = findViewById(R.id.CheckBox);
        chBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean b) {
                if(b) {
                    Snackbar.make(chBox, getResources().getString(R.string.check_message) + " " +getResources().getString(R.string.on_string), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getString(R.string.undo_string), click -> cb.setChecked(!b)).show();
                }
                else{
                    Snackbar.make(chBox, getResources().getString(R.string.check_message) + " " + getResources().getString(R.string.off_string), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.undo_string), click -> cb.setChecked(!b)).show();
                }
            }
        });
        final Switch sw = findViewById(R.id.Switch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean b) {
                if(b) {
                    Snackbar.make(sw, getResources().getString(R.string.switch_message) + " " + getResources().getString(R.string.on_string), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.undo_string), click -> sw.setChecked(!b)).show();
                }
                else{
                    Snackbar.make(sw, getResources().getString(R.string.switch_message) + " " + getResources().getString(R.string.off_string), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.undo_string), click -> sw.setChecked(!b)).show();
                }
            }
        });
    }
}