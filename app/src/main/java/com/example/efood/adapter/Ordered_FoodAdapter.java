package com.example.efood.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efood.R;
import com.example.efood.activity.order.RatingActivity;
import com.example.efood.model.Food;
import com.example.efood.model.Order;
import com.example.efood.model.User;

import java.util.ArrayList;
import java.util.List;

public class Ordered_FoodAdapter extends RecyclerView.Adapter<Ordered_FoodAdapter.HomeViewHolder>{

    private Itemlistener itemlistener;
    private List<Food> foodList;
    private List<Integer> quantity;
    private User user;
    private Order order;
    private Context context;

    public void setClickListener(Itemlistener itemlistener){
        this.itemlistener=itemlistener;
    }

    public Ordered_FoodAdapter(Context context, User user, Order order) {
        this.context = context;
        this.user = user;
        this.order = order;
        foodList = new ArrayList<>();
        quantity = new ArrayList<>();
    }

    public void setlist(List<Food> foodList, List<Integer> quantity){
        this.foodList = foodList;
        this.quantity = quantity;
        notifyDataSetChanged();
    }
    public Food getItem(int pos){
        return foodList.get(pos);
    }
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_ordered, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Food food = foodList.get(position);
        if(food == null)
            return;
//        holder.img.setImageResource();
        holder.name.setText(food.getFoodName());
        holder.price.setText("Đơn giá: " + String.valueOf(food.getPrice()));
        holder.quantity.setText("Số lượng: " + String.valueOf(quantity.get(position))); //-> lấy quantity từ bảng ordered_food+
        // chuyen byte[] --> bitmap
        byte[] hinh = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
        holder.img.setImageBitmap(bitmap);

        if(order.getStatus().equalsIgnoreCase("Đã hoàn thành")){
            holder.btdanhgia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RatingActivity.class);
                    intent.putExtra("food", food);
                    intent.putExtra("user", user);
                    context.startActivity(intent);
                }
            });
        }
        else {
            holder.btdanhgia.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if(foodList!= null)
            return foodList.size();
        return 0;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name, price, quantity;
        private ImageView img;
        private Button btdanhgia;
        public HomeViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.tvName);
            price = view.findViewById(R.id.tvPrice);
            quantity = view.findViewById(R.id.tvQuantity);
            btdanhgia = view.findViewById(R.id.btdanhgia);
            img = view.findViewById(R.id.img);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == btdanhgia){
                itemlistener.onItemClick(view,getAdapterPosition());// => rating
            }
        }
    }
    public interface Itemlistener{
        void onItemClick(View view, int pos);
    }
}
