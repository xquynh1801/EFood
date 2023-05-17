package com.example.efood.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.efood.R;
import com.example.efood.activity.utils.LocateActivity;
import com.example.efood.db.UserDB;
import com.example.efood.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getName();
    private EditText txtName, txtAge, txtPhone, txtPassword, txtReinput;
    private CheckBox checkBox;
    private Button btnSignup;
    private FirebaseAuth mAuth;
    private UserDB userDB;
    private User user;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        userDB = new UserDB(getApplicationContext());

        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        txtReinput = findViewById(R.id.txtReinput);
        checkBox = findViewById(R.id.checkBox);
        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    btnSignup.setEnabled(true);
                } else {
                    btnSignup.setEnabled(false);
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtName.getText().toString().trim();
                String age = txtAge.getText().toString().trim();
                String phone = txtPhone.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String repass = txtReinput.getText().toString().trim();

                if(name==null || name.length()==0){
                    txtName.setError("Tên không được để trống");
                    return;
                }
                if(age==null || age.length()==0){
                    txtAge.setError("Tuổi không được để trống");
                    return;
                }
                if(phone==null || phone.length()==0){
                    txtPhone.setError("Số điện thoại không được để trống");
                    return;
                }
                if(password==null || password.length()==0){
                    txtPassword.setError("Mật khẩu không được để trống");
                    return;
                }
                if(repass==null || repass.length()==0){
                    txtReinput.setError("Không được để trống trường này");
                    return;
                }

                int num_age;
                try{
                    num_age = Integer.parseInt(age);
                }catch (NumberFormatException e){
                    txtAge.setError("Tuổi phải là số nguyên");
                    return;
                }

                if(!password.equals(repass)){
                    txtReinput.setError("Mật khẩu không khớp");
                    return;
                }

                user = new User();
                user.setPhoneNumber(phone);
                user.setFullName(name);
                user.setPassword(password);
                user.setAge(num_age);
                user.setAddress("20.980356447957945;105.78690022230148");

                phoneNumber = phone;
                if(userDB.selectUserByPhone(phone)==null){
                    sendVerificationCode(phone);
//                    Toast.makeText(SignupActivity.this, "Đăng ký thành công",Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(SignupActivity.this, LocateActivity.class).putExtra("user", user));
                }else{
                    Toast.makeText(SignupActivity.this, "Số điện thoại này đã được đăng kí",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
            PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84"+number)            // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(SignupActivity.this, "Xác minh thất bại", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        // redirect VerifyPhoneActivity
                        Intent intent = new Intent(SignupActivity.this, VerifyPhoneActivity.class);
                        intent.putExtra("phone", phoneNumber);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                        // redirect Locate activity
                        Toast.makeText(SignupActivity.this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, LocateActivity.class).putExtra("user", user));

                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(SignupActivity.this, "Mã xác minh không có giá trị", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }
}
