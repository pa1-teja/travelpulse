package com.trimax.vts.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.trimax.vts.database.dao.NotificationDao;
import com.trimax.vts.database.entity.Notification;
import com.trimax.vts.utils.Constants;

@Database(entities = {Notification.class},exportSchema = false,version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase dbInstance=null;

    public static AppDatabase getDbInstance(Context context) {
        if (dbInstance==null){
            dbInstance = Room.databaseBuilder(context,AppDatabase.class, Constants.DATABASE_NAME)
                    //.fallbackToDestructiveMigration()
                    .addMigrations(migration1_2)
                    .build();
        }
        return dbInstance;
    }

    public abstract NotificationDao notificationDao();

    private static Migration migration1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };
}
