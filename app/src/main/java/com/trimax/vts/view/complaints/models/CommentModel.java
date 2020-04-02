package com.trimax.vts.view.complaints.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentModel {

    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("ticket_id")
    @Expose
    private String ticketId;
    @SerializedName("parent_comment_id")
    @Expose
    private String parentCommentId;
    @SerializedName("comment_user_type")
    @Expose
    private String commentUserType;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("comment_by")
    @Expose
    private String commentBy;
    @SerializedName("ticket_comment_status")
    @Expose
    private String ticketCommentStatus;
    @SerializedName("comment_date")
    @Expose
    private String commentDate;
    private String mobile;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getCommentUserType() {
        return commentUserType;
    }

    public void setCommentUserType(String commentUserType) {
        this.commentUserType = commentUserType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public String getTicketCommentStatus() {
        return ticketCommentStatus;
    }

    public void setTicketCommentStatus(String ticketCommentStatus) {
        this.ticketCommentStatus = ticketCommentStatus;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
