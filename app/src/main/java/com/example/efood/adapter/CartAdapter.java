package com.example.efood.adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efood.R;
import com.example.efood.model.FoodCart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private CartClickedListeners cartClickedListeners;
    private List<FoodCart> foodCartList;

    public CartAdapter(CartClickedListeners cartClickedListeners){
        this.cartClickedListeners = cartClickedListeners;
    }

    public void setFoodCartList(List<FoodCart> foodCartList){
        this.foodCartList = foodCartList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_cart_item , parent , false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        FoodCart foodCart = foodCartList.get(position);
        holder.foodImageView.setImageBitmap(BitmapFactory.decodeByteArray(foodCart.getFoodImage(), 0, foodCart.getFoodImage().length));
        holder.foodNameTv.setText(foodCart.getFoodName());
        holder.foodDescriptionTv.setText(foodCart.getFoodDescription());
        holder.foodQuantity.setText(foodCart.getQuantity() + "");
        holder.foodPriceTv.setText("đ " + String.valueOf(foodCart.getTotalItemPrice()));


        holder.deleteFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartClickedListeners.onDeleteClicked(foodCart);
            }
        });

        holder.addQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartClickedListeners.onPlusClicked(foodCart);
            }
        });

        holder.minusQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartClickedListeners.onMinusClicked(foodCart);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (foodCartList == null) {
            return 0;
        } else {
            return foodCartList.size();
        }
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        private TextView foodNameTv, foodDescriptionTv, foodPriceTv, foodQuantity;
        private ImageView deleteFoodBtn;
        private ImageView foodImageView;
        private ImageButton addQuantityBtn, minusQuantityBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            foodNameTv = itemView.findViewById(R.id.eachCartItemName);
            foodDescriptionTv = itemView.findViewById(R.id.eachCartItemDescriptionTv);
            foodPriceTv = itemView.findViewById(R.id.eachCartItemPriceTv);
            deleteFoodBtn = itemView.findViewById(R.id.eachCartItemDeleteBtn);
            foodImageView = itemView.findViewById(R.id.eachCartItemIV);
            foodQuantity = itemView.findViewById(R.id.eachCartItemQuantityTV);
            addQuantityBtn = itemView.findViewById(R.id.eachCartItemAddQuantityBtn);
            minusQuantityBtn = itemView.findViewById(R.id.eachCartItemMinusQuantityBtn);
        }
    }

    public interface CartClickedListeners{
        void onDeleteClicked(FoodCart foodCart);

        void onPlusClicked(FoodCart foodCart);

        void onMinusClicked(FoodCart foodCart);
    }
}
