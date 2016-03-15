package com.myservice.view.activity;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.myservice.R;
import com.myservice.model.transaction.ITransaction;

public class ContactActivity  extends AppCompatActivity implements ITransaction {


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.activity_contact);
    }

    @Override
    public void execute() throws Exception {

    }

    @Override
    public void updateView() {

    }
}
