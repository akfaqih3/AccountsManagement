package com.yemen.ums.ak.accounts_management.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.yemen.ums.ak.accounts_management.MainActivity;
import com.yemen.ums.ak.accounts_management.R;
import com.yemen.ums.ak.accounts_management.helpers.MySharedPreferences;

public class LoginActivity extends AppCompatActivity implements TextWatcher {
    ViewFlipper viewFlipper;
    EditText signupUsername,signupPassword,signupCoPassword,loginUsername,loginPassword;
    Button btn_login,btn_signup;
    ProgressBar progressBar;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        linkVarsWithViews();
        if (MySharedPreferences.isUserLoggedIn(context)){
            toMainActivity();
        }
        viewFlipper.setDisplayedChild(MySharedPreferences.isFirstTimeLaunch(context)?1:0);

    }

    private void linkVarsWithViews(){
        context = getApplicationContext();
        viewFlipper = findViewById(R.id.viewFlipper);
        signupUsername = findViewById(R.id.signupUsername_txt);
        signupPassword = findViewById(R.id.signupPassword_txt);
        signupCoPassword = findViewById(R.id.signupCoPassword_txt);
        loginUsername = findViewById(R.id.loginUsername_txt);
        loginPassword = findViewById(R.id.loginPassword_txt);
        btn_login = viewFlipper.getChildAt(0).findViewById(R.id.btn_login);
        btn_signup = viewFlipper.getChildAt(1).findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.progressBar);

        loginUsername.addTextChangedListener(this);
        loginPassword.addTextChangedListener(this);
        signupUsername.addTextChangedListener(this);
        signupPassword.addTextChangedListener(this);
        signupCoPassword.addTextChangedListener(this);

        btn_login.setOnClickListener(view1 -> login());
        btn_signup.setOnClickListener(view1 -> signup());
    }

    private void toMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void signup(){
        if (!signupUsername.getText().toString().trim().isEmpty()){
            if (signupPassword.getText().toString().trim().contentEquals(signupCoPassword.getText().toString().trim())){
                MySharedPreferences.signup(context,
                        signupUsername.getText().toString().trim(),
                        signupPassword.getText().toString().trim());
                progressBar.setVisibility(View.VISIBLE);
                toMainActivity();
            }else{
                Toast.makeText(context,"Password Not Match!",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void login(){

        String username = loginUsername.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();
        if (!username.isEmpty() && ! password.isEmpty()){
            if (MySharedPreferences.checkUser(context,username,password)){
                progressBar.setVisibility(View.VISIBLE);
                toMainActivity();
            }else {
                Toast.makeText(context,"username or passwrod is incorrect.",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!signupUsername.getText().toString().trim().isEmpty()
            && !signupPassword.getText().toString().trim().isEmpty()
            && !signupCoPassword.getText().toString().trim().isEmpty()){
            btn_signup.setEnabled(true);
        }else {
            btn_signup.setEnabled(false);
        }

        if (!loginUsername.getText().toString().trim().isEmpty()
            && !loginPassword.getText().toString().trim().isEmpty()){
            btn_login.setEnabled(true);
        }
        else {
            btn_login.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}