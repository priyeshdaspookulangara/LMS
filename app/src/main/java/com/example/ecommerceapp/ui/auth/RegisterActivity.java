package com.example.ecommerceapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerceapp.MainActivity; // Assuming this is your main activity after registration
import com.example.ecommerceapp.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFullName; // Added
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLoginLink;
    private ProgressBar progressBar;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextFullName = findViewById(R.id.editTextFullName); // Added
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLoginLink = findViewById(R.id.textViewLoginLink);
        progressBar = findViewById(R.id.progressBarRegister);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getRegistrationResult().observe(this, registrationResult -> {
            progressBar.setVisibility(View.GONE);
            if (registrationResult.isSuccess()) {
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity or LoginActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class); // Or LoginActivity
                startActivity(intent);
                finishAffinity(); // Finish this and all parent activities up to MainActivity
            } else {
                Toast.makeText(RegisterActivity.this, "Registration failed: " + registrationResult.getError(), Toast.LENGTH_LONG).show();
            }
        });

        buttonRegister.setOnClickListener(v -> {
            String fullName = editTextFullName.getText().toString().trim(); // Added
            String username = editTextUsername.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            if (fullName.isEmpty()) { // Added validation
                editTextFullName.setError("Full name is required");
                editTextFullName.requestFocus();
                return;
            }

            if (username.isEmpty()) {
                editTextUsername.setError("Username is required");
                editTextUsername.requestFocus();
                return;
            }

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Enter a valid email");
                editTextEmail.requestFocus();
                return;
            }

            if (password.isEmpty() || password.length() < 6) {
                editTextPassword.setError("Password must be at least 6 characters");
                editTextPassword.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                editTextConfirmPassword.setError("Passwords do not match");
                editTextConfirmPassword.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            authViewModel.register(username, email, password, fullName); // Pass fullName
        });

        textViewLoginLink.setOnClickListener(v -> {
            // Navigate to LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Optional: finish RegisterActivity
        });
    }
}
