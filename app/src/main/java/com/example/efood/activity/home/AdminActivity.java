package com.example.efood.activity.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.efood.R;
import com.example.efood.db.FoodDAO;
import com.example.efood.model.AdapterAdmin;
import com.example.efood.model.Food;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private EditText editName, editPrice, editDesc;
    private Button btnThem, btnSua, btnXoa, btnHienthi, btnChoose;

    private ImageView imageFood;
    ListView listView;
    AdapterAdmin arrayAdapter;
    FoodDAO foodDAO;
    List<Food> list = new ArrayList<>();
    Context context;

    int REQUEST_CODE_CHOOSE = 123;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // ánh xạ
        btnChoose = findViewById(R.id.btnChoose);
        imageFood = findViewById(R.id.imageFood);
        editName = findViewById(R.id.editName);
        editPrice = findViewById(R.id.editPrice);
        editDesc = findViewById(R.id.editDesc);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        btnHienthi = findViewById(R.id.btnHienthi);
        listView = findViewById(R.id.listview);

        context = this;
        //--hiển thị dữ liệu khi chạy chương trình
        list.clear();
        foodDAO = new FoodDAO(context); //tạo CSDL và bảng dữ liệu
        list = foodDAO.getAllFoodToString();
        arrayAdapter = new AdapterAdmin(context, R.layout.item_food_admin, list);
        listView.setAdapter(arrayAdapter);
        //--


        // button Choose
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,
                        REQUEST_CODE_CHOOSE);
            }
        });

        // button hiển thị
        btnHienthi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                foodDAO = new FoodDAO(context); //tạo CSDL và bảng dữ liệu
                list = foodDAO.getAllFoodToString();
                arrayAdapter = new AdapterAdmin(context, R.layout.item_food_admin, list);
                listView.setAdapter(arrayAdapter);
            }
        });

        // button thêm
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = new Food();

                // chuyen data imageView --> byte[]
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageFood.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                byte[] hinhAnh = byteArray.toByteArray();

                food.setFoodName(editName.getText().toString());
                food.setPrice(Double.parseDouble(editPrice.getText().toString()));
                food.setDesc(editDesc.getText().toString());
                food.setImage(hinhAnh);

                int kq = foodDAO.InsertFood(food);
                if(kq == -1) {
                    Toast.makeText(context, "Insert thất bại", Toast.LENGTH_LONG).show();
                }
                if(kq == 1) {
                    Toast.makeText(context, "Insert thành công", Toast.LENGTH_LONG).show();
                }
            }
        });


        // click item listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food f = list.get(position);
                editName.setText(f.getFoodName());
                editPrice.setText(String.valueOf(f.getPrice()));
                editDesc.setText(f.getDesc());

                // chuyen byte[] --> bitmap
                byte[] hinh = f.getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
                imageFood.setImageBitmap(bitmap);

                // button sửa
                btnSua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageFood.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                        byte[] hinhAnh = byteArray.toByteArray();

                        f.setFoodName(editName.getText().toString());
                        f.setPrice(Double.parseDouble(editPrice.getText().toString()));
                        f.setDesc(editDesc.getText().toString());
                        f.setImage(hinhAnh);

                        int kq = foodDAO.UpdateFood(f);
                        if(kq == -1) {
                            Toast.makeText(context, "Update thất bại", Toast.LENGTH_LONG).show();
                        }
                        if(kq == 1) {
                            Toast.makeText(context, "Update thành công", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                // button xóa
                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int kq = foodDAO.DeleteFood(f.getId());

                        if(kq == -1) {
                            Toast.makeText(context, "Delete thất bại", Toast.LENGTH_LONG).show();
                        }
                        if(kq == 1) {
                            Toast.makeText(context, "Delete thành công", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageFood.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
