package com.example.adminstatisticsscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LeadersActivity extends AppCompatActivity {
    BottomNavigationView bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders);
        bottom = findViewById(R.id.navigation);
        bottom.setSelectedItemId(R.id.leadersScreen);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.leadersScreen:
                        return true;
                    case R.id.homeScreen:
                        startActivity(new Intent(LeadersActivity.this, AdminMainActivity.class));
                        return true;
                    case R.id.usersScreen:
                        startActivity(new Intent(LeadersActivity.this, UsersActivity.class));
                        return true;
                    case R.id.coursesScreen:
                        startActivity(new Intent(LeadersActivity.this, CoursesActivity.class));
                        return true;
                    case R.id.moreScreen:
                        startActivity(new Intent(LeadersActivity.this, MoreActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
}