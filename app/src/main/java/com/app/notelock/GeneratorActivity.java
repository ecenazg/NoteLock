package com.app.notelock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.jetbrains.annotations.NotNull;

public class GeneratorActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button btn_regen, btn_copy;
    ImageButton btn_len_plus, btn_len_minus, btn_num_plus, btn_num_minus, btn_char_plus, btn_char_minus;
    TextView tv_len, tv_num, tv_char, tv_password;
    int passwordLength = 10, minNum = 1, minChar = 0;
    SwitchMaterial sw_cap, sw_small, sw_num, sw_char, sw_ambiguous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator_relative);

        setTitle("Generator");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        btn_regen = findViewById(R.id.gen_regen_btn);
        btn_copy = findViewById(R.id.gen_copy_btn);
        btn_len_plus = findViewById(R.id.gen_len_plus);
        btn_len_minus = findViewById(R.id.gen_len_minus);
        btn_num_plus = findViewById(R.id.gen_num_plus);
        btn_num_minus = findViewById(R.id.gen_num_minus);
        btn_char_minus = findViewById(R.id.gen_char_minus);
        btn_char_plus = findViewById(R.id.gen_char_plus);
        tv_len = findViewById(R.id.gen_len_tv);
        tv_num = findViewById(R.id.gen_num_tv);
        tv_char = findViewById(R.id.gen_char_tv);
        tv_password = findViewById(R.id.gen_pass);
        sw_cap = findViewById(R.id.gen_cap_sw);
        sw_small = findViewById(R.id.gen_small_sw);
        sw_num = findViewById(R.id.gen_num_sw);
        sw_char = findViewById(R.id.gen_special_sw);
        sw_ambiguous = findViewById(R.id.gen_ambi_sw);

        sw_small.setChecked(true);
        sw_cap.setChecked(true);
        sw_num.setChecked(true);
        sw_ambiguous.setChecked(true);

        tv_char.setText(String.valueOf(minChar));
        tv_num.setText(String.valueOf(minNum));
        tv_len.setText(String.valueOf(passwordLength));

        updatePassword();

        bottomNavigationView.setSelectedItemId(R.id.btm_nav_generator);

        setNavigationBar();
        setClickListeners();
    }

    private void updatePassword() {
        String password = PasswordGenerator.generate(passwordLength, minNum, minChar, sw_cap.isChecked(),
                sw_small.isChecked(), sw_num.isChecked(), sw_char.isChecked());
        tv_password.setText(password);
    }

    private void setClickListeners() {
        buttonListeners();
        switchListeners();
    }

    private void switchListeners() {
        sw_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sw_small.isChecked()){
                    sw_small.setChecked(true);
                }
            }
        });
        sw_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
        sw_char.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
        sw_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
        sw_ambiguous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void buttonListeners() {
        btn_regen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Password", tv_password.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(GeneratorActivity.this, "Password Copied", Toast.LENGTH_SHORT).show();
            }
        });
        btn_len_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordLength < 20){
                    passwordLength++;
                    tv_len.setText(String.valueOf(passwordLength));
                    updatePassword();
                }
            }
        });
        btn_len_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordLength > 8){
                    passwordLength--;
                    tv_len.setText(String.valueOf(passwordLength));
                    updatePassword();
                }
            }
        });
        btn_num_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minNum < 3){
                    minNum++;
                    tv_num.setText(String.valueOf(minNum));
                    updatePassword();
                }
            }
        });

        btn_num_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minNum > 0){
                    minNum--;
                    tv_num.setText(String.valueOf(minNum));
                    updatePassword();
                }
            }
        });

        btn_char_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minChar < 3){
                    minChar++;
                    tv_char.setText(String.valueOf(minChar));
                    updatePassword();
                }
            }
        });

        btn_char_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minChar > 0){
                    minChar--;
                    tv_char.setText(String.valueOf(minChar));
                    updatePassword();
                }
            }
        });
    }

    private void setNavigationBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.btm_nav_generator:
                        return true;
                    case R.id.btm_nav_myvault:
                        /*
                        Intent intent = new Intent(GeneratorActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);*/
                        finish();
                        return true;
                    case R.id.btm_nav_search:
                        intent = new Intent(GeneratorActivity.this, SearchActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.btm_nav_settings:
                        intent = new Intent(GeneratorActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                }
                return false;
            }
        });
    }
}