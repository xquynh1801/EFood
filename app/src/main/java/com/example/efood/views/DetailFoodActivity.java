package com.example.efood.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efood.R;
import com.example.efood.adapter.Cmt_Detail_FoodAdapter;
import com.example.efood.db.CommentDB;
import com.example.efood.db.FoodDAO;
import com.example.efood.db.UserDB;
import com.example.efood.model.Comment;
import com.example.efood.model.CommentDTO;
import com.example.efood.model.Food;
import com.example.efood.model.FoodCart;
import com.example.efood.model.FoodItem;
import com.example.efood.model.User;
import com.example.efood.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailFoodActivity extends AppCompatActivity{

    private ImageView foodImageView;
    private TextView foodNameTV, foodDescriptionTV, foodPriceTV;
    private AppCompatButton addToCartBtn;
    private FoodItem food;
    private Food f;
    private User user;
    private CartViewModel viewModel;
    private List<FoodCart> foodCartList;
    private RecyclerView recyclerView;
    private Cmt_Detail_FoodAdapter cmt_detail_foodAdapter;

    private CommentDB commentDB;
    private FoodDAO foodDAO;
    private UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        this.user = (User)getIntent().getSerializableExtra("user");
        food = getIntent().getParcelableExtra("foodItem");
        initializeVariables();
        commentDB = new CommentDB(getApplicationContext());
        foodDAO = new FoodDAO(getApplicationContext());
        userDB = new UserDB(getApplicationContext());

        f = new Food();
        f.setFoodName(food.getFoodName());
        f.setId(food.getId());
        f.setDesc(food.getFoodDescription());
        f.setImage(food.getFoodImage());
        f.setPrice(food.getFoodPrice());

        cmt_detail_foodAdapter = new Cmt_Detail_FoodAdapter(getApplicationContext(), user, f);

        List<CommentDTO> commentDTOS = commentDB.getCommentsByFood(food.getId());
        List<Comment> comments = new ArrayList<>();
        for(CommentDTO commentDTO:commentDTOS){
            Comment c = new Comment();
            c.setId(commentDTO.getId());
            c.setRating(commentDTO.getRating());
            c.setContent(commentDTO.getContent());
            c.setFood(foodDAO.getFoodById(commentDTO.getFoodID()));
            c.setUser(userDB.selectUserById(commentDTO.getUserID()));
            c.setImg(commentDTO.getImg());
            comments.add(c);
        }

        cmt_detail_foodAdapter.setlist(comments);
        LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(cmt_detail_foodAdapter);

        viewModel.getAllCartItems().observe(this, new Observer<List<FoodCart>>() {
            @Override
            public void onChanged(List<FoodCart> foodCarts) {
                foodCartList.addAll(foodCarts);
            }
        });

        if(food != null){
            setDataToWidgets();
        }


        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertToRoom();
            }
        });
    }

    private void insertToRoom(){
        FoodCart foodCart = new FoodCart();
        foodCart.setFoodName(food.getFoodName());
        foodCart.setFoodDescription(food.getFoodDescription());
        foodCart.setFoodPrice(food.getFoodPrice());
        foodCart.setFoodImage(food.getFoodImage());


        final int[] quantity = {1};
        final int[] id = new int[1];
        if(!foodCartList.isEmpty()){
            for (int i = 0; i < foodCartList.size(); i++) {
                if (foodCart.getFoodName().equals(foodCartList.get(i).getFoodName())) {
                    quantity[0] = foodCartList.get(i).getQuantity();
                    quantity[0]++;
                    id[0] = foodCartList.get(i).getId();
                }
            }
        }

        if (quantity[0] == 1) {
            foodCart.setQuantity(quantity[0]);
            foodCart.setTotalItemPrice(quantity[0] * foodCart.getFoodPrice());
            viewModel.insertCartItem(foodCart);
        } else {
            viewModel.updateQuantity(id[0], quantity[0]);
            viewModel.updatePrice(id[0], quantity[0] * foodCart.getFoodPrice());
        }

//        startActivity(new Intent(DetailFoodActivity.this, CartActivity.class));


    }

    private void setDataToWidgets() {
        foodNameTV.setText(food.getFoodName());
        foodDescriptionTV.setText(food.getFoodDescription());
        foodPriceTV.setText("đ" + String.valueOf(food.getFoodPrice()));

        byte[] foodImageByteArray = food.getFoodImage();
        Bitmap foodImageBitmap = BitmapFactory.decodeByteArray(foodImageByteArray, 0, foodImageByteArray.length);
        foodImageView.setImageBitmap(foodImageBitmap);
    }

    private void initializeVariables() {

        foodCartList = new ArrayList<>();
        foodImageView = findViewById(R.id.detailActivityFoodIV);
        foodNameTV = findViewById(R.id.detailActivityFoodNameTv);
        foodDescriptionTV = findViewById(R.id.detailActivityFoodDescriptionTv);
        foodPriceTV = findViewById(R.id.detailActivityFoodPriceTv);
        addToCartBtn = findViewById(R.id.detailActivityAddToCartBtn);
        recyclerView = findViewById(R.id.recycleView);

        viewModel = new ViewModelProvider(this).get(CartViewModel.class);

    }
}