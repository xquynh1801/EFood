package com.example.efood.activity.home;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.efood.R;
import com.example.efood.activity.home.fragment.AccountFragment;
import com.example.efood.activity.home.fragment.CartFragment;
import com.example.efood.activity.home.fragment.HomeFragment;
import com.example.efood.activity.home.fragment.OrderFragment;
import com.example.efood.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private OrderFragment orderFragment;
    private AccountFragment accountFragment;

    private FrameLayout frameFrag;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        frameFrag = findViewById(R.id.frameFrag);
        bottomNav = findViewById(R.id.bottomNav);

        homeFragment = new HomeFragment();
        cartFragment = new CartFragment();
        orderFragment = new OrderFragment();
        accountFragment = new AccountFragment();

        Fragment renderFragment = homeFragment;
        int selectedId = R.id.itemHome;
        try{
            String intent_frag = getIntent().getStringExtra("fragment_name");
            if(intent_frag.equals("CART_FRAGMENT")) {
                renderFragment = cartFragment;
                selectedId = R.id.itemCart;
            }
            if(intent_frag.equals("ORDER_FRAGMENT")) {
                renderFragment = orderFragment;
                selectedId = R.id.itemOrder;
            }
            if(intent_frag.equals("ACCOUNT_FRAGMENT")) {
                renderFragment = accountFragment;
                selectedId = R.id.itemUser;
            }
        }catch(Exception e){
            renderFragment = homeFragment;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", (User)getIntent().getSerializableExtra("user"));
        renderFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameFrag, renderFragment).commit();
        bottomNav.setSelectedItemId(selectedId);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.itemHome:
                        homeFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameFrag, homeFragment).commit();
                        return true;
                    case R.id.itemCart:
                        cartFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameFrag, cartFragment).commit();
                        return true;
                    case R.id.itemOrder:
                        orderFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameFrag, orderFragment).commit();
                        return true;
                    case R.id.itemUser:
                        accountFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameFrag, accountFragment).commit();
                        return true;

                }
                return false;
            }
        });
    }
}
