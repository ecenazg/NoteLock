package com.app.notelock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    SearchView searchView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference entries = db.collection(user.getEmail());
    EntryAdapterForSearch adapter;
    EntryAdapterForSearch2 adapter2;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("Search");

        //searchView = findViewById(R.id.search_searchview);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.btm_nav_search);

        setNavigationBar();
        //searchViewListener();


        recyclerView = findViewById(R.id.search_recylerview);
        setupRecyclerView2();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter2);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupRecyclerView2();
        recyclerView.setAdapter(adapter2);
    }

    private void searchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setupRecyclerView(newText);
                return false;
            }
        });
    }

    private void setupRecyclerView(String newText) {
        ArrayList<Item> array = new ArrayList<>();
        entries.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Item item = documentSnapshot.toObject(Item.class);
                            item.setId(documentSnapshot.getId());
                            String itemName = item.getName();
                            String itemUsername = item.getUsername();

                            Boolean bool1 = itemName.toLowerCase().contains(newText.toLowerCase());
                            Boolean bool2 = newText.toLowerCase().contains(itemName.toLowerCase());
                            Boolean bool3 = itemUsername.toLowerCase().contains(newText.toLowerCase());
                            Boolean bool4 = newText.toLowerCase().contains(itemUsername.toLowerCase());

                            if (itemUsername.trim().length() == 0) {
                                bool3 = false;
                                bool4 = false;
                            }

                            if (itemName.trim().length() == 0) {
                                bool1 = false;
                                bool2 = false;
                            }

                            if (bool1 || bool2 || bool3 || bool4) {
                                array.add(item);
                            }
                        }
                    }
                });
        adapter = new EntryAdapterForSearch(SearchActivity.this, array);

        recyclerView.setAdapter(adapter);
    }

    private void setupRecyclerView2(){
        ArrayList<Item> array2 = new ArrayList<>();
        entries.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Item item = documentSnapshot.toObject(Item.class);
                            item.setId(documentSnapshot.getId());
                            array2.add(item);

                        }
                        adapter2 = new EntryAdapterForSearch2(SearchActivity.this, array2);
                        recyclerView.setAdapter(adapter2);
                    }
                });

    }

    private void setNavigationBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btm_nav_search:
                        return true;
                    case R.id.btm_nav_generator:
                        Intent intent = new Intent(SearchActivity.this, GeneratorActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.btm_nav_myvault:
                        /*intent = new Intent(SearchActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);*/
                        finish();
                        return true;
                    case R.id.btm_nav_settings:
                        intent = new Intent(SearchActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search_searchIcon);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter2.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}