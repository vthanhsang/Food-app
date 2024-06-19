package com.example.foodapp.Domain;

import java.util.List;

//public class OrderHistory {
//    private String orderId;
//    private String orderTime;
//    private int totalPrice;
//    private String imageUrl;
//    private String foodTitle;
//
//    private int soluong;
//
//    public int getSoluong() {
//        return soluong;
//    }
//
//    public void setSoluong(int soluong) {
//        this.soluong = soluong;
//    }
//
//    public OrderHistory(String orderId, String orderTime, int totalPrice, String imageUrl, String foodTitle, int soluong) {
//        this.orderId = orderId;
//        this.orderTime = orderTime;
//        this.totalPrice = totalPrice;
//        this.imageUrl = imageUrl;
//        this.foodTitle = foodTitle;
//        this.soluong=soluong;
//    }
//
//    public String getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(String orderId) {
//        this.orderId = orderId;
//    }
//
//    public String getOrderTime() {
//        return orderTime;
//    }
//
//    public void setOrderTime(String orderTime) {
//        this.orderTime = orderTime;
//    }
//
//    public int getTotalPrice() {
//        return totalPrice;
//    }
//
//    public void setTotalPrice(int totalPrice) {
//        this.totalPrice = totalPrice;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public String getFoodTitle() {
//        return foodTitle;
//    }
//
//    public void setFoodTitle(String foodTitle) {
//        this.foodTitle = foodTitle;
//    }
//}
public class OrderHistory {
    private String orderId;
    private String orderTime;
    private int totalPrice;
    private List<String> imageUrls;
    private List<String> foodTitles;
    private List<Integer> soluongs;

    private List<Double> price;
    private String status;

    public OrderHistory(String orderId, String orderTime, int totalPrice, List<String> imageUrls, List<String> foodTitles, List<Integer> soluongs,List<Double> price) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.totalPrice = totalPrice;
        this.imageUrls = imageUrls;
        this.foodTitles = foodTitles;
        this.soluongs = soluongs;
        this.price=price;
    }
    public OrderHistory(String orderId, String orderTime, int totalPrice, List<String> imageUrls, List<String> foodTitles, List<Integer> soluongs,List<Double> price,String status) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.totalPrice = totalPrice;
        this.imageUrls = imageUrls;
        this.foodTitles = foodTitles;
        this.soluongs = soluongs;
        this.price=price;
        this.status=status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getFoodTitles() {
        return foodTitles;
    }

    public void setFoodTitles(List<String> foodTitles) {
        this.foodTitles = foodTitles;
    }

    public List<Integer> getSoluongs() {
        return soluongs;
    }

    public void setSoluongs(List<Integer> soluongs) {
        this.soluongs = soluongs;
    }

    public List<Double> getPrice() {
        return price;
    }

    public void setPrice(List<Double> price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}