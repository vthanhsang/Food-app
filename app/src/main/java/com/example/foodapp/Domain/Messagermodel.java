package com.example.foodapp.Domain;

public class Messagermodel {


   public static String SENT_BY_ME="me";
   public static String SENT_BY_BOT="bot";

    String message;
    String sentby;

    public Messagermodel(String message, String sentby) {
        this.message = message;
        this.sentby = sentby;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentby() {
        return sentby;
    }

    public void setSentby(String sentby) {
        this.sentby = sentby;
    }
}
