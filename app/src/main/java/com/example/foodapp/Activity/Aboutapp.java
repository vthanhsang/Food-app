package com.example.foodapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityAboutappBinding;
import com.example.foodapp.databinding.ActivityProfileBinding;

public class Aboutapp extends AppCompatActivity {
ActivityAboutappBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutapp);
        binding = ActivityAboutappBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Backz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}