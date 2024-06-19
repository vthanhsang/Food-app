package com.example.foodapp.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.foodapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddFoodActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100;

    private EditText titleEditText, descriptionEditText, priceEditText, timeValueEditText;
    private ImageView uploadImageButton;
    private Spinner categorySpinner;
    private Uri imageUri = null;
    private DatabaseReference mDatabase;
    private int nextAvailableId = -1;
    private int selectedCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        timeValueEditText = findViewById(R.id.timevalue);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        categorySpinner = findViewById(R.id.categorySpinner);
        ImageView backadmin = findViewById(R.id.Backadmin);

        backadmin.setOnClickListener(v -> finish());

        mDatabase = FirebaseDatabase.getInstance().getReference("Foods");

        findNextAvailableId();

        uploadImageButton.setOnClickListener(v -> openFileChooser());

        findViewById(R.id.submitButton).setOnClickListener(v -> {
            if (imageUri != null && nextAvailableId != -1 && selectedCategoryId != -1) {
                uploadImageToFirebase();
            } else {
                Toast.makeText(AddFoodActivity.this, "Please select an image and category", Toast.LENGTH_SHORT).show();
            }
        });

        // Check for storage permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }

        // Set up the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryId = position; // Set the selected category ID based on the selected position
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = -1; // No category selected
            }
        });
    }

    private void findNextAvailableId() {
        mDatabase.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String lastId = childSnapshot.getKey();
                        if (lastId != null) {
                            nextAvailableId = Integer.parseInt(lastId) + 1;
                        }
                    }
                } else {
                    nextAvailableId = 1; // Start IDs from 1 if no data exists
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                uploadImageButton.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("food_images/" + UUID.randomUUID().toString());
            storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                saveFoodData(imageUrl);
            })).addOnFailureListener(e -> {
                Toast.makeText(AddFoodActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveFoodData(String imageUrl) {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        double price = Double.parseDouble(priceEditText.getText().toString().trim());
        int timeValue = Integer.parseInt(timeValueEditText.getText().toString().trim());

        Map<String, Object> foodData = new HashMap<>();
        foodData.put("Id", nextAvailableId);
        foodData.put("Title", title);
        foodData.put("Description", description);
        foodData.put("Price", price);
        foodData.put("TimeValue", timeValue);
        foodData.put("ImagePath", imageUrl);
        foodData.put("CategoryId", selectedCategoryId);

        mDatabase.child(String.valueOf(nextAvailableId)).setValue(foodData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddFoodActivity.this, "Food data saved successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close activity after successful save
            } else {
                Toast.makeText(AddFoodActivity.this, "Failed to save food data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
