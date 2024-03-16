package com.app.notelock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ViewItemActivity extends AppCompatActivity {
    FloatingActionButton btn_edit, btn_delete;
    TextView name;
    TextView username;
    TextView password;
    TextView url;
    TextView note;
    ImageView logo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String sName, sUsername, sPassword, sUrl, sNote;
    String path;
    ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        setTitle("View Item");
        name = findViewById(R.id.viewItemName);
        username = findViewById(R.id.viewItemUsername);
        password = findViewById(R.id.viewItemPassword);
        url = findViewById(R.id.viewItemUrl);
        note = findViewById(R.id.viewItemNote);
        logo = findViewById(R.id.viewItemImageView);
        btn_delete = findViewById(R.id.viewItemDelete);
        btn_edit = findViewById(R.id.viewItemEdit);
        mainLayout = findViewById(R.id.viewItemMainLayout);

        setClickListener();

        path = getIntent().getStringExtra("path");

        getItem();


    }

    private void getItem() {
        db.document(path)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Item item = documentSnapshot.toObject(Item.class);
                            sName = item.getName();
                            sUsername = item.getUsername();
                            sPassword = item.getPassword();
                            sUrl = item.getUrl();
                            sNote = "will be added soon";

                            setViewItems();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ViewItemActivity.this, "failed: " + e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Failled");
            }
        });
    }

    private void setClickListener() {
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewItemActivity.this, UpdateItemActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
                finish();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(ViewItemActivity.this)
                        .setMessage("Do you really want to delete this item?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("Cancel", null)
                        .show();
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button button_negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.document(path).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ViewItemActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(ViewItemActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                button_negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void setViewItems() {
        Picasso.get()
                .load("https://logo.clearbit.com/" + sUrl)
                .placeholder(R.drawable.web_24)
                .error(R.drawable.web_24)
                .into(logo);

        name.setText(sName);
        username.setText(sUsername);
        password.setText(sPassword);
        url.setText(sUrl);
        note.setText(sNote);
    }
}