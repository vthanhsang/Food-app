package com.example.foodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Domain.Messagermodel;
import com.example.foodapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHolder> {
    public MessageAdapter(List<Messagermodel> moderlist) {
        this.moderlist = moderlist;
    }

    List<Messagermodel> moderlist;
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,null);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Messagermodel model=moderlist.get(position);
        if(model.getSentby().equals(Messagermodel.SENT_BY_ME)){
            holder.leftChat.setVisibility(View.GONE);
            holder.rightChat.setVisibility(View.VISIBLE);
            holder.righttext.setText(model.getMessage());
        }else {
            holder.rightChat.setVisibility(View.GONE);
            holder.leftChat.setVisibility(View.VISIBLE);
            holder.lefttext.setText(model.getMessage());
            Glide.with(holder.itemView.getContext())
                    .asGif()
                    .load(R.drawable.hibot) // replace with your GIF file name
                    .into(holder.gif);
        }
    }

    @Override
    public int getItemCount() {
        return moderlist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout leftChat,rightChat;
        TextView lefttext,righttext;
        ImageView avata,gif;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            leftChat=itemView.findViewById(R.id.leftchat);
            rightChat=itemView.findViewById(R.id.rightchat);
            lefttext=itemView.findViewById(R.id.lefttext);
            righttext=itemView.findViewById(R.id.righttext);
            avata = itemView.findViewById(R.id.avatara);
            gif=itemView.findViewById(R.id.imageView16);

        }
    }
}
