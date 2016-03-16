package com.myservice.model.component;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by AlexGP on 21/01/2016.
 */
public class UserVO implements Serializable {

    private String name;
    private String lastName;
    private String email;
    private String password;
    private String city;
    private String state;
    private String celPhone;
    private String phone;
    private String address;
    private UserVO user;

    public UserVO(){}

    public  UserVO(JSONObject userObj){
        try {
            this.setName(userObj.getString("name"));
            this.setLastName(userObj.getString("last_name"));
            this.setEmail(userObj.getString("email"));
            this.setPhone(userObj.getString("mobile_phone"));
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCelPhone() {
        return celPhone;
    }

    public void setCelPhone(String celPhone) {
        this.celPhone = celPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }
}