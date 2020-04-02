package com.trimax.vts.model;

import java.io.Serializable;

public class MenuModel implements Serializable {


    int status;

    public MenuModel(int status, String position) {
        this.status = status;
        this.position = position;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    String position;

}
