package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Activity.DetailActivity;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.Domain.Lovefood;
import com.example.foodapp.R;

import java.util.ArrayList;
import java.util.List;

public class LovefoodAdapter extends RecyclerView.Adapter<LovefoodAdapter.LovefoodViewHolder> {
    private Context context;
    private List<Lovefood> lovefoodList;




    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }

    public LovefoodAdapter(Context context, List<Lovefood> lovefoodList) {
        this.context = context;
        this.lovefoodList = lovefoodList;
    }

    @NonNull
    @Override
    public LovefoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_love_food, parent, false);
        return new LovefoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LovefoodViewHolder holder, int position) {
        Lovefood lovefood = lovefoodList.get(position);
        holder.titleTxt.setText(lovefood.getTitlefood());
        holder.priceTxt.setText("$" + lovefood.getPricefood());
        Glide.with(context).load(lovefood.getImgfood()).into(holder.imgFood);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            }
        });


      

    }

    @Override
    public int getItemCount() {
        return lovefoodList.size();
    }

    public class LovefoodViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt;
        ImageView imgFood;
        Button deleteButton;

        public LovefoodViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.Namee);
            priceTxt = itemView.findViewById(R.id.Pricefood);
            imgFood = itemView.findViewById(R.id.imgfood);
            deleteButton = itemView.findViewById(R.id.deletefood);
        }
    }
}
