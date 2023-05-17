package com.example.efood.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.efood.R;
import com.example.efood.activity.home.HomeActivity;
import com.example.efood.db.UserDB;
import com.example.efood.model.User;


public class ChangePasswordActivity extends AppCompatActivity {

    User user;

    private UserDB userDB;

    private EditText txtOldPassword,txtNewPassword,txtConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Thay đổi mật khẩu");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

        this.user = (User)getIntent().getSerializableExtra("user");
        userDB = new UserDB(getApplicationContext());

        txtOldPassword = findViewById(R.id.txtOldPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);

        Button btnSave = findViewById(R.id.btnSaveChanges);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getPassword().equals(txtOldPassword.getText().toString().trim())){
                    if(txtNewPassword.getText().toString().trim().equals(txtConfirmPassword.getText().toString().trim())){
                        int status = userDB.updatePassword(user, txtNewPassword.getText().toString().trim());
                        Toast.makeText(getApplicationContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("user",userDB.selectUserByPhone(user.getPhoneNumber()));
                        intent.putExtra("fragment_name", "ACCOUNT_FRAGMENT");
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Mật khẩu mới không khớp nhau", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Mật khẩu cũ không chính xác", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

                // Call account fragment
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("user",this.user);
                intent.putExtra("fragment_name", "ACCOUNT_FRAGMENT");
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
