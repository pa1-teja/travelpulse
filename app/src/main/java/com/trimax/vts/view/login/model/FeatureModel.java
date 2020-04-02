package com.trimax.vts.view.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeatureModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fkey")
    @Expose
    private String fkey;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("access_by")
    @Expose
    private String accessBy;
    @SerializedName("fgroup_title")
    @Expose
    private String fgroupTitle;
    @SerializedName("fgroup_key")
    @Expose
    private String fgroupKey;
    @SerializedName("feature_scope")
    @Expose
    private String featureScope;
    @SerializedName("seq_no")
    @Expose
    private String seqNo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("androidapp_menu_status")
    @Expose
    private String androidappMenuStatus;
    @SerializedName("androidapp_menu_sequence")
    @Expose
    private String androidappMenuSequence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkey() {
        return fkey;
    }

    public void setFkey(String fkey) {
        this.fkey = fkey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccessBy() {
        return accessBy;
    }

    public void setAccessBy(String accessBy) {
        this.accessBy = accessBy;
    }

    public String getFgroupTitle() {
        return fgroupTitle;
    }

    public void setFgroupTitle(String fgroupTitle) {
        this.fgroupTitle = fgroupTitle;
    }

    public String getFgroupKey() {
        return fgroupKey;
    }

    public void setFgroupKey(String fgroupKey) {
        this.fgroupKey = fgroupKey;
    }

    public String getFeatureScope() {
        return featureScope;
    }

    public void setFeatureScope(String featureScope) {
        this.featureScope = featureScope;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAndroidappMenuStatus() {
        return androidappMenuStatus;
    }

    public void setAndroidappMenuStatus(String androidappMenuStatus) {
        this.androidappMenuStatus = androidappMenuStatus;
    }

    public String getAndroidappMenuSequence() {
        return androidappMenuSequence;
    }

    public void setAndroidappMenuSequence(String androidappMenuSequence) {
        this.androidappMenuSequence = androidappMenuSequence;
    }
}
