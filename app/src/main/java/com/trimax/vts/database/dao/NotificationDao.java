package com.trimax.vts.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.trimax.vts.database.entity.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    long insertNotification(Notification notification);

    @Query("select * from notification where notificationId=:notificationId")
    List<Notification> getNotifications(String notificationId);
}
