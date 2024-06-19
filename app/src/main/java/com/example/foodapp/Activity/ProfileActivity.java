package com.example.foodapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private ActivityProfileBinding binding;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView animatedImageView = findViewById(R.id.avatar);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.hello) // replace with your GIF file name
                .into(animatedImageView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView backhomeImageView = findViewById(R.id.Back);
        backhomeImageView.setOnClickListener(v -> finish());

        binding.editprofile.setOnClickListener(v -> {
            // Launch EditProfileActivity
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        TextView historyPurchaseTextView = findViewById(R.id.historyPurchase);
        historyPurchaseTextView.setOnClickListener(v -> {
            // Launch HistoryOrder Activity
            Intent intent = new Intent(ProfileActivity.this, HistoryOrder.class);
            startActivity(intent);
        });

        binding.aboutapp.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, Aboutapp.class);
            startActivity(intent);
        });

        binding.lovefood.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LovefoodActivity.class);
            startActivity(intent);
        });

        // Initialize Firebase Database reference
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Load user data
        loadUserData();
    }

    private void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            userDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String avatarUrl = dataSnapshot.child("AvatarImage").getValue(String.class);
                        String userName = dataSnapshot.child("name").getValue(String.class);

                        ImageView avatarImageView = findViewById(R.id.avatar);

                        // Load the avatar image using Glide, or set a default image if avatarUrl is empty
                        RequestOptions requestOptions = new RequestOptions()
                                .transform(new CircleCrop());
                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Glide.with(ProfileActivity.this)
                                    .load(avatarUrl)
                                    .apply(requestOptions)
                                    .into(avatarImageView);
                        } else {
                            Glide.with(ProfileActivity.this)
                                    .load(R.drawable.hello)
                                    .apply(requestOptions)// replace with your default avatar resource
                                    .into(avatarImageView);

                        }

                        // Set the user's name in the TextView
                        if (userName != null && !userName.isEmpty()) {
                            TextView nameTextView = findViewById(R.id.textView21);
                            nameTextView.setText(userName);
                        }
                    } else {
                        // Set default avatar and notify user
                        RequestOptions requestOptions = new RequestOptions()
                                .transform(new RoundedCorners(75));
                        ImageView avatarImageView = findViewById(R.id.avatar);
                        Glide.with(ProfileActivity.this)
                                .load(R.drawable.hello)
                                .apply(requestOptions)// replace with your default avatar resource
                                .into(avatarImageView);

                        Toast.makeText(ProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
