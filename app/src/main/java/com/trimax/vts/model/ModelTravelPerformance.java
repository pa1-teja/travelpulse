package com.trimax.vts.model;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelTravelPerformance {

    @SerializedName("harsh_braking")
    @Expose
    private String harsh_braking;
    @SerializedName("harsh_acceleration")
    @Expose
    private String harsh_acceleration;
    @SerializedName("dangerous_speed")
    @Expose
    private String dangerous_speed;
    @SerializedName("ac_idle_repeat")
    @Expose
    private String ac_idle_repeat;

    public ModelTravelPerformance(String harsh_braking, String harsh_acceleration, String dangerous_speed, String ac_idle_repeat) {
        this.harsh_braking = harsh_braking;
        this.harsh_acceleration = harsh_acceleration;
        this.dangerous_speed = dangerous_speed;
        this.ac_idle_repeat = ac_idle_repeat;

    }

    public String getHarsh_braking() {
        return harsh_braking;
    }

    public void setHarsh_braking(String harsh_braking) {
        this.harsh_braking = harsh_braking;
    }

    public String getHarsh_acceleration() {
        return harsh_acceleration;
    }

    public void setHarsh_acceleration(String harsh_acceleration) {
        this.harsh_acceleration = harsh_acceleration;
    }

    public String getDangerous_speed() {
        return dangerous_speed;
    }

    public void setDangerous_speed(String dangerous_speed) {
        this.dangerous_speed = dangerous_speed;
    }

    public String getAc_idle_repeat() {
        return ac_idle_repeat;
    }

    public void setAc_idle_repeat(String ac_idle_repeat) {
        this.ac_idle_repeat = ac_idle_repeat;
    }


}






