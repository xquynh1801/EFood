package com.example.efood.adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efood.R;
import com.example.efood.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {

    private List<FoodItem> foodItemList;
    private FoodClickedListeners foodClickedListeners;

    public void setClickFoodItem(FoodClickedListeners foodClickedListeners) {
        this.foodClickedListeners = foodClickedListeners;
    }

    public void setFoodItemList(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
        notifyDataSetChanged();
    }
    public FoodItemAdapter() {
        foodItemList = new ArrayList<>();
    }

    public FoodItem getItem(int pos){
        return foodItemList.get(pos);
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_food, parent, false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
        holder.foodNameTv.setText(foodItem.getFoodName());
        holder.foodDescriptionTv.setText(foodItem.getFoodDescription());
        holder.foodPriceTv.setText("đ " + String.valueOf(foodItem.getFoodPrice()));
        // Set image from byte array
        holder.foodImageView.setImageBitmap(BitmapFactory.decodeByteArray(foodItem.getFoodImage(), 0, foodItem.getFoodImage().length));

        holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodClickedListeners.onAddToCartBtnClicked(foodItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(foodItemList != null)
            return foodItemList.size();
        return 0;
    }

    public class FoodItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView foodImageView, addToCartBtn;
        private TextView foodNameTv, foodDescriptionTv, foodPriceTv;

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);

            addToCartBtn = itemView.findViewById(R.id.eachFoodAddToCartBtn);
            foodNameTv = itemView.findViewById(R.id.eachFoodName);
            foodImageView = itemView.findViewById(R.id.eachFoodIv);
            foodDescriptionTv = itemView.findViewById(R.id.eachFoodDescriptionTv);
            foodPriceTv = itemView.findViewById(R.id.eachFoodPriceTv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(foodClickedListeners != null){
                foodClickedListeners.onItemClick(view,getAdapterPosition());// => view mon an
            }
        }
    }

    public interface FoodClickedListeners {
        void onAddToCartBtnClicked(FoodItem foodItem);
        void onItemClick(View view, int pos);
    }
}
