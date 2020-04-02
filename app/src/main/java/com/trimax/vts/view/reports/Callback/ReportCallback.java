package com.trimax.vts.view.reports.Callback;

public interface ReportCallback {
    void updateHashmap(String nameOfSelection, String key, Boolean value);
    void onButtonClick(String nameOfSelection, String buttonSelection);
}
