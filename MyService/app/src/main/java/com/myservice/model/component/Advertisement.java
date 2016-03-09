package com.myservice.model.component;

import java.math.BigDecimal;
import java.util.Date;

public class Advertisement {

    private Integer id;
    private String title;
    private String description;
    private Date expiration_at;
    private BigDecimal price;
    private Boolean visible;
    private UserVO user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExpiration_at() {
        return expiration_at;
    }

    public void setExpiration_at(Date expiration_at) {
        this.expiration_at = expiration_at;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }
}
