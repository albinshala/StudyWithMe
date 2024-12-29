package com.example.studywithme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studywithme.R;
import com.example.studywithme.database.DBHelper;
import java.util.regex.Pattern;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailField, newPasswordField, confirmPasswordField;
    private Button resetPasswordButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailField = findViewById(R.id.emailField);
        newPasswordField = findViewById(R.id.newPasswordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        dbHelper = new DBHelper(this);

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String newPassword = newPasswordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPassword(newPassword)) {
                Toast.makeText(this, "Password must contain at least 8 characters, including a number, a special character, an uppercase, and a lowercase letter.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isUpdated = dbHelper.updatePassword(email, newPassword);
            if (isUpdated) {
                Toast.makeText(this, "Password reset successfully.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("showNotification", true);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to reset password. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidPassword(String password) {
        // Regular expression for password validation
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }
}
