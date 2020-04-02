package com.trimax.vts.view.model;

public class NotificationSettingModel {
    private String title;
    private String content;
    private String setting;

    public NotificationSettingModel() {
    }

    public NotificationSettingModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NotificationSettingModel(String title, String content, String setting) {
        this.title = title;
        this.content = content;
        this.setting = setting;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }
}
