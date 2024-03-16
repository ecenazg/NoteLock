package com.app.notelock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {
    Button btn_register, btn_login;
    FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputLayout et_name_layout, et_email_layout, et_password_layout;
    TextInputEditText et_name_field, et_email_field, et_password_field;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Registration");

        progressBar = findViewById(R.id.registerProgressbar);
        auth = FirebaseAuth.getInstance();
        btn_register = findViewById(R.id.registerRegisterButton);
        btn_login = findViewById(R.id.registerLoginButton);
        et_name_field = findViewById(R.id.registerName);
        et_email_field = findViewById(R.id.registerEmail);
        et_password_field = findViewById(R.id.registerPassword);
        et_name_layout = findViewById(R.id.registerNameLayout);
        et_email_layout = findViewById(R.id.registerEmailLayout);
        et_password_layout = findViewById(R.id.registerPasswordLayout);

        progressBar.setVisibility(View.INVISIBLE);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // registering account

                createNewAccount();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void createNewAccount() {
        String name = et_name_field.getText().toString().trim();
        String email = et_email_field.getText().toString().trim();
        String password = et_password_field.getText().toString().trim();

        if (checkForError(name, email, password)) return;

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("login_status ", "createUserwithEmail: Success");
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();


                            FirebaseUser user = auth.getCurrentUser();
                            UserProfileChangeRequest updateName = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(updateName)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d("Update Name", "Name is updated");
                                            }
                                        }
                                    });





                            Intent intent = new Intent(RegisterActivity.this, AddItemActivity.class);
                            startActivity(intent);
                            progressBar.setVisibility(View.GONE);
                            finish();
                        }
                        else {
                            Log.w("Registration Status", "CreateUserwithEmail: Failed", task.getException());
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private boolean checkForError(String name, String email, String password) {
        Boolean b1 = false, b2 = false, b3 = false, b4 = false, b5 = false;

        if(name.length() == 0){
            et_name_layout.setError("Name cannot be empty");
            b1 = true;
        }
        else {
            et_name_layout.setError(null);
        }

        if(email.length() == 0){
            et_email_layout.setError("This field cannot be blank");
            b2 = true;
        }

        else {
            et_email_layout.setError(null);
        }

        if(password.length() == 0){
            et_password_layout.setError("This field cannot be blank");
            b3 = true;
        }

        else {
            et_password_layout.setError(null);
        }
        if(!b2) {
            if (email.length() < 10 || !checkForAtTheRate(email)) {
                et_email_layout.setError("Please enter a valid email address");
                b4 = true;
            } else {
                et_email_layout.setError(null);
            }
        }
        if(!b3) {
            if (password.length() < 6) {
                et_password_layout.setError("Minimum length is 6");
                b5 = true;
            } else {
                et_password_layout.setError(null);
            }
        }


        if(b1 || b2 || b3 || b4 || b5){
            return true;
        }
        return false;
    }

    private boolean checkForAtTheRate(String email) {
        for(int i = 0 ; i < email.length(); i++){
            char ch = email.charAt(i);
            if(ch == '@')
                return true;
        }
        return false;
    }
}