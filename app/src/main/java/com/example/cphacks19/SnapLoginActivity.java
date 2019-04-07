package com.example.cphacks19;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.snapchat.kit.sdk.SnapLogin;
import android.view.LayoutInflater;


public class SnapLoginActivity extends AppCompatActivity {

    Button loginButton;
    View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap_login_layout);
        rootView = getWindow().getDecorView();

        View mLoginButton = SnapLogin.getButton(getContext(), (ViewGroup)rootView);





    }
    private Context getContext(){return this;}
}
