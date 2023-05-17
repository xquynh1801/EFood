package com.example.efood.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.efood.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FoodItemDAO extends DBConnection {
    private SQLiteDatabase db;

    public FoodItemDAO(Context context) {
        super(context);
        db = getReadableDatabase();
    }

    // Retrieve all food items
    public List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItemList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM food", null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int descriptionIndex = cursor.getColumnIndex("description");
            int priceIndex = cursor.getColumnIndex("price");
            int imageIndex = cursor.getColumnIndex("image");

            do {
                int id = cursor.getInt(idIndex);
                String foodName = cursor.getString(nameIndex);
                String foodDescription = cursor.getString(descriptionIndex);
                double foodPrice = cursor.getDouble(priceIndex);
                byte[] foodImage = cursor.getBlob(imageIndex);

                FoodItem foodItem = new FoodItem(id, foodName, foodDescription, foodImage, foodPrice);
                foodItemList.add(foodItem);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return foodItemList;
    }
    public List<FoodItem> searchByName(String key){
        List<FoodItem> foodItemList = new ArrayList<>();
        String whereClause = "name like ?";
        String[] whereArgs = {"%"+key+"%"};
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("food", null, whereClause, whereArgs, null, null, null);
        while (rs!=null && rs.moveToNext()){
            int id= rs.getInt(0);
            String name = rs.getString(1);
            double price = rs.getDouble(2);
            String description = rs.getString(3);
            byte[] img = rs.getBlob(4);
            foodItemList.add(new FoodItem(id, name ,description, img, price));
        }
        return foodItemList;
    }
}
