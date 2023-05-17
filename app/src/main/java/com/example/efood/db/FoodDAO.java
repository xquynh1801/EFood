package com.example.efood.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.efood.model.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodDAO extends DBConnection{
    private SQLiteDatabase db;

    public FoodDAO(Context context) {
        super(context);
        db = getReadableDatabase();
    }

    // thêm dữ liệu
    public int InsertFood(Food food) {
        ContentValues values = new ContentValues(); // tạo đối tượng chứa dữ liệu

        // đưa dữ liệu vào đối tượng chứa
        values.put("name", food.getFoodName());
        values.put("price",food.getPrice());
        values.put("description", food.getDesc());
        values.put("image", food.getImage());

        //thực thi insert
        long kq = db.insert("food", null, values);

        //kiểm tra kết quả
        if(kq <= 0) {
            return -1;
        }
        return 1;
    }

    // hiển thị dữ liệu
    public List<Food> getAllFoodToString() {
        List<Food> ls = new ArrayList<>();

        //tạo con trỏ đọc bảng dữ liệu sản phẩm
        Cursor c = db.rawQuery("Select * from food", null);
        c.moveToFirst(); // di chuyển con trỏ về bản ghi đầu tiên

        while(c.isAfterLast() == false) {

            int id = (c.getInt(0));
            String name = (c.getString(1));
            double price = (c.getDouble(2));
            String desc = (c.getString(3));
            byte[] image = c.getBlob(4);
            Food f = new Food(id,name, price, desc, image);
            ls.add(f);
            c.moveToNext(); // di chuyển đến bản ghi tiếp theo
        }
        c.close();
        return ls;
    }

    public Food getFoodById(int id){
        Cursor rs = db.rawQuery("Select * from food where id = ?", new String[]{id+""});
        Food f = new Food();
        if (rs != null && rs.moveToFirst()) {
            f.setId(rs.getInt(0));
            f.setFoodName((rs.getString(1)));
            f.setPrice((rs.getDouble(2)));
            f.setDesc((rs.getString(3)));
            f.setImage((rs.getBlob(4)));
            rs.close();
        }
        return f;
    }

    // sửa dữ liệu
    public int UpdateFood(Food food) {
        ContentValues values = new ContentValues(); // tạo đối tượng chứa dữ liệu

        // đưa dữ liệu vào đối tượng chứa
        values.put("name", food.getFoodName());
        values.put("price",food.getPrice());
        values.put("description", food.getDesc());
        values.put("image", food.getImage());

        //thực thi update
        long kq = db.update("food", values, "id=?", new String[]{food.getId() + ""});

        //kiểm tra kết quả
        if(kq <= 0) {
            return -1;
        }
        return 1;
    }

    // xóa dữ liệu
    public int DeleteFood(int id) {
        int kq = db.delete("food", "id=?", new String[]{id + ""});
        if(kq <= 0) {
            return -1;
        }
        return 1;
    }

}
