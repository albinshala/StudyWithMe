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
