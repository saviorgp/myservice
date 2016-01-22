package com.myservice.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by AlexGP on 01/04/2016.
 */
public class Preferences {

   private SharedPreferences prefs;
   private static Preferences prefsInstance;


   private Preferences(Context context){
       prefs = context.getSharedPreferences("MyService",Context.MODE_PRIVATE);
   }

   public static Preferences getPreferences(Context context){
       if(prefsInstance == null){
           prefsInstance = new Preferences(context);
       }
       return prefsInstance;
   }

    public void editPreference(final String key, final Object value) {
        SharedPreferences.Editor sharedEditor = prefs.edit();
        if (value instanceof Boolean) {
            sharedEditor.putBoolean(key, Boolean.parseBoolean(value.toString()));
        } else if (value instanceof Integer) {
            sharedEditor.putInt(key, Integer.parseInt(value.toString()));
        } else if (value instanceof Float) {
            sharedEditor.putFloat(key, Float.parseFloat(value.toString()));
        } else if (value instanceof String) {
            sharedEditor.putString(key, String.valueOf(value));
        } else if (value instanceof Long) {
            sharedEditor.putLong(key, Long.parseLong(value.toString()));
        } else if (value instanceof Set) {
            sharedEditor.putStringSet(key, (Set<String>)value);
        } else {
            sharedEditor.remove(key);
        }
        sharedEditor.apply();
    }

    public Object getSharedPreference(String key){
        return prefs.getAll().get(key);
    }
}