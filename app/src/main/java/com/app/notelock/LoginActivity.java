package com.app.notelock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    Button btn_login, btn_register;
    TextView tv_forgot;
    FirebaseAuth auth;
    TextInputEditText et_email, et_password;
    TextInputLayout et_email_Text, et_password_Text;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        auth = FirebaseAuth.getInstance();
        btn_login = findViewById(R.id.loginLogin);
        btn_register = findViewById(R.id.loginRegister);
        tv_forgot = findViewById(R.id.loginForgot);

        et_email_Text = findViewById(R.id.loginEmailText);
        et_password_Text = findViewById(R.id.loginPasswordText);
        et_email = findViewById(R.id.loginEmail);
        et_password = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.loginProgressbar);

        progressBar.setVisibility(View.INVISIBLE);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login processing

                authenticateUser();

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_forgot.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

    }

    private void authenticateUser() {

        progressBar.setVisibility(View.VISIBLE);
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString();

        Boolean b1 = false, b2 = false, b3 = false;

        if (email.length() == 0) {
            et_email_Text.setError("This field cannot be blank");
            b3 = true;
        } else {
            et_email_Text.setError(null);
        }

        if (!b3) {
            if (email.length() < 8 || !checkForAtTheRate(email)) {
                et_email_Text.setError("Please enter a valid email address");
                b1 = true;
            } else {
                et_email_Text.setError(null);

            }
        }


        if (password.length() == 0) {
            et_password_Text.setError("This field cannot be blank");
            b2 = true;
        } else {
            et_password_Text.setError(null);
        }


        if (b1 || b2 || b3) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        //login
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = auth.getCurrentUser();

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            // put Extra
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            Log.w("Login", "Login Failed: ", task.getException());
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    private boolean checkForAtTheRate(String email) {
        for (int i = 0; i < email.length(); i++) {
            char ch = email.charAt(i);
            if (ch == '@')
                return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }
}