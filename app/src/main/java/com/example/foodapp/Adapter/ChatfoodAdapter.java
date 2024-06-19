package com.example.foodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Domain.FoodItem;
import com.example.foodapp.R;

import java.util.List;

public class ChatfoodAdapter extends RecyclerView.Adapter<ChatfoodAdapter.FoodViewHolder> {
    private List<FoodItem> foodItems;

    public ChatfoodAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.bind(foodItem);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public void updateData(List<FoodItem> newFoodItems) {
        this.foodItems.clear();
        this.foodItems.addAll(newFoodItems);
        notifyDataSetChanged();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageFood);
            textView = itemView.findViewById(R.id.textFoodName);
        }

        public void bind(FoodItem foodItem) {
            Glide.with(itemView.getContext())
                    .load(foodItem.getImageUrl())
                    .centerCrop()
                    .into(imageView);
            textView.setText(foodItem.getName());
        }
    }
}
