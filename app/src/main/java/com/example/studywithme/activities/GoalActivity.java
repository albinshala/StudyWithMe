package com.example.studywithme.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studywithme.R;
import com.example.studywithme.database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GoalActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText goalDescriptionEditText, dueDateEditText;
    private Button saveGoalButton;
    private LinearLayout goalsSection;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        dbHelper = new DBHelper(this);

        // Initialize UI components
        goalDescriptionEditText = findViewById(R.id.etGoalDescription);
        dueDateEditText = findViewById(R.id.etDueDate);
        saveGoalButton = findViewById(R.id.btnSaveGoal);
        goalsSection = findViewById(R.id.goalsSection);

        // Set up DatePicker for due date input
        calendar = Calendar.getInstance();
        dueDateEditText.setOnClickListener(v -> showDatePicker());

        // Set up the Save button functionality
        saveGoalButton.setOnClickListener(v -> saveGoal());

        // Load existing goals
        loadGoals();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dueDateEditText.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void saveGoal() {
        String goalDescription = goalDescriptionEditText.getText().toString().trim();
        String dueDate = dueDateEditText.getText().toString().trim();

        if (goalDescription.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.insertGoal(goalDescription, dueDate);

        if (success) {
            Toast.makeText(this, "Goal saved successfully", Toast.LENGTH_SHORT).show();

            // Refresh goals and return to DashboardActivity
            loadGoals();
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("fromOtherActivity", true); // Set flag to indicate return
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error saving goal", Toast.LENGTH_SHORT).show();
        }
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
