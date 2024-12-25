package com.example.studywithme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studywithme.R;
import com.example.studywithme.database.DBHelper;
import com.example.studywithme.utils.EmailSender;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnForgotPassword;
    private TextView tvSignUp; // Declare the TextView
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp); // Initialize tvSignUp

        // Initialize database helper
        dbHelper = new DBHelper(this);
        DBHelper dbHelper = new DBHelper(this);


        // Set click listeners
        btnLogin.setOnClickListener(view -> login());
        btnForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        // Handle click event for Sign Up
        tvSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check user credentials
        if (dbHelper.validateUser(email, password)) {
            generateAndSendOtp(email);

            // Navigate to OTP Verification screen
            Intent intent = new Intent(this, VerifyOtpActivity.class);
            intent.putExtra("userEmail", email);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateAndSendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        boolean isOtpUpdated = dbHelper.updateOtp(email, otp);

        if (!isOtpUpdated) {
            Toast.makeText(this, "Failed to generate OTP. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                EmailSender.sendEmail(
                        email,
                        "Your OTP Code",
                        "Use this OTP to log in: " + otp
                );
                runOnUiThread(() -> Toast.makeText(this, "OTP sent to your email!", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Failed to send OTP.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}