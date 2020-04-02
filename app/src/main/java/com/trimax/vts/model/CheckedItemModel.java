package com.trimax.vts.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sandip on 2/8/2017.
 */

public class CheckedItemModel implements Serializable {
    String alertSourceCode;
    List<String> code;
    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }

    public String getAlertSourceCode() {
        return alertSourceCode;
    }

    public void setAlertSourceCode(String alertSourceCode) {
        this.alertSourceCode = alertSourceCode;
    }
}
