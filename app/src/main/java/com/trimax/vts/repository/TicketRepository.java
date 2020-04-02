package com.trimax.vts.repository;

import com.trimax.vts.api.ApiClient;
import com.trimax.vts.view.complaints.models.CommentResponse;
import com.trimax.vts.view.complaints.models.DataSaveResponse;
import com.trimax.vts.view.complaints.models.TicketTypeResponse;
import com.trimax.vts.view.complaints.models.TicketsResponse;

import retrofit2.Call;

public class TicketRepository {
    public Call<TicketTypeResponse> getTicketType(){
        return ApiClient.getApiClient().getTicketTypes();
    }

    public Call<TicketsResponse> getTickets(String customerId){
        return ApiClient.getApiClient().getTickets(customerId);
    }

    public Call<CommentResponse> getTicketComments(String ticketId){
        return ApiClient.getApiClient().getTicketComments(ticketId);
    }

    public Call<DataSaveResponse> saveTicket(String customerId, String vehicleId, String ticketTypeId, String issueDate, String issueSource,
                                             String mobile, String mobileModel, String applicationVersion, String problem,String osVersion){
        return ApiClient.getApiClient().saveTicket(customerId,vehicleId,ticketTypeId,issueDate,issueSource,mobile,mobileModel,applicationVersion,problem,osVersion);
    }

    public Call<DataSaveResponse> saveTicketComment(String customerId, String ticketId, String comment,String ticketStatus, String parentTicketId){
        return ApiClient.getApiClient().saveTicketComment(customerId,ticketId,comment,ticketStatus,parentTicketId);
    }
}
