package com.myservice.view.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myservice.R;

import org.codepond.wizardroid.WizardStep;

public class AddUserStepOneActivity extends WizardStep {

    public AddUserStepOneActivity(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_service_one, container, false);
    }
}