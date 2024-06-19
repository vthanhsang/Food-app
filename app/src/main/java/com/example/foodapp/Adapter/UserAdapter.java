package com.example.foodapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Domain.User;
import com.example.foodapp.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private OnDeleteUserClickListener onDeleteUserClickListener;

    public UserAdapter(List<User> userList, OnDeleteUserClickListener onDeleteUserClickListener) {
        this.userList = userList;
        this.onDeleteUserClickListener = onDeleteUserClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view, onDeleteUserClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user, position);  // Pass position to bind method
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
//        private TextView nameTextView;
        private TextView emailTextView;
        private TextView passwordTextView;
        private Button deleteButton;
        private TextView sttTxt;

        public UserViewHolder(@NonNull View itemView, OnDeleteUserClickListener onDeleteUserClickListener) {
            super(itemView);
//            nameTextView = itemView.findViewById(R.id.name);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            passwordTextView = itemView.findViewById(R.id.passwordTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            sttTxt = itemView.findViewById(R.id.sttTxt);

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteUserClickListener.onDeleteUserClick(position);
                }
            });
        }

        public void bind(User user, int position) {
//            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
            passwordTextView.setText(user.getPassword());
            sttTxt.setText(String.valueOf(position + 1));  // Set stt starting from 1
        }
    }

    public interface OnDeleteUserClickListener {
        void onDeleteUserClick(int position);
    }
}
