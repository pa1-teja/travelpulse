package com.trimax.vts.database.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.trimax.vts.database.entity.Notification;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class NotificationDao_Impl implements NotificationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Notification> __insertionAdapterOfNotification;

  public NotificationDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNotification = new EntityInsertionAdapter<Notification>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `notification` (`notificationId`,`notificationType`,`notificationTypeId`,`notificationSubtype`,`msg`,`showAlarm`,`customerId`,`vehicleId`,`vehicleLat`,`vehicleLng`,`ign`,`ac`,`speed`,`driverId`,`createdAt`,`receivedAt`,`vehicleTypeId`,`groupId`,`location`,`dateTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Notification value) {
        if (value.getNotificationId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getNotificationId());
        }
        if (value.getNotificationType() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getNotificationType());
        }
        if (value.getNotificationTypeId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getNotificationTypeId());
        }
        if (value.getNotificationSubtype() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getNotificationSubtype());
        }
        if (value.getMsg() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getMsg());
        }
        if (value.getShowAlarm() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getShowAlarm());
        }
        if (value.getCustomerId() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getCustomerId());
        }
        if (value.getVehicleId() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getVehicleId());
        }
        if (value.getVehicleLat() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getVehicleLat());
        }
        if (value.getVehicleLng() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getVehicleLng());
        }
        if (value.getIgn() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getIgn());
        }
        if (value.getAc() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getAc());
        }
        if (value.getSpeed() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getSpeed());
        }
        if (value.getDriverId() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getDriverId());
        }
        if (value.getCreatedAt() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getCreatedAt());
        }
        if (value.getReceivedAt() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getReceivedAt());
        }
        if (value.getVehicleTypeId() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getVehicleTypeId());
        }
        if (value.getGroupId() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getGroupId());
        }
        if (value.getLocation() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getLocation());
        }
        if (value.getDateTime() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getDateTime());
        }
      }
    };
  }

  @Override
  public long insertNotification(final Notification notification) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfNotification.insertAndReturnId(notification);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Notification> getNotifications(final String notificationId) {
    final String _sql = "select * from notification where notificationId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (notificationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, notificationId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfNotificationId = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationId");
      final int _cursorIndexOfNotificationType = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationType");
      final int _cursorIndexOfNotificationTypeId = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationTypeId");
      final int _cursorIndexOfNotificationSubtype = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationSubtype");
      final int _cursorIndexOfMsg = CursorUtil.getColumnIndexOrThrow(_cursor, "msg");
      final int _cursorIndexOfShowAlarm = CursorUtil.getColumnIndexOrThrow(_cursor, "showAlarm");
      final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
      final int _cursorIndexOfVehicleId = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleId");
      final int _cursorIndexOfVehicleLat = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleLat");
      final int _cursorIndexOfVehicleLng = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleLng");
      final int _cursorIndexOfIgn = CursorUtil.getColumnIndexOrThrow(_cursor, "ign");
      final int _cursorIndexOfAc = CursorUtil.getColumnIndexOrThrow(_cursor, "ac");
      final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
      final int _cursorIndexOfDriverId = CursorUtil.getColumnIndexOrThrow(_cursor, "driverId");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfReceivedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "receivedAt");
      final int _cursorIndexOfVehicleTypeId = CursorUtil.getColumnIndexOrThrow(_cursor, "vehicleTypeId");
      final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTime");
      final List<Notification> _result = new ArrayList<Notification>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Notification _item;
        final String _tmpNotificationId;
        _tmpNotificationId = _cursor.getString(_cursorIndexOfNotificationId);
        final String _tmpNotificationType;
        _tmpNotificationType = _cursor.getString(_cursorIndexOfNotificationType);
        final String _tmpNotificationTypeId;
        _tmpNotificationTypeId = _cursor.getString(_cursorIndexOfNotificationTypeId);
        final String _tmpNotificationSubtype;
        _tmpNotificationSubtype = _cursor.getString(_cursorIndexOfNotificationSubtype);
        final String _tmpMsg;
        _tmpMsg = _cursor.getString(_cursorIndexOfMsg);
        final String _tmpShowAlarm;
        _tmpShowAlarm = _cursor.getString(_cursorIndexOfShowAlarm);
        final String _tmpCustomerId;
        _tmpCustomerId = _cursor.getString(_cursorIndexOfCustomerId);
        final String _tmpVehicleId;
        _tmpVehicleId = _cursor.getString(_cursorIndexOfVehicleId);
        final String _tmpVehicleLat;
        _tmpVehicleLat = _cursor.getString(_cursorIndexOfVehicleLat);
        final String _tmpVehicleLng;
        _tmpVehicleLng = _cursor.getString(_cursorIndexOfVehicleLng);
        final String _tmpIgn;
        _tmpIgn = _cursor.getString(_cursorIndexOfIgn);
        final String _tmpAc;
        _tmpAc = _cursor.getString(_cursorIndexOfAc);
        final String _tmpSpeed;
        _tmpSpeed = _cursor.getString(_cursorIndexOfSpeed);
        final String _tmpDriverId;
        _tmpDriverId = _cursor.getString(_cursorIndexOfDriverId);
        final String _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
        final String _tmpReceivedAt;
        _tmpReceivedAt = _cursor.getString(_cursorIndexOfReceivedAt);
        final String _tmpVehicleTypeId;
        _tmpVehicleTypeId = _cursor.getString(_cursorIndexOfVehicleTypeId);
        final String _tmpGroupId;
        _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
        final String _tmpLocation;
        _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        final String _tmpDateTime;
        _tmpDateTime = _cursor.getString(_cursorIndexOfDateTime);
        _item = new Notification(_tmpNotificationId,_tmpNotificationType,_tmpNotificationTypeId,_tmpNotificationSubtype,_tmpMsg,_tmpShowAlarm,_tmpCustomerId,_tmpVehicleId,_tmpVehicleLat,_tmpVehicleLng,_tmpIgn,_tmpAc,_tmpSpeed,_tmpDriverId,_tmpCreatedAt,_tmpReceivedAt,_tmpVehicleTypeId,_tmpGroupId,_tmpLocation,_tmpDateTime);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
