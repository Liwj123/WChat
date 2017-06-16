package com.example.lenovo.wchat.model;

/**
 * Created by Lenovo on 2017/5/18.
 */

public class DeffStringBean {
    private String key;
    private String deff;

    public DeffStringBean(String key, String deff) {
        this.key = key;
        this.deff = deff;
    }

    public DeffStringBean() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDeff() {
        return deff;
    }

    public void setDeff(String deff) {
        this.deff = deff;
    }
}
