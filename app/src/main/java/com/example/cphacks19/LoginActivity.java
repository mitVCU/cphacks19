package com.example.cphacks19;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.snapchat.kit.sdk.SnapLogin;
public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    LinearLayout rootContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login_layout);
        //rootContainer = findViewById(R.id.rootContainer);
        //View mLoginButton = SnapLogin.getButton(getApplicationContext(), rootContainer);

    }
}


