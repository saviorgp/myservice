package com.myservice.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.myservice.R;


public class LoginActivity extends Activity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_block).getBackground().setAlpha(127);
    }

}

