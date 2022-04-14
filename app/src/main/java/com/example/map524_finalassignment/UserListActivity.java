package com.example.map524_finalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UserListActivity extends AppCompatActivity {

    DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        dbManager = ((MyApp)getApplication()).dbManager;
        dbManager.getMovieDB(this);
    }
}