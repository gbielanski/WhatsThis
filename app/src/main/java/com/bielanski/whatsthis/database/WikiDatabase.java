package com.bielanski.whatsthis.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.bielanski.whatsthis.database.data.WikiEntity;

@Database(entities = {WikiEntity.class}, version = 3)
public abstract class WikiDatabase extends RoomDatabase {
    private static final String LOG_TAG = WikiDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static WikiDatabase sInstance;
    private static final String DATABASE_NAME = "wikies";

    public static WikiDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        WikiDatabase.class, WikiDatabase.DATABASE_NAME).fallbackToDestructiveMigration().build();
                Log.d(LOG_TAG, "Made new database");
            }
        }
        return sInstance;
    }

    public abstract WikiDao wikiDao();
}
