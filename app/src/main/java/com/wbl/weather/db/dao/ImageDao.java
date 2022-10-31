package com.wbl.weather.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wbl.weather.db.bean.Image;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
@Dao
public interface ImageDao {

    @Query("SELECT * FROM image")
    List<Image> getAll();

    @Query("SELECT * FROM image WHERE uid LIKE :uid LIMIT 1")
    Flowable<Image> queryById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Image... images);

    @Delete
    void delete(Image image);
}
