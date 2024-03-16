package com.app.notelock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class SettingsActivity extends AppCompatActivity {
    Button btn_logout;
    Button btn_update;
    Button btn_verify;
    Button btn_upload_image;
    FirebaseAuth auth;
    BottomNavigationView bottomNavigationView;
    TextView tv_display_name, tv_display_email, tv_verified;
    ImageView iv_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        initVariables();


        clickListeners();
        setNavigationBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final String[] link = new String[1];

        String colRef = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        colRef = colRef + "picture";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference entries = db.collection(colRef);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads/");
        entries.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Toast.makeText(SettingsActivity.this, "success", Toast.LENGTH_SHORT).show();
                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            UploadedImage uploadedImage = documentSnapshot.toObject(UploadedImage.class);
                            Log.d("cir", "odnSuccess: " + uploadedImage.getUrl());

                            Picasso.get()
                                    .load(uploadedImage.getUrl())
                                    .error(R.drawable.ic_person)
                                    .into(iv_image);
                            Picasso.get().setLoggingEnabled(true);
                            break;

                        }
                    }
                });



        FirebaseUser user;
        if((user = auth.getCurrentUser()) != null){
            if(!user.isEmailVerified()){
                btn_verify.setVisibility(View.VISIBLE);
                tv_verified.setVisibility(View.VISIBLE);
                tv_verified.setText("Account is not verified");
                tv_verified.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.red));
            }
            else {
                btn_verify.setVisibility(View.INVISIBLE);
                tv_verified.setVisibility(View.VISIBLE);
                tv_verified.setText("Account is verified");
                tv_verified.setTextColor(ContextCompat.getColor(SettingsActivity.this, R.color.green));
            }
        }

    }

    private void clickListeners() {
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_update.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, UpdatePasswordActivity.class);
            startActivity(intent);
        });

        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, UploadImageActivity.class);
                startActivity(intent);
            }
        });

    }


    private void initVariables() {
        iv_image = findViewById(R.id.settingsPicture);
        auth = FirebaseAuth.getInstance();
        btn_logout = findViewById(R.id.settingsLogout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        btn_update = findViewById(R.id.settingsUpdate);
        btn_verify = findViewById(R.id.settingsVerify);
        btn_upload_image = findViewById(R.id.settingsUploadImage);
        bottomNavigationView.setSelectedItemId(R.id.btm_nav_settings);
        tv_display_name = findViewById(R.id.settingsName);
        tv_display_email = findViewById(R.id.settingsEmail);
        tv_verified = findViewById(R.id.settingsVerifiedTv);
        FirebaseUser user = auth.getCurrentUser();
        String displayName = user.getDisplayName().toUpperCase();
        tv_display_name.setText(displayName);
        String displayEmail = user.getEmail().toLowerCase();
        tv_display_email.setText(displayEmail);

        btn_verify.setVisibility(View.INVISIBLE);
        tv_verified.setVisibility(View.INVISIBLE);
    }

    private void setNavigationBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btm_nav_settings:
                        return true;
                    case R.id.btm_nav_generator:
                        Intent intent = new Intent(SettingsActivity.this, GeneratorActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.btm_nav_search:
                        intent = new Intent(SettingsActivity.this, SearchActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.btm_nav_myvault:
                        /*intent = new Intent(SettingsActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);*/
                        finish();
                        return true;

                }
                return false;
            }
        });
    }
}