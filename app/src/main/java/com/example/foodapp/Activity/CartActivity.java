package com.example.foodapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.foodapp.Adapter.CartAdapter;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.Helper.ChangeNumberItemsListener;
import com.example.foodapp.Helper.ManagmentCart;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityCartBinding;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import com.google.firebase.messaging.FirebaseMessaging;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    private CardView cardView2;
    private CheckBox checkbox1;
    private CheckBox checkbox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        setVariable();
        calculateCart();
        initList();

        cardView2 = findViewById(R.id.cardView2);
        checkbox1 = findViewById(R.id.checkbox1);
        checkbox2 = findViewById(R.id.checkbox2);

        findViewById(R.id.Place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardView2.getVisibility() == View.VISIBLE) {
                    cardView2.setVisibility(View.GONE);
                } else {
                    cardView2.setVisibility(View.VISIBLE);
                }
            }
        });
        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox1.isChecked()) {
                    checkbox2.setChecked(false);
                }
            }
        });

        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox2.isChecked()) {
                    checkbox1.setChecked(false);
                }
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("order_notifications")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed to order notifications";
                    if (!task.isSuccessful()) {
                        msg = "Subscription failed";
                    }
                    Toast.makeText(CartActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
    }

    private void initList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.EmptyTxt.setVisibility(View.VISIBLE);
            binding.scrollviewCart.setVisibility(View.GONE);
        } else {
            binding.EmptyTxt.setVisibility(View.GONE);
            binding.scrollviewCart.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managmentCart.getListCart(), this, () -> calculateCart());
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemtotal = Math.round(managmentCart.getTotalFee() * 100) / 100;
        binding.Totalfee.setText("$" + itemtotal);
        binding.TotalTax.setText("%" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.Totall.setText("$" + total);
    }
    private void calculateCartvoucher() {
        double percentTax = 0.02;
        double delivery = 0;
        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemtotal = Math.round(managmentCart.getTotalFee() * 100) / 100;
        binding.Totalfee.setText("$" + itemtotal);
        binding.TotalTax.setText("%" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.Totall.setText("$" + total);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.button.setOnClickListener(v -> {
            String voucherCode = binding.editTextText.getText().toString().trim();
            if ("SANFOOD".equalsIgnoreCase(voucherCode)) {
                calculateCartvoucher();
                Toast.makeText(CartActivity.this, "Voucher applied successfully! You reduce 100% delivery", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(CartActivity.this, "Invalid voucher code", Toast.LENGTH_SHORT).show();
            }

        });
        binding.pay.setOnClickListener(v -> {
            String name = binding.editTextName.getText().toString().trim();
            String gmail = binding.editTextGmail.getText().toString().trim();
            String phone = binding.editTextPhone.getText().toString().trim();
            String address = binding.editTextAddress.getText().toString().trim();
            double totalPrice = Math.round((managmentCart.getTotalFee() + tax + 10) * 100) / 100;

            if (name.isEmpty() || gmail.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(CartActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.length() != 10) {
                Toast.makeText(CartActivity.this, "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<Integer> foodIds = new ArrayList<>();
            ArrayList<String> foodImages = new ArrayList<>();
            ArrayList<String> foodTitle=new ArrayList<>();
            ArrayList<Integer> foodCount=new ArrayList<>();
            ArrayList<Double> foodprice=new ArrayList<>();

            for (Foods food : managmentCart.getListCart()) {
                foodIds.add(food.getId());
                foodImages.add(food.getImagePath());
                foodTitle.add(food.getTitle());
                foodCount.add(food.getNumberInCart());
                foodprice.add(food.getPrice());
            }
            String orderId = generateRandomOrderId();

            // Lưu thông tin vào Firebase
            String userId = mAuth.getCurrentUser().getUid();
            DatabaseReference orderRef = Reference.child("Orders").push();
//            String orderId = orderRef.getKey();
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("iduser", userId);
            orderData.put("orderId", orderId);
            orderData.put("name", name);
            orderData.put("gmail", gmail);
            orderData.put("phone", phone);
            orderData.put("address", address);
            orderData.put("totalprice", totalPrice);
            orderData.put("idfood", foodIds);
            orderData.put("foodImages", foodImages);
            orderData.put("foodTitle", foodTitle);
            orderData.put("foodsoluong", foodCount);
            orderData.put("foodprice", foodprice);
            orderData.put("status", "Chưa xác nhận");


            // Lấy thời gian hiện tại và định dạng nó
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            orderData.put("orderTime", currentTime);

            orderRef.setValue(orderData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    managmentCart.clearCart();
                    initList(); // Update UI
                    calculateCart();

                    finish(); // Đóng activity sau khi đặt hàng thành công
                } else {
                    Toast.makeText(CartActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private String generateRandomOrderId() {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder orderId = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            orderId.append(chars.charAt(index));
        }
        return orderId.toString();
    }
}
