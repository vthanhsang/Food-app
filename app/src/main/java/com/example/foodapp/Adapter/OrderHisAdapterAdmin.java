package com.example.foodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Domain.OrderHistory;
import com.example.foodapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHisAdapterAdmin extends RecyclerView.Adapter<OrderHisAdapterAdmin.OrderHistoryViewHolder> {
    private List<OrderHistory> orderHistoryList;
    private DatabaseReference mDatabase;


    public OrderHisAdapterAdmin(List<OrderHistory> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_order, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrderHistory orderHistory = orderHistoryList.get(position);

        holder.sttTextView.setText(String.valueOf(position + 1));
        holder.orderIdTextView.setText(orderHistory.getOrderId());
//        holder.nameTextView.setText(orderHistory.getFoodTitles().get(0));
        holder.orderTimeTextView.setText(orderHistory.getOrderTime());
//        double price = Double.parseDouble(orderHistory.getPrice().toString());
//        int quantity = Integer.parseInt(orderHistory.getSoluongs().toString());
//
//        // Tính toán totalPrice bằng cách nhân giá và số lượng
//        double totalPrice = price * quantity;
//        holder.totalPriceTextView.setText(String.valueOf(totalPrice) + "$");
        holder.totalPriceTextView.setText(String.valueOf(orderHistory.getTotalPrice()) + "$");
//        holder.soluong.setText(String.valueOf(orderHistory.getSoluongs().get(0)));
//
//        Glide.with(holder.itemView.getContext())
//                .load(orderHistory.getImageUrls().get(0))
//                .into(holder.imageView);
//        holder.cancle.setText(orderHistory.getStatus());

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
        holder.done.setOnClickListener(v -> {
            String orderId = orderHistory.getOrderId();
            updateOrderStatus(orderId, "Chấp nhận", holder);
        });

        holder.cancle.setOnClickListener(v -> {
            String orderId = orderHistory.getOrderId();
            updateOrderStatus(orderId, "Bị hủy", holder);
        });




    }


    private void updateOrderStatus(String orderId, String status, OrderHistoryViewHolder holder) {
        DatabaseReference statusOrderRef = mDatabase.child("StatusOrder").child(orderId);

        // Kiểm tra xem orderId đã tồn tại trong cơ sở dữ liệu hay không
        statusOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Hiển thị thông báo rằng đơn hàng đã được cập nhật
                    Toast.makeText(holder.itemView.getContext(), "Đơn hàng đã được cập nhật trước đó.", Toast.LENGTH_SHORT).show();

                } else {
                    // Nếu orderId chưa tồn tại, thêm mới dữ liệu
                    Map<String, Object> statusData = new HashMap<>();
                    statusData.put("status", status);

                    statusOrderRef.setValue(statusData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Hiển thị Toast thông báo thành công
                                    Toast.makeText(holder.itemView.getContext(), "Thêm trạng thái thành công", Toast.LENGTH_SHORT).show();
                                    // Ẩn nút khi đã thêm thành công

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Hiển thị Toast thông báo thất bại
                                    Toast.makeText(holder.itemView.getContext(), "Thêm trang thái thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ cơ sở dữ liệu
                Toast.makeText(holder.itemView.getContext(), "Lỗi khi đọc dữ liệu từ cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView sttTextView;
        TextView orderIdTextView;
        TextView nameTextView;
        TextView orderTimeTextView;
        TextView totalPriceTextView;
        ImageView imageView;
        TextView soluong;
        Button seeMoreButton,done,cancle;
        View additionalInfoLayout;
        RecyclerView foodListRecyclerView;
        Spinner spinner;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            sttTextView = itemView.findViewById(R.id.STT);
            orderIdTextView = itemView.findViewById(R.id.IDuser);
            orderTimeTextView = itemView.findViewById(R.id.Timeorder);
            totalPriceTextView = itemView.findViewById(R.id.Pricespp);
            imageView = itemView.findViewById(R.id.imageImage);


            seeMoreButton = itemView.findViewById(R.id.Detail);

            additionalInfoLayout = itemView.findViewById(R.id.additionalInfoLayoutad);
            foodListRecyclerView = itemView.findViewById(R.id.foodListRecyclerView);
            done=itemView.findViewById(R.id.buttonchapnhan);
            cancle=itemView.findViewById(R.id.buttoncancle);



        }
    }
}
