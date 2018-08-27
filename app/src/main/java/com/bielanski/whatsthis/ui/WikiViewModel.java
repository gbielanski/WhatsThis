package com.bielanski.whatsthis.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.bielanski.whatsthis.database.WikiDatabase;
import com.bielanski.whatsthis.database.data.WikiEntity;

import java.util.List;

public class WikiViewModel extends ViewModel {
    private WikiDatabase database = null;
    private MutableLiveData<List<WikiEntity>> wikies;
    public LiveData<List<WikiEntity>> getWikies(WikiDatabase database) {
        if(this.database == null)
            this.database = database;

        if (wikies == null) {
            wikies = new MutableLiveData<>();
            loadUsers();
        }
        return wikies;
    }

    private void loadUsers() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final List<WikiEntity> allSavedWikies = database.wikiDao().getAllSavedWikies();
                wikies.postValue(allSavedWikies);
            }
        });
    }

    public void setDatabase(WikiDatabase database) {
        this.database = database;
    }
}
