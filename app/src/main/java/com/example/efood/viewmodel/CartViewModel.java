package com.example.efood.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.efood.model.FoodCart;
import com.example.efood.repository.CartRepo;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private CartRepo cartRepo;



    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepo = new CartRepo(application);
    }


    public LiveData<List<FoodCart>> getAllCartItems(){
        return cartRepo.getAllCartItemsLiveData();
    }

    public List<Integer> getAllFoodIds(){
        List<FoodCart> foodCarts = cartRepo.getAllCartItemsLiveData().getValue();
        List<Integer> foodIds = new ArrayList<>();
        for (FoodCart foodCart : foodCarts) {
            foodIds.add(foodCart.getId());
        }
        return foodIds;
    }

    public void insertCartItem(FoodCart foodCart){
        cartRepo.insertCartItem(foodCart);
    }

    public void updateQuantity(int id, int quantity){
        cartRepo.updateQuantity(id,quantity);
    }

    public void updatePrice(int id, double price){
        cartRepo.updatePrice(id, price);
    }

    public void deleteCartItem(FoodCart foodCart){
        cartRepo.deleteCartItem(foodCart);
    }

    public void deleteAllCartItems(){
        cartRepo.deleteAllCartItems();
    }

}
