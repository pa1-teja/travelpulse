package com.trimax.vts.view.reports;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportData {
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("max_speed")
    @Expose
    private String maxSpeed;
    @SerializedName("moving")
    @Expose
    private String moving;
    @SerializedName("idle")
    @Expose
    private String idle;
    @SerializedName("stop")
    @Expose
    private String stop;
    @SerializedName("harsh_acceleration")
    @Expose
    private String  harshAcceleration;
    @SerializedName("harsh_braking")
    @Expose
    private String harshBraking;
    @SerializedName("dangerous_speed")
    @Expose
    private String dangerousSpeed;
    @SerializedName("ac_idle_repeat")
    @Expose
    private String  acIdleRepeat;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getMoving() {
        return moving;
    }

    public void setMoving(String moving) {
        this.moving = moving;
    }

    public String getIdle() {
        return idle;
    }

    public void setIdle(String idle) {
        this.idle = idle;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getHarshAcceleration() {
        return harshAcceleration;
    }

    public void setHarshAcceleration(String harshAcceleration) {
        this.harshAcceleration = harshAcceleration;
    }

    public String getHarshBraking() {
        return harshBraking;
    }

    public void setHarshBraking(String harshBraking) {
        this.harshBraking = harshBraking;
    }

    public String getDangerousSpeed() {
        return dangerousSpeed;
    }

    public void setDangerousSpeed(String dangerousSpeed) {
        this.dangerousSpeed = dangerousSpeed;
    }

    public String getAcIdleRepeat() {
        return acIdleRepeat;
    }

    public void setAcIdleRepeat(String acIdleRepeat) {
        this.acIdleRepeat = acIdleRepeat;
    }
}
