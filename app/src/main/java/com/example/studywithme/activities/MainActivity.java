package com.example.studywithme.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studywithme.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Directly navigate to LoginActivity
        Intent intent = new Intent(this, com.example.studywithme.activities.LoginActivity.class);

        // Start the LoginActivity
        startActivity(intent);
        finish(); // Close MainActivity
    }
}