package com.app.notelock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UpdateItemActivity extends AppCompatActivity {


    TextInputEditText name, username, password, url;
    TextInputLayout name_layout, username_layout, password_layout, url_layout;
    String sName, sUsername, sPassword, sUrl;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar progressBar;
    String path;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        setTitle("Update Item");

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        name = findViewById(R.id.updateItemName);
        username = findViewById(R.id.updateItemUserName);
        password = findViewById(R.id.updateItemPassword);
        url = findViewById(R.id.updateItemUrl);
        progressBar = findViewById(R.id.updateItemProgressbar);

        name_layout = findViewById(R.id.updateItemNameLayout);
        username_layout = findViewById(R.id.updateItemUserNameLayout);
        password_layout = findViewById(R.id.updateItemPasswordLayout);
        url_layout = findViewById(R.id.updateItemUrlLayout);


        progressBar.setVisibility(View.INVISIBLE);

        path = getIntent().getStringExtra("path");


        db.document(path)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Item item = documentSnapshot.toObject(Item.class);
                        sName = item.getName();
                        sUsername = item.getUsername();
                        sPassword = item.getPassword();
                        sUrl = item.getUrl();

                        setViewItems();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(UpdateItemActivity.this, "failed: " + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "Failed");
                    }
                });
    }

    private void setViewItems() {

        name.setText(sName);
        username.setText(sUsername);
        password.setText(sPassword);
        url.setText(sUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item_save_button:
                addItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addItem() {


        String _name = name.getText().toString().trim();
        String _username = username.getText().toString().trim();
        String _password = password.getText().toString().trim();
        String _url = url.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        Item item = new Item(_name, _username, _password, _url);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userNameForCollection = user.getEmail();

        if(userNameForCollection == null){
            Toast.makeText(UpdateItemActivity.this, "Why email is not returning", Toast.LENGTH_SHORT).show();
            return;
        }

        db.document(path).set(item, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateItemActivity.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateItemActivity.this, ViewItemActivity.class);
                        intent.putExtra("path", path);
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(UpdateItemActivity.this, "Item updating failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("Item Add", e.toString());
                    }
                });


    }
}