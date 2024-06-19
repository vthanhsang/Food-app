package com.example.foodapp.Adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.foodapp.Domain.Slide;
import com.example.foodapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    private List<Slide> slides = new ArrayList<>();
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private int currentPage = 0;
    private boolean isGoingForward = true;

    public SlideAdapter(ViewPager2 viewPager2) {
        this.viewPager2 = viewPager2;
        fetchDataFromFirebase();
        startAutoSlide();
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.setImageView(slides.get(position));
    }

    @Override
    public int getItemCount() {
        return slides.size();
    }

    class SlideViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageslide);
        }

        public void setImageView(Slide slide) {
            Glide.with(itemView.getContext())
                    .load(slide.getImageUrl())
                    .into(imageView);
        }
    }

    private void fetchDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("slide");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                slides.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Slide slide = snapshot.getValue(Slide.class);
                    if (slide != null) {
                        slides.add(slide);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void startAutoSlide() {
        sliderHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (slides.size() > 0) {
                    if (isGoingForward) {
                        currentPage++;
                        if (currentPage == slides.size()) {
                            currentPage--;
                            isGoingForward = false;
                        }
                    } else {
                        currentPage--;
                        if (currentPage < 0) {
                            currentPage++;
                            isGoingForward = true;
                        }
                    }
                    viewPager2.setCurrentItem(currentPage, true);
                }
                sliderHandler.postDelayed(this, 15000);
            }
        }, 15000);
    }
}
