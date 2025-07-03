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

import com.example.ecommerceapp.MainActivity; // Assuming this is your main activity after login
import com.example.ecommerceapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegisterLink;
    private ProgressBar progressBar;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegisterLink = findViewById(R.id.textViewRegisterLink);
        progressBar = findViewById(R.id.progressBarLogin);

        // Initialize ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe LiveData from ViewModel
        authViewModel.getLoginResult().observe(this, loginResult -> {
            progressBar.setVisibility(View.GONE);
            if (loginResult.isSuccess()) {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                // Navigate to MainActivity or Dashboard
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish LoginActivity so user can't navigate back
            } else {
                Toast.makeText(LoginActivity.this, "Login failed: " + loginResult.getError(), Toast.LENGTH_LONG).show();
            }
        });

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Enter a valid email");
                editTextEmail.requestFocus();
                return;
            }

            if (password.isEmpty() || password.length() < 6) { // Basic password validation
                editTextPassword.setError("Password must be at least 6 characters");
                editTextPassword.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            authViewModel.login(email, password);
        });

        textViewRegisterLink.setOnClickListener(v -> {
            // Navigate to RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
