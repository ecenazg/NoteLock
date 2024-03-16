package com.app.notelock;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UploadImageActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    Button btn_upload, btn_choose_file, btn_done;
    ImageView iv_image;
    ProgressBar progressBar;
    Uri uri;
    ActivityResultLauncher<Intent> activityResultLauncher;
    StorageReference storageReference;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        initVariables();

        clickListeners();


    }

    private void clickListeners() {
        btn_upload.setOnClickListener(v -> {
            uploadFile();
            progressBar.setVisibility(View.VISIBLE);
        });
        btn_choose_file.setOnClickListener(v -> {
            openFileChooser();
        });
        btn_done.setOnClickListener(v -> {
            finish();
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            if(data != null && data.getData() != null){
                                uri = data.getData();
                                Log.d("ball", "onActivityResult: " + uri.toString());
                                Picasso.get()
                                        .load(uri)
                                        .into(iv_image);
                            }
                        }
                    }
                }
        );
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        if(uri != null){
            StorageReference fileRef = storageReference.child(System.currentTimeMillis()
            + '.' + getFileExtension(uri));
            fileRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(UploadImageActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete()) ;
                            Uri uri = uriTask.getResult() ;
                            String Url = Objects.requireNonNull(uri).toString() ;

                            UploadedImage uploadedImage = new UploadedImage(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                    Url);
                            db.collection(uploadedImage.getName() + "picture").document().set(uploadedImage);
                            btn_done.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(UploadImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                            //
                        }
                    });
        }
        else {
            Toast.makeText(this, "File not attached", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void initVariables() {
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        btn_upload = findViewById(R.id.uploadImageUpload);
        btn_choose_file = findViewById(R.id.uploadImageChooseFile);
        iv_image = findViewById(R.id.uploadImageImage);
        progressBar = findViewById(R.id.uploadImageProgressbar);
        btn_done = findViewById(R.id.uploadImageDone);
        progressBar.setVisibility(View.INVISIBLE);
        btn_done.setVisibility(View.INVISIBLE);
    }
}