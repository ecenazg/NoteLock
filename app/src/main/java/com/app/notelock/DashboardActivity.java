package com.app.notelock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.GetChars;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton btn_float_add_item;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference entries;

    EntryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setTitle("Dashboard");
        String nameToUse = user.getEmail();
        entries = db.collection(nameToUse);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        btn_float_add_item = findViewById(R.id.dashboardFloatingAddItem);

        bottomNavigationView.setSelectedItemId(R.id.btm_nav_myvault);

        btn_float_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });


        setNavigationBar();
        setupRecylerView();
    }

    private void setNavigationBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btm_nav_myvault:
                        return true;
                    case R.id.btm_nav_generator:
                        Intent intent = new Intent(DashboardActivity.this, GeneratorActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        //finish();
                        return true;
                    case R.id.btm_nav_search:
                        intent = new Intent(DashboardActivity.this, SearchActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        //finish();
                        return true;
                    case R.id.btm_nav_settings:
                        intent = new Intent(DashboardActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        //finish();
                        return true;

                }
                return false;
            }
        });
    }

    private void setupRecylerView() {

        Query query = entries.orderBy("name");

        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        adapter = new EntryAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.dashboardRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        adapterListeners();

    }

    private void adapterListeners() {
        adapter.setOnItemClickListener(new EntryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Item item = documentSnapshot.toObject(Item.class);
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(DashboardActivity.this, "Position: " + position
                        + "Path: " + path, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, ViewItemActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

        adapter.setOnItemLaunchClickListener(new EntryAdapter.OnItemLaunchClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Toast.makeText(DashboardActivity.this, "launch", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemCopyUserClickListener(new EntryAdapter.OnItemCopyUserClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Toast.makeText(DashboardActivity.this, "copy user", Toast.LENGTH_SHORT).show();

            }
        });

        adapter.setOnItemCopyPasswordClickListener(new EntryAdapter.OnItemCopyPasswordClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Toast.makeText(DashboardActivity.this, "password", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.btm_nav_myvault);
        Log.d("item", "onresume");
    }

    @Override
    protected void onStart() {

        super.onStart();
        adapter.startListening();
        Log.d("item", "onstart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.d("item", "onstop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("item", "onpause");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("item", "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("item", "onDestroy");
    }
}