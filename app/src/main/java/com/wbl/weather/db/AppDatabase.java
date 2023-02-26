package com.wbl.weather.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wbl.weather.db.bean.Image;
import com.wbl.weather.db.bean.News;
import com.wbl.weather.db.dao.ImageDao;
import com.wbl.weather.db.dao.NewsDao;

@Database(entities = {Image.class, News.class},version = 2,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


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
                            AppDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return mInstance;
    }

    /**
     * 版本升级迁移到2 新增新闻表
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //创建新闻表
            database.execSQL("CREATE TABLE `news` " +
                    "(uid INTEGER NOT NULL, " +
                    "uniquekey TEXT, " +
                    "title TEXT, " +
                    "date TEXT," +
                    "category TEXT," +
                    "author_name TEXT," +
                    "url TEXT," +
                    "thumbnail_pic_s TEXT," +
                    "is_content TEXT," +
                    "PRIMARY KEY(`uid`))");
        }
    };

    public abstract ImageDao imageDao();
    public abstract NewsDao newsDao();

}