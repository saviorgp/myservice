package com.myservice.model.component;

import java.io.Serializable;


public class Image implements Serializable {

    private Long id;
    private String url;

    public Image(){

    }

    public Image(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
