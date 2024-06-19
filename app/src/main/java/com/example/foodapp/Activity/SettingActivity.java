package com.example.foodapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivitySettingBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    private TextView tvTiktok, tvInstagram, tvFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo binding trước khi sử dụng
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thiết lập các listener cho các view
        binding.logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(SettingActivity.this, LoginActivity.class));
        });
        binding.textView44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ShareandResponActivity.class);
                startActivity(intent);
            }
        });

        tvTiktok = findViewById(R.id.tvTiktok);
        tvInstagram = findViewById(R.id.tvInstagram);
        tvFacebook = findViewById(R.id.tvFacebook);
        binding.BackProfile.setOnClickListener(v -> finish());
        tvTiktok.setOnClickListener(v -> openUrl("https://www.tiktok.com"));
        tvInstagram.setOnClickListener(v -> openUrl("https://www.instagram.com/thanhsan_g/"));
        tvFacebook.setOnClickListener(v -> openUrl("https://www.facebook.com/profile.php?id=100035082763253"));
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
