package com.example.efood.activity;//package com.example.gon17.activity;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.example.gon17.R;
//import com.example.gon17.adapter.FoodItemAdapter;
//import com.example.gon17.model.FoodCart;
//import com.example.gon17.model.FoodItem;
//import com.example.gon17.viewmodel.CartViewModel;
//import com.example.gon17.views.CartActivity;
//import com.example.gon17.views.DetailFoodActivity;
//import com.google.android.material.snackbar.Snackbar;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GoToListFoodActivity extends AppCompatActivity implements FoodItemAdapter.FoodClickedListeners {
//
//    private RecyclerView recyclerView;
//    private List<FoodItem> foodItemList;
//    private FoodItemAdapter adapter;
//
//    private CartViewModel viewModel;
//
//    private List<FoodCart> foodCartList;
//
//    private CoordinatorLayout coordinatorLayout;
//
//    private ImageView cartImageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_go_to_list_food);
//
//        initializeVariables();
//        setUpList();
//
//
//        adapter.setFoodItemList(foodItemList);
//        recyclerView.setAdapter(adapter);
//
//        cartImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(GoToListFoodActivity.this, CartActivity.class));
//            }
//        });
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        viewModel.getAllCartItems().observe(this, new Observer<List<FoodCart>>() {
//            @Override
//            public void onChanged(List<FoodCart> foodCarts) {
//                foodCartList.addAll(foodCarts);
//            }
//        });
//    }
//
//    private void setUpList(){
//        foodItemList.add(new FoodItem("Fried Chicken 01","Ngon 01",R.drawable.fried_chicken_01,80000));
//        foodItemList.add(new FoodItem("Fried Chicken 02","Ngon 02",R.drawable.fried_chicken_02,60000));
//        foodItemList.add(new FoodItem("Fried Chicken 03","Ngon 03",R.drawable.fried_chicken_03,50000));
//        foodItemList.add(new FoodItem("Fried Chicken 04","Ngon 04",R.drawable.fried_chicken_04,90000));
//        foodItemList.add(new FoodItem("Fried Chicken 05","Ngon 05",R.drawable.fried_chicken_05,100000));
//
//    }
//
//    private void initializeVariables(){
//
//        cartImageView = findViewById(R.id.cartIv);
//
//        coordinatorLayout = findViewById(R.id.coordinatorLayout);
//        foodCartList = new ArrayList<>();
//        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
//
//        foodItemList = new ArrayList<>();
//        recyclerView = findViewById(R.id.listFoodRecyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//
//
//        adapter = new FoodItemAdapter(this);
//    }
//
//    @Override
//    public void onCardClicked(FoodItem food) {
//        Intent intent = new Intent(GoToListFoodActivity.this, DetailFoodActivity.class);
//        intent.putExtra("foodItem",food);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onAddToCartBtnClicked(FoodItem foodItem) {
//        FoodCart foodCart = new FoodCart();
//        foodCart.setFoodName(foodItem.getFoodName());
//        foodCart.setFoodDecription(foodItem.getFoodDecription());
//        foodCart.setFoodPrice(foodItem.getFoodPrice());
//        foodCart.setFoodImage(foodItem.getFoodImage());
//
//
//        final int[] quantity = {1};
//        final int[] id = new int[1];
//        if(!foodCartList.isEmpty()){
//            for (int i = 0; i < foodCartList.size(); i++) {
//                if (foodCart.getFoodName().equals(foodCartList.get(i).getFoodName())) {
//                    quantity[0] = foodCartList.get(i).getQuantity();
//                    quantity[0]++;
//                    id[0] = foodCartList.get(i).getId();
//                }
//            }
//        }
//
//        if (quantity[0] == 1) {
//            foodCart.setQuantity(quantity[0]);
//            foodCart.setTotalItemPrice(quantity[0] * foodCart.getFoodPrice());
//            viewModel.insertCartItem(foodCart);
//        } else {
//            viewModel.updateQuantity(id[0], quantity[0]);
//            viewModel.updatePrice(id[0], quantity[0] * foodCart.getFoodPrice());
//        }
//        makeSnackBar("Món Ăn Đã Được Thêm");
//    }
//
//    private void makeSnackBar(String msg){
//        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT)
//                .setAction("Giỏ Hàng", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent(GoToListFoodActivity.this, CartActivity.class));
//                    }
//                }).show();
//    }
//}