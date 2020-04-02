package com.trimax.vts.view.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMenu {
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("parent")
    @Expose
    private String parent;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
