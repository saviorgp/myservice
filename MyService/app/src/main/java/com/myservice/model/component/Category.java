package com.myservice.model.component;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Category implements Serializable {

    private Integer id;
    private String name;
    private Category parent;

    public Category(){}

    public Category(String name){
        setName(name);
    }

    public  Category(JSONObject categoryObj){
        try {
            this.setId(categoryObj.getInt("id"));
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getName();
    }
}
