package com.example.efood.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.efood.R;
import com.example.efood.activity.home.AdminActivity;
import com.example.efood.activity.home.HomeActivity;
import com.example.efood.db.UserDB;
import com.example.efood.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsername, txtPassword;

    private Button btnLogin;

    private UserDB userDB;

    private TextView lblSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        lblSignup = findViewById(R.id.lblSignup);

        userDB = new UserDB(getApplicationContext());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                int status = userDB.checkUser(username,password);

                if(status==-1){
                    Toast.makeText(LoginActivity.this, "Số điện thoại này chưa được đăng kí",Toast.LENGTH_LONG).show();
                }else if(status==0) {
                    Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_LONG).show();
                } else {
                    User user = userDB.selectUserByPhone(username);
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    if(user.getRole().equals("ROLE_ADMIN"))
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class).putExtra("user", user));
                    else
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class).putExtra("user", user));
                }
            }
        });

        lblSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
