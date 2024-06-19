package com.example.foodapp.Domain;

public class FoodItem {
    private int id; // Thêm trường Id
    private String imageUrl;
    private String name;

    public FoodItem() {
        // Required empty public constructor
    }

    public FoodItem(int id, String imageUrl, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
