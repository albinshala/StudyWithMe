package com.example.studywithme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studywithme.R;
import com.example.studywithme.database.DBHelper;

public class VerifyOtpActivity extends AppCompatActivity {

    private EditText etOtp;
    private Button btnVerifyOtp;
    private DBHelper dbHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        etOtp = findViewById(R.id.etOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        dbHelper = new DBHelper(this);
        userEmail = getIntent().getStringExtra("userEmail");

        btnVerifyOtp.setOnClickListener(view -> verifyOtp());
    }

    private void verifyOtp() {
        String inputOtp = etOtp.getText().toString().trim();

        if (inputOtp.isEmpty()) {
            Toast.makeText(this, "Please enter the OTP.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch OTP from the database
        String savedOtp = dbHelper.getOtp(userEmail);

        if (savedOtp == null) {
            Toast.makeText(this, "No OTP found. Please request a new one.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Debugging logs
        Log.d("VerifyOtpActivity", "Input OTP: " + inputOtp);
        Log.d("VerifyOtpActivity", "Saved OTP: " + savedOtp);

        if (inputOtp.equals(savedOtp)) {
            Toast.makeText(this, "OTP verified successfully!", Toast.LENGTH_SHORT).show();

            // Clear OTP after verification
            boolean isOtpCleared = dbHelper.updateOtp(userEmail, null);
            Log.d("VerifyOtpActivity", "OTP cleared: " + isOtpCleared);

            // Navigate to DashboardActivity
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
