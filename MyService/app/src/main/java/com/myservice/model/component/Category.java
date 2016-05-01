package com.myservice.model.component;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Category implements Serializable {

    String name;
    Category parent;

    public Category(){}

    public Category(String name){
        setName(name);
    }

    public  Category(JSONObject categoryObj){
        try {
            this.setName(categoryObj.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return getName();
    }
}
