package com.example.efood.activity.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.efood.R;
import com.example.efood.activity.home.HomeActivity;
import com.example.efood.activity.home.fragment.Ordered_FoodFragment;
import com.example.efood.model.Order;
import com.example.efood.model.User;

public class Ordered_FoodActivity extends AppCompatActivity {
    private Ordered_FoodFragment ordered_foodFragment;
    private FrameLayout frameFrag;
    private User user;
    private Order order;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_food);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Chi tiết đơn hàng");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        order = (Order) intent.getSerializableExtra("order");
        frameFrag = findViewById(R.id.frameFrag);
        ordered_foodFragment = new Ordered_FoodFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putSerializable("order", order);
        ordered_foodFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameFrag, ordered_foodFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

                // Call account fragment
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("user",this.user);
                intent.putExtra("fragment_name", "ORDER_FRAGMENT");
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}