package com.example.efood.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItem implements Parcelable {

    private int id;
    private String foodName;
    private String foodDescription;
    private byte[] foodImage;
    private double foodPrice;

    public FoodItem(int id, String foodName, String foodDescription, byte[] foodImage, double foodPrice) {
        this.id = id;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodImage = foodImage;
        this.foodPrice = foodPrice;
    }

    protected FoodItem(Parcel in) {
        id = in.readInt();
        foodName = in.readString();
        foodDescription = in.readString();
        foodImage = in.createByteArray();
        foodPrice = in.readDouble();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

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

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public byte[] getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(byte[] foodImage) {
        this.foodImage = foodImage;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(foodName);
        parcel.writeString(foodDescription);
        parcel.writeByteArray(foodImage);
        parcel.writeDouble(foodPrice);
    }
}
