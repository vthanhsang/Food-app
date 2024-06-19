package com.example.foodapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ShareandResponActivity extends AppCompatActivity {

    private EditText editTextSend;
    private DatabaseReference databaseRespon;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareand_respon);

        editTextSend = findViewById(R.id.editsend);
        databaseRespon = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.sendddd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRespon();
            }
        });

        findViewById(R.id.backProfilee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendRespon() {
        String infor = editTextSend.getText().toString().trim();

        if (TextUtils.isEmpty(infor)) {
            editTextSend.setError("Nhập phản hồi");
            return;
        }

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference responseRef = databaseRespon.child("Responses").push();


            // Get current datetime
            String datetime = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
            String randomId = generateRandomId();
            // Prepare response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("idrespon", randomId);
            responseData.put("iduser", userId);
            responseData.put("datetime", datetime);
            responseData.put("infor", infor);



            // Save response data to Firebase
            responseRef.setValue(responseData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ShareandResponActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                    editTextSend.setText("");
                } else {
                    Toast.makeText(ShareandResponActivity.this, "Failed to submit response", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateRandomId() {
        // Generate a random string for the ID
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomId = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) { // Adjust the length of the random string as needed
            randomId.append(characters.charAt(random.nextInt(characters.length())));
        }
        return randomId.toString();
    }
}
