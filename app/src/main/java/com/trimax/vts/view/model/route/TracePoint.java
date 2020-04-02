package com.trimax.vts.view.model.route;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TracePoint {
    @SerializedName("calculatedAccel")
    @Expose
    private Integer calculatedAccel;
    @SerializedName("confidenceValue")
    @Expose
    private double confidenceValue;
    @SerializedName("elevation")
    @Expose
    private Integer elevation;
    @SerializedName("headingDegreeNorthClockwise")
    @Expose
    private Integer headingDegreeNorthClockwise;
    @SerializedName("headingMatched")
    @Expose
    private Integer headingMatched;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("latMatched")
    @Expose
    private Double latMatched;
    @SerializedName("linkIdMatched")
    @Expose
    private Integer linkIdMatched;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("lonMatched")
    @Expose
    private Double lonMatched;
    @SerializedName("matchDistance")
    @Expose
    private double matchDistance;
    @SerializedName("matchOffsetOnLink")
    @Expose
    private double matchOffsetOnLink;
    @SerializedName("minError")
    @Expose
    private Integer minError;
    @SerializedName("routeLinkSeqNrMatched")
    @Expose
    private Integer routeLinkSeqNrMatched;
    @SerializedName("speedMps")
    @Expose
    private Integer speedMps;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;

    public Integer getCalculatedAccel() {
        return calculatedAccel;
    }

    public void setCalculatedAccel(Integer calculatedAccel) {
        this.calculatedAccel = calculatedAccel;
    }

    public double getConfidenceValue() {
        return confidenceValue;
    }

    public void setConfidenceValue(double confidenceValue) {
        this.confidenceValue = confidenceValue;
    }

    public Integer getElevation() {
        return elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    public Integer getHeadingDegreeNorthClockwise() {
        return headingDegreeNorthClockwise;
    }

    public void setHeadingDegreeNorthClockwise(Integer headingDegreeNorthClockwise) {
        this.headingDegreeNorthClockwise = headingDegreeNorthClockwise;
    }

    public Integer getHeadingMatched() {
        return headingMatched;
    }

    public void setHeadingMatched(Integer headingMatched) {
        this.headingMatched = headingMatched;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLatMatched() {
        return latMatched;
    }

    public void setLatMatched(Double latMatched) {
        this.latMatched = latMatched;
    }

    public Integer getLinkIdMatched() {
        return linkIdMatched;
    }

    public void setLinkIdMatched(Integer linkIdMatched) {
        this.linkIdMatched = linkIdMatched;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLonMatched() {
        return lonMatched;
    }

    public void setLonMatched(Double lonMatched) {
        this.lonMatched = lonMatched;
    }

    public double getMatchDistance() {
        return matchDistance;
    }

    public void setMatchDistance(double matchDistance) {
        this.matchDistance = matchDistance;
    }

    public double getMatchOffsetOnLink() {
        return matchOffsetOnLink;
    }

    public void setMatchOffsetOnLink(double matchOffsetOnLink) {
        this.matchOffsetOnLink = matchOffsetOnLink;
    }

    public Integer getMinError() {
        return minError;
    }

    public void setMinError(Integer minError) {
        this.minError = minError;
    }

    public Integer getRouteLinkSeqNrMatched() {
        return routeLinkSeqNrMatched;
    }

    public void setRouteLinkSeqNrMatched(Integer routeLinkSeqNrMatched) {
        this.routeLinkSeqNrMatched = routeLinkSeqNrMatched;
    }

    public Integer getSpeedMps() {
        return speedMps;
    }

    public void setSpeedMps(Integer speedMps) {
        this.speedMps = speedMps;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
}
