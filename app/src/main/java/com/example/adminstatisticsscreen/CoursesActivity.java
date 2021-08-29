package com.example.adminstatisticsscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoursesActivity extends AppCompatActivity {
    BottomNavigationView bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        bottom = findViewById(R.id.navigation);
        bottom.setSelectedItemId(R.id.coursesScreen);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.coursesScreen:
                        return true;
                    case R.id.homeScreen:
                        startActivity(new Intent(CoursesActivity.this, AdminMainActivity.class));
                        return true;
                    case R.id.usersScreen:
                        startActivity(new Intent(CoursesActivity.this, UsersActivity.class));
                        return true;
                    case R.id.leadersScreen:
                        startActivity(new Intent(CoursesActivity.this, LeadersActivity.class));
                        return true;
                    case R.id.moreScreen:
                        startActivity(new Intent(CoursesActivity.this, MoreActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
}