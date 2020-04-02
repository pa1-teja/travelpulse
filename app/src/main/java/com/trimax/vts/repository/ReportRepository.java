package com.trimax.vts.repository;

import com.trimax.vts.api.ApiClient;
import com.trimax.vts.view.reports.AlertReportResponse;
import com.trimax.vts.view.reports.ReportDetailResponse;
import com.trimax.vts.view.reports.ReportResponse;

import retrofit2.Call;

public class ReportRepository {

    public Call<ReportResponse> getStatuReport( final String userId,String vehicleId, String forDate, final String userTypeId){
        return ApiClient.getApiClient().getStatuReport(userId,userTypeId,vehicleId,forDate);
    }

    public Call<ReportDetailResponse> getDeatilStatuReport(final String userId, String vehicleId, String forDate, final String status){
        return ApiClient.getApiClient().getDetailStatuReport(userId,vehicleId,forDate,status);
    }

    public Call<AlertReportResponse> getDetailNotificationReports(final String userId, String vehicleId, String forDate){
        return ApiClient.getApiClient().getDetailNotificationReport(userId,vehicleId,forDate);
    }
}
