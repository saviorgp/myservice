package com.myservice.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.myservice.R;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_block).getBackground().setAlpha(127);
    }

}

