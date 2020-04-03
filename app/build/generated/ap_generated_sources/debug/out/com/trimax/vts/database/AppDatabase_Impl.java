package com.trimax.vts.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.trimax.vts.database.dao.NotificationDao;
import com.trimax.vts.database.dao.NotificationDao_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile NotificationDao _notificationDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `notification` (`notificationId` TEXT NOT NULL, `notificationType` TEXT, `notificationTypeId` TEXT, `notificationSubtype` TEXT, `msg` TEXT, `showAlarm` TEXT, `customerId` TEXT, `vehicleId` TEXT, `vehicleLat` TEXT, `vehicleLng` TEXT, `ign` TEXT, `ac` TEXT, `speed` TEXT, `driverId` TEXT, `createdAt` TEXT, `receivedAt` TEXT, `vehicleTypeId` TEXT, `groupId` TEXT, `location` TEXT, `dateTime` TEXT, PRIMARY KEY(`notificationId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7bb0be349eaeac162fae7a352c04b0c3')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `notification`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsNotification = new HashMap<String, TableInfo.Column>(20);
        _columnsNotification.put("notificationId", new TableInfo.Column("notificationId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("notificationType", new TableInfo.Column("notificationType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("notificationTypeId", new TableInfo.Column("notificationTypeId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("notificationSubtype", new TableInfo.Column("notificationSubtype", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("msg", new TableInfo.Column("msg", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("showAlarm", new TableInfo.Column("showAlarm", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("customerId", new TableInfo.Column("customerId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("vehicleId", new TableInfo.Column("vehicleId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("vehicleLat", new TableInfo.Column("vehicleLat", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("vehicleLng", new TableInfo.Column("vehicleLng", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("ign", new TableInfo.Column("ign", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("ac", new TableInfo.Column("ac", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("speed", new TableInfo.Column("speed", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("driverId", new TableInfo.Column("driverId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("createdAt", new TableInfo.Column("createdAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("receivedAt", new TableInfo.Column("receivedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("vehicleTypeId", new TableInfo.Column("vehicleTypeId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("groupId", new TableInfo.Column("groupId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotification.put("dateTime", new TableInfo.Column("dateTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotification = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotification = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotification = new TableInfo("notification", _columnsNotification, _foreignKeysNotification, _indicesNotification);
        final TableInfo _existingNotification = TableInfo.read(_db, "notification");
        if (! _infoNotification.equals(_existingNotification)) {
          return new RoomOpenHelper.ValidationResult(false, "notification(com.trimax.vts.database.entity.Notification).\n"
                  + " Expected:\n" + _infoNotification + "\n"
                  + " Found:\n" + _existingNotification);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "7bb0be349eaeac162fae7a352c04b0c3", "4043fca74f409adf8a258e01c51ebb1b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "notification");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `notification`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public NotificationDao notificationDao() {
    if (_notificationDao != null) {
      return _notificationDao;
    } else {
      synchronized(this) {
        if(_notificationDao == null) {
          _notificationDao = new NotificationDao_Impl(this);
        }
        return _notificationDao;
      }
    }
  }
}
