package com.example.lenovo.wchat.model;

/**
 * Created by Lenovo on 2017/6/5.
 */

public class FaceBean {
    private String name;
    private int res;

    public FaceBean(String name, int res) {
        this.name = name;
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
