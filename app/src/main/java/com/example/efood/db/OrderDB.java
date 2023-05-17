package com.example.efood.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.efood.model.Food;
import com.example.efood.model.Order;
import com.example.efood.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderDB extends DBConnection{
    public OrderDB(Context context) {
        super(context);
    }
    public List<Order> getAll(User user){
        List<Order> list=new ArrayList<>();
        String sql="select t.id,t.date,t.status,t.isPaid,t.note,t.total " +
                "from orders t " +
                "where (t.userID=?)";

        SQLiteDatabase st=getReadableDatabase();
        String[] agrs={""+user.getId()+""};
        Cursor rs=st.rawQuery(sql,agrs);
        while(rs!=null && rs.moveToNext()){
            list.add(new Order(rs.getInt(0),rs.getString(1),rs.getString(2),
                                rs.getInt(3),rs.getString(4),rs.getDouble(5), user));
        }
        rs.close();
        return list;
    }

    public long createOrder(User user, List<Food> food, List<Integer> quantity){

        double total = 0;
        for(int i=0; i<food.size(); i++)
            total += food.get(i).getPrice()*quantity.get(i);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        // insert into order table
        ContentValues values = new ContentValues();
        values.put("date", currentDate);
        values.put("status", "Chờ xác nhận");
        values.put("isPaid", 0);
        values.put("note", "");
        values.put("total", total);
        values.put("userID", user.getId());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long res = sqLiteDatabase.insert("orders", null, values);

        if(res!=-1){
            for(int i=0; i<food.size(); i++){
                ContentValues values2 = new ContentValues();
                values2.put("quantity", quantity.get(i));
                values2.put("orderID", res);
                values2.put("foodID", food.get(i).getId());
                return sqLiteDatabase.insert("ordered_food", null, values2);
            }
        }
        return -1;
    }

    public Map<Food, Integer> getFoodByOrderID(int orderID){
        System.out.println("=====> orderID: "+orderID);
        Map<Food, Integer> foodList = new HashMap<>();
        String sql="select f.*, o.quantity " +
                "from food f " +
                "join ordered_food o " +
                "on o.foodID = f.id " +
                "where o.orderID = ?";
        SQLiteDatabase st=getReadableDatabase();
        String[] agrs={""+orderID+""};
        Cursor rs=st.rawQuery(sql,agrs);
        while(rs!=null && rs.moveToNext()){
            foodList.put(new Food(rs.getInt(0),rs.getString(1),rs.getDouble(2),
                    rs.getString(3),rs.getBlob(4)), rs.getInt(5));
        }
        rs.close();
        return foodList;
    }
}
