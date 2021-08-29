package com.example.adminstatisticsscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UsersActivity extends AppCompatActivity {
    BottomNavigationView bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        bottom = findViewById(R.id.navigation);
        bottom.setSelectedItemId(R.id.usersScreen);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.usersScreen:
                        return true;
                    case R.id.homeScreen:
                        startActivity(new Intent(UsersActivity.this, AdminMainActivity.class));
                        return true;
                    case R.id.leadersScreen:
                        startActivity(new Intent(UsersActivity.this, LeadersActivity.class));
                        return true;
                    case R.id.coursesScreen:
                        startActivity(new Intent(UsersActivity.this, CoursesActivity.class));
                        return true;
                    case R.id.moreScreen:
                        startActivity(new Intent(UsersActivity.this, MoreActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
}