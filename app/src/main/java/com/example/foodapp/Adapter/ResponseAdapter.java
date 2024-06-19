package com.example.foodapp.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Domain.Response;
import com.example.foodapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ViewHolder> {
    private List<Response> responseList;

    public ResponseAdapter(List<Response> responseList) {
        this.responseList = responseList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView iduserTextView, noidung;
        public EditText inforEditText;
        public TextView datetimeTextView;
        public Button replyButton;

        public ViewHolder(View itemView) {
            super(itemView);
            iduserTextView = itemView.findViewById(R.id.Iduser);
            noidung = itemView.findViewById(R.id.noidung);
            inforEditText = itemView.findViewById(R.id.noidungrespon); // đây là nội dung để admin nhập vào và ấn gửi
            datetimeTextView = itemView.findViewById(R.id.time);
            replyButton = itemView.findViewById(R.id.gui);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_respon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Response response = responseList.get(position);
        holder.iduserTextView.setText(response.getIduser());
        holder.noidung.setText(response.getInfor());
        holder.datetimeTextView.setText(response.getDatetime());

        holder.replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String responseId = response.getIdrespon();
                String responseText = holder.inforEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(responseText)) {
                    sendReplyToFirebase(v.getContext(), responseId, responseText);
                } else {
                    Toast.makeText(v.getContext(), "Nhập nội dung phản hồi trước khi gửi", Toast.LENGTH_SHORT).show();
                }
            }

            private void sendReplyToFirebase(Context context, String responseId, String responseText) {
                DatabaseReference adminResponseRef = FirebaseDatabase.getInstance().getReference("ResponseAdmin").push();
                Map<String, Object> adminReplyData = new HashMap<>();
                adminReplyData.put("idrespon", responseId);
                adminReplyData.put("inforadmin", responseText);
                adminResponseRef.setValue(adminReplyData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Phản hồi đã được gửi", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Gửi phản hồi thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }
}
