package com.example.foodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;

import org.w3c.dom.Text;

import java.util.List;

public class FoodHistoryAdapter extends RecyclerView.Adapter<FoodHistoryAdapter.FoodViewHolder> {
    private List<String> foodTitles;
    private List<Integer> quantities;
    private List<String> imageUrls;

    private List<Double> price;
    public FoodHistoryAdapter(List<String> foodTitles, List<Integer> quantities, List<String> imageUrls,List<Double> price) {
        this.foodTitles = foodTitles;
        this.quantities = quantities;
        this.imageUrls = imageUrls;
        this.price=price;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodviewholder, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.foodTitle.setText(foodTitles.get(position));
        holder.quantity.setText(String.valueOf(quantities.get(position)));
        holder.price.setText(String.valueOf(price.get(position)+" $"));
        Glide.with(holder.itemView.getContext())
                .load(imageUrls.get(position))
                .into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return foodTitles.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodTitle;
        TextView quantity;
        ImageView foodImage;
        TextView price;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodTitle = itemView.findViewById(R.id.Namefood);
            quantity = itemView.findViewById(R.id.sl);
            foodImage = itemView.findViewById(R.id.imageViewfod);
            price = itemView.findViewById(R.id.pricesp);
        }
    }
}
