package com.example.foodapp.Domain;

public class Slide {
    private String imageUrl;

    public Slide() {
        // Default constructor required for calls to DataSnapshot.getValue(Slide.class)
    }

    public Slide(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
