package com.example.foodapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainAdminBinding;
import com.example.foodapp.databinding.ActivityMainBinding;

public class MainAdminActivity extends AppCompatActivity {
    ActivityMainAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.qlkh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAdminActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
        binding.qlsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAdminActivity.this, AddFoodActivity.class);
                startActivity(intent);
            }
        });
        binding.qldh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAdminActivity.this, AdminOrderActivity.class);
                startActivity(intent);
            }
        });
        binding.qlrespon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAdminActivity.this, ResponActivity.class);
                startActivity(intent);
            }
        });

//        binding.gobuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainAdminActivity.this, MainActivity.class);
//
//                startActivity(intent);
//
//            }
//        });
    }
}