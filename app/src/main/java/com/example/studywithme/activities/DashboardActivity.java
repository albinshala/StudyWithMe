package com.example.studywithme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studywithme.R;
import com.example.studywithme.database.DBHelper;

public class DashboardActivity extends AppCompatActivity {

    private LinearLayout studySessionSection, goalsSection;
    private DBHelper dbHelper;
    private Button addSessionButton, addGoalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize UI components
        studySessionSection = findViewById(R.id.studySessionSection);
        goalsSection = findViewById(R.id.goalsSection);
        addSessionButton = findViewById(R.id.addSessionButton);
        addGoalButton = findViewById(R.id.addGoalButton);

        dbHelper = new DBHelper(this);

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

        // Load study sessions and goals
        loadStudySessions();
        loadGoals();

        // Show welcome alarm if not returning from another activity
        showWelcomeAlarmIfLoggedIn();
    }

    // Method to display a welcome alarm if not returning from another activity
    private void showWelcomeAlarmIfLoggedIn() {
        Intent intent = getIntent();
        boolean fromOtherActivity = intent.getBooleanExtra("fromOtherActivity", false);

        if (!fromOtherActivity) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Welcome!");
            builder.setMessage("Welcome to Study With Me! We're glad to have you here ☺ ♥. ");
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.setCancelable(false); // Ensure user acknowledges the message
            builder.show();
        }
    }

    private void loadStudySessions() {
        studySessionSection.removeAllViews(); // Clear existing views
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, subject, date, time, duration, description FROM StudySessions", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow("duration"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                // Create a layout to hold session details and the delete button
                LinearLayout sessionLayout = new LinearLayout(this);
                sessionLayout.setOrientation(LinearLayout.VERTICAL);
                sessionLayout.setPadding(8, 8, 8, 8);
                sessionLayout.setBackgroundResource(R.drawable.card_background);

                // TextView for session details
                TextView sessionView = new TextView(this);
                sessionView.setText("Subject: " + subject + "\nDate: " + date + "\nTime: " + time +
                        "\nDuration: " + duration + "\nDescription: " + description);
                sessionLayout.addView(sessionView);

                // Delete Button
                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setOnClickListener(v -> deleteStudySession(id));
                sessionLayout.addView(deleteButton);

                studySessionSection.addView(sessionLayout);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void loadGoals() {
        goalsSection.removeAllViews(); // Clear existing views
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, goal, dueDate, isCompleted FROM Goals", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String goal = cursor.getString(cursor.getColumnIndexOrThrow("goal"));
                String dueDate = cursor.getString(cursor.getColumnIndexOrThrow("dueDate"));
                int isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted"));

                String status = isCompleted == 1 ? "Completed" : "Pending";

                // Create a layout to hold goal details and the delete button
                LinearLayout goalLayout = new LinearLayout(this);
                goalLayout.setOrientation(LinearLayout.VERTICAL);
                goalLayout.setPadding(8, 8, 8, 8);
                goalLayout.setBackgroundResource(R.drawable.card_background);

                // TextView for goal details
                TextView goalView = new TextView(this);
                goalView.setText("Goal: " + goal + "\nDue Date: " + dueDate + "\nStatus: " + status);
                goalLayout.addView(goalView);

                // Delete Button
                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setOnClickListener(v -> deleteGoal(id));
                goalLayout.addView(deleteButton);

                goalsSection.addView(goalLayout);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void deleteStudySession(int id) {
        boolean isDeleted = dbHelper.deleteStudySession(id);
        if (isDeleted) {
            loadStudySessions(); // Refresh the study sessions
            Toast.makeText(this, "Study session deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete study session", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteGoal(int id) {
        boolean isDeleted = dbHelper.deleteGoal(id);
        if (isDeleted) {
            loadGoals(); // Refresh the goals
            Toast.makeText(this, "Goal deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete goal", Toast.LENGTH_SHORT).show();
        }
    }
}
