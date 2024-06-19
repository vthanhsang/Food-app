package com.example.foodapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();

        ImageView animatedImageView = findViewById(R.id.imageView4);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.hello) // replace with your GIF file name
                .into(animatedImageView);
    }

    private void setVariable() {
        binding.Signup.setOnClickListener(v -> {
            String name = binding.name.getText().toString();
            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();
            String retrypassword = binding.Retrypassword.getText().toString();
            mAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Check if email is already registered
                            List<String> signInMethods = task.getResult().getSignInMethods();
                            if (signInMethods != null && !signInMethods.isEmpty()) {
                                Toast.makeText(SignupActivity.this, "Email đã được đăng ký trước đó!", Toast.LENGTH_SHORT).show();
                            } else if (!password.equals(retrypassword)) {
                                Toast.makeText(SignupActivity.this, "Mật khẩu nhập lại không chính xác", Toast.LENGTH_SHORT).show();
                            } else {
                                // Create new user
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(SignupActivity.this, registrationTask -> {
                                            if (registrationTask.isSuccessful()) {
                                                Log.i(TAG, "Đăng ký thành công");
                                                String userId = mAuth.getCurrentUser().getUid();
                                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                                                // Save user information including name
                                                usersRef.child("name").setValue(name);
                                                usersRef.child("email").setValue(email);
                                                usersRef.child("password").setValue(password);
                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            } else {
                                                Log.e(TAG, "Đăng ký thất bại", registrationTask.getException());
                                                Toast.makeText(SignupActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Log.e(TAG, "Lỗi trong quá trình kiểm tra email", task.getException());
                            Toast.makeText(SignupActivity.this, "Đã xảy ra lỗi, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.Backlogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }

}
