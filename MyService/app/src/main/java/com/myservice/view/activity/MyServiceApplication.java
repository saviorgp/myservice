package com.myservice.view.activity;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.codepond.wizardroid.Wizard;


@ReportsCrashes(formUri = "https://collector.tracepot.com/ef2258cd")
public class MyServiceApplication extends Application {

    private Wizard wizard;
    private int currentPage;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        ACRA.init(this);
    }

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

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
