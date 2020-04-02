package com.trimax.vts.view.notifications.send;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendNotification {
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("include_player_ids")
    @Expose
    private List<String> includePlayerIds = null;
    @SerializedName("contents")
    @Expose
    private Contents contents;
    @SerializedName("data")
    @Expose
    private NotificationData data;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<String> getIncludePlayerIds() {
        return includePlayerIds;
    }

    public void setIncludePlayerIds(List<String> includePlayerIds) {
        this.includePlayerIds = includePlayerIds;
    }

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }
}
