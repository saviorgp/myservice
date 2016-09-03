package com.myservice.view.activity;

import android.app.Application;

import org.codepond.wizardroid.Wizard;


public class MyServiceApplication extends Application {

    private Wizard wizard;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public Wizard getWizard() {
        return wizard;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }
}
