package com.example.efood.activity.home.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.efood.R;
import com.example.efood.adapter.FoodItemAdapter;
import com.example.efood.adapter.PhotoAdapter;
import com.example.efood.db.FoodItemDAO;
import com.example.efood.model.FoodCart;
import com.example.efood.model.FoodItem;
import com.example.efood.model.User;
import com.example.efood.viewmodel.CartViewModel;
import com.example.efood.views.DetailFoodActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment implements FoodItemAdapter.FoodClickedListeners {

    private TextView lblWelcome;
    private TextView lblPosDetails;
    private SearchView searchView;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;

    private RecyclerView recyclerView;
    private FoodItemAdapter adapter;

    private FoodItemDAO foodItemDAO;
//    private List<FoodItem> foodItemList;

    private CartViewModel viewModel;
    private List<FoodCart> foodCartList;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        user = (User)bundle.getSerializable("user");
        String[] latlongPos = user.getAddress().split(";");
        String theAddress;
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latlongPos[0]),
                    Double.parseDouble(latlongPos[1]), 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                int n = address.getMaxAddressLineIndex();
                for (int i=0; i<=n; i++) {
                    if (i!=0)
                        sb.append(", ");
                    sb.append(address.getAddressLine(i));
                }
                theAddress = sb.toString();
            } else {
                theAddress = "Thanh Xuân Nam, Hà Nội";
            }
        } catch (IOException e) {
            theAddress = "Thanh Xuân Nam, Hà Nội";
        }
        lblPosDetails = view.findViewById(R.id.lblPosDetails);
        lblPosDetails.setText(theAddress);
        viewPager = view.findViewById(R.id.viewPager);
        circleIndicator = view.findViewById(R.id.circle);

        photoAdapter = new PhotoAdapter(getContext());
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        recyclerView = view.findViewById(R.id.listFoodRecyclerView);
        searchView = view.findViewById(R.id.search);
        adapter=new FoodItemAdapter();
        foodItemDAO=new FoodItemDAO(getContext());
        List<FoodItem> foodItemList = foodItemDAO.getAllFoodItems();
        adapter.setFoodItemList(foodItemList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        adapter.setClickFoodItem(this);

        searchView.setQueryHint("Bạn muốn ăn gì nào?");

        for(FoodItem f : foodItemList){
                System.out.println("================ foodItemList: " + f.getFoodName());
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println("=========================== s: " + s);
                List<FoodItem> foodItems = new ArrayList<>();
                for(FoodItem f:foodItemList){
                    if(f.getFoodName().toLowerCase().contains(s.toLowerCase())){
                        System.out.println("========================== name: " + f.getFoodName());
                        foodItems.add(f);
                    }
                }
//                foodItemList = foodItemDAO.searchByName(s);
                adapter.setFoodItemList(foodItems);
                return true;
            }
        });

        foodCartList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

        viewModel.getAllCartItems().observe(getViewLifecycleOwner(), new Observer<List<FoodCart>>() {
            @Override
            public void onChanged(List<FoodCart> foodCarts) {
                foodCartList.clear();
                foodCartList.addAll(foodCarts);
            }
        });
    }

    @Override
    public void onAddToCartBtnClicked(FoodItem foodItem) {
        FoodCart foodCart = new FoodCart();
        foodCart.setFoodId(foodItem.getId());
        foodCart.setFoodName(foodItem.getFoodName());
        foodCart.setFoodDescription(foodItem.getFoodDescription());
        foodCart.setFoodPrice(foodItem.getFoodPrice());
        foodCart.setFoodImage(foodItem.getFoodImage());

        int quantity = 1;
        int id = -1;
        // Check if the food item already exists in the cart
        for (FoodCart cartItem : foodCartList) {
            if (cartItem.getFoodName().equals(foodItem.getFoodName())) {
                quantity = cartItem.getQuantity() + 1;
                id = cartItem.getId();
                break;
            }
        }

        if (id == -1) {
            // The food item is not in the cart, so insert it
            foodCart.setQuantity(quantity);
            foodCart.setTotalItemPrice(quantity * foodCart.getFoodPrice());
            viewModel.insertCartItem(foodCart);
        } else {
            // The food item is already in the cart, so update its quantity and total price
            viewModel.updateQuantity(id, quantity);
            viewModel.updatePrice(id, quantity * foodCart.getFoodPrice());
        }

        Snackbar.make(recyclerView, "Món ăn đã được thêm", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int pos) {
        FoodItem foodItem = adapter.getItem(pos);
        Intent intent = new Intent(getContext(), DetailFoodActivity.class);
        intent.putExtra("foodItem", foodItem);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        List<FoodItem> foodItemList = foodItemDAO.getAllFoodItems();
        adapter.setFoodItemList(foodItemList);
    }

}