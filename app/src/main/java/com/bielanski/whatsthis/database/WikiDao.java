package com.bielanski.whatsthis.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bielanski.whatsthis.database.data.WikiEntity;

import java.util.List;

@Dao
public interface   WikiDao {
    @Query("SELECT * FROM wiki")
    List<WikiEntity> getAllSavedWikies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(WikiEntity ... wiki);

    @Query("DELETE FROM wiki")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM wiki")
    int countWikiEntities();

    @Query("SELECT * FROM wiki WHERE title == :title")
    List<WikiEntity> getWikiesWithTitle(String title);

    @Query("DELETE FROM wiki WHERE title == :title")
    void deleteWiki(String title);
}
