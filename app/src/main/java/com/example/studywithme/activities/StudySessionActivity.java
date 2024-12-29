package com.example.studywithme.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studywithme.R;
import com.example.studywithme.database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StudySessionActivity extends AppCompatActivity {

    private TextView tvTimer;
    private EditText etSessionNotes;
    private Button btnStartSession, btnStopSession, btnSaveSession;
    private LinearLayout sessionSection;
    private DBHelper dbHelper;

    private long startTime = 0L;
    private Handler timerHandler = new Handler();
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_session);

        // Initialize UI components
        tvTimer = findViewById(R.id.tvTimer);
        etSessionNotes = findViewById(R.id.etSessionNotes);
        btnStartSession = findViewById(R.id.btnStartSession);
        btnStopSession = findViewById(R.id.btnStopSession);
        btnSaveSession = findViewById(R.id.btnSaveSession);
        sessionSection = findViewById(R.id.sessionSection);

        // Initialize database helper
        dbHelper = new DBHelper(this);

        // Set button listeners
        btnStartSession.setOnClickListener(v -> startTimer());
        btnStopSession.setOnClickListener(v -> stopTimer());
        btnSaveSession.setOnClickListener(v -> saveStudySession());

        // Load existing study sessions
        loadStudySessions();
    }
    
        }
    };

    private void saveStudySession() {
        String notes = etSessionNotes.getText().toString().trim();
        String duration = tvTimer.getText().toString().trim(); // Get duration from timer
        String sessionTitle = "Study Session"; // Default title for the session

        // Fetch current date and time
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        if (notes.isEmpty()) {
            Toast.makeText(this, "Please enter session notes", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbHelper.insertStudySession(sessionTitle, date, time, duration, notes);

        if (isInserted) {
            Toast.makeText(this, "Study session saved successfully", Toast.LENGTH_SHORT).show();

            // Refresh sessions and return to DashboardActivity
            loadStudySessions();
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("fromOtherActivity", true); // Set flag to indicate return
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to save study session", Toast.LENGTH_SHORT).show();
        }
    }
private void loadStudySessions() {
        sessionSection.removeAllViews(); // Clear existing views
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

                sessionSection.addView(sessionLayout);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}

