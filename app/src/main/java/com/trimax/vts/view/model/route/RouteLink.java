package com.trimax.vts.view.model.route;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteLink {
    @SerializedName("linkId")
    @Expose
    private Integer linkId;
    @SerializedName("functionalClass")
    @Expose
    private Integer functionalClass;
    @SerializedName("confidence")
    @Expose
    private double confidence;
    @SerializedName("shape")
    @Expose
    private String shape;
    @SerializedName("mSecToReachLinkFromStart")
    @Expose
    private Integer mSecToReachLinkFromStart;
    @SerializedName("linkLength")
    @Expose
    private double linkLength;
    @SerializedName("offset")
    @Expose
    private double offset;

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public Integer getFunctionalClass() {
        return functionalClass;
    }

    public void setFunctionalClass(Integer functionalClass) {
        this.functionalClass = functionalClass;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getMSecToReachLinkFromStart() {
        return mSecToReachLinkFromStart;
    }

    public void setMSecToReachLinkFromStart(Integer mSecToReachLinkFromStart) {
        this.mSecToReachLinkFromStart = mSecToReachLinkFromStart;
    }

    public Double getLinkLength() {
        return linkLength;
    }

    public void setLinkLength(Double linkLength) {
        this.linkLength = linkLength;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }
}
