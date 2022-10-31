package com.wbl.weather.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wbl.weather.db.bean.Image;
import com.wbl.weather.db.dao.ImageDao;

import org.jetbrains.annotations.NotNull;

@Database(entities = {Image.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ImageDao imageDao();
    private static final String DATABASE_NAME = "weather";
    private static volatile AppDatabase mInstance;

    /**
     * 单例模式
     */
    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return mInstance;
    }
}