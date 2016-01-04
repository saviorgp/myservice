package com.myservice.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by AlexGP on 01/04/2016.
 */
public class MyServiceService extends IntentService {
   
    public MyServiceService() {
        super("MyServiceService");
    }
 
    @Override
    protected void onHandleIntent(Intent intent) {
    
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}