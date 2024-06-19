package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.Helper.ChangeNumberItemsListener;
import com.example.foodapp.Helper.ManagmentCart;
import com.example.foodapp.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    public CartAdapter(ArrayList<Foods> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        managmentCart=new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    ArrayList<Foods> list;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;
    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent,false);

        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
    holder.title.setText(list.get(position).getTitle());
    holder.feeEachItem.setText("$"+ (list.get(position).getNumberInCart()*list.get(position).getPrice()));
    holder.totalEachItem.setText(list.get(position).getNumberInCart()+" * $" +(list.get(position).getPrice()));
    holder.num.setText(list.get(position).getNumberInCart()+"");

        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(),new RoundedCorners(30))
                .into(holder.pic);

        holder.plusitem.setOnClickListener(v -> managmentCart.plusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));
        holder.minusitem.setOnClickListener(v -> managmentCart.minusNumberItem(list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class viewholder extends  RecyclerView.ViewHolder{
        TextView title,feeEachItem,plusitem,minusitem;
        ImageView pic;
        TextView totalEachItem,num;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.Titletxt);
            pic=itemView.findViewById(R.id.Pic);
            feeEachItem=itemView.findViewById(R.id.feeEachItem);
            plusitem=itemView.findViewById(R.id.plusCartTxt);
            minusitem=itemView.findViewById(R.id.minusCartTxt);
            totalEachItem=itemView.findViewById(R.id.totalEachitem);
            num=itemView.findViewById(R.id.numberitemtxt);



        }
    }
}
