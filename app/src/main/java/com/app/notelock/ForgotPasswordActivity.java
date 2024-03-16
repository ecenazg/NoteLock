package com.app.notelock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText email;
    Button btn_submit;
    ProgressBar progressBar;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.forgotPassEmail);
        btn_submit = findViewById(R.id.forgotPassButton);
        progressBar = findViewById(R.id.forgotPassProgressbar);
        mainLayout = findViewById(R.id.forgotPassMainLayout);

        progressBar.setVisibility(View.INVISIBLE);
        btn_submit.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.INVISIBLE);
                                //Toast.makeText(ForgotPasswordActivity.this, "Email has been sent to your email address", Toast.LENGTH_SHORT).show();
                                Snackbar.make(mainLayout, "Email has been sent", Snackbar.LENGTH_LONG)
                                        .setAction("Close", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        })
                                        .setActionTextColor(ContextCompat.getColor(ForgotPasswordActivity.this, R.color.colorSecondary))
                                        .show();
                            }
                            else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(ForgotPasswordActivity.this, "Error happened", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ForgotPasswordActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}