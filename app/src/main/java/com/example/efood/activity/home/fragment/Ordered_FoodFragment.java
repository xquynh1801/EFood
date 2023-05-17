package com.example.efood.activity.home.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.efood.R;
import com.example.efood.activity.order.RatingActivity;
import com.example.efood.adapter.Ordered_FoodAdapter;
import com.example.efood.db.OrderDB;
import com.example.efood.model.Food;
import com.example.efood.model.Order;
import com.example.efood.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Ordered_FoodFragment extends Fragment implements Ordered_FoodAdapter.Itemlistener{
    private Ordered_FoodAdapter adapter;
    private RecyclerView recyclerView;
    private OrderDB db;
    private User user;
    private Order order;
    private TextView title, total, username, address, time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ordered_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycleView);
        title=view.findViewById(R.id.tvTitle);
        total=view.findViewById(R.id.tvTotal);
        username=view.findViewById(R.id.tvusername);
        address=view.findViewById(R.id.tvAddress);
        time=view.findViewById(R.id.tvTime);

        db=new OrderDB(getContext());
        Bundle bundle = getArguments();
        user = (User)bundle.getSerializable("user");
        order = (Order) bundle.getSerializable("order");
        adapter=new Ordered_FoodAdapter(getContext(), user, order);

        title.setText("Đơn hàng #" + order.getId());
        total.setText("Tổng: " + order.getTotal() + " - Tiền mặt");
        username.setText(user.getFullName() + " - " + user.getPhoneNumber());

        // Lấy vị trí người dùng
        Geocoder geocoder = new Geocoder(getContext());
        String theAddress;
        String[] pos = user.getAddress().split(";");
        try{
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), 1);
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
                theAddress = null;
            }
        } catch (IOException e) {
            theAddress = null;
        }
        address.setText(theAddress);

        time.setText(order.getDate());

        Map<Food, Integer> list=db.getFoodByOrderID(order.getId());
        List<Food> foodList = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        for(Map.Entry<Food,Integer> entry: list.entrySet()){
            foodList.add(entry.getKey());
            quantity.add(entry.getValue());
        }

        System.out.println("====================================>" + list.size());
        adapter.setlist(foodList, quantity);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
    }
    @Override
    public void onItemClick(View view, int pos) {
        Food food = adapter.getItem(pos);
        Toast.makeText(getContext(), "===========> thanh cong", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), RatingActivity.class);
        intent.putExtra("food", food);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();

        Map<Food, Integer> list=db.getFoodByOrderID(order.getId());
        List<Food> foodList = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        for(Map.Entry<Food,Integer> entry: list.entrySet()){
            foodList.add(entry.getKey());
            quantity.add(entry.getValue());
        }
        adapter.setlist(foodList, quantity);
    }
}
