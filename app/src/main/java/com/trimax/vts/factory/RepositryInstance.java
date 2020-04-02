package com.trimax.vts.factory;

import com.trimax.vts.repository.LoginRepository;
import com.trimax.vts.repository.NotificationRepository;
import com.trimax.vts.repository.RemoteImmobilizationRepository;
import com.trimax.vts.repository.ReportRepository;
import com.trimax.vts.repository.TicketRepository;


public class RepositryInstance {
    private static LoginRepository loginRepository;
    private static NotificationRepository notificationRepository;
    private static ReportRepository reportRepository;
    private static TicketRepository ticketRepository;
    private static RemoteImmobilizationRepository immobilizationRepository;

    public static LoginRepository getLoginRepository() {
        if (loginRepository==null)
            loginRepository=new LoginRepository();
        return loginRepository;
    }

    public static NotificationRepository getNotificationRepository() {
        if (notificationRepository==null)
            notificationRepository = new NotificationRepository();
        return notificationRepository;
    }

    public static ReportRepository getReportRepository() {
        if (reportRepository==null)
            reportRepository = new ReportRepository();
        return reportRepository;
    }

    public static TicketRepository getTicketRepository(){
        if (ticketRepository==null)
            ticketRepository = new TicketRepository();
        return ticketRepository;
    }

    public static RemoteImmobilizationRepository getImmobilizationRepository(){
        if (immobilizationRepository==null)
            immobilizationRepository = new RemoteImmobilizationRepository();
        return immobilizationRepository;
    }
}
