package com.example.efood.activity.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.efood.R;
import com.example.efood.activity.home.HomeActivity;
import com.example.efood.db.CommentDB;
import com.example.efood.model.Comment;
import com.example.efood.model.Food;
import com.example.efood.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RatingActivity extends AppCompatActivity implements View.OnClickListener{
    int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private ImageView imgFood, img, imageView1;
    private Button bt;
    private RatingBar ratingBar;
    private EditText cmt;
    private User user;
    private Food food;

    private byte[] img1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Đánh giá");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

        user = (User)getIntent().getSerializableExtra("user");
        food = (Food) getIntent().getSerializableExtra("food");

        img = findViewById(R.id.imgRating);
        imgFood = findViewById(R.id.imgFood);
        bt = findViewById(R.id.btGui);
        ratingBar = findViewById(R.id.rating_bar);
        cmt = findViewById(R.id.cmt);
        imageView1 = findViewById(R.id.image_view1);
        img.setOnClickListener(this);
        bt.setOnClickListener(this);

        byte[] imageData = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        imgFood.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {
        if(view==img){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        }

        if(view==bt){
            int rating = (int) ratingBar.getRating();
            String content = cmt.getText().toString();
            Comment c = new Comment();
            c.setRating(rating);
            c.setContent(content);
            c.setUser(user);
            c.setFood(food);
            if(img1 != null){
                c.setImg(img1);
                CommentDB db = new CommentDB(this);
                long status = db.addComment(c);
                if(status!=-1)
                    Toast.makeText(this, "Thêm đánh giá thành công", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Không thể tạo đánh giá. Xin vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Vui lòng chọn một ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                img1 = uriToByteArray(selectedImageUri);
                imageView1.setImageURI(selectedImageUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public byte[] uriToByteArray(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }

        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Call account fragment
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("user",this.user);
                intent.putExtra("fragment_name", "ORDER_FRAGMENT");
                startActivity(intent);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}