package com.example.foodapp.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.OrderHisAdapterAdmin;
import com.example.foodapp.Domain.OrderHistory;
import com.example.foodapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderActivity extends BaseActivity {
    private RecyclerView recyclerViewOrder;
    private ProgressBar progressBar;
    private TextView orderIdTextView;
    private TextView totalAmountTextView;

    private List<OrderHistory> orderHistoryList;
    private OrderHisAdapterAdmin orderHistoryAdapter;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        recyclerViewOrder = findViewById(R.id.ViewManager);
        progressBar = findViewById(R.id.progressBarHistory);
        orderIdTextView = findViewById(R.id.IDuser);

        orderHistoryList = new ArrayList<>();
        orderHistoryAdapter = new OrderHisAdapterAdmin(orderHistoryList);

        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrder.setAdapter(orderHistoryAdapter);

        totalAmountTextView = findViewById(R.id.amount);


        loadOrders();

        ImageView backProfile = findViewById(R.id.Backk);
        backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void loadOrders() {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference("Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderHistoryList.clear();
                        int totalAmount = 0;
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            String orderId = orderSnapshot.child("orderId").getValue(String.class);
                            String orderTime = orderSnapshot.child("orderTime").getValue(String.class);
                            int totalPrice = orderSnapshot.child("totalprice").getValue(Integer.class);
                            List<String> imageUrls = new ArrayList<>();
                            List<String> foodTitles = new ArrayList<>();
                            List<Integer> soluongs = new ArrayList<>();
                            List<Double> prices = new ArrayList<>();

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
                                prices.add(priceSnapshot.getValue(Double.class));
                            }

                            orderHistoryList.add(new OrderHistory(orderId, orderTime, totalPrice, imageUrls, foodTitles, soluongs, prices));
                            totalAmount += totalPrice;
                        }
                        orderHistoryAdapter.notifyDataSetChanged();
                        totalAmountTextView.setText(String.valueOf(totalAmount)+" $");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }



}
