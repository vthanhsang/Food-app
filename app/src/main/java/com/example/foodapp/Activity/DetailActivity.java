package com.example.foodapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodapp.Adapter.CommentsAdapter;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.Domain.Lovefood;
import com.example.foodapp.Domain.Comment;
import com.example.foodapp.Helper.ManagmentCart;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;
    private RecyclerView recyclerViewComments;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList;
    private TextView textView51;

    private int num = 1;
    private ManagmentCart managmentCart;
    private DatabaseReference lovefoodRef;
    private DatabaseReference commentRef;
    private FirebaseAuth mAuth;
    private EditText editTextComment;
    private ImageView sendCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        lovefoodRef = database.getReference("Lovefood");
        commentRef = database.getReference("Comment");

        commentList = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(commentList);

        recyclerViewComments = findViewById(R.id.rcycmt);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComments.setAdapter(commentsAdapter);
        getIntentExtra();
        setVariable();
        loadComments();
    }

    private void loadComments() {
        String foodId = object.getId() + "";
        commentRef.orderByChild("foodId").equalTo(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    if (comment != null) {
                        commentList.add(comment);
                    }
                }
                commentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi lấy dữ liệu từ Firebase
                Toast.makeText(DetailActivity.this, "Có lỗi xảy ra khi lấy bình luận", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVariable() {

        managmentCart = new ManagmentCart(this);

        binding.backBtn.setOnClickListener(v -> finish());
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pictu);
        binding.Price.setText("$" + object.getPrice());
        binding.Title.setText(object.getTitle());
        binding.decriptionTxt.setText(object.getDescription());
        binding.Rate.setText(object.getStar() + "");
        binding.ratingBar.setRating((float) object.getStar());
        binding.TotalTxt.setText((num * object.getPrice() + "$"));
        binding.Time.setText(object.getTimeValue() + "P");

        binding.plusBtn.setOnClickListener(v -> {
            num = num + 1;
            binding.numTxt.setText(num + " ");
            binding.TotalTxt.setText("$" + (num * object.getPrice()));
        });

        binding.minusBtn.setOnClickListener(v -> {
            if (num > 1) {
                num = num - 1;
                binding.numTxt.setText(num + " ");
                binding.TotalTxt.setText("$" + (num * object.getPrice()));
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            object.setNumberInCart(num);
            managmentCart.insertFood(object);
        });

        binding.favBtn.setOnClickListener(v -> {
            // Lưu vào Realtime Database khi bấm vào nút yêu thích
            saveToFavorites();
        });

        // Thiết lập cho comment
        editTextComment = findViewById(R.id.editTextcmt);
        sendCommentButton = findViewById(R.id.sendcmt);

        sendCommentButton.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                saveComment(commentText);
            } else {
                Toast.makeText(DetailActivity.this, "Vui lòng nhập bình luận", Toast.LENGTH_SHORT).show();
            }
        });

        textView51=findViewById(R.id.textView51);
        textView51.setOnClickListener(v -> {
            if (editTextComment.getVisibility() == View.GONE) {
                editTextComment.setVisibility(View.VISIBLE);
                sendCommentButton.setVisibility(View.VISIBLE);
                recyclerViewComments.setVisibility(View.VISIBLE);
            } else {
                editTextComment.setVisibility(View.GONE);
                sendCommentButton.setVisibility(View.GONE);
                recyclerViewComments.setVisibility(View.GONE);
            }
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }

    private void saveToFavorites() {
        // Lấy ID người dùng hiện tại
        String userId = mAuth.getCurrentUser().getUid();

        // Kiểm tra xem món ăn đã được thêm vào yêu thích của người dùng hay chưa
        lovefoodRef.orderByChild("iduser").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isExist = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Lovefood lovefood = snapshot.getValue(Lovefood.class);
                    if (lovefood != null && lovefood.getIdfood().equals(object.getId() + "")) {
                        isExist = true;
                        break;
                    }
                }

                if (isExist) {
                    // Món ăn đã tồn tại trong danh sách yêu thích của người dùng
                    Toast.makeText(DetailActivity.this, "Món ăn đã được thêm vào yêu thích rồi", Toast.LENGTH_SHORT).show();
                } else {
                    // Món ăn chưa tồn tại trong danh sách yêu thích của người dùng, tiến hành thêm mới
                    // Tạo ID mới cho món ăn yêu thích
                    String lovefoodId = lovefoodRef.push().getKey();

                    Lovefood lovefood = new Lovefood(
                            object.getId() + "", // ID của món ăn
                            object.getImagePath(), // Hình ảnh của món ăn
                            object.getPrice() + "", // Giá của món ăn
                            object.getTitle(), // Tiêu đề của món ăn
                            userId // ID của người dùng
                    );

                    // Đẩy dữ liệu vào Realtime Database
                    if (lovefoodId != null) {
                        lovefoodRef.child(lovefoodId).setValue(lovefood);
                        Toast.makeText(DetailActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Lỗi khi thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(DetailActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveComment(String commentText) {
        String userId = mAuth.getCurrentUser().getUid();
        String userName = mAuth.getCurrentUser().getDisplayName(); // Giả sử bạn đã có tên người dùng lưu trong FirebaseAuth

        // Tạo ID mới cho bình luận
        String commentId = commentRef.push().getKey();

        Comment comment = new Comment(
                commentId,
                userId,
                userName,
                commentText,
                object.getId() + "" // ID của món ăn
        );

        // Đẩy dữ liệu vào Realtime Database
        if (commentId != null) {
            commentRef.child(commentId).setValue(comment);
            Toast.makeText(DetailActivity.this, "Bình luận đã được thêm", Toast.LENGTH_SHORT).show();
            editTextComment.setText(""); // Xóa nội dung của EditText sau khi gửi bình luận
        } else {
            Toast.makeText(DetailActivity.this, "Lỗi khi thêm bình luận", Toast.LENGTH_SHORT).show();
        }
    }
}
