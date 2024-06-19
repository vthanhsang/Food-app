package com.example.foodapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private EditText editName, editGmail, editAddress, editPhone,editschool,editjob;
    private TextView namee, textphone,textgmail;
    private ImageView avatarImageView;
    private Button updateButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private LinearLayout linearLayout3;
    private Uri imageUri = null; // Uri của ảnh được chọn

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Liên kết EditText và TextView với layout
        editName = findViewById(R.id.EditNameus);
//        editGmail = findViewById(R.id.editGmailus);
        editAddress = findViewById(R.id.editAddressus);
        editjob= findViewById(R.id.editjob);

        editPhone = findViewById(R.id.editPhoneus);

        namee = findViewById(R.id.namee);
        updateButton = findViewById(R.id.updateinfor);
        ImageView backcart = findViewById(R.id.Backcart);
        avatarImageView = findViewById(R.id.Avatar);
        linearLayout3 = findViewById(R.id.linearLayout3);


        textphone=findViewById(R.id.textPhone);
        textgmail=findViewById(R.id.textgmail);

        // Thiết lập sự kiện nhấn nút "Backcart"
        backcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kết thúc hoạt động hiện tại
            }
        });

        // Khởi tạo Firebase Auth và Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Lấy dữ liệu người dùng từ Firebase
        loadUserData();

        // Thiết lập sự kiện nhấn nút "Update"
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadImageToFirebase(); // Nếu có ảnh được chọn, đẩy ảnh lên Firebase
                } else {
                    updateUserData(); // Nếu không có ảnh được chọn, chỉ cập nhật dữ liệu người dùng
                }
            }
        });

        // Kiểm tra quyền đọc bộ nhớ ngoài
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }

        // Thiết lập sự kiện nhấn vào ImageView Avatar để chọn ảnh từ thư viện
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLinearLayoutVisibility();
            }
        });
    }

    private void toggleLinearLayoutVisibility() {
        if (linearLayout3.getVisibility() == View.VISIBLE) {
            linearLayout3.setVisibility(View.GONE);
        } else {
            linearLayout3.setVisibility(View.VISIBLE);
        }
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Lấy dữ liệu từ snapshot và thiết lập cho các EditText và TextView
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String job = snapshot.child("job").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String imageUrl = snapshot.child("AvatarImage").getValue(String.class);
                        if (name != null) {
                            editName.setText(name);
                            namee.setText(name); // Thiết lập TextView namee
                        }
                        if (email != null) {
                            textgmail.setText(email);
                        }if (job != null) {
                            editjob.setText(job);
                        }
                        if (address != null) {
                            editAddress.setText(address);
                        }
                        if (phone != null) {
                            editPhone.setText(phone);
                            textphone.setText(phone);
                        }
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get().load(imageUrl).transform(new CircleTransform()).into(avatarImageView);
                        }

                    } else {
                        Toast.makeText(EditProfileActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditProfileActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String name = editName.getText().toString().trim();
            String job = editjob.getText().toString().trim();
            String address = editAddress.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
//            String school =editschool.getText().toString().trim();


            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("name", name);
            userUpdates.put("job", job);
            userUpdates.put("address", address);
            userUpdates.put("phone", phone);
//            userUpdates.put("School", school);

            mDatabase.child(uid).updateChildren(userUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "User data updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to update user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                String uid = currentUser.getUid();
                                // Lưu URL ảnh vào cột AvatarImage của nút users trong database
                                mDatabase.child(uid).child("AvatarImage").setValue(imageUrl)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(EditProfileActivity.this, "Image URL saved", Toast.LENGTH_SHORT).show();
                                            updateUserData(); // Cập nhật dữ liệu người dùng sau khi lưu URL ảnh thành công
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Failed to save image URL", Toast.LENGTH_SHORT).show());
                            } else {
                                Toast.makeText(EditProfileActivity.this, "User not signed in.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Mở trình chọn tệp hình ảnh từ bộ nhớ
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    // Xử lý kết quả trả về từ việc chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // Hiển thị ảnh được chọn lên ImageView
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedBitmap = BitmapFactory.decodeStream(imageStream);
                ImageView avatarImageView = findViewById(R.id.Avatar);
                avatarImageView.setImageBitmap(selectedBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Xử lý kết quả của việc yêu cầu quyền đọc bộ nhớ ngoài
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp
            } else {
                // Quyền bị từ chối
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
