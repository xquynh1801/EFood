package com.example.efood.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    private static final String TAG = VerifyPhoneActivity.class.getName();
    private FirebaseAuth mAuth;

    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    private ProgressBar progressBar;
    private EditText editText;

    private TextView textView2, textView3;
    private String mPhoneNumber, mVerificationID;
    private Button buttonSignIn;

    private User user;

    private UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAuth = FirebaseAuth.getInstance();
        userDB = new UserDB(getApplicationContext());

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        mPhoneNumber = getIntent().getStringExtra("phone");
        mVerificationID = getIntent().getStringExtra("verificationId");
        user = (User)getIntent().getSerializableExtra("user");

        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView2.setText("Nhập mã OTP được gửi đến "+mPhoneNumber);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editText.getText().toString().trim();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, code);
                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Nhập mã xác minh...");
                    editText.requestFocus();
                    return;
                }
                signInWithPhoneAuthCredential(credential);
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84"+mPhoneNumber)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(VerifyPhoneActivity.this)                 // Activity (for callback binding)
                        .setForceResendingToken(mForceResendingToken)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyPhoneActivity.this, "Xác minh thất bại", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                mVerificationID = verificationId;
                                mForceResendingToken = forceResendingToken;
                            }
                        })
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");

//                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            // redirect Locate activity
                            Toast.makeText(VerifyPhoneActivity.this, "Xác minh thành công", Toast.LENGTH_SHORT).show();
                            if(userDB.addUser(user)==-1){
                                Toast.makeText(VerifyPhoneActivity.this, "Thêm người dùng thất bại", Toast.LENGTH_SHORT).show();
                            }else{
                                startActivity(new Intent(VerifyPhoneActivity.this, LocateActivity.class).putExtra("user", user));
                            }
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(VerifyPhoneActivity.this, "Mã xác minh đã hết hạn", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
