package com.example.foodapp.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends BaseActivity {
    private GoogleSignInClient googlelogin;
    private DatabaseReference databaseReference;
ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();


        ImageView animatedImageView = findViewById(R.id.imageView4);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.hello) // replace with your GIF file name
                .into(animatedImageView);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googlelogin = GoogleSignIn.getClient(this, gso);

//        binding.google.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
        binding.andanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInAnonymously();
            }
        });
    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this, "Đăng nhập ẩn danh thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập ẩn danh thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });}
    private void setVariable() {
        binding.Login.setOnClickListener(v -> {
            String email = binding.email.getText().toString();
            String password = binding.Retrypassword.getText().toString();
            if(!email.isEmpty() && !password.isEmpty()){
                if (email.equals("admin@gmail.com") && password.equals("123456")) {
                    startActivity(new Intent(LoginActivity.this, MainAdminActivity.class));
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công với tài khoản admin", Toast.LENGTH_SHORT).show();
                } else {

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, task -> {
                    if(task.isSuccessful()){
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        if (user != null) {
//                            registerFCMToken(user.getUid());
//                        }
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this, "Tài khoản và mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                });
            }} else
            {
                Toast.makeText(LoginActivity.this, "Hãy nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
            }
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu để trống", Toast.LENGTH_SHORT).show();
            }

        });

        binding.textView9.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,SignupActivity.class));
        });
        

    }

//    private void signIn() {
//        Intent signInIntent = googlelogin.getSignInIntent();
//        launcher.launch(signInIntent);
//    }
//    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == Activity.RESULT_OK) {
//                    Intent data = result.getData();
//                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                    manageResults(task);
//                }
//            }
//    );
//
//    private void manageResults(Task<GoogleSignInAccount> task) {
//        try {
//            GoogleSignInAccount account = task.getResult();
//            if (account != null) {
//                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//                mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            Toast.makeText(LoginActivity.this, "Account created", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void registerFCMToken(String userId) {
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        Log.w("FCM", "Fetching FCM registration token failed", task.getException());
//                        return;
//                    }
//
//                    // Get new FCM registration token
//                    String token = task.getResult();
//                    DatabaseReference userRef = databaseReference.child("users").child(userId);
//                    userRef.child("fcmToken").setValue(token);
//                });
//    }

}