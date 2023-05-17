package com.example.efood.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.efood.R;

import java.util.List;

public class AdapterAdmin extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Food> list;

    public AdapterAdmin(Context context, int layout, List<Food> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout, null);
        Food food = list.get(position);

        TextView name = convertView.findViewById(R.id.textViewName);
        TextView price = convertView.findViewById(R.id.textViewPrice);
        TextView desc = convertView.findViewById(R.id.textViewDesc);
        ImageView image = convertView.findViewById(R.id.imageItemAdmin);

        name.setText(food.getFoodName());
        price.setText(String.valueOf(food.getPrice()));
        desc.setText(food.getDesc());

        byte[] hinh = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
        image.setImageBitmap(bitmap);
        return convertView;
    }
}
