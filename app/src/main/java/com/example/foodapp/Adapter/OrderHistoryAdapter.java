package com.example.foodapp.Adapter;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Domain.OrderHistory;
import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    private static final String TAG = "OrderHistoryAdapter";
    private List<OrderHistory> orderHistoryList;

    public OrderHistoryAdapter(List<OrderHistory> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_history_users, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrderHistory orderHistory = orderHistoryList.get(position);

        holder.sttTextView.setText(String.valueOf(position + 1));
        holder.orderIdTextView.setText(orderHistory.getOrderId());
        holder.orderTimeTextView.setText(orderHistory.getOrderTime());
//        double price = Double.parseDouble(orderHistory.getPrice().toString());
//        int quantity = Integer.parseInt(orderHistory.getSoluongs().toString());
//
//        // Tính toán totalPrice bằng cách nhân giá và số lượng
//        double totalPrice = price * quantity;
//        holder.totalPriceTextView.setText(String.valueOf(totalPrice) + "$");
        holder.totalPriceTextView.setText(String.valueOf(orderHistory.getTotalPrice()) + "$");
        holder.seeMoreButton.setOnClickListener(v -> {
            if (holder.additionalInfoLayout.getVisibility() == View.GONE) {
                holder.additionalInfoLayout.setVisibility(View.VISIBLE);
                FoodHistoryAdapter foodListAdapter = new FoodHistoryAdapter(
                        orderHistory.getFoodTitles(),
                        orderHistory.getSoluongs(),
                        orderHistory.getImageUrls(),
                        orderHistory.getPrice()
                );
                holder.foodListRecyclerView.setAdapter(foodListAdapter);
                holder.foodListRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            } else {
                holder.additionalInfoLayout.setVisibility(View.GONE);
            }
        });


        DatabaseReference statusOrderRef = FirebaseDatabase.getInstance().getReference().child("StatusOrder").child(orderHistory.getOrderId());
        statusOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Nếu OrderId tồn tại trong StatusOrder, lấy trạng thái và thiết lập cho button cancel
                    String status = dataSnapshot.child("status").getValue(String.class);
                    if (status != null) {
                        holder.cancelButton.setText(status);
                    } else {
                        holder.cancelButton.setText("Chưa xác nhận");
                    }
                } else {
                    holder.cancelButton.setText("Chưa xác nhận");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error reading status data: " + databaseError.getMessage());
            }
        });


//        holder.cancelButton.setOnClickListener(v -> {
//            cancelOrder(holder, orderHistory.getOrderId());
//        });

        // Hide the cancel button after 5 minutes (300,000 milliseconds)
        new Handler().postDelayed(() -> holder.cancelButton.setVisibility(View.GONE), 300000);
    }

//    private void cancelOrder(OrderHistoryViewHolder holder, String orderId) {
//
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//            FirebaseDatabase.getInstance().getReference("Orders")
//                    .child(orderId)
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()) {
//                                String orderUserId = snapshot.child("iduser").getValue(String.class);
//
//                                if (orderUserId != null && orderUserId.equals(userId)) {
//                                    snapshot.getRef().removeValue()
//                                            .addOnSuccessListener(aVoid -> {
//                                                Toast.makeText(holder.itemView.getContext(), "Order cancelled", Toast.LENGTH_SHORT).show();
//                                                removeOrderFromList(orderId);
//                                            })
//                                            .addOnFailureListener(e -> {
//                                                Toast.makeText(holder.itemView.getContext(), "Failed to cancel order", Toast.LENGTH_SHORT).show();
//                                                Log.e(TAG, "Failed to cancel order: ", e);
//                                            })
//                                            .addOnCompleteListener(task -> {
//                                                if (!task.isSuccessful()) {
//                                                    DatabaseError error = DatabaseError.fromException(task.getException());
//                                                    Log.e(TAG, "Error cancelling order: " + (error != null ? error.getMessage() : "Unknown error"));
//                                                }
//                                            });
//                                } else {
//                                    Toast.makeText(holder.itemView.getContext(), "You don't have permission to cancel this order", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(holder.itemView.getContext(), "Order does not exist", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.e(TAG, "Database error: " + error.getMessage());
//                        }
//                    });
//
//        } else {
//            Toast.makeText(holder.itemView.getContext(), "Please log in to cancel orders", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void removeOrderFromList(String orderId) {
//        for (int i = 0; i < orderHistoryList.size(); i++) {
//            if (orderHistoryList.get(i).getOrderId().equals(orderId)) {
//                orderHistoryList.remove(i);
//                notifyItemRemoved(i);
//                notifyItemRangeChanged(i, orderHistoryList.size());
//                Log.d(TAG, "Order removed from the list: " + orderId); // Thêm log ở đây
//                break;
//            }
//        }
//    }


    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView sttTextView;
        TextView orderIdTextView;
        TextView orderTimeTextView;
        TextView totalPriceTextView;
        Button seeMoreButton;
        View additionalInfoLayout;
        Button cancelButton;
        RecyclerView foodListRecyclerView;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            sttTextView = itemView.findViewById(R.id.STT);
            orderIdTextView = itemView.findViewById(R.id.ID);
            orderTimeTextView = itemView.findViewById(R.id.Timeorder);
            totalPriceTextView = itemView.findViewById(R.id.Priceor);
            seeMoreButton = itemView.findViewById(R.id.Detailorder);
            additionalInfoLayout = itemView.findViewById(R.id.additionalInfoLayout);
            foodListRecyclerView = itemView.findViewById(R.id.foodListRecyclerView);
            cancelButton = itemView.findViewById(R.id.Cancle);
        }
    }
}
