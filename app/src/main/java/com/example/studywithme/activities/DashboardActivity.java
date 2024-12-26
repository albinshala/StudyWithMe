package com.example.studywithme.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studywithme.R;
import com.example.studywithme.database.DBHelper;

public class DashboardActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private LinearLayout studySessionSection, goalsSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DBHelper(this);

        studySessionSection = findViewById(R.id.studySessionSection);
        goalsSection = findViewById(R.id.goalsSection);

        Button addSessionButton = findViewById(R.id.addSessionButton);
        Button addGoalButton = findViewById(R.id.addGoalButton);

        // Load animations
        Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Add bounce animation and navigate to GoalActivity
        addGoalButton.setOnClickListener(view -> {
            addGoalButton.startAnimation(bounceAnimation);
            addGoalButton.postDelayed(() -> {
                Intent intent = new Intent(DashboardActivity.this, GoalActivity.class);
                startActivity(intent);
            }, 300);
        });

        // Add fade-in animation and navigate to StudySessionActivity
        addSessionButton.setOnClickListener(view -> {
            addSessionButton.startAnimation(fadeInAnimation);
            addSessionButton.postDelayed(() -> {
                Intent intent = new Intent(DashboardActivity.this, StudySessionActivity.class);
                startActivity(intent);
            }, 300);
        });

        // Load existing data for study sessions and goals
        loadStudySessions();
        loadGoals();
    }

    private void loadStudySessions() {
        studySessionSection.removeAllViews(); // Clear existing views
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT subject, date, time, duration, description FROM StudySessions", null);

        if (cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow("duration"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                String sessionDetails = "Subject: " + subject + "\nDate: " + date + "\nTime: " + time +
                        "\nDuration: " + duration + " mins\nDescription: " + description;

                TextView sessionView = new TextView(this);
                sessionView.setText(sessionDetails);
                sessionView.setPadding(8, 8, 8, 8);
                sessionView.setBackgroundResource(R.drawable.card_background); // Optional card style
                studySessionSection.addView(sessionView);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void loadGoals() {
        goalsSection.removeAllViews(); // Clear existing views
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT goal, dueDate, isCompleted FROM Goals", null);

        if (cursor.moveToFirst()) {
            do {
                String goal = cursor.getString(cursor.getColumnIndexOrThrow("goal"));
                String dueDate = cursor.getString(cursor.getColumnIndexOrThrow("dueDate"));
                int isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted"));

                String status = isCompleted == 1 ? "Completed" : "Pending";
                String goalDetails = "Goal: " + goal + "\nDue Date: " + dueDate + "\nStatus: " + status;

                TextView goalView = new TextView(this);
                goalView.setText(goalDetails);
                goalView.setPadding(8, 8, 8, 8);
                goalView.setBackgroundResource(R.drawable.card_background); // Optional card style
                goalsSection.addView(goalView);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
