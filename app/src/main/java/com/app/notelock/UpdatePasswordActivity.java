package com.app.notelock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class UpdatePasswordActivity extends AppCompatActivity {
    TextInputEditText et_old, et_new, et_confirm;
    Button btn_update;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        setTitle("Update Password");

        et_old = findViewById(R.id.updatePassOld);
        et_new = findViewById(R.id.updatePassNew);
        et_confirm = findViewById(R.id.updatePassNewConfirm);
        btn_update = findViewById(R.id.updatePassUpdate);
        progressBar = findViewById(R.id.updatePassProgressbar);

        progressBar.setVisibility(View.INVISIBLE);

        btn_update.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            reAuthenticate();
        });

    }

    private void reAuthenticate() {
        String emailId = user.getEmail();
        String _oldPassword = et_old.getText().toString();

        AuthCredential credential = EmailAuthProvider.getCredential(emailId, _oldPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            updatePassword();
                        }
                        else {
                            Toast.makeText(UpdatePasswordActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    private void updatePassword() {
        String password = et_new.getText().toString();
        String passwordConfirmed = et_confirm.getText().toString();
        if(password.equals(passwordConfirmed)){
            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(UpdatePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(UpdatePasswordActivity.this, "Password update failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(this, "Password did not match", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}