package com.example.foodapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.foodapp.Adapter.LovefoodAdapter;
import com.example.foodapp.Domain.Lovefood;
import com.example.foodapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LovefoodActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LovefoodAdapter adapter;
    private List<Lovefood> lovefoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lovefood);

        recyclerView = findViewById(R.id.lovefoodListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lovefoodList = new ArrayList<>();
        adapter = new LovefoodAdapter(this, lovefoodList);
        recyclerView.setAdapter(adapter);

       ProgressBar progressBar = findViewById(R.id.progressBarlove);
        ImageView backprofile = findViewById(R.id.backProfilee);
        backprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Lấy iduser hiện tại
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Truy vấn dữ liệu từ Realtime Database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Lovefood");
            databaseReference.orderByChild("iduser").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Lovefood lovefood = dataSnapshot.getValue(Lovefood.class);
                        if (lovefood != null) {
                            lovefoodList.add(lovefood);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("LovefoodActivity", "Failed to read value.", error.toException());
                }
            });
        }

        adapter.setOnDeleteClickListener(new LovefoodAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                Lovefood itemToDelete = lovefoodList.get(position);
                deleteItemFromFavorites(itemToDelete);
            }
        });
    }
    private void deleteItemFromFavorites(Lovefood itemToDelete) {
        // Lấy ID người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Thực hiện truy vấn để tìm món ăn trong danh sách yêu thích của người dùng
        DatabaseReference lovefoodRef = FirebaseDatabase.getInstance().getReference("Lovefood");
        Query query = lovefoodRef.orderByChild("iduser").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Lovefood lovefood = snapshot.getValue(Lovefood.class);
                    if (lovefood != null && lovefood.getIdfood().equals(itemToDelete.getIdfood())) {
                        // Tìm thấy món ăn trong danh sách yêu thích của người dùng, xóa nó khỏi cơ sở dữ liệu
                        snapshot.getRef().removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Xóa thành công, thông báo cho người dùng và cập nhật danh sách hiển thị
                                        Toast.makeText(LovefoodActivity.this, "Món ăn đã được xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                        // Xóa món ăn khỏi danh sách hiển thị
                                        lovefoodList.remove(itemToDelete);
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xảy ra lỗi trong quá trình xóa, thông báo cho người dùng
                                        Toast.makeText(LovefoodActivity.this, "Đã xảy ra lỗi khi xóa món ăn khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        return; // Đã tìm thấy và xử lý, không cần tìm kiếm nữa
                    }
                }
                // Không tìm thấy món ăn trong danh sách yêu thích của người dùng
                Toast.makeText(LovefoodActivity.this, "Không tìm thấy món ăn trong danh sách yêu thích", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(LovefoodActivity.this, "Có lỗi xảy ra khi truy vấn dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }





}
