package com.example.foodapp.Activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityIntroBinding;

public class introActivity extends BaseActivity {
    ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(v -> {
    if(mAuth.getCurrentUser()!=null ){
        startActivity(new Intent(introActivity.this, MainActivity.class));
        }else {
        startActivity(new Intent(introActivity.this, LoginActivity.class));
        }});


//        binding.Signup.setOnClickListener(v -> {
//            startActivity(new Intent(introActivity.this, SignupActivity.class));
//        });
    }


}
