package com.example.cphacks19;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.GetChars;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.snapchat.kit.sdk.SnapLogin;
import android.view.LayoutInflater;
import com.snapchat.kit.sdk.core.controller.LoginStateController;


public class SnapLoginActivity extends AppCompatActivity {

    Button loginButton;
    View rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap_login_layout);
        rootView = getWindow().getDecorView();

        View mLoginButton = SnapLogin.getButton(getContext(), (ViewGroup)rootView);



        final LoginStateController.OnLoginStateChangedListener mLoginStateChangedListener =
                new LoginStateController.OnLoginStateChangedListener() {
                    @Override
                    public void onLoginSucceeded() {
                        //TODO: Change to main activity
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getContext(), "Successfully logged in", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onLoginFailed() {
                        // Here you could update UI to show login failure
                        Toast.makeText(getContext(), "Failed  logged in", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onLogout() {
                        // Here you could update UI to reflect logged out state
                        Toast.makeText(getContext(), "Successfully logged in", Toast.LENGTH_LONG).show();
                    }
                };
        SnapLogin.getLoginStateController(this).addOnLoginStateChangedListener(mLoginStateChangedListener);

    }
    private Context getContext(){return this;}

}
