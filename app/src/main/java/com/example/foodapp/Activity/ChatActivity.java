package com.example.foodapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodapp.Adapter.MessageAdapter;
import com.example.foodapp.Domain.Messagermodel;
import com.example.foodapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText message;
    ImageView send, back;
    List<Messagermodel> list;
    MessageAdapter adapter;

    //gửi yêu cầu HTTP
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recycleviewchat);
        message = findViewById(R.id.messager);
        send = findViewById(R.id.send);
        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(list);
        recyclerView.setAdapter(adapter);

        addToChat("Xin chào, bạn có thể hỏi tôi bất cứ thứ gì.", Messagermodel.SENT_BY_BOT);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = message.getText().toString();
                if (question.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
                } else {
                    addToChat(question, Messagermodel.SENT_BY_ME);
                    message.setText("");
                    try {
                        callAPI(question);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        ImageView animatedImageView = findViewById(R.id.imageView15);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.hibot) // replace with your GIF file name
                .into(animatedImageView);

        back = findViewById(R.id.imageView13);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callAPI(String question) throws JSONException {
        addToChat("Đang nhập...", Messagermodel.SENT_BY_BOT);
        // Tạo yêu cầu HTTP để gửi tới API Wit.ai
        Request request = new Request.Builder()
                .url("https://api.wit.ai/message?v=20240605&q=" + question)
                .header("Authorization", "Bearer XFQN573HNES6XMVUP4B73KXAJI3WT7PW")
                .build();
        // Gửi yêu cầu bất đồng bộ và xử lý kết quả trả về
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Thất bại: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONObject entities = jsonResponse.optJSONObject("entities");
                        if (entities != null) {
                            //Tạo đối tượng StringBuilder để lưu trữ kết quả.
                            StringBuilder result = new StringBuilder();
                            // Sử dụng Set để loại bỏ các giá trị trùng lặp
                            Set<String> uniqueValues = new HashSet<>();

                            //Lặp qua khóa tron entities
                            for (Iterator<String> it = entities.keys(); it.hasNext(); ) {
                                // lấy khóa
                                String key = it.next();
                                JSONArray entityArray = entities.getJSONArray(key); //giá mảng của thực thể key
                                for (int i = 0; i < entityArray.length(); i++) {  //chạy qua các giá trị  trong mảng
                                    JSONObject entity = entityArray.getJSONObject(i);
                                    String value = entity.optString("value");
                                    uniqueValues.add(value);
                                }
                            }

                            // Nối các giá trị duy nhất thành chuỗi kết quả
                            for (String value : uniqueValues) {
                                result.append(value).append("\n");
                            }
                            // Thêm phản hồi từ API vào RecyclerView
                            addResponse(result.toString().trim());
                        } else {
                            addResponse("Không tìm thấy thực thể.");
                        }
                    } catch (JSONException e) {
                        addResponse("Lỗi: " + e.getMessage());
                    }
                } else {
                    addResponse("Thất bại: " + response.body().string());
                }
            }
        });
    }

        //phản hồi từ API
    private void addResponse(String response) {
        list.remove(list.size() - 1);
        addToChat(response, Messagermodel.SENT_BY_BOT);
    }

    private void addToChat(String message, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.add(new Messagermodel(message, sentBy));
                //cập nhật recycle view
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });
    }
}
