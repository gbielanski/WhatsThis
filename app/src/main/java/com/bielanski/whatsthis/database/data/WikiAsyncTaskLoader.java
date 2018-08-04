package com.bielanski.whatsthis.database.data;

import android.arch.persistence.room.Database;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.bielanski.whatsthis.database.WikiDao;
import com.bielanski.whatsthis.database.WikiDatabase;

import java.util.List;

public class WikiAsyncTaskLoader extends AsyncTaskLoader<List<WikiEntity>> {
    private WikiDatabase database;

    public WikiAsyncTaskLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public List<WikiEntity> loadInBackground() {
        final WikiDao wikiDao = database.wikiDao();
        return wikiDao.getAllSavedWikies();
    }

    @Override
    protected void onStartLoading() {
        //Think of this as AsyncTask onPreExecute() method,you can start your progress bar,and at the end call forceLoad();
        forceLoad();
    }

    public void setDatabase(WikiDatabase database) {
        this.database = database;
    }
}
