package com.example.foodapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.foodapp.Adapter.ResponseAdapter;
import com.example.foodapp.Domain.Response;
import com.example.foodapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResponActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView back;
    private ResponseAdapter adapter;
    private List<Response> responseList;
    private Set<String> adminResponseIds;
    private DatabaseReference responsesRef;
    private DatabaseReference adminResponsesRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respon);

        recyclerView = findViewById(R.id.recycleviewrespon);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize response list and adminResponseIds
        responseList = new ArrayList<>();
        adminResponseIds = new HashSet<>();

        // Firebase database references
        responsesRef = FirebaseDatabase.getInstance().getReference("Responses");
        adminResponsesRef = FirebaseDatabase.getInstance().getReference("ResponseAdmin");

        // Set the adapter
        adapter = new ResponseAdapter(responseList);
        recyclerView.setAdapter(adapter);
         back=findViewById(R.id.backProfilee);
         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });

        // Fetch data from Firebase
        fetchAdminResponses();
    }

    private void fetchAdminResponses() {
        adminResponsesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminResponseIds.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String idrespon = postSnapshot.child("idrespon").getValue(String.class);
                    if (idrespon != null) {
                        adminResponseIds.add(idrespon);
                    }
                }
                fetchResponses();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void fetchResponses() {
        responsesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                responseList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Response response = postSnapshot.getValue(Response.class);
                    if (response != null && !adminResponseIds.contains(response.getIdrespon())) {
                        responseList.add(response);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}
