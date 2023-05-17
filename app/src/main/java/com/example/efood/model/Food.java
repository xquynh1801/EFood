package com.example.efood.model;

import java.io.Serializable;

public class Food implements Serializable {
    private int id;
    private String foodName;
    private double price;
    private String desc;
    private byte[] image;


    public Food(int id, String foodName, double price, String desc, byte[] image) {
        this.id = id;
        this.foodName = foodName;
        this.price = price;
        this.desc = desc;
        this.image = image;
    }

    public Food() {
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return foodName + " - " + price + " - " + desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
