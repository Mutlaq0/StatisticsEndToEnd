package com.example.adminstatisticsscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MoreActivity extends AppCompatActivity {
    BottomNavigationView bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        bottom = findViewById(R.id.navigation);
        bottom.setSelectedItemId(R.id.moreScreen);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.moreScreen:
                        return true;
                    case R.id.homeScreen:
                        startActivity(new Intent(MoreActivity.this, AdminMainActivity.class));
                        return true;
                    case R.id.usersScreen:
                        startActivity(new Intent(MoreActivity.this, UsersActivity.class));
                        return true;
                    case R.id.leadersScreen:
                        startActivity(new Intent(MoreActivity.this, LeadersActivity.class));
                        return true;
                    case R.id.coursesScreen:
                        startActivity(new Intent(MoreActivity.this, CoursesActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
}