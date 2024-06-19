package com.example.foodapp.Activity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.OrderHistoryAdapter;
import com.example.foodapp.Domain.OrderHistory;
import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryOrder extends BaseActivity {
    private RecyclerView recyclerViewOrder;
    private ProgressBar progressBar;
    private EditText cancelButton;


    private List<OrderHistory> orderHistoryList;
    private OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);

        recyclerViewOrder = findViewById(R.id.ViewHistory);
        progressBar = findViewById(R.id.progressBarHistory);


        orderHistoryList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(orderHistoryList);

        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrder.setAdapter(orderHistoryAdapter);

        loadOrders();
        ImageView BackProfile = findViewById(R.id.BackProfile);;
        BackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void loadOrders() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentUser.getUid();

        FirebaseDatabase.getInstance().getReference("Orders")
                .orderByChild("iduser")
                .equalTo(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderHistoryList.clear();
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            String orderId = orderSnapshot.child("orderId").getValue(String.class);
                            String orderTime = orderSnapshot.child("orderTime").getValue(String.class);
                            int totalPrice = orderSnapshot.child("totalprice").getValue(Integer.class);
                            String status = orderSnapshot.child("status").getValue(String.class);
                            List<String> imageUrls = new ArrayList<>();
                            List<String> foodTitles = new ArrayList<>();
                            List<Integer> soluongs = new ArrayList<>();
                            List<Double>  price = new ArrayList<>();

                            for (DataSnapshot imageSnapshot : orderSnapshot.child("foodImages").getChildren()) {
                                imageUrls.add(imageSnapshot.getValue(String.class));
                            }

                            for (DataSnapshot titleSnapshot : orderSnapshot.child("foodTitle").getChildren()) {
                                foodTitles.add(titleSnapshot.getValue(String.class));
                            }

                            for (DataSnapshot soluongSnapshot : orderSnapshot.child("foodsoluong").getChildren()) {
                                soluongs.add(soluongSnapshot.getValue(Integer.class));
                            }
                            for (DataSnapshot priceSnapshot : orderSnapshot.child("foodprice").getChildren()) {
                                price.add(priceSnapshot.getValue(Double.class));
                            }

//                            orderHistoryList.add(new OrderHistory(orderId, orderTime, totalPrice, imageUrls, foodTitles, soluongs,price));
                            orderHistoryList.add(new OrderHistory(orderId, orderTime, totalPrice, imageUrls, foodTitles, soluongs,price,status));
                        }
                        orderHistoryAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
