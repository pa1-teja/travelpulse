package com.trimax.vts.view.model.route;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Warning {
    @SerializedName("category")
    @Expose
    private Integer category;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("routeLinkSeqNum")
    @Expose
    private Integer routeLinkSeqNum;
    @SerializedName("tracePointSeqNum")
    @Expose
    private Integer tracePointSeqNum;

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRouteLinkSeqNum() {
        return routeLinkSeqNum;
    }

    public void setRouteLinkSeqNum(Integer routeLinkSeqNum) {
        this.routeLinkSeqNum = routeLinkSeqNum;
    }

    public Integer getTracePointSeqNum() {
        return tracePointSeqNum;
    }

    public void setTracePointSeqNum(Integer tracePointSeqNum) {
        this.tracePointSeqNum = tracePointSeqNum;
    }
}
