package com.example.efood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.efood.R;

import java.util.ArrayList;
import java.util.List;

class Photo {
    private int resourceId;

    public Photo(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}

public class PhotoAdapter extends PagerAdapter {

    private Context mContext;
    private List<Photo> mListPhoto;

    public PhotoAdapter(Context mContext) {
        this.mContext = mContext;
        this.mListPhoto = new ArrayList<>();
        this.mListPhoto.add(new Photo(R.drawable.banner1));
        this.mListPhoto.add(new Photo(R.drawable.banner2));
        this.mListPhoto.add(new Photo(R.drawable.banner3));
        this.mListPhoto.add(new Photo(R.drawable.banner4));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo, container, false);
        ImageView imageView = view.findViewById(R.id.banner);

        Photo photo = mListPhoto.get(position);
        if(photo!=null){
            Glide.with(mContext).load(photo.getResourceId()).into(imageView);
        }

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if(mListPhoto!=null){
            return mListPhoto.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
