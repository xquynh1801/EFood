package com.example.efood.model;

import java.io.Serializable;

public class CommentDTO implements Serializable {
    private int id;
    private int rating;
    private String content;
    private int userID;
    private int foodID;
    private byte[] img;

    public CommentDTO(int id, int rating, String content, int userID, int foodID, byte[] img) {
        this.id = id;
        this.rating = rating;
        this.content = content;
        this.userID = userID;
        this.foodID = foodID;
        this.img=img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
